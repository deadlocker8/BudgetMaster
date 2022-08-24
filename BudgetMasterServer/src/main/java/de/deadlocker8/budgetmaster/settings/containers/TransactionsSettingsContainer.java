package de.deadlocker8.budgetmaster.settings.containers;

import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeyword;
import org.springframework.validation.Errors;

import java.util.List;


public final class TransactionsSettingsContainer implements SettingsContainer
{
	private Boolean restActivated;

	private List<TransactionNameKeyword> keywords;

	public TransactionsSettingsContainer(Boolean restActivated, List<TransactionNameKeyword> keywords)
	{
		this.restActivated = restActivated;
		this.keywords = keywords;
	}

	@Override
	public void validate(Errors errors)
	{
		// nothing to do
	}

	@Override
	public void fixBooleans()
	{
		if(restActivated == null)
		{
			restActivated = false;
		}
	}

	@Override
	public String getErrorLocalizationKey()
	{
		return "notification.settings.transactions.error";
	}

	@Override
	public String getSuccessLocalizationKey()
	{
		return "notification.settings.transactions.saved";
	}

	@Override
	public String getTemplatePath()
	{
		return "settings/containers/settingsTransactions";
	}

	@Override
	public Settings updateSettings(SettingsService settingsService)
	{
		final Settings settings = settingsService.getSettings();

		settings.setRestActivated(restActivated);

		return settings;
	}

	@Override
	public void persistChanges(SettingsService settingsService, Settings previousSettings, Settings settings)
	{
		settingsService.updateSettings(settings);
	}

	public List<TransactionNameKeyword> getKeywords()
	{
		return keywords;
	}

	public void setKeywords(List<TransactionNameKeyword> keywords)
	{
		this.keywords = keywords;
	}
}
