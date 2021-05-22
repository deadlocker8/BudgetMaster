package de.deadlocker8.budgetmaster.database.model;

import de.deadlocker8.budgetmaster.database.InternalDatabase;

public interface BackupDatabase
{
	int getVersion();

	BackupDatabase upgrade();

	InternalDatabase convertToInternal();
}
