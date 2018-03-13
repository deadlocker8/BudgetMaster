package de.deadlocker8.budgetmaster.entities;

import de.deadlocker8.budgetmaster.utils.LanguageType;
import javafx.scene.paint.Color;
import tools.ConvertTo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Settings
{
	@Id
	private int ID;
	@NotNull
	@Size(min = 1)
	private String password;
	private String currency;
	private LanguageType language;
	private boolean restActivated;
	private boolean useDarkTheme;
	private boolean autoUpdateCheckEnabled;


	public Settings()
	{
	}

	public static Settings getDefault()
	{
		Settings defaultSettings = new Settings();
		defaultSettings.setPassword("BudgetMaster");
		defaultSettings.setCurrency("€");
		defaultSettings.setLanguage(LanguageType.ENGLISH);
		defaultSettings.setRestActivated(true);
		defaultSettings.setUseDarkTheme(false);
		defaultSettings.setAutoUpdateCheckEnabled(true);

		return defaultSettings;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
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

	@Override
	public String toString()
	{
		return "SettingsRepository{" +
				"password='" + password + '\'' +
				", currency='" + currency + '\'' +
				", language=" + language +
				", restActivated=" + restActivated +
				", useDarkTheme=" + useDarkTheme +
				", autoUpdateCheckEnabled=" + autoUpdateCheckEnabled +
				'}';
	}
}