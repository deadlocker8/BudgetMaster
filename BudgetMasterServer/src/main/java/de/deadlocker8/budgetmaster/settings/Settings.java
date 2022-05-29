package de.deadlocker8.budgetmaster.settings;

import de.deadlocker8.budgetmaster.backup.AutoBackupStrategy;
import de.deadlocker8.budgetmaster.backup.AutoBackupTime;
import de.deadlocker8.budgetmaster.utils.DateHelper;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Settings
{
	@Id
	private int ID = 1;
	private String currency;
	private boolean restActivated;

	private LanguageType language;
	private boolean useDarkTheme;
	private Boolean showCategoriesAsCircles;

	private boolean autoUpdateCheckEnabled;

	private Boolean backupReminderActivated;
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private LocalDate lastBackupReminderDate;

	private Integer searchItemsPerPage;

	private AutoBackupStrategy autoBackupStrategy;
	private Integer autoBackupDays;
	private AutoBackupTime autoBackupTime;

	private Integer autoBackupFilesToKeep;
	private String autoBackupGitUrl;
	private String autoBackupGitBranchName;
	private String autoBackupGitUserName;
	private String autoBackupGitToken;

	private Integer installedVersionCode;
	private Boolean whatsNewShownForCurrentVersion;

	private Boolean migrationDeclined;

	public Settings()
	{
		// empty
	}

	public static Settings getDefault()
	{
		Settings defaultSettings = new Settings();
		defaultSettings.setCurrency("€");
		defaultSettings.setLanguage(LanguageType.ENGLISH);
		defaultSettings.setRestActivated(true);
		defaultSettings.setUseDarkTheme(true);
		defaultSettings.setShowCategoriesAsCircles(true);
		defaultSettings.setAutoUpdateCheckEnabled(true);
		defaultSettings.setBackupReminderActivated(true);
		defaultSettings.setLastBackupReminderDate(LocalDate.now());
		defaultSettings.setSearchItemsPerPage(10);
		defaultSettings.setAutoBackupStrategy(AutoBackupStrategy.NONE);
		defaultSettings.setAutoBackupDays(1);
		defaultSettings.setAutoBackupTime(AutoBackupTime.TIME_00);
		defaultSettings.setAutoBackupFilesToKeep(3);
		defaultSettings.setAutoBackupGitUrl("");
		defaultSettings.setAutoBackupGitBranchName("");
		defaultSettings.setAutoBackupGitUserName("");
		defaultSettings.setAutoBackupGitToken("");
		defaultSettings.setInstalledVersionCode(0);
		defaultSettings.setWhatsNewShownForCurrentVersion(false);
		defaultSettings.setMigrationDeclined(false);

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

	public Boolean getShowCategoriesAsCircles()
	{
		return showCategoriesAsCircles;
	}

	public void setShowCategoriesAsCircles(Boolean showCategoriesAsCircles)
	{
		this.showCategoriesAsCircles = showCategoriesAsCircles;
	}

	public boolean isAutoUpdateCheckEnabled()
	{
		return autoUpdateCheckEnabled;
	}

	public void setAutoUpdateCheckEnabled(boolean autoUpdateCheckEnabled)
	{
		this.autoUpdateCheckEnabled = autoUpdateCheckEnabled;
	}

	public Boolean getBackupReminderActivated()
	{
		return backupReminderActivated;
	}

	public void setBackupReminderActivated(Boolean backupReminderActivated)
	{
		this.backupReminderActivated = backupReminderActivated;
	}

	public LocalDate getLastBackupReminderDate()
	{
		return lastBackupReminderDate;
	}

	public void setLastBackupReminderDate(LocalDate lastBackupReminderDate)
	{
		this.lastBackupReminderDate = lastBackupReminderDate;
	}

	public boolean needToShowBackupReminder()
	{
		if(backupReminderActivated)
		{
			return lastBackupReminderDate.getMonthValue() != DateHelper.getCurrentDate().getMonthValue();
		}
		return false;
	}

	public Integer getSearchItemsPerPage()
	{
		return searchItemsPerPage;
	}

	public void setSearchItemsPerPage(Integer searchItemsPerPage)
	{
		this.searchItemsPerPage = searchItemsPerPage;
	}

	public Integer getAutoBackupDays()
	{
		return autoBackupDays;
	}

	public void setAutoBackupDays(Integer autoBackupDays)
	{
		this.autoBackupDays = autoBackupDays;
	}

	public AutoBackupTime getAutoBackupTime()
	{
		return autoBackupTime;
	}

	public void setAutoBackupTime(AutoBackupTime autoBackupTime)
	{
		this.autoBackupTime = autoBackupTime;
	}

	public AutoBackupStrategy getAutoBackupStrategy()
	{
		return autoBackupStrategy;
	}

	public void setAutoBackupStrategy(AutoBackupStrategy autoBackupStrategy)
	{
		this.autoBackupStrategy = autoBackupStrategy;
	}

	public boolean isAutoBackupActive()
	{
		return this.autoBackupStrategy != AutoBackupStrategy.NONE;
	}

	public Integer getAutoBackupFilesToKeep()
	{
		return autoBackupFilesToKeep;
	}

	public void setAutoBackupFilesToKeep(Integer autoBackupFilesToKeep)
	{
		this.autoBackupFilesToKeep = autoBackupFilesToKeep;
	}

	public String getAutoBackupGitUrl()
	{
		return autoBackupGitUrl;
	}

	public void setAutoBackupGitUrl(String autoBackupGitUrl)
	{
		this.autoBackupGitUrl = autoBackupGitUrl;
	}

	public String getAutoBackupGitBranchName()
	{
		return autoBackupGitBranchName;
	}

	public void setAutoBackupGitBranchName(String autoBackupGitBranchName)
	{
		this.autoBackupGitBranchName = autoBackupGitBranchName;
	}

	public String getAutoBackupGitUserName()
	{
		return autoBackupGitUserName;
	}

	public void setAutoBackupGitUserName(String gitUserName)
	{
		this.autoBackupGitUserName = gitUserName;
	}

	public String getAutoBackupGitToken()
	{
		return autoBackupGitToken;
	}

	public void setAutoBackupGitToken(String gitToken)
	{
		this.autoBackupGitToken = gitToken;
	}

	public Integer getInstalledVersionCode()
	{
		return installedVersionCode;
	}

	public void setInstalledVersionCode(Integer installedVersionCode)
	{
		this.installedVersionCode = installedVersionCode;
	}

	public Boolean getWhatsNewShownForCurrentVersion()
	{
		return whatsNewShownForCurrentVersion;
	}

	public void setWhatsNewShownForCurrentVersion(Boolean whatsNewShownForCurrentVersion)
	{
		this.whatsNewShownForCurrentVersion = whatsNewShownForCurrentVersion;
	}

	public boolean needToShowWhatsNew()
	{
		return !this.whatsNewShownForCurrentVersion;
	}

	public Boolean getMigrationDeclined()
	{
		return migrationDeclined;
	}

	public void setMigrationDeclined(Boolean migrationDeclined)
	{
		this.migrationDeclined = migrationDeclined;
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
				", showCategoriesAsCircles=" + showCategoriesAsCircles +
				", autoUpdateCheckEnabled=" + autoUpdateCheckEnabled +
				", backupReminderActivated=" + backupReminderActivated +
				", lastBackupReminderDate=" + lastBackupReminderDate +
				", searchItemsPerPage=" + searchItemsPerPage +
				", autoBackupStrategy=" + autoBackupStrategy +
				", autoBackupDays=" + autoBackupDays +
				", autoBackupTime=" + autoBackupTime +
				", autoBackupFilesToKeep=" + autoBackupFilesToKeep +
				", autoBackupGitUrl='" + autoBackupGitUrl + '\'' +
				", autoBackupGitBranchName='" + autoBackupGitBranchName + '\'' +
				", autoBackupGitUserName='" + autoBackupGitUserName + '\'' +
				", autoBackupGitToken='" + autoBackupGitToken + '\'' +
				", installedVersionCode=" + installedVersionCode +
				", whatsNewShownForCurrentVersion=" + whatsNewShownForCurrentVersion +
				", migrationDeclined=" + migrationDeclined +
				'}';
	}
}