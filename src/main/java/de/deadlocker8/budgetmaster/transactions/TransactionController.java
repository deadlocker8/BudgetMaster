package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
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
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.Localization;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(Mappings.TRANSACTIONS)
public class TransactionController extends BaseController
{
	private final TransactionService transactionService;
	private final CategoryService categoryService;
	private final AccountService accountService;
	private final SettingsService settingsService;
	private final RepeatingTransactionUpdater repeatingTransactionUpdater;
	private final HelpersService helpers;
	private final DateService dateService;
	private final FilterHelpersService filterHelpers;

	@Autowired
	public TransactionController(TransactionService transactionService, CategoryService categoryService, AccountService accountService, SettingsService settingsService, RepeatingTransactionUpdater repeatingTransactionUpdater, HelpersService helpers, DateService dateService, FilterHelpersService filterHelpers)
	{
		this.transactionService = transactionService;
		this.categoryService = categoryService;
		this.accountService = accountService;
		this.settingsService = settingsService;
		this.repeatingTransactionUpdater = repeatingTransactionUpdater;
		this.helpers = helpers;
		this.dateService = dateService;
		this.filterHelpers = filterHelpers;
	}

	@GetMapping
	public String transactions(HttpServletRequest request, Model model, @CookieValue(value = "currentDate", required = false) String cookieDate)
	{
		DateTime date = dateService.getDateTimeFromCookie(cookieDate);
		repeatingTransactionUpdater.updateRepeatingTransactions(date.dayOfMonth().withMaximumValue());

		prepareModelTransactions(filterHelpers.getFilterConfiguration(request), model, date);

		return "transactions/transactions";
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteTransaction(HttpServletRequest request, Model model, @PathVariable("ID") Integer ID, @CookieValue("currentDate") String cookieDate)
	{
		if(!transactionService.isDeletable(ID))
		{
			return "redirect:/transactions";
		}

		DateTime date = dateService.getDateTimeFromCookie(cookieDate);
		prepareModelTransactions(filterHelpers.getFilterConfiguration(request), model, date);
		model.addAttribute("transactionToDelete", transactionService.getRepository().getById(ID));

		return "transactions/deleteTransactionModal";
	}

	private void prepareModelTransactions(FilterConfiguration filterConfiguration, Model model, DateTime date)
	{
		Account currentAccount = helpers.getCurrentAccount();
		List<Transaction> transactions = transactionService.getTransactionsForMonthAndYear(currentAccount, date.getMonthOfYear(), date.getYear(), settingsService.getSettings().isRestActivated(), filterConfiguration);

		model.addAttribute("transactions", transactions);
		model.addAttribute("account", currentAccount);
		model.addAttribute("budget", helpers.getBudget(transactions, currentAccount));
		model.addAttribute("currentDate", date);
		model.addAttribute("filterConfiguration", filterConfiguration);
	}

	@GetMapping("/{ID}/delete")
	public String deleteTransaction(WebRequest request, @PathVariable("ID") Integer ID)
	{
		final Transaction transactionToDelete = transactionService.getRepository().getById(ID);
		transactionService.deleteTransaction(ID);

		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.transaction.delete.success", transactionToDelete.getName()), NotificationType.SUCCESS));

		return "redirect:/transactions";
	}

	@GetMapping("/newTransaction/{type}")
	public String newTransaction(WebRequest request, Model model, @CookieValue("currentDate") String cookieDate, @PathVariable String type)
	{
		final AccountState accountState = helpers.getCurrentAccount().getAccountState();
		if(accountState != AccountState.FULL_ACCESS)
		{
			WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.transaction.add.warning", Localization.getString(accountState.getLocalizationKey())), NotificationType.WARNING));
			return "redirect:/transactions";
		}

		DateTime date = dateService.getDateTimeFromCookie(cookieDate);
		Transaction emptyTransaction = new Transaction();
		emptyTransaction.setCategory(categoryService.findByType(CategoryType.NONE));
		transactionService.prepareModelNewOrEdit(model, false, date, false, emptyTransaction, accountService.getAllActivatedAccountsAsc());
		return "transactions/newTransaction" + StringUtils.capitalize(type);
	}

	@PostMapping(value = "/newTransaction")
	public String post(Model model, @CookieValue("currentDate") String cookieDate,
					   @ModelAttribute("NewTransaction") Transaction transaction, BindingResult bindingResult,
					   @RequestParam(value = "isRepeating", required = false) boolean isRepeating,
					   @RequestParam(value = "repeatingModifierNumber", required = false, defaultValue = "0") int repeatingModifierNumber,
					   @RequestParam(value = "repeatingModifierType", required = false) String repeatingModifierType,
					   @RequestParam(value = "repeatingEndType", required = false) String repeatingEndType,
					   @RequestParam(value = "repeatingEndValue", required = false) String repeatingEndValue)
	{
		DateTime date = dateService.getDateTimeFromCookie(cookieDate);

		handlePreviousType(transaction, isRepeating);

		TransactionValidator transactionValidator = new TransactionValidator();
		transactionValidator.validate(transaction, bindingResult);

		transactionService.handleAmount(transaction);
		transactionService.handleTags(transaction);

		if(isRepeating)
		{
			final RepeatingOption repeatingOption = createRepeatingOption(transaction.getDate(), repeatingModifierNumber, repeatingModifierType, repeatingEndType, repeatingEndValue);
			transaction.setRepeatingOption(repeatingOption);
		}
		else
		{
			transaction.setRepeatingOption(null);
		}

		String redirectUrl;
		if(transaction.isTransfer())
		{
			redirectUrl = "transactions/newTransactionTransfer";
		}
		else
		{
			redirectUrl = "transactions/newTransactionNormal";
		}

		return handleRedirect(model, transaction.getID() != null, transaction, bindingResult, date, redirectUrl);
	}

	private void handlePreviousType(Transaction transaction, boolean isRepeating)
	{
		if(transaction.getID() != null && isRepeating)
		{
			transactionService.deleteTransaction(transaction.getID());
		}
	}

	@SuppressWarnings("ConstantConditions")
	private RepeatingOption createRepeatingOption(DateTime startDate, int repeatingModifierNumber, String repeatingModifierType, String repeatingEndType, String repeatingEndValue)
	{
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

		return new RepeatingOption(startDate, repeatingModifier, repeatingEnd);
	}

	private String handleRedirect(Model model, boolean isEdit, @ModelAttribute("NewTransaction") Transaction transaction, BindingResult bindingResult, DateTime date, String url)
	{
		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			transactionService.prepareModelNewOrEdit(model, isEdit, date, false, transaction, accountService.getAllActivatedAccountsAsc());
			return url;
		}

		transactionService.getRepository().save(transaction);
		return "redirect:/transactions";
	}

