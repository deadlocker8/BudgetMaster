package de.deadlocker8.budgetmaster.search;

import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@Controller
public class SearchController extends BaseController
{
	private final TransactionService transactionService;
	private final AccountService accountService;

	@Autowired
	public SearchController(TransactionService transactionService, AccountService accountService)
	{
		this.transactionService = transactionService;
		this.accountService = accountService;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String get(Model model, @ModelAttribute("NewSearch") Search search)
	{
		return "redirect:/";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String post(Model model, @ModelAttribute("NewSearch") Search search)
	{
		List<Transaction> transactions = transactionService.getTransactionsForAccountUntilDate(accountService.getRepository().findAllByType(AccountType.ALL).get(0), DateTime.now(), FilterConfiguration.DEFAULT);
		model.addAttribute("transactions", transactions);
		model.addAttribute("searchText", search.getSearchText());
		return "search/search";
	}
}