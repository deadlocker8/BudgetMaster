package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.repeating.RepeatingOptionRepository;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsRepository;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.RepeatingTransactionUpdater;
import de.deadlocker8.budgetmaster.repeating.endoption.*;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifier;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierType;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.filter.FilterHelpersService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Controller
public class TransactionController extends BaseController
{
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private RepeatingOptionRepository repeatingOptionRepository;

	@Autowired
	private RepeatingTransactionUpdater repeatingTransactionUpdater;

	@Autowired
	private HelpersService helpers;

	@Autowired
	private FilterHelpersService filterHelpers;


	@RequestMapping("/transactions")
	public String transactions(HttpServletRequest request, Model model, @CookieValue(value = "currentDate", required = false) String cookieDate)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);
		repeatingTransactionUpdater.updateRepeatingTransactions(date.dayOfMonth().withMaximumValue());

		FilterConfiguration filterConfiguration = filterHelpers.getFilterConfiguration(request);

		List<Transaction> transactions = transactionService.getTransactionsForMonthAndYear(helpers.getCurrentAccount(), date.getMonthOfYear(), date.getYear(), getSettings().isRestActivated(), filterConfiguration);
		int incomeSum = helpers.getIncomeSumForTransactionList(transactions);
		int paymentSum = helpers.getExpenditureSumForTransactionList(transactions);
		int rest = incomeSum + paymentSum;

		model.addAttribute("transactions", transactions);
		model.addAttribute("incomeSum", incomeSum);
		model.addAttribute("paymentSum", paymentSum);
		model.addAttribute("currentDate", date);
		model.addAttribute("rest", rest);
		model.addAttribute("filterConfiguration", filterConfiguration);
		model.addAttribute("settings", settingsRepository.findOne(0));

		return "transactions/transactions";
	}

	@RequestMapping("/transactions/{ID}/requestDelete")
	public String requestDeleteTransaction(HttpServletRequest request, Model model, @PathVariable("ID") Integer ID, @CookieValue("currentDate") String cookieDate)
	{
		if(!transactionService.isDeletable(ID))
		{
			return "redirect:/transactions";
		}

		DateTime date = helpers.getDateTimeFromCookie(cookieDate);
		FilterConfiguration filterConfiguration = filterHelpers.getFilterConfiguration(request);

		List<Transaction> transactions = transactionService.getTransactionsForMonthAndYear(helpers.getCurrentAccount(), date.getMonthOfYear(), date.getYear(), getSettings().isRestActivated(), filterConfiguration);
		int incomeSum = helpers.getIncomeSumForTransactionList(transactions);
		int paymentSum = helpers.getExpenditureSumForTransactionList(transactions);
		int rest = incomeSum + paymentSum;

		model.addAttribute("transactions", transactions);
		model.addAttribute("incomeSum", incomeSum);
		model.addAttribute("paymentSum", paymentSum);
		model.addAttribute("currentDate", date);
		model.addAttribute("currentTransaction", transactionRepository.getOne(ID));
		model.addAttribute("rest", rest);
		model.addAttribute("filterConfiguration", filterConfiguration);
		model.addAttribute("settings", settingsRepository.findOne(0));

		return "transactions/transactions";
	}

	@RequestMapping("/transactions/{ID}/delete")
	public String deleteTransaction(Model model, @PathVariable("ID") Integer ID)
	{
		transactionService.deleteTransaction(ID);
		return "redirect:/transactions";
	}

	@RequestMapping("/transactions/newTransaction/normal")
	public String newTransactionNormal(Model model, @CookieValue("currentDate") String cookieDate)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);
		Transaction emptyTransaction = new Transaction();
		emptyTransaction.setCategory(categoryRepository.findByType(CategoryType.NONE));
		model.addAttribute("currentDate", date);
		model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("transaction", emptyTransaction);
		model.addAttribute("settings", settingsRepository.findOne(0));
		return "transactions/newTransactionNormal";
	}

	@RequestMapping("/transactions/newTransaction/repeating")
	public String newTransactionRepeating(Model model, @CookieValue("currentDate") String cookieDate)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);
		Transaction emptyTransaction = new Transaction();
		emptyTransaction.setCategory(categoryRepository.findByType(CategoryType.NONE));
		model.addAttribute("currentDate", date);
		model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("transaction", emptyTransaction);
		model.addAttribute("settings", settingsRepository.findOne(0));
		return "transactions/newTransactionRepeating";
	}

	@SuppressWarnings("ConstantConditions")
	@RequestMapping(value = "/transactions/newTransaction/normal", method = RequestMethod.POST)
	public String post(Model model, @CookieValue("currentDate") String cookieDate,
					   @ModelAttribute("NewTransaction") Transaction transaction, BindingResult bindingResult,
					   @RequestParam(value = "isRepeating", required = false) boolean isRepeating,
					   @RequestParam(value = "isPayment", required = false) boolean isPayment)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);

		TransactionValidator transactionValidator = new TransactionValidator();
		transactionValidator.validate(transaction, bindingResult);

		if(transaction.getAmount() == null)
		{
			transaction.setAmount(0);
		}

		if(isPayment)
		{
			transaction.setAmount(-Math.abs(transaction.getAmount()));
		}
		else
		{
			transaction.setAmount(Math.abs(transaction.getAmount()));
		}

		List<Tag> tags = transaction.getTags();
		if(tags != null)
		{
			transaction.setTags(new ArrayList<>());
			for(Tag currentTag : tags)
			{
				//noinspection ConstantConditions
				transaction = addTagForTransaction(currentTag.getName(), transaction);
			}
		}

		transaction.setRepeatingOption(null);

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("currentDate", date);
			model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
			model.addAttribute("accounts", accountService.getAllAccountsAsc());
			model.addAttribute("transaction", transaction);
			model.addAttribute("settings", settingsRepository.findOne(0));
			return "transactions/newTransactionNormal";
		}

		transactionRepository.save(transaction);
		return "redirect:/transactions";
	}

	@SuppressWarnings("ConstantConditions")
	@RequestMapping(value = "/transactions/newTransaction/repeating", method = RequestMethod.POST)
	public String post(Model model, @CookieValue("currentDate") String cookieDate,
					   @ModelAttribute("NewTransaction") Transaction transaction, BindingResult bindingResult,
					   @RequestParam(value = "isRepeating", required = false) boolean isRepeating,
					   @RequestParam(value = "isPayment", required = false) boolean isPayment,
					   @RequestParam(value = "repeatingModifierNumber", required = false) int repeatingModifierNumber,
					   @RequestParam(value = "repeatingModifierType", required = false) String repeatingModifierType,
					   @RequestParam(value = "repeatingEndType", required = false) String repeatingEndType,
					   @RequestParam(value = "repeatingEndValue", required = false) String repeatingEndValue)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);

		// handle repeating transactions
		if(transaction.getID() != null && isRepeating)
		{
			transactionService.deleteTransaction(transaction.getID());
		}

		TransactionValidator transactionValidator = new TransactionValidator();
		transactionValidator.validate(transaction, bindingResult);

		if(transaction.getAmount() == null)
		{
			transaction.setAmount(0);
		}

		if(isPayment)
		{
			transaction.setAmount(-Math.abs(transaction.getAmount()));
		}
		else
		{
			transaction.setAmount(Math.abs(transaction.getAmount()));
		}

		List<Tag> tags = transaction.getTags();
		if(tags != null)
		{
			transaction.setTags(new ArrayList<>());
			for(Tag currentTag : tags)
			{
				//noinspection ConstantConditions
				transaction = addTagForTransaction(currentTag.getName(), transaction);
			}
		}

		RepeatingOption repeatingOption;
		RepeatingModifierType type = RepeatingModifierType.getByLocalization(repeatingModifierType);
		RepeatingModifier repeatingModifier = RepeatingModifier.fromModifierType(type, repeatingModifierNumber);

		RepeatingEnd repeatingEnd = null;
		RepeatingEndType endType = RepeatingEndType.getByLocalization(repeatingEndType);
		switch(endType)
		{
			case NEVER:
				repeatingEnd = new RepeatingEndNever();
				break;
			case AFTER_X_TIMES:
				repeatingEnd = new RepeatingEndAfterXTimes(Integer.parseInt(repeatingEndValue));
				break;
			case DATE:
				DateTime endDate = DateTime.parse(repeatingEndValue, DateTimeFormat.forPattern("dd.MM.yy").withLocale(getSettings().getLanguage().getLocale()));
				repeatingEnd = new RepeatingEndDate(endDate);
				break;
		}

		repeatingOption = new RepeatingOption(transaction.getDate(), repeatingModifier, repeatingEnd);
		transaction.setRepeatingOption(repeatingOption);

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("currentDate", date);
			model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
			model.addAttribute("accounts", accountService.getAllAccountsAsc());
			model.addAttribute("transaction", transaction);
			model.addAttribute("settings", settingsRepository.findOne(0));
			return "transactions/newTransactionNormal";
		}

		transactionRepository.save(transaction);
		return "redirect:/transactions";
	}


	@RequestMapping("/transactions/{ID}/edit")
	public String editTransaction(Model model, @CookieValue("currentDate") String cookieDate, @PathVariable("ID") Integer ID)
	{
		Transaction transaction = transactionRepository.findOne(ID);
		if(transaction == null)
		{
			throw new ResourceNotFoundException();
		}

		// select first transaction in order to provide correct start date for repeating transactions
		if(transaction.getRepeatingOption() != null)
		{
			transaction = transaction.getRepeatingOption().getReferringTransactions().get(0);
		}

		DateTime date = helpers.getDateTimeFromCookie(cookieDate);
		model.addAttribute("currentDate", date);
		model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("transaction", transaction);
		model.addAttribute("settings", settingsRepository.findOne(0));

		if(transaction.isRepeating())
		{
			return "transactions/newTransactionRepeating";
		}
		return "transactions/newTransactionNormal";
	}

	private Settings getSettings()
	{
		return settingsRepository.findOne(0);
	}

	private Transaction addTagForTransaction(String name, Transaction transaction)
	{
		if(tagRepository.findByName(name) == null)
		{
			tagRepository.save(new Tag(name));
		}

		List<Transaction> referringTransactions = tagRepository.findByName(name).getReferringTransactions();
		if(referringTransactions == null || !referringTransactions.contains(transaction))
		{
			transaction.getTags().add(tagRepository.findByName(name));
		}

		return transaction;
	}

	private void removeTagForTransaction(String name, Transaction transaction)
	{
		Tag currentTag = tagRepository.findByName(name);
		currentTag.getReferringTransactions().remove(transaction);
		tagRepository.save(currentTag);
	}
}