package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.account.Account;
import de.deadlocker8.budgetmaster.entities.account.AccountType;
import de.deadlocker8.budgetmaster.entities.category.CategoryType;
import de.deadlocker8.budgetmaster.update.BudgetMasterUpdateService;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.entities.*;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierType;
import de.deadlocker8.budgetmaster.repositories.AccountRepository;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.repositories.TagRepository;
import de.deadlocker8.budgetmaster.utils.Colors;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.ColorUtils;
import de.thecodelabs.utils.util.Localization;
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

	public String getDateString(DateTime date)
	{
		return date.toString(DateTimeFormat.forPattern("dd.MM.yy").withLocale(settingsRepository.findOne(0).getLanguage().getLocale()));
	}

	public String getLongDateString(DateTime date)
	{
		return date.toString(DateTimeFormat.forPattern("dd.MM.yyyy").withLocale(settingsRepository.findOne(0).getLanguage().getLocale()));
	}

	public String getDateStringWithMonthAndYear(DateTime date)
	{
		return date.toString(DateTimeFormat.forPattern("MMMM yyyy").withLocale(settingsRepository.findOne(0).getLanguage().getLocale()));
	}

	public ArrayList<String> getWeekDays()
	{
		ArrayList<String> weekDays = new ArrayList<>();
		weekDays.add(Localization.getString(Strings.SUNDAY));
		weekDays.add(Localization.getString(Strings.MONDAY));
		weekDays.add(Localization.getString(Strings.TUESDAY));
		weekDays.add(Localization.getString(Strings.WEDNESDAY));
		weekDays.add(Localization.getString(Strings.THURSDAY));
		weekDays.add(Localization.getString(Strings.FRIDAY));
		weekDays.add(Localization.getString(Strings.SATURDAY));
		return weekDays;
	}

	public ArrayList<String> getMonthList()
	{
		ArrayList<String> monthNames = new ArrayList<>();
		monthNames.add(Localization.getString(Strings.MONTH_JANUARY));
		monthNames.add(Localization.getString(Strings.MONTH_FEBRUARY));
		monthNames.add(Localization.getString(Strings.MONTH_MARCH));
		monthNames.add(Localization.getString(Strings.MONTH_APRIL));
		monthNames.add(Localization.getString(Strings.MONTH_MAY));
		monthNames.add(Localization.getString(Strings.MONTH_JUNE));
		monthNames.add(Localization.getString(Strings.MONTH_JULY));
		monthNames.add(Localization.getString(Strings.MONTH_AUGUST));
		monthNames.add(Localization.getString(Strings.MONTH_SEPTEMBER));
		monthNames.add(Localization.getString(Strings.MONTH_OCTOBER));
		monthNames.add(Localization.getString(Strings.MONTH_NOVEMBER));
		monthNames.add(Localization.getString(Strings.MONTH_DECEMBER));
		return monthNames;
	}

	public ArrayList<Integer> getYearList()
	{
		ArrayList<Integer> years = new ArrayList<>();
		for(int i = 2000; i < 2100; i++)
		{
			years.add(i);
		}
		return years;
	}

	/**
	 * Replaces line breaks and tabs with spaces
	 *
	 * @param text
	 * @return String
	 */
	public String getFlatText(String text)
	{
		text = text.replace("\n", " ");
		text = text.replace("\t", " ");
		return text;
	}

	public ArrayList<String> getCategoryColorList()
	{
		ArrayList<String> categoryColors = new ArrayList<>();
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_GREY).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_GREY).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_YELLOW).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_YELLOW).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_ORANGE).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_RED).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_RED).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_PINK).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_PURPLE).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_PURPLE).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_BLUE).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_SOFT_BLUE).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_BLUE).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREEN).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIME_GREEN).toLowerCase());
		categoryColors.add(ColorUtils.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_GREEN).toLowerCase());

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
			Account account = accountRepository.findAllByType(AccountType.CUSTOM).get(0);
			accountService.selectAccount(account.getID());
			selectedAccount = accountRepository.findByIsSelected(true);
		}

		return selectedAccount;
	}

	public int getIncomeSumForTransactionList(List<Transaction> transactions)
	{
		int sum = 0;
		for(Transaction transaction : transactions)

		{
			if(transaction.getAmount() > 0)
			{
				sum += transaction.getAmount();
			}
		}
		return sum;
	}

	public int getExpenditureSumForTransactionList(List<Transaction> transactions)
	{
		int sum = 0;
		for(Transaction transaction : transactions)
		{
			if(transaction.getAmount() < 0)
			{
				sum += transaction.getAmount();
			}
		}
		return sum;
	}

	public DateTime getCurrentDate()
	{
		return DateTime.now();
	}

	public int getAccountBudget()
	{
		List<Transaction> transactions = transactionService.getTransactionsForAccountUntilDate(getCurrentAccount(), getCurrentDate());

		int sum = 0;
		for(Transaction transaction : transactions)
		{
			sum += transaction.getAmount();
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