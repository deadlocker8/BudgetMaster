package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class BackupTask implements Runnable
{
	private final DatabaseService databaseService;

	@Autowired
	public BackupTask(DatabaseService databaseService)
	{
		this.databaseService = databaseService;
	}

	@Override
	public void run()
	{
		final Path applicationSupportFolder = Main.getApplicationSupportFolder();
		final Path backupFolder = applicationSupportFolder.resolve("backups");
		databaseService.backupDatabase(backupFolder);
	}
}
