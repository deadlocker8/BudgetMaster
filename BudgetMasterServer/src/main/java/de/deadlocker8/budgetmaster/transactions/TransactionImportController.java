package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.transactions.csvImport.*;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(Mappings.TRANSACTION_IMPORT)
public class TransactionImportController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String ERROR = "error";
	}

	private static class ReturnValues
	{
		public static final String TRANSACTION_IMPORT = "transactions/transactionImport";
		public static final String REDIRECT_IMPORT = "redirect:/transactionImport";
		public static final String REDIRECT_CANCEL = "redirect:/transactionImport/cancel";
	}

	private static class RequestAttributeNames
	{
		public static final String CSV_IMPORT = "csvImport";
		public static final String CSV_ROWS = "csvRows";
		public static final String CSV_TRANSACTIONS = "csvTransactions";
		public static final String ERROR_UPLOAD = "errorUpload";
		public static final String ERROR_UPLOAD_FILE = "errorUploadFile";
	}

	private final TransactionService transactionService;
	private final HelpersService helpers;

	@Autowired
	public TransactionImportController(TransactionService transactionService, HelpersService helpers)
	{
		this.transactionService = transactionService;
		this.helpers = helpers;
	}

	@GetMapping
	public String transactionImport(WebRequest request, Model model)
	{
		if(request.getAttribute(RequestAttributeNames.CSV_IMPORT, RequestAttributes.SCOPE_SESSION) == null)
		{
			model.addAttribute(RequestAttributeNames.CSV_IMPORT, new CsvImport(null, ";", StandardCharsets.UTF_8.name(), 0));
		}

		final Object bindingResult = request.getAttribute(RequestAttributeNames.ERROR_UPLOAD, RequestAttributes.SCOPE_SESSION);
		if(bindingResult != null)
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);
		}

		return ReturnValues.TRANSACTION_IMPORT;
	}

	@PostMapping("/upload")
	public String upload(WebRequest request,
						 @ModelAttribute("CsvImport") CsvImport csvImport,
						 BindingResult bindingResult)
	{
		if(csvImport.file().isEmpty())
		{
			return ReturnValues.REDIRECT_CANCEL;
		}

		if(!csvImport.isValidSeparator())
		{
			bindingResult.addError(new FieldError("CsvImport", "separator", "", false, new String[]{"warning.transaction.import.separator"}, null, null));
		}

		if(!csvImport.isEncodingSupported())
		{
			bindingResult.addError(new FieldError("CsvImport", "encoding", "", false, new String[]{"warning.transaction.import.encoding"}, null, null));
		}

		if(bindingResult.hasErrors())
		{
			request.setAttribute(RequestAttributeNames.ERROR_UPLOAD, bindingResult, RequestAttributes.SCOPE_SESSION);
			request.setAttribute(RequestAttributeNames.CSV_IMPORT, csvImport, RequestAttributes.SCOPE_SESSION);
			return ReturnValues.REDIRECT_IMPORT;
		}

		try
		{
			final String csvString = new String(csvImport.file().getBytes(), csvImport.encoding());
			final List<CsvRow> csvRows = CsvParser.parseCsv(csvString, csvImport.separator().charAt(0), csvImport.numberOfLinesToSkip());

			removeAllAttributes(request);

			request.setAttribute(RequestAttributeNames.CSV_IMPORT, csvImport, RequestAttributes.SCOPE_SESSION);
			request.setAttribute(RequestAttributeNames.CSV_ROWS, csvRows, RequestAttributes.SCOPE_SESSION);
		}
		catch(Exception e)
		{
			LOGGER.error("CSV upload failed", e);

			// TODO: show in html
			request.setAttribute(RequestAttributeNames.ERROR_UPLOAD_FILE, e.getMessage(), RequestAttributes.SCOPE_SESSION);
		}
		return ReturnValues.REDIRECT_IMPORT;
	}

	@PostMapping("/columnSettings")
	public String columnSettings(WebRequest request,
								 @ModelAttribute("CsvColumnSettings") CsvColumnSettings csvColumnSettings,
								 BindingResult bindingResult)
	{
		if(bindingResult.hasErrors())
		{
			request.setAttribute(RequestAttributeNames.ERROR_UPLOAD, bindingResult, RequestAttributes.SCOPE_SESSION);
			return ReturnValues.REDIRECT_IMPORT;
		}

		final Object attribute = request.getAttribute(RequestAttributeNames.CSV_ROWS, RequestAttributes.SCOPE_SESSION);
		if(attribute == null)
		{
			return ReturnValues.REDIRECT_CANCEL;
		}

		final List<CsvRow> csvRows = (List<CsvRow>) attribute;
		final List<CsvTransaction> csvTransactions = new ArrayList<>();
		for(CsvRow csvRow : csvRows)
		{
			final String date = csvRow.getColumns().get(csvColumnSettings.columnDate() - 1);
			final String name = csvRow.getColumns().get(csvColumnSettings.columnName() - 1);
			final String amount = csvRow.getColumns().get(csvColumnSettings.columnAmount() - 1);
			csvTransactions.add(new CsvTransaction(date, name, amount, CsvTransactionStatus.PENDING));
		}

		request.setAttribute(RequestAttributeNames.CSV_TRANSACTIONS, csvTransactions, RequestAttributes.SCOPE_SESSION);

		return ReturnValues.REDIRECT_IMPORT;
	}

	@GetMapping("/cancel")
	public String cancel(WebRequest request)
	{
		removeAllAttributes(request);
		return ReturnValues.REDIRECT_IMPORT;
	}

	@GetMapping("/{index}/skip")
	public String skip(WebRequest request, @PathVariable("index") Integer index)
	{
		final Object attribute = request.getAttribute(RequestAttributeNames.CSV_TRANSACTIONS, RequestAttributes.SCOPE_SESSION);
		if(attribute == null)
		{
			return ReturnValues.REDIRECT_CANCEL;
		}

		final List<CsvTransaction> csvTransactions = (List<CsvTransaction>) attribute;
		csvTransactions.get(index).setStatus(CsvTransactionStatus.SKIPPED);
		return ReturnValues.REDIRECT_IMPORT;
	}

	private void removeAllAttributes(WebRequest request)
	{
		request.removeAttribute(RequestAttributeNames.CSV_IMPORT, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.CSV_ROWS, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.CSV_TRANSACTIONS, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.ERROR_UPLOAD, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.ERROR_UPLOAD_FILE, RequestAttributes.SCOPE_SESSION);
	}
}