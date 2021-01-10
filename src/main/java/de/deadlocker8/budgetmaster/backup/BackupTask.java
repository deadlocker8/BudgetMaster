package de.deadlocker8.budgetmaster.backup;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;

import java.nio.file.Path;

public abstract class BackupTask implements Runnable
{
	protected static final String DATABASE_FILE_NAME = "BudgetMasterDatabase.json";

	protected final DatabaseService databaseService;
	protected final SettingsService settingsService;
	private final Path backupFolder;
	private boolean hasErrors;

	protected BackupTask(DatabaseService databaseService, SettingsService settingsService)
	{
		this.databaseService = databaseService;
		this.settingsService = settingsService;

		final Path applicationSupportFolder = Main.getApplicationSupportFolder();
		this.backupFolder = applicationSupportFolder.resolve("backups");

		this.hasErrors = false;
	}

	protected Path getBackupFolder()
	{
		return backupFolder;
	}

	public boolean hasErrors()
	{
		return hasErrors;
	}

	protected void setHasErrors(boolean hasErrors)
	{
		this.hasErrors = hasErrors;
	}

	public abstract void cleanup(Settings previousSettings, Settings newSettings);

	protected abstract boolean needsCleanup(Settings previousSettings, Settings newSettings);
}
