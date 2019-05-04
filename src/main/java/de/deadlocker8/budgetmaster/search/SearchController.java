package de.deadlocker8.budgetmaster.search;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionSearchSpecifications;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(Model model, @ModelAttribute("NewSearch") Search search)
	{
		System.out.println(search);
		Specification<Transaction> specification = TransactionSearchSpecifications.withDynamicQuery(search);
		List<Transaction> transactions = transactionService.getRepository().findAll(specification);
		model.addAttribute("transactions", transactions);
		model.addAttribute("search", search);
		return "search/search";
	}
}