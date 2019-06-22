package de.deadlocker8.budgetmaster.search;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionSearchSpecifications;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	private final SettingsService settingsService;

	@Autowired
	public SearchController(TransactionService transactionService, SettingsService settingsService)
	{
		this.transactionService = transactionService;
		this.settingsService = settingsService;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(Model model, Search search)
	{
		if(search.isEmptySearch())
		{
			search = Search.DEFAULT;
		}

		Specification<Transaction> specification = TransactionSearchSpecifications.withDynamicQuery(search);
		System.out.println(settingsService.getSettings());
		Page<Transaction> resultPage = transactionService.getRepository().findAll(specification, new PageRequest(search.getPage(), settingsService.getSettings().getSearchItemsPerPage()));
		model.addAttribute("page", resultPage);
		model.addAttribute("search", search);
		return "search/search";
	}
}