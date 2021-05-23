package de.deadlocker8.budgetmaster.database.model;

import de.deadlocker8.budgetmaster.database.InternalDatabase;

import java.util.ArrayList;
import java.util.List;

public interface BackupDatabase
{
	int getVersion();

	BackupDatabase upgrade();

	InternalDatabase convertToInternal();

	default <T> List<T> upgradeItems(List<? extends Upgradeable<T>> items)
	{
		List<T> upgradedItems = new ArrayList<>();
		for(Upgradeable<T> item : items)
		{
			upgradedItems.add(item.upgrade());
		}
		return upgradedItems;
	}
}
