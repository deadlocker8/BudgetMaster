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
import de.deadlocker8.budgetmaster.utils.notification.NotificationLinkBuilder;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Controller
@RequestMapping(Mappings.TRANSACTIONS)
public class TransactionController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String KEYWORD = "keyword";
	}

	private static class ReturnValues
	{
		public static final String ALL_ENTITIES = "transactions/transactions";
		public static final String REDIRECT_ALL_ENTITIES = "redirect:/transactions";
		public static final String NEW_ENTITY = "transactions/newTransaction";
		public static final String DELETE_ENTITY = "transactions/deleteTransactionModal";
		public static final String REDIRECT_NEW_TRANSFER = "redirect:/transactions/newTransaction/transfer";
		public static final String NEW_TRANSFER = "transactions/newTransactionTransfer";
		public static final String REDIRECT_NEW_TRANSACTION = "redirect:/transactions/newTransaction/normal";
		public static final String NEW_TRANSACTION = "transactions/newTransactionNormal";
		public static final String CHANGE_TYPE = "transactions/changeTypeModal";
		public static final String KEYWORD_WARNING = "transactions/transactionNameKeywordWarningModal";
	}

	private static final String CONTINUE = "continue";
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
		LocalDate date = dateService.getDateTimeFromCookie(cookieDate);
		repeatingTransactionUpdater.updateRepeatingTransactions(date.with(lastDayOfMonth()));

		prepareModelTransactions(filterHelpers.getFilterConfiguration(request), model, date);

		return ReturnValues.ALL_ENTITIES;
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteTransaction(HttpServletRequest request, Model model, @PathVariable("ID") Integer ID, @CookieValue("currentDate") String cookieDate)
	{
		if(!transactionService.isDeletable(ID))
		{
			return ReturnValues.REDIRECT_ALL_ENTITIES;
		}

		LocalDate date = dateService.getDateTimeFromCookie(cookieDate);
		prepareModelTransactions(filterHelpers.getFilterConfiguration(request), model, date);
		model.addAttribute(TransactionModelAttributes.ENTITY_TO_DELETE, transactionService.getRepository().getReferenceById(ID));

		return ReturnValues.DELETE_ENTITY;
	}

	private void prepareModelTransactions(FilterConfiguration filterConfiguration, Model model, LocalDate date)
	{
		Account currentAccount = helpers.getCurrentAccount();
		List<Transaction> transactions = transactionService.getTransactionsForMonthAndYear(currentAccount, date.getMonthValue(), date.getYear(), settingsService.getSettings().isRestActivated(), filterConfiguration);

		model.addAttribute(TransactionModelAttributes.ALL_ENTITIES, transactions);
		model.addAttribute(TransactionModelAttributes.ACCOUNT, currentAccount);
		model.addAttribute(TransactionModelAttributes.BUDGET, helpers.getBudget(transactions, currentAccount));
		model.addAttribute(TransactionModelAttributes.CURRENT_DATE, date);
		model.addAttribute(TransactionModelAttributes.FILTER_CONFIGURATION, filterConfiguration);
	}

	@GetMapping("/{ID}/delete")
	public String deleteTransaction(WebRequest request, @PathVariable("ID") Integer ID)
	{
		final Transaction transactionToDelete = transactionService.getRepository().getReferenceById(ID);
		transactionService.deleteTransaction(ID);

		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.transaction.delete.success", transactionToDelete.getName()), NotificationType.SUCCESS));

		return ReturnValues.REDIRECT_ALL_ENTITIES;
	}

	@GetMapping("/newTransaction/{type}")
	public String newTransaction(WebRequest request, Model model, @CookieValue("currentDate") String cookieDate, @PathVariable String type)
	{
		final AccountState accountState = helpers.getCurrentAccount().getAccountState();
		if(accountState != AccountState.FULL_ACCESS)
		{
			WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.transaction.add.warning", Localization.getString(accountState.getLocalizationKey())), NotificationType.WARNING));
			return ReturnValues.REDIRECT_ALL_ENTITIES;
		}

		LocalDate date = dateService.getDateTimeFromCookie(cookieDate);
		Transaction emptyTransaction = new Transaction();
		emptyTransaction.setCategory(categoryService.findByType(CategoryType.NONE));
		transactionService.prepareModelNewOrEdit(model, false, date, false, emptyTransaction, accountService.getAllActivatedAccountsAsc());
		return ReturnValues.NEW_ENTITY + StringUtils.capitalize(type);
	}

	@PostMapping(value = "/newTransaction")
	public String post(HttpServletRequest servletRequest,
					   WebRequest request,
					   Model model, @CookieValue("currentDate") String cookieDate,
					   @ModelAttribute("NewTransaction") Transaction transaction, BindingResult bindingResult,
					   @RequestParam(value = "isRepeating", required = false) boolean isRepeating,
					   @RequestParam(value = "repeatingModifierNumber", required = false, defaultValue = "0") int repeatingModifierNumber,
					   @RequestParam(value = "repeatingModifierType", required = false) String repeatingModifierType,
					   @RequestParam(value = "repeatingEndType", required = false) String repeatingEndType,
					   @RequestParam(value = "repeatingEndValue", required = false) String repeatingEndValue,
					   @RequestParam(value = "action", required = false) String action)
	{
		LocalDate date = dateService.getDateTimeFromCookie(cookieDate);

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
			redirectUrl = ReturnValues.NEW_TRANSFER;
		}
		else
		{
			redirectUrl = ReturnValues.NEW_TRANSACTION;
		}


		final boolean isContinueActivated = action.equals(CONTINUE);
		return handleRedirect(servletRequest, request, model, transaction.getID() != null, transaction, bindingResult, date, redirectUrl, isContinueActivated);
	}

	private void handlePreviousType(Transaction transaction, boolean isRepeating)
	{
		if(transaction.getID() != null && isRepeating)
		{
			transactionService.deleteTransaction(transaction.getID());
		}
	}

	@SuppressWarnings("ConstantConditions")
	private RepeatingOption createRepeatingOption(LocalDate startDate, int repeatingModifierNumber, String repeatingModifierType, String repeatingEndType, String repeatingEndValue)
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
				LocalDate endDate = LocalDate.parse(repeatingEndValue, DateTimeFormatter.ofPattern(DateFormatStyle.LONG.getKey()).withLocale(settingsService.getSettings().getLanguage().getLocale()));
				repeatingEnd = new RepeatingEndDate(endDate);
				break;
		}

		return new RepeatingOption(startDate, repeatingModifier, repeatingEnd);
	}

	private String handleRedirect(HttpServletRequest servletRequest, WebRequest request, Model model, boolean isEdit, @ModelAttribute("NewTransaction") Transaction transaction, BindingResult bindingResult, LocalDate date, String url, boolean isContinueActivated)
	{
		if(bindingResult.hasErrors())
		{
			model.addAttribute(TransactionModelAttributes.ERROR, bindingResult);
			transactionService.prepareModelNewOrEdit(model, isEdit, date, false, transaction, accountService.getAllActivatedAccountsAsc());
			return url;
		}

		transaction = transactionService.getRepository().save(transaction);

		final String link = NotificationLinkBuilder.buildEditLink(servletRequest, transaction.getName(), Mappings.TRANSACTIONS, transaction.getID());
		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.transaction.save.success", link), NotificationType.SUCCESS));

		if(isContinueActivated)
		{
			if(transaction.isTransfer())
			{
				return ReturnValues.REDIRECT_NEW_TRANSFER;
			}
			return ReturnValues.REDIRECT_NEW_TRANSACTION;
		}

		return ReturnValues.REDIRECT_ALL_ENTITIES;
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
			return ReturnValues.REDIRECT_ALL_ENTITIES;
		}

		// select first transaction in order to provide correct start date for repeating transactions
		if(transaction.getRepeatingOption() != null)
		{
			transaction = transaction.getRepeatingOption().getReferringTransactions().get(0);
		}

		LocalDate date = dateService.getDateTimeFromCookie(cookieDate);
		transactionService.prepareModelNewOrEdit(model, true, date, false, transaction, accountService.getAllActivatedAccountsAsc());

		if(transaction.isTransfer())
		{
			return ReturnValues.NEW_TRANSFER;
		}
		return ReturnValues.NEW_TRANSACTION;
	}

	@GetMapping("/{ID}/highlight")
	public String highlight(Model model, @PathVariable("ID") Integer ID)
	{
		Transaction transaction = transactionService.getRepository().getReferenceById(ID);

		Account currentAccount = helpers.getCurrentAccount();
		if(currentAccount.getType() != AccountType.ALL || transaction.isTransfer())
		{
			accountService.selectAccount(transaction.getAccount().getID());
		}

		repeatingTransactionUpdater.updateRepeatingTransactions(transaction.getDate().with(lastDayOfMonth()));

		FilterConfiguration filterConfiguration = FilterConfiguration.DEFAULT;
		filterConfiguration.setFilterCategories(filterHelpers.getFilterCategories());
		filterConfiguration.setFilterTags(filterHelpers.getFilterTags());

		prepareModelTransactions(filterConfiguration, model, transaction.getDate());
		model.addAttribute(TransactionModelAttributes.HIGHLIGHT_ID, ID);
		return ReturnValues.ALL_ENTITIES;
	}

	@GetMapping("/{ID}/changeTypeModal")
	public String changeTypeModal(Model model, @PathVariable("ID") Integer ID)
	{
		final Optional<Transaction> transactionOptional = transactionService.getRepository().findById(ID);
		if(transactionOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		model.addAttribute(TransactionModelAttributes.ONE_ENTITY, transactionOptional.get());
		return ReturnValues.CHANGE_TYPE;
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
				redirectUrl = ReturnValues.NEW_TRANSACTION;
				break;
			case TRANSFER:
				redirectUrl = ReturnValues.NEW_TRANSFER;
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + newTransactionType);
		}

		LocalDate date = dateService.getDateTimeFromCookie(cookieDate);
		transactionService.prepareModelNewOrEdit(model, true, date, true, transactionCopy, accountService.getAllActivatedAccountsAsc());

		return redirectUrl;
	}

	@GetMapping("/{ID}/newFromExisting")
	public String newFromExisting(Model model, @PathVariable("ID") Integer ID, @CookieValue("currentDate") String cookieDate)
	{
		Optional<Transaction> transactionOptional = transactionService.getRepository().findById(ID);
		if(transactionOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		Transaction existingTransaction = transactionOptional.get();

		// select first transaction in order to provide correct start date for repeating transactions
		if(existingTransaction.getRepeatingOption() != null)
		{
			existingTransaction = existingTransaction.getRepeatingOption().getReferringTransactions().get(0);
		}

		LocalDate date = dateService.getDateTimeFromCookie(cookieDate);

		Transaction newTransaction = new Transaction(existingTransaction);
		newTransaction.setID(null);
		newTransaction.setDate(date);

		if(newTransaction.getAccount().getAccountState() != AccountState.FULL_ACCESS)
		{
			newTransaction.setAccount(accountService.getRepository().findByIsDefault(true));
		}

		transactionService.prepareModelNewOrEdit(model, false, date, false, newTransaction, accountService.getAllActivatedAccountsAsc());

		if(newTransaction.isTransfer())
		{
			return ReturnValues.NEW_TRANSFER;
		}
		return ReturnValues.NEW_TRANSACTION;
	}

	@GetMapping("/keywordCheck")
	public String keywordWarningModal(Model model)
	{
		// TODO implement real check
		// TODO only return keyword for transaction name not template
		model.addAttribute(ModelAttributes.KEYWORD, "income");
		return ReturnValues.KEYWORD_WARNING;
	}
}