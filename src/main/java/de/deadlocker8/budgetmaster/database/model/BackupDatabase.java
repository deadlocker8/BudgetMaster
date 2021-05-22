package de.deadlocker8.budgetmaster.database.model;

import de.deadlocker8.budgetmaster.database.Database;

public interface BackupDatabase
{
	int getVersion();

	BackupDatabase upgrade();

	Database convertToInternal();
}
