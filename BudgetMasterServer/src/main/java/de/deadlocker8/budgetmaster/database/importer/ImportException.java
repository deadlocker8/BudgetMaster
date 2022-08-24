package de.deadlocker8.budgetmaster.database.importer;

public class ImportException extends Exception
{
	public ImportException(String message)
	{
		super(message);
	}

	public ImportException(String message, Throwable cause)
	{
		super(message, cause);
	}
}

