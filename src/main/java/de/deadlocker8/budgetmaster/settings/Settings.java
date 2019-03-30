package de.deadlocker8.budgetmaster.settings;

import de.deadlocker8.budgetmaster.utils.LanguageType;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Settings
{
	@Id
	private int ID;
	private String currency;
	private LanguageType language;
	private boolean restActivated;
	private boolean useDarkTheme;
	private boolean autoUpdateCheckEnabled;
	private Boolean backupReminderActivated;
	private Boolean backupReminderShownThisMonth;

	public Settings()
	{
	}

	public static Settings getDefault()
	{
		Settings defaultSettings = new Settings();
		defaultSettings.setCurrency("€");
		defaultSettings.setLanguage(LanguageType.ENGLISH);
		defaultSettings.setRestActivated(true);
		defaultSettings.setUseDarkTheme(false);
		defaultSettings.setAutoUpdateCheckEnabled(true);
		defaultSettings.setBackupReminderActivated(true);
		defaultSettings.setBackupReminderShownThisMonth(false);

		return defaultSettings;
	}

	public int getID()
	{
		return ID;
	}

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public LanguageType getLanguage()
	{
		return language;
	}

	public void setLanguage(LanguageType language)
	{
		this.language = language;
	}

	public boolean isRestActivated()
	{
		return restActivated;
	}

	public void setRestActivated(boolean restActivated)
	{
		this.restActivated = restActivated;
	}

	public boolean isUseDarkTheme()
	{
		return useDarkTheme;
	}

	public void setUseDarkTheme(boolean useDarkTheme)
	{
		this.useDarkTheme = useDarkTheme;
	}

	public boolean isAutoUpdateCheckEnabled()
	{
		return autoUpdateCheckEnabled;
	}

	public void setAutoUpdateCheckEnabled(boolean autoUpdateCheckEnabled)
	{
		this.autoUpdateCheckEnabled = autoUpdateCheckEnabled;
	}

	public Boolean isBackupReminderActivated()
	{
		return backupReminderActivated;
	}

	public void setBackupReminderActivated(Boolean backupReminderActivated)
	{
		this.backupReminderActivated = backupReminderActivated;
	}

	public Boolean isBackupReminderShownThisMonth()
	{
		return backupReminderShownThisMonth;
	}

	public void setBackupReminderShownThisMonth(Boolean backupReminderShownThisMonth)
	{
		this.backupReminderShownThisMonth = backupReminderShownThisMonth;
	}

	@Override
	public String toString()
	{
		return "Settings{" +
				"ID=" + ID +
				", currency='" + currency + '\'' +
				", language=" + language +
				", restActivated=" + restActivated +
				", useDarkTheme=" + useDarkTheme +
				", autoUpdateCheckEnabled=" + autoUpdateCheckEnabled +
				", backupReminderActivated=" + backupReminderActivated +
				", backupReminderShownThisMonth=" + backupReminderShownThisMonth +
				'}';
	}
}