package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.reports.Budget;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierType;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.settings.SettingsRepository;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.update.BudgetMasterUpdateService;
import de.deadlocker8.budgetmaster.utils.Colors;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import de.thecodelabs.utils.util.ColorUtilsNonJavaFX;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class HelpersService
{
	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private SettingsService settingsService;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private BudgetMasterUpdateService budgetMasterUpdateService;

	public String getCurrencyString(int amount)
	{
		return getCurrencyString(amount / 100.0);
	}

	public String getCurrencyString(double amount)
	{
		return getAmountString(amount, true) + " " + settingsRepository.findOne(0).getCurrency();
	}

	public String getAmountString(int amount)
	{
		return getAmountString(Math.abs(amount) / 100.0, false);
	}

	public String getAmountString(double amount, boolean useGrouping)
	{
		Settings settings = settingsRepository.findOne(0);
		NumberFormat format = NumberFormat.getNumberInstance(settings.getLanguage().getLocale());
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
		format.setRoundingMode(RoundingMode.HALF_UP);
		format.setGroupingUsed(useGrouping);
		return String.valueOf(format.format(amount));
	}

	public String getURLEncodedString(String input)
	{
		try
		{
			return URLEncoder.encode(input, "UTF-8");
		}
		catch(UnsupportedEncodingException e)
		{
			return input;
		}
	}

	public List<LanguageType> getAvailableLanguages()
	{
		return Arrays.asList(LanguageType.values());
	}

	 // Replaces line breaks and tabs with spaces
	public String getFlatText(String text)
	{
		text = text.replace("\n", " ");
		text = text.replace("\t", " ");
		return text;
	}

	public ArrayList<String> getCategoryColorList()
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
		return tagRepository.findAllByOrderByNameAsc();
	}

	public List<Account> getAllAccounts()
	{
		return accountService.getAllAccountsAsc();
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

	public DateTime getCurrentDate()
	{
		return DateTime.now();
	}

	public int getAccountBudget()
	{
		Account currentAccount = getCurrentAccount();
		List<Transaction> transactions = transactionService.getTransactionsForAccountUntilDate(currentAccount, getCurrentDate(), FilterConfiguration.DEFAULT);

		int sum = 0;
		for(Transaction transaction : transactions)
		{
			sum += getAmount(transaction, currentAccount);
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

	public List<AccountMatch> getAccountMatches(List<Account> accounts)
	{
		List<AccountMatch> accountMatches = new ArrayList<>();
		for(Account account : accounts)
		{
			if(account.getType().equals(AccountType.CUSTOM))
			{
				accountMatches.add(new AccountMatch(account));
			}
		}

		return accountMatches;
	}

	public int getIDOfNoCatgeory()
	{
		return categoryRepository.findByType(CategoryType.NONE).getID();
	}

	public boolean isUpdateAvailable()
	{
		 try
		 {
		 	return budgetMasterUpdateService.getUpdateService().isUpdateAvailable();
		 }
		 catch(NullPointerException e)
		 {
			return false;
		 }
	}

	public String getAvailableVersionString()
	{
		return budgetMasterUpdateService.getAvailableVersionString();
	}

	public DateTime getDateTimeFromCookie(String cookieDate)
	{
		if(cookieDate == null)
		{
			return DateTime.now();
		}
		else
		{
			return DateTime.parse(cookieDate, DateTimeFormat.forPattern("dd.MM.yy").withLocale(getSettings().getLanguage().getLocale()));
		}
	}
}