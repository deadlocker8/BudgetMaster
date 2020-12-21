package de.deadlocker8.budgetmaster.backup;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.database.DatabaseService;

import java.nio.file.Path;

public abstract class BackupTask implements Runnable
{
	protected static final String DATABASE_FILE_NAME = "budgetmaster.mv.db";

	private final DatabaseService databaseService;
	private final Path backupFolder;

	protected BackupTask(DatabaseService databaseService)
	{
		this.databaseService = databaseService;

		final Path applicationSupportFolder = Main.getApplicationSupportFolder();
		this.backupFolder = applicationSupportFolder.resolve("backups");
	}

	protected Path getBackupFolder()
	{
		return backupFolder;
	}
}
