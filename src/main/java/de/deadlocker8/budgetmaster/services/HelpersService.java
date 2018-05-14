package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.entities.Tag;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierType;
import de.deadlocker8.budgetmaster.repositories.AccountRepository;
import de.deadlocker8.budgetmaster.repositories.PaymentRepository;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.repositories.TagRepository;
import de.deadlocker8.budgetmaster.utils.Colors;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.ConvertTo;
import tools.Localization;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class HelpersService
{
	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	public String getCurrencyString(int amount)
	{
		return getCurrencyString(amount / 100.0);
	}

	public String getCurrencyString(double amount)
	{
		return getAmountString(amount) + " " + settingsRepository.findOne(0).getCurrency();
	}

	public String getAmountString(int amount)
	{
		return getAmountString(amount / 100.0);
	}

	public String getAmountString(double amount)
	{
		Settings settings = settingsRepository.findOne(0);
		NumberFormat format = NumberFormat.getNumberInstance(settings.getLanguage().getLocale());
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
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
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREY).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_GREY).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_GREY).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_YELLOW).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_YELLOW).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_ORANGE).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_RED).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_RED).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_PINK).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_PURPLE).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_PURPLE).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_BLUE).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_SOFT_BLUE).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_BLUE).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIGHT_GREEN).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_LIME_GREEN).toLowerCase());
		categoryColors.add(ConvertTo.toRGBHexWithoutOpacity(Colors.CATEGORIES_DARK_GREEN).toLowerCase());

		return categoryColors;
	}

	public List<Tag> getAllTags()
	{
		return tagRepository.findAllByOrderByNameAsc();
	}

	public List<Account> getAllAccounts()
	{
		return accountRepository.findAllByOrderByNameAsc();
	}

	public Account getCurrentAccount()
	{
		return accountRepository.findByIsSelected(true);
	}

	public int getIncomeSumForPaymentList(List<Payment> payments)
	{
		int sum = 0;
		for(Payment payment : payments)

		{
			if(payment.getAmount() > 0)
			{
				sum += payment.getAmount();
			}
		}
		return sum;
	}

	public int getPaymentSumForPaymentList(List<Payment> payments)
	{
		int sum = 0;
		for(Payment payment : payments)
		{
			if(payment.getAmount() < 0)
			{
				sum += payment.getAmount();
			}
		}
		return sum;
	}

	public int getAccountBudget()
	{
		List<Payment> payments = paymentRepository.findAllByAccount(getCurrentAccount());
		int sum = 0;
		for(Payment payment : payments)
		{
			sum += payment.getAmount();
		}

		return sum;
	}

	public List<RepeatingModifierType> getRepeatingModifierTypes()
	{
		return Arrays.asList(RepeatingModifierType.values());
	}
}