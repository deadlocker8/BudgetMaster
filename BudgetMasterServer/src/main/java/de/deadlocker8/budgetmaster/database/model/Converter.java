package de.deadlocker8.budgetmaster.database.model;

import de.deadlocker8.budgetmaster.utils.ProvidesID;

import java.util.List;

public interface Converter<T, S>
{
	T convertToInternalForm(S backupItem);

	S convertToExternalForm(T internalItem);

	default <U extends ProvidesID> U getItemById(List<U> items, Integer ID)
	{
		return items.stream()
				.filter(item -> item.getID().equals(ID))
				.findFirst().orElse(null);
	}
}
