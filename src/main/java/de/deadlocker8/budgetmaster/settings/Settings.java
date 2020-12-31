package de.deadlocker8.budgetmaster.settings;

import de.deadlocker8.budgetmaster.backup.AutoBackupTime;
import de.deadlocker8.budgetmaster.backup.AutoBackupStrategy;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

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
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private DateTime lastBackupReminderDate;

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
	private Boolean showFirstUseBanner;

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
		defaultSettings.setLastBackupReminderDate(DateTime.now());
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
		defaultSettings.setShowFirstUseBanner(true);

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

	public Boolean getBackupReminderActivated()
	{
		return backupReminderActivated;
	}

	public void setBackupReminderActivated(Boolean backupReminderActivated)
	{
		this.backupReminderActivated = backupReminderActivated;
	}

	public DateTime getLastBackupReminderDate()
	{
		return lastBackupReminderDate;
	}

	public void setLastBackupReminderDate(DateTime lastBackupReminderDate)
	{
		this.lastBackupReminderDate = lastBackupReminderDate;
	}

	public boolean needToShowBackupReminder()
	{
		if(backupReminderActivated)
		{
			return lastBackupReminderDate.getMonthOfYear() != DateTime.now().getMonthOfYear();
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

	public Boolean getShowFirstUseBanner()
	{
		return showFirstUseBanner;
	}

	public void setShowFirstUseBanner(Boolean showFirstUseBanner)
	{
		this.showFirstUseBanner = showFirstUseBanner;
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
				", showFirstUseBanner=" + showFirstUseBanner +
				'}';
	}
}