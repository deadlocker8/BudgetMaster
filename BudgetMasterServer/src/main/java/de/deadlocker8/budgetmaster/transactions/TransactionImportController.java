package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.transactions.csvimport.*;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.Localization;
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
import java.util.Optional;

@Controller
@RequestMapping(Mappings.TRANSACTION_IMPORT)
public class TransactionImportController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String ERROR = "error";
		public static final String CATEGORIES = "categories";
	}

	private static class ReturnValues
	{
		public static final String TRANSACTION_IMPORT = "transactions/transactionImport";
		public static final String REDIRECT_IMPORT = "redirect:/transactionImport";
		public static final String REDIRECT_CANCEL = "redirect:/transactionImport/cancel";
		public static final String NEW_TRANSACTION_NORMAL = "transactions/newTransactionNormal";
		public static final String NEW_TRANSACTION_TRANSFER = "transactions/newTransactionTransfer";
		public static final String REDIRECT_TEMPLATES = "redirect:/templates";
	}

	public static class RequestAttributeNames
	{
		private RequestAttributeNames()
		{
		}

		public static final String CSV_IMPORT = "csvImport";
		public static final String CSV_ROWS = "csvRows";
		public static final String CSV_TRANSACTIONS = "csvTransactions";
		public static final String ERROR_UPLOAD = "errorUpload";
		public static final String ERRORS_COLUMN_SETTINGS = "errorsColumnSettings";
		public static final String CURRENT_CSV_TRANSACTION = "currentCsvTransaction";
	}

	private final TransactionService transactionService;
	private final CategoryService categoryService;
	private final AccountService accountService;
	private final TransactionImportService transactionImportService;

	@Autowired
	public TransactionImportController(TransactionService transactionService, CategoryService categoryService, AccountService accountService, TransactionImportService transactionImportService)
	{
		this.transactionService = transactionService;
		this.categoryService = categoryService;
		this.accountService = accountService;
		this.transactionImportService = transactionImportService;
	}

	@GetMapping
	public String transactionImport(WebRequest request, Model model)
	{
		request.removeAttribute(RequestAttributeNames.CURRENT_CSV_TRANSACTION, RequestAttributes.SCOPE_SESSION);

		if(request.getAttribute(RequestAttributeNames.CSV_IMPORT, RequestAttributes.SCOPE_SESSION) == null)
		{
			model.addAttribute(RequestAttributeNames.CSV_IMPORT, new CsvImport(null, ";", StandardCharsets.UTF_8.name(), 0));
		}

		final Object bindingResult = request.getAttribute(RequestAttributeNames.ERROR_UPLOAD, RequestAttributes.SCOPE_SESSION);
		if(bindingResult != null)
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);
		}

		model.addAttribute(ModelAttributes.CATEGORIES, categoryService.getAllEntitiesAsc());

		return ReturnValues.TRANSACTION_IMPORT;
	}

	@PostMapping("/upload")
	public String upload(WebRequest request,
						 @ModelAttribute("CsvImport") CsvImport csvImport,
						 BindingResult bindingResult)
	{
		if(csvImport.file().isEmpty())
		{
			removeAllAttributes(request);
			WebRequestUtils.putNotification(request, new Notification(Localization.getString("transactions.import.error.upload.empty"), NotificationType.ERROR));
			return ReturnValues.REDIRECT_IMPORT;
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
			WebRequestUtils.putNotification(request, new Notification(Localization.getString("transactions.import.error.upload", e.getMessage()), NotificationType.ERROR));
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
		final List<String> errors = new ArrayList<>();
		for(int i = 0; i < csvRows.size(); i++)
		{
			final CsvRow csvRow = csvRows.get(i);
			try
			{
				csvTransactions.add(transactionImportService.createCsvTransactionFromCsvRow(csvRow, csvColumnSettings, i));
			}
			catch(IndexOutOfBoundsException e)
			{
				LOGGER.error("Invalid access to column", e);
				errors.add(Localization.getString("transactions.import.error.column", i, csvRow));
			}
			catch(CsvTransactionParseException e)
			{
				errors.add(e.getMessage());
			}
		}

		request.setAttribute(RequestAttributeNames.ERRORS_COLUMN_SETTINGS, errors, RequestAttributes.SCOPE_SESSION);
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
		final Optional<CsvTransaction> transactionOptional = getTransactionByIndex(request, index);
		if(transactionOptional.isEmpty())
		{
			return ReturnValues.REDIRECT_IMPORT;
		}

		transactionOptional.get().setStatus(CsvTransactionStatus.SKIPPED);
		return ReturnValues.REDIRECT_IMPORT;
	}

	@GetMapping("/{index}/newTransaction/{type}")
	public String newTransaction(WebRequest request,
								 @PathVariable("index") Integer index,
								 @PathVariable("type") String type,
								 Model model)
	{
		final Optional<CsvTransaction> transactionOptional = getTransactionByIndex(request, index);
		if(transactionOptional.isEmpty())
		{
			return ReturnValues.REDIRECT_IMPORT;
		}

		final CsvTransaction csvTransaction = transactionOptional.get();
		request.setAttribute(RequestAttributeNames.CURRENT_CSV_TRANSACTION, csvTransaction, RequestAttributes.SCOPE_SESSION);

		final Transaction newTransaction = transactionImportService.createTransactionFromCsvTransaction(csvTransaction);

		transactionService.prepareModelNewOrEdit(model, false, csvTransaction.getDate(), false, newTransaction, accountService.getAllActivatedAccountsAsc());

		if(type.equals("transfer"))
		{
			return ReturnValues.NEW_TRANSACTION_TRANSFER;
		}
		return ReturnValues.NEW_TRANSACTION_NORMAL;
	}

	@GetMapping("/{index}/newFromTemplate")
	public String newFromTemplate(WebRequest request,
								  @PathVariable("index") Integer index)
	{
		final Optional<CsvTransaction> transactionOptional = getTransactionByIndex(request, index);
		if(transactionOptional.isEmpty())
		{
			return ReturnValues.REDIRECT_IMPORT;
		}

		final CsvTransaction csvTransaction = transactionOptional.get();
		request.setAttribute(RequestAttributeNames.CURRENT_CSV_TRANSACTION, csvTransaction, RequestAttributes.SCOPE_SESSION);

		return ReturnValues.REDIRECT_TEMPLATES;
	}

	@PostMapping("/{index}/newTransactionInPlace")
	public String newTransactionInPlace(WebRequest request,
										@PathVariable("index") Integer index,
										@ModelAttribute("NewTransactionInPlace") CsvTransaction newCsvTransaction)
	{
		final Optional<CsvTransaction> transactionOptional = getTransactionByIndex(request, index);
		if(transactionOptional.isEmpty())
		{
			return ReturnValues.REDIRECT_IMPORT;
		}

		final CsvTransaction csvTransaction = transactionOptional.get();
		csvTransaction.setStatus(CsvTransactionStatus.IMPORTED);

		transactionImportService.updateCsvTransaction(csvTransaction, newCsvTransaction);

		final Transaction newTransaction = transactionImportService.createTransactionFromCsvTransaction(csvTransaction);
		transactionService.getRepository().save(newTransaction);

		return ReturnValues.REDIRECT_IMPORT;
	}

	private void removeAllAttributes(WebRequest request)
	{
		request.removeAttribute(RequestAttributeNames.CSV_IMPORT, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.CSV_ROWS, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.CSV_TRANSACTIONS, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.ERROR_UPLOAD, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.ERRORS_COLUMN_SETTINGS, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.CURRENT_CSV_TRANSACTION, RequestAttributes.SCOPE_SESSION);
	}

	private Optional<CsvTransaction> getTransactionByIndex(WebRequest request, Integer index)
	{
		final Object attribute = request.getAttribute(RequestAttributeNames.CSV_TRANSACTIONS, RequestAttributes.SCOPE_SESSION);
		if(attribute == null)
		{
			return Optional.empty();
		}

		final List<CsvTransaction> csvTransactions = (List<CsvTransaction>) attribute;
		return Optional.of(csvTransactions.get(index));
	}
}