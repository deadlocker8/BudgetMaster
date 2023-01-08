package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(Mappings.TRANSACTION_IMPORT)
public class TransactionImportController extends BaseController
{
	private static class ReturnValues
	{
		public static final String TRANSACTION_IMPORT = "transactions/transactionImport";
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
}