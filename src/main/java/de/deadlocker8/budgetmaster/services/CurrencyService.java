package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.NumberFormat;

@Service
public class CurrencyService
{
	private final SettingsService settingsService;

	@Autowired
	public CurrencyService(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	public String getCurrencyString(int amount)
	{
		return getCurrencyString(amount / 100.0);
	}

	public String getCurrencyString(double amount)
	{
		return MessageFormat.format("{0} {1}", getAmountString(amount, true), settingsService.getSettings().getCurrency());
	}

	public String getAmountString(int amount)
	{
		return getAmountString(Math.abs(amount) / 100.0, false);
	}

	public String getAmountString(double amount, boolean useGrouping)
	{
		Settings settings = settingsService.getSettings();
		NumberFormat format = NumberFormat.getNumberInstance(settings.getLanguage().getLocale());
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
		format.setRoundingMode(RoundingMode.HALF_UP);
		format.setGroupingUsed(useGrouping);
		return String.valueOf(format.format(amount));
	}
}
