package de.deadlocker8.budgetmaster.database.model;

public interface Converter<T, S>
{
	T convertToInternalForm(S backupItem);

	S convertToExternalForm(T internalItem);
}
