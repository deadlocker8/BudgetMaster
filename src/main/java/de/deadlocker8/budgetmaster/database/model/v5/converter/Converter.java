package de.deadlocker8.budgetmaster.database.model.v5.converter;

public interface Converter<T, S>
{
	T convert(S backupItem);
}
