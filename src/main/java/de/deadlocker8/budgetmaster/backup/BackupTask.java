package de.deadlocker8.budgetmaster.backup;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.settings.SettingsService;

import java.nio.file.Path;

public abstract class BackupTask implements Runnable
{
	protected static final String DATABASE_FILE_NAME = "budgetmaster.mv.db";

	protected final DatabaseService databaseService;
	protected final SettingsService settingsService;
	private final Path backupFolder;

	protected BackupTask(DatabaseService databaseService, SettingsService settingsService)
	{
		this.databaseService = databaseService;
		this.settingsService = settingsService;

		final Path applicationSupportFolder = Main.getApplicationSupportFolder();
		this.backupFolder = applicationSupportFolder.resolve("backups");
	}

	protected Path getBackupFolder()
	{
		return backupFolder;
	}
}
