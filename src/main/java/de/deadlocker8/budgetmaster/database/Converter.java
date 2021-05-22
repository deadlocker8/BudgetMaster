package de.deadlocker8.budgetmaster.database;

public interface Converter<T, S>
{
	T convert(S backupItem);
}
