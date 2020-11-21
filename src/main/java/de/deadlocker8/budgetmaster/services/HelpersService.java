package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierType;
import de.deadlocker8.budgetmaster.reports.Budget;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.utils.Colors;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import de.thecodelabs.utils.util.ColorUtilsNonJavaFX;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class HelpersService
{
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

	@Value("${use.simple.datepicker:false}")
	private boolean useSimpleDatepickerForTransactions;

	public List<LanguageType> getAvailableLanguages()
	{
		return Arrays.asList(LanguageType.values());
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

	public int getAccountBudget()
	{
		Account currentAccount = getCurrentAccount();
		List<Transaction> transactions = transactionService.getTransactionsForAccountUntilDate(currentAccount, DateTime.now(), FilterConfiguration.DEFAULT);

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

	public Long getUsageCountForCategory(Category category)
	{
		return transactionService.getRepository().countByCategory(category);
	}

	public boolean isUseSimpleDatepickerForTransactions()
	{
		return useSimpleDatepickerForTransactions;
	}
}