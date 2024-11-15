package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.backup.AutoBackupStrategy;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.hints.Hint;
import de.deadlocker8.budgetmaster.hints.HintService;
import de.deadlocker8.budgetmaster.images.ImageFileExtension;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierType;
import de.deadlocker8.budgetmaster.reports.Budget;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.utils.Colors;
import de.deadlocker8.budgetmaster.utils.DateHelper;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import de.thecodelabs.utils.util.ColorUtilsNonJavaFX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class HelpersService
{
	@Autowired
	private SettingsService settingsService;

	@Autowired
	private TagService tagService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private HintService hintService;

	@Value("${budgetmaster.datepicker.simple:false}")
	private boolean useSimpleDatepickerForTransactions;

	public List<LanguageType> getAvailableLanguages()
	{
		return Arrays.asList(LanguageType.values());
	}

	public List<AutoBackupStrategy> getAvailableAutoBackupStrategies()
	{
		List<AutoBackupStrategy> autoBackupStrategies = new ArrayList<>(Arrays.asList(AutoBackupStrategy.values()));
		autoBackupStrategies.remove(AutoBackupStrategy.NONE);
		return autoBackupStrategies;
	}

	public List<String> getCategoryColorList()
	{
		ArrayList<String> categoryColors = new ArrayList<>();
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_GREY).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_GREY).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_YELLOW).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_YELLOW).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_ORANGE).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_RED).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_RED).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_PINK).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_PURPLE).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_PURPLE).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_BLUE).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_SOFT_BLUE).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_BLUE).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREEN).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIME_GREEN).toLowerCase());
		categoryColors.add(ColorUtilsNonJavaFX.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_GREEN).toLowerCase());

		return categoryColors;
	}

	public List<Tag> getAllTags()
	{
		return tagService.getAllEntitiesAsc();
	}

	public List<Account> getAllAccounts()
	{
		return accountService.getAllEntitiesAsc();
	}

	public List<Account> getAllReadableAccounts()
	{
		return accountService.getAllReadableAccounts();
	}

	public Account getCurrentAccount()
	{
		Account selectedAccount = accountRepository.findByIsSelected(true);

		// select random account if no account is selected
		if(selectedAccount == null)
		{
			selectedAccount = accountRepository.findByIsDefault(true);
			accountService.selectAccount(selectedAccount.getID());
		}

		return selectedAccount;
	}

	public Account getCurrentAccountOrDefault()
	{
		Account selectedAccount = getCurrentAccount();
		if(selectedAccount.getType().equals(AccountType.ALL))
		{
			return accountService.getRepository().findByIsDefault(true);
		}
		return selectedAccount;
	}

	public Budget getBudget(List<Transaction> transactions, Account account)
	{
		int incomeSum = 0;
		int expenditureSum = 0;
		for(Transaction transaction : transactions)
		{
			int currentAmount = getAmount(transaction, account);

			if(currentAmount > 0)
			{
				incomeSum += currentAmount;
			}
			else
			{
				expenditureSum += currentAmount;
			}
		}
		return new Budget(incomeSum, expenditureSum);
	}

	public int getAmount(Transaction transaction, Account account)
	{
		// All accounts
		if(account.getType().equals(AccountType.ALL))
		{
			return transaction.getAmount();
		}

		if(transaction.getTransferAccount() != null && transaction.getTransferAccount().getID().equals(account.getID()))
		{
			return -transaction.getAmount();
		}

		return transaction.getAmount();
	}

	public int getCurrentAccountBudget()
	{
		final Account currentAccount = getCurrentAccount();
		return getBudgetForAccount(currentAccount);
	}

	public int getAccountBudgetByID(Integer accountID)
	{
		final Optional<Account> accountOptional = accountRepository.findById(accountID);
		if(accountOptional.isEmpty())
		{
			throw new IllegalArgumentException(MessageFormat.format("No account with ID \"{0}\" found", accountID));
		}

		final Account account = accountOptional.get();
		return getBudgetForAccount(account);
	}

	private int getBudgetForAccount(Account account)
	{
		final LocalDate endDate = DateHelper.getCurrentDate();
		List<Transaction> transactions = transactionService.getTransactionsForAccountUntilDate(account, endDate, FilterConfiguration.DEFAULT);

		int sum = 0;
		for(Transaction transaction : transactions)
		{
			sum += getAmount(transaction, account);
		}

		return sum;
	}

	public List<RepeatingModifierType> getRepeatingModifierTypes()
	{
		return Arrays.asList(RepeatingModifierType.values());
	}

	public Settings getSettings()
	{
		return settingsService.getSettings();
	}

	public Long getUsageCountForCategory(Category category)
	{
		return transactionService.getRepository().countByCategory(category);
	}

	public boolean isUseSimpleDatepickerForTransactions()
	{
		return useSimpleDatepickerForTransactions;
	}

	public String getValidImageUploadTypes()
	{
		return Arrays.stream(ImageFileExtension.values())
				.map(e -> "image/" + e.getBase64Type())
				.collect(Collectors.joining(", "));
	}

	public Hint getHintByLocalizationKey(String localizationKey)
	{
		return hintService.findByLocalizationKey(localizationKey);
	}

	public char getDecimalSeparator()
	{
		return new DecimalFormatSymbols(settingsService.getSettings().getLanguage().getLocale()).getDecimalSeparator();
	}

	public char getGroupingSeparator()
	{
		return new DecimalFormatSymbols(settingsService.getSettings().getLanguage().getLocale()).getGroupingSeparator();
	}

	public AccountEndDateReminderData getAccountEndDateReminderData()
	{
		final Settings settings = settingsService.getSettings();
		if(!settings.getAccountEndDateReminderActivated())
		{
			return new AccountEndDateReminderData(false, List.of());
		}

		if(!Objects.equals(settings.getLastAccountEndDateReminderDate(), DateHelper.getCurrentDate()))
		{
			final List<String> accountsWithEndDateSoon = accountRepository.findAll().stream()
					.filter(account -> account.getEndDate() != null)
					.filter(account -> account.getRemainingDays() > 0)
					.filter(account -> account.getRemainingDays() <= 30)
					.map(account -> MessageFormat.format("{0} ({1})", account.getName(), account.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yy"))))
					.sorted()
					.toList();

			if(accountsWithEndDateSoon.isEmpty())
			{
				return new AccountEndDateReminderData(false, List.of());
			}

			return new AccountEndDateReminderData(true, accountsWithEndDateSoon);
		}

		return new AccountEndDateReminderData(false, List.of());
	}
}