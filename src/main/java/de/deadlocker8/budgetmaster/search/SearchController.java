package de.deadlocker8.budgetmaster.search;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionSearchSpecifications;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class SearchController extends BaseController
{
	private final TransactionService transactionService;

	@Autowired
	public SearchController(TransactionService transactionService)
	{
		this.transactionService = transactionService;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(Model model, @RequestParam(defaultValue = "") String searchText)
	{
		Specification<Transaction> specification = TransactionSearchSpecifications.withDynamicQuery(searchText);
		List<Transaction> transactions = transactionService.getRepository().findAll(specification);
		model.addAttribute("transactions", transactions);
		model.addAttribute("searchText", searchText);
		return "search/search";
	}
}