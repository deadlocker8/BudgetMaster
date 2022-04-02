package de.deadlocker8.budgetmaster.databasemigrator.steps.reader;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.settings.DestinationSettings;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsReader extends BaseReader<DestinationSettings>
{
	private static class DatabaseColumns
	{
		public static final String ID = "ID";
		public static final String CURRENCY = "CURRENCY";
		public static final String LANGUAGE = "LANGUAGE";
		public static final String USE_DARK_THEME = "USE_DARK_THEME";
		public static final String SHOW_CATEGORIES_AS_CIRCLES = "SHOW_CATEGORIES_AS_CIRCLES";
		public static final String AUTO_UPDATE_CHECK_ENABLED = "AUTO_UPDATE_CHECK_ENABLED";
		public static final String BACKUP_REMINDER_ACTIVATED = "BACKUP_REMINDER_ACTIVATED";
		public static final String LAST_BACKUP_REMINDER_DATE = "LAST_BACKUP_REMINDER_DATE";
		public static final String SEARCH_ITEMS_PER_PAGE = "SEARCH_ITEMS_PER_PAGE";

		public static final String AUTO_BACKUP_STRATEGY = "AUTO_BACKUP_STRATEGY";
		public static final String AUTO_BACKUP_DAYS = "AUTO_BACKUP_DAYS";
		public static final String AUTO_BACKUP_TIME = "AUTO_BACKUP_TIME";
		public static final String AUTO_BACKUP_FILES_TO_KEEP = "AUTO_BACKUP_FILES_TO_KEEP";

		public static final String AUTO_BACKUP_GIT_URL = "AUTO_BACKUP_GIT_URL";
		public static final String AUTO_BACKUP_GIT_BRANCH_NAME = "AUTO_BACKUP_GIT_BRANCH_NAME";
		public static final String AUTO_BACKUP_GIT_USER_NAME = "AUTO_BACKUP_GIT_USER_NAME";
		public static final String AUTO_BACKUP_GIT_TOKEN = "AUTO_BACKUP_GIT_TOKEN";

		public static final String INSTALLED_VERSION_CODE = "INSTALLED_VERSION_CODE";
		public static final String WHATS_NEW_SHOWN_FOR_CURRENT_VERSION = "WHATS_NEW_SHOWN_FOR_CURRENT_VERSION";
	}

	public SettingsReader(DataSource primaryDataSource)
	{
		super(TableNames.SETTINGS, primaryDataSource);
	}

	@Override
	protected RowMapper<DestinationSettings> getRowMapper()
	{
		return new SettingsRowMapper();
	}

	public static class SettingsRowMapper implements RowMapper<DestinationSettings>
	{
		@Override
		public DestinationSettings mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			final DestinationSettings settings = new DestinationSettings();
			settings.setID(rs.getInt(DatabaseColumns.ID));
			settings.setCurrency(rs.getString(DatabaseColumns.CURRENCY));
			settings.setLanguage(rs.getInt(DatabaseColumns.LANGUAGE));
			settings.setUseDarkTheme(rs.getBoolean(DatabaseColumns.USE_DARK_THEME));
			settings.setShowCategoriesAsCircles(rs.getBoolean(DatabaseColumns.SHOW_CATEGORIES_AS_CIRCLES));
			settings.setAutoUpdateCheckEnabled(rs.getBoolean(DatabaseColumns.AUTO_UPDATE_CHECK_ENABLED));
			settings.setBackupReminderActivated(rs.getBoolean(DatabaseColumns.BACKUP_REMINDER_ACTIVATED));
			settings.setLastBackupReminderDate(rs.getString(DatabaseColumns.LAST_BACKUP_REMINDER_DATE));
			settings.setSearchItemsPerPage(rs.getInt(DatabaseColumns.SEARCH_ITEMS_PER_PAGE));

			settings.setAutoBackupStrategy(rs.getInt(DatabaseColumns.AUTO_BACKUP_STRATEGY));
			settings.setAutoBackupDays(rs.getInt(DatabaseColumns.AUTO_BACKUP_DAYS));
			settings.setAutoBackupTime(rs.getInt(DatabaseColumns.AUTO_BACKUP_TIME));
			settings.setAutoBackupFilesToKeep(rs.getInt(DatabaseColumns.AUTO_BACKUP_FILES_TO_KEEP));

			settings.setAutoBackupGitUrl(rs.getString(DatabaseColumns.AUTO_BACKUP_GIT_URL));
			settings.setAutoBackupGitBranchName(rs.getString(DatabaseColumns.AUTO_BACKUP_GIT_BRANCH_NAME));
			settings.setAutoBackupGitUserName(rs.getString(DatabaseColumns.AUTO_BACKUP_GIT_USER_NAME));
			settings.setAutoBackupGitToken(rs.getString(DatabaseColumns.AUTO_BACKUP_GIT_TOKEN));

			settings.setInstalledVersionCode(rs.getInt(DatabaseColumns.INSTALLED_VERSION_CODE));
			settings.setWhatsNewShownForCurrentVersion(rs.getBoolean(DatabaseColumns.WHATS_NEW_SHOWN_FOR_CURRENT_VERSION));
			return settings;
		}
	}
}
