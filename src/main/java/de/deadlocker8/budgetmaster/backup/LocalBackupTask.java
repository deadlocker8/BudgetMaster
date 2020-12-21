package de.deadlocker8.budgetmaster.backup;

import de.deadlocker8.budgetmaster.database.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class LocalBackupTask extends BackupTask
{
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalBackupTask.class);

	private final DatabaseService databaseService;

	public LocalBackupTask(DatabaseService databaseService)
	{
		super(databaseService);
		this.databaseService = databaseService;
	}

	@Override
	public void run()
	{
		LOGGER.debug(MessageFormat.format("Starting backup with strategy \"{0}\"", AutoBackupStrategy.LOCAL));
		databaseService.backupDatabase(getBackupFolder());
		LOGGER.debug("Backup DONE");
	}
}
