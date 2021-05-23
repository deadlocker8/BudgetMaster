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

	default <T, S> List<T> convertItemsToInternal(List<S> backupItems, Converter<T, S> converter)
	{
		List<T> convertedItems = new ArrayList<>();
		for(S backupItem : backupItems)
		{
			convertedItems.add(converter.convertToInternalForm(backupItem));
		}
		return convertedItems;
	}

	default <T, S> List<S> convertItemsToExternal(List<T> backupItems, Converter<T, S> converter)
	{
		List<S> convertedItems = new ArrayList<>();
		for(T backupItem : backupItems)
		{
			convertedItems.add(converter.convertToExternalForm(backupItem));
		}
		return convertedItems;
	}
}
