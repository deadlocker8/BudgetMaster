package de.deadlocker8.budgetmaster.transactions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.filter.FilterHelpersService;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.RepeatingTransactionUpdater;
import de.deadlocker8.budgetmaster.repeating.endoption.*;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifier;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierType;
import de.deadlocker8.budgetmaster.services.DateFormatStyle;
import de.deadlocker8.budgetmaster.services.DateService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.tags.TagService;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TransactionController extends BaseController
{
	private static final int MAX_SUGGESTIONS = 25;
	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	private final TransactionService transactionService;
	private final CategoryService categoryService;
	private final AccountService accountService;
	private final SettingsService settingsService;
	private final TagService tagService;
	private final RepeatingTransactionUpdater repeatingTransactionUpdater;
	private final HelpersService helpers;
	private final DateService dateService;
	private final FilterHelpersService filterHelpers;

	@Autowired
	public TransactionController(TransactionService transactionService, CategoryService categoryService, AccountService accountService, SettingsService settingsService, TagService tagService, RepeatingTransactionUpdater repeatingTransactionUpdater, HelpersService helpers, DateService dateService, FilterHelpersService filterHelpers)
	{
		this.transactionService = transactionService;
		this.categoryService = categoryService;
		this.accountService = accountService;
		this.settingsService = settingsService;
		this.tagService = tagService;
		this.repeatingTransactionUpdater = repeatingTransactionUpdater;
		this.helpers = helpers;
		this.dateService = dateService;
		this.filterHelpers = filterHelpers;
	}

	@RequestMapping("/transactions")
	public String transactions(HttpServletRequest request, Model model, @CookieValue(value = "currentDate", required = false) String cookieDate)
	{
		DateTime date = dateService.getDateTimeFromCookie(cookieDate);
		repeatingTransactionUpdater.updateRepeatingTransactions(date.dayOfMonth().withMaximumValue());

		prepareModelTransactions(filterHelpers.getFilterConfiguration(request), model, date);

		return "transactions/transactions";
	}

	@RequestMapping("/transactions/{ID}/requestDelete")
	public String requestDeleteTransaction(HttpServletRequest request, Model model, @PathVariable("ID") Integer ID, @CookieValue("currentDate") String cookieDate)
	{
		if(!transactionService.isDeletable(ID))
		{
			return "redirect:/transactions";
		}

		DateTime date = dateService.getDateTimeFromCookie(cookieDate);
		prepareModelTransactions(filterHelpers.getFilterConfiguration(request), model, date);
		model.addAttribute("currentTransaction", transactionService.getRepository().getOne(ID));

		return "transactions/transactions";
	}

	private void prepareModelTransactions(FilterConfiguration filterConfiguration, Model model, DateTime date)
	{
		List<Transaction> transactions = transactionService.getTransactionsForMonthAndYear(helpers.getCurrentAccount(), date.getMonthOfYear(), date.getYear(), settingsService.getSettings().isRestActivated(), filterConfiguration);
		Account currentAccount = helpers.getCurrentAccount();

		model.addAttribute("transactions", transactions);
		model.addAttribute("account", currentAccount);
		model.addAttribute("budget", helpers.getBudget(transactions, currentAccount));
		model.addAttribute("currentDate", date);
		model.addAttribute("filterConfiguration", filterConfiguration);
		model.addAttribute("settings", settingsService.getSettings());
	}

	@RequestMapping("/transactions/{ID}/delete")
	public String deleteTransaction(@PathVariable("ID") Integer ID)
	{
		transactionService.deleteTransaction(ID);
		return "redirect:/transactions";
	}

	@RequestMapping("/transactions/newTransaction/{type}")
	public String newTransaction(Model model, @CookieValue("currentDate") String cookieDate, @PathVariable String type)
	{
		DateTime date = dateService.getDateTimeFromCookie(cookieDate);
		Transaction emptyTransaction = new Transaction();
		emptyTransaction.setCategory(categoryService.getRepository().findByType(CategoryType.NONE));
		prepareModelNewOrEdit(model, date, emptyTransaction, true);
		return "transactions/newTransaction" + StringUtils.capitalize(type);
	}

	@PostMapping(value = "/transactions/newTransaction/normal")
	public String postNormal(Model model, @RequestParam String action,
							 @CookieValue("currentDate") String cookieDate,
							 @ModelAttribute("NewTransaction") Transaction transaction, BindingResult bindingResult,
							 @RequestParam(value = "isPayment", required = false) boolean isPayment)
	{
		if(action.equals(TransactionSubmitAction.TEMPLATE.getActionName()))
		{
			LOGGER.debug("Received request to create template from existing normal transaction");
			return "redirect:/transactions";
		}

		if(action.equals(TransactionSubmitAction.SAVE.getActionName()))
		{
			DateTime date = dateService.getDateTimeFromCookie(cookieDate);

			TransactionValidator transactionValidator = new TransactionValidator();
			transactionValidator.validate(transaction, bindingResult);

			handleAmount(transaction, isPayment);
			handleTags(transaction);

			transaction.setRepeatingOption(null);

			return handleRedirect(model, transaction, bindingResult, date, "transactions/newTransactionNormal", isPayment);
		}

		return "redirect:/transactions";
	}

	@SuppressWarnings("ConstantConditions")
	@PostMapping(value = "/transactions/newTransaction/repeating")
	public String postRepeating(Model model, @CookieValue("currentDate") String cookieDate,
								@ModelAttribute("NewTransaction") Transaction transaction, BindingResult bindingResult,
								@RequestParam(value = "isRepeating", required = false) boolean isRepeating,
								@RequestParam(value = "isPayment", required = false) boolean isPayment,
								@RequestParam(value = "repeatingModifierNumber", required = false) int repeatingModifierNumber,
								@RequestParam(value = "repeatingModifierType", required = false) String repeatingModifierType,
								@RequestParam(value = "repeatingEndType", required = false) String repeatingEndType,
								@RequestParam(value = "repeatingEndValue", required = false) String repeatingEndValue)
	{
		DateTime date = dateService.getDateTimeFromCookie(cookieDate);

		// handle repeating transactions
		if(transaction.getID() != null && isRepeating)
		{
			transactionService.deleteTransaction(transaction.getID());
		}

		TransactionValidator transactionValidator = new TransactionValidator();
		transactionValidator.validate(transaction, bindingResult);

		handleAmount(transaction, isPayment);
		handleTags(transaction);

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
				DateTime endDate = DateTime.parse(repeatingEndValue, DateTimeFormat.forPattern(DateFormatStyle.NORMAL.getKey()).withLocale(settingsService.getSettings().getLanguage().getLocale()));
				repeatingEnd = new RepeatingEndDate(endDate);
				break;
		}

		repeatingOption = new RepeatingOption(transaction.getDate(), repeatingModifier, repeatingEnd);
		transaction.setRepeatingOption(repeatingOption);

		return handleRedirect(model, transaction, bindingResult, date, "transactions/newTransactionRepeating", isPayment);
	}

	@PostMapping(value = "/transactions/newTransaction/transfer")
	public String postTransfer(Model model, @RequestParam String action,
							   @CookieValue("currentDate") String cookieDate,
							   @ModelAttribute("NewTransaction") Transaction transaction, BindingResult bindingResult,
							   @RequestParam(value = "isPayment", required = false) boolean isPayment)
	{
		if(action.equals(TransactionSubmitAction.TEMPLATE.getActionName()))
		{
			LOGGER.debug("Received request to create template from existing transfer");
			return "redirect:/transactions";
		}

		if(action.equals(TransactionSubmitAction.SAVE.getActionName()))
		{
			DateTime date = dateService.getDateTimeFromCookie(cookieDate);

			TransactionValidator transactionValidator = new TransactionValidator();
			transactionValidator.validate(transaction, bindingResult);

			handleAmount(transaction, isPayment);
			handleTags(transaction);

			transaction.setRepeatingOption(null);

			return handleRedirect(model, transaction, bindingResult, date, "transactions/newTransactionTransfer", isPayment);
		}

		return "redirect:/transactions";
	}

	private void handleAmount(Transaction transaction, boolean isPayment)
	{
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
	}

	private void handleTags(Transaction transaction)
	{
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
	}

	private String handleRedirect(Model model, @ModelAttribute("NewTransaction") Transaction transaction, BindingResult bindingResult, DateTime date, String url, boolean isPayment)
	{
		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			prepareModelNewOrEdit(model, date, transaction, isPayment);
			return url;
		}

		transactionService.getRepository().save(transaction);
		return "redirect:/transactions";
	}

	@RequestMapping("/transactions/{ID}/edit")
	public String editTransaction(Model model, @CookieValue("currentDate") String cookieDate, @PathVariable("ID") Integer ID)
	{
		Optional<Transaction> transactionOptional = transactionService.getRepository().findById(ID);
		if(!transactionOptional.isPresent())
		{
			throw new ResourceNotFoundException();
		}

		Transaction transaction = transactionOptional.get();

		// select first transaction in order to provide correct start date for repeating transactions
		if(transaction.getRepeatingOption() != null)
		{
			transaction = transaction.getRepeatingOption().getReferringTransactions().get(0);
		}

		DateTime date = dateService.getDateTimeFromCookie(cookieDate);
		prepareModelNewOrEdit(model, date, transaction, transaction.getAmount() <= 0);

		if(transaction.isRepeating())
		{
			return "transactions/newTransactionRepeating";
		}

		if(transaction.isTransfer())
		{
			return "transactions/newTransactionTransfer";
		}
		return "transactions/newTransactionNormal";
	}

	private void prepareModelNewOrEdit(Model model, DateTime date, Transaction emptyTransaction, boolean isPayment)
	{
		model.addAttribute("currentDate", date);
		model.addAttribute("categories", categoryService.getRepository().findAllByOrderByNameAsc());
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("transaction", emptyTransaction);
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("isPayment", isPayment);

		final List<Transaction> allByOrderByDateDesc = transactionService.getRepository().findAllByOrderByDateDesc();
		final List<String> nameSuggestions = allByOrderByDateDesc.stream()
				.map(Transaction::getName)
				.distinct()
				.limit(MAX_SUGGESTIONS)
				.collect(Collectors.toList());
		model.addAttribute("suggestionsJSON", GSON.toJson(nameSuggestions));
	}

	private Transaction addTagForTransaction(String name, Transaction transaction)
	{
		TagRepository tagRepository = tagService.getRepository();

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

	@RequestMapping("/transactions/{ID}/highlight")
	public String highlight(Model model, @PathVariable("ID") Integer ID)
	{
		Transaction transaction = transactionService.getRepository().getOne(ID);
		accountService.selectAccount(transaction.getAccount().getID());
		repeatingTransactionUpdater.updateRepeatingTransactions(transaction.getDate().dayOfMonth().withMaximumValue());

		FilterConfiguration filterConfiguration = FilterConfiguration.DEFAULT;
		filterConfiguration.setFilterCategories(filterHelpers.getFilterCategories());
		filterConfiguration.setFilterTags(filterHelpers.getFilterTags());

		prepareModelTransactions(filterConfiguration, model, transaction.getDate());
		model.addAttribute("highlightID", ID);
		return "transactions/transactions";
	}
}