	@GetMapping("/{ID}/edit")
	public String editTransaction(Model model, @CookieValue("currentDate") String cookieDate, @PathVariable("ID") Integer ID)
	{
		Optional<Transaction> transactionOptional = transactionService.getRepository().findById(ID);
		if(transactionOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		Transaction transaction = transactionOptional.get();

		if(transaction.getAccount().getAccountState() != AccountState.FULL_ACCESS)
		{
			return "redirect:/transactions";
		}

		// select first transaction in order to provide correct start date for repeating transactions
		if(transaction.getRepeatingOption() != null)
		{
			transaction = transaction.getRepeatingOption().getReferringTransactions().get(0);
		}

		DateTime date = dateService.getDateTimeFromCookie(cookieDate);
		transactionService.prepareModelNewOrEdit(model, true, date, false, transaction, accountService.getAllActivatedAccountsAsc());

		if(transaction.isTransfer())
		{
			return "transactions/newTransactionTransfer";
		}
		return "transactions/newTransactionNormal";
	}

	@GetMapping("/{ID}/highlight")
	public String highlight(Model model, @PathVariable("ID") Integer ID)
	{
		Transaction transaction = transactionService.getRepository().getById(ID);

		Account currentAccount = helpers.getCurrentAccount();
		if(currentAccount.getType() != AccountType.ALL || transaction.isTransfer())
		{
			accountService.selectAccount(transaction.getAccount().getID());
		}

		repeatingTransactionUpdater.updateRepeatingTransactions(transaction.getDate().dayOfMonth().withMaximumValue());

		FilterConfiguration filterConfiguration = FilterConfiguration.DEFAULT;
		filterConfiguration.setFilterCategories(filterHelpers.getFilterCategories());
		filterConfiguration.setFilterTags(filterHelpers.getFilterTags());

		prepareModelTransactions(filterConfiguration, model, transaction.getDate());
		model.addAttribute("highlightID", ID);
		return "transactions/transactions";
	}

	@GetMapping("/{ID}/changeTypeModal")
	public String changeTypeModal(Model model, @PathVariable("ID") Integer ID)
	{
		final Optional<Transaction> transactionOptional = transactionService.getRepository().findById(ID);
		if(transactionOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		model.addAttribute("transaction", transactionOptional.get());
		return "transactions/changeTypeModal";
	}

	@GetMapping("/{ID}/changeType")
	public String changeTypeModal(Model model, @PathVariable("ID") Integer ID,
								  @CookieValue("currentDate") String cookieDate,
								  @RequestParam(value = "newType") int newType)
	{
		final Optional<Transaction> transactionOptional = transactionService.getRepository().findById(ID);
		if(transactionOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		final Optional<TransactionType> transactionTypeOptional = TransactionType.getByID(newType);
		if(transactionTypeOptional.isEmpty())
		{
			throw new IllegalArgumentException();
		}

		Transaction transaction = transactionOptional.get();
		// select first transaction in order to provide correct start date for repeating transactions
		if(transaction.getRepeatingOption() != null)
		{
			transaction = transaction.getRepeatingOption().getReferringTransactions().get(0);
		}

		Transaction transactionCopy = new Transaction(transaction);
		final TransactionType newTransactionType = transactionTypeOptional.get();
		LOGGER.debug(MessageFormat.format("Changing transaction type to {0} for transaction with ID {1}", newTransactionType, String.valueOf(transaction.getID())));

		String redirectUrl = "";
		switch(newTransactionType)
		{
			case NORMAL:
				transactionCopy.setTransferAccount(null);
				redirectUrl = "transactions/newTransactionNormal";
				break;
			case TRANSFER:
				redirectUrl = "transactions/newTransactionTransfer";
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + newTransactionType);
		}

		DateTime date = dateService.getDateTimeFromCookie(cookieDate);
		transactionService.prepareModelNewOrEdit(model, true, date, true, transactionCopy, accountService.getAllActivatedAccountsAsc());

		return redirectUrl;
	}
}