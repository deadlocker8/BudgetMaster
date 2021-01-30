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
	private BackupStatus backupStatus;

	protected BackupTask(DatabaseService databaseService, SettingsService settingsService)
	{
		this.databaseService = databaseService;
		this.settingsService = settingsService;

		final Path applicationSupportFolder = Main.getApplicationSupportFolder();
		this.backupFolder = applicationSupportFolder.resolve("backups");

		this.backupStatus = BackupStatus.UNKNOWN;
	}

	protected Path getBackupFolder()
	{
		return backupFolder;
	}

	public BackupStatus getBackupStatus()
	{
		return backupStatus;
	}

	public void setBackupStatus(BackupStatus backupStatus)
	{
		this.backupStatus = backupStatus;
	}

	public abstract void cleanup(Settings previousSettings, Settings newSettings);

	public abstract boolean needsCleanup(Settings previousSettings, Settings newSettings);
}
