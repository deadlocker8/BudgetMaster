package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.transactions.csvImport.CsvImport;
import de.deadlocker8.budgetmaster.transactions.csvImport.CsvParser;
import de.deadlocker8.budgetmaster.transactions.csvImport.CsvRow;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping(Mappings.TRANSACTION_IMPORT)
public class TransactionImportController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String ERROR = "error";
		public static final String ERROR_UPLOAD = "errorUpload";
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
	public String transactionImport(HttpServletRequest request, Model model)
	{
		model.addAttribute(RequestAttributeNames.CSV_IMPORT, new CsvImport(null, ";"));
		return ReturnValues.TRANSACTION_IMPORT;
	}

	@PostMapping("/upload")
	public String upload(WebRequest request,
						 Model model,
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

		if(bindingResult.hasErrors())
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);
			request.setAttribute(RequestAttributeNames.CSV_IMPORT, csvImport, RequestAttributes.SCOPE_SESSION);
			return ReturnValues.TRANSACTION_IMPORT;
		}

		try
		{
			final String csvString = new String(csvImport.file().getBytes(), StandardCharsets.UTF_8);
			final List<CsvRow> csvRows = CsvParser.parseCsv(csvString, csvImport.separator().charAt(0));

			request.setAttribute(RequestAttributeNames.CSV_IMPORT, csvImport, RequestAttributes.SCOPE_SESSION);
			request.setAttribute(RequestAttributeNames.CSV_ROWS, csvRows, RequestAttributes.SCOPE_SESSION);
		}
		catch(Exception e)
		{
			LOGGER.error("CSV upload failed", e);

			// TODO: show in html
			model.addAttribute(ModelAttributes.ERROR_UPLOAD, e.getMessage());
		}

		return ReturnValues.TRANSACTION_IMPORT;
	}

	@GetMapping("/cancel")
	public String cancel(WebRequest request)
	{
		request.removeAttribute(RequestAttributeNames.CSV_IMPORT, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.CSV_ROWS, RequestAttributes.SCOPE_SESSION);

		return ReturnValues.REDIRECT_IMPORT;
	}
}