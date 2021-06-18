package de.deadlocker8.budgetmaster.database.model;

import java.util.List;

public interface Upgradeable<T>
{
	T upgrade(List<? extends BackupInfo> backupInfoItems);
}
