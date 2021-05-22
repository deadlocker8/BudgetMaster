package de.deadlocker8.budgetmaster.database.model;

public interface Converter<T, S>
{
	T convert(S backupItem);
}
