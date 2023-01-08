package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.transactions.csvImport.CsvParser;
import de.deadlocker8.budgetmaster.transactions.csvImport.CsvRow;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping(Mappings.TRANSACTION_IMPORT)
public class TransactionImportController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String ERROR_UPLOAD = "errorUpload";
	}

	private static class ReturnValues
	{
		public static final String TRANSACTION_IMPORT = "transactions/transactionImport";
		public static final String REDIRECT_CANCEL = "redirect:/transactionImport/cancel";

	}

	private static class RequestAttributeNames
	{
		public static final String IMPORTED_FILE = "importedFile";
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
		return ReturnValues.TRANSACTION_IMPORT;
	}

	@PostMapping("/upload")
	public String upload(WebRequest request, Model model, @RequestParam("file") MultipartFile file)
	{
		if(file.isEmpty())
		{
			return ReturnValues.REDIRECT_CANCEL;
		}

		try
		{
			final String csvString = new String(file.getBytes(), StandardCharsets.UTF_8);
			final List<CsvRow> csvRows = CsvParser.parseCsv(csvString, ';');

			String fileName = file.getOriginalFilename();
			if(fileName == null)
			{
				fileName = file.getName();
			}

			request.setAttribute(RequestAttributeNames.IMPORTED_FILE, fileName, RequestAttributes.SCOPE_SESSION);
			request.setAttribute(RequestAttributeNames.CSV_ROWS, csvRows, RequestAttributes.SCOPE_SESSION);
		}
		catch(Exception e)
		{
			LOGGER.error("CSV upload failed", e);

			model.addAttribute(ModelAttributes.ERROR_UPLOAD, e.getMessage());
		}

		return ReturnValues.TRANSACTION_IMPORT;
	}

	@GetMapping("/cancel")
	public String cancel(WebRequest request)
	{
		request.removeAttribute(RequestAttributeNames.IMPORTED_FILE, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.CSV_ROWS, RequestAttributes.SCOPE_SESSION);

		return ReturnValues.TRANSACTION_IMPORT;
	}
}