package de.deadlocker8.budgetmaster.transactions.keywords;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(Mappings.KEYWORDS)
public class TransactionNameKeywordController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String KEYWORD = "transactionNameKeyword";
	}

	private static class ReturnValues
	{
		public static final String KEYWORD_WARNING = "transactions/transactionNameKeywordWarningModal";
		public static final String EMPTY = "transactions/empty";
	}

	private final TransactionNameKeywordService transactionNameKeywordService;

	@Autowired
	public TransactionNameKeywordController(TransactionNameKeywordService transactionNameKeywordService)
	{
		this.transactionNameKeywordService = transactionNameKeywordService;
	}

	@GetMapping("/keywordCheck")
	public String keywordCheck(HttpServletResponse response, Model model, @RequestParam(value = "transactionName", required = false) String transactionName)
	{
		final List<String> matchingKeywords = transactionNameKeywordService.getMatchingKeywords(transactionName);

		if(matchingKeywords.isEmpty())
		{
			response.setStatus(204);
			return null;
		}
		else
		{
			model.addAttribute(ModelAttributes.KEYWORD, matchingKeywords.get(0));
			return ReturnValues.KEYWORD_WARNING;
		}
	}
}