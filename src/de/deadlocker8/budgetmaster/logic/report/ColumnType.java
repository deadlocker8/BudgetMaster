package de.deadlocker8.budgetmaster.logic.report;

public enum ColumnType
{
	POSITION("Nr."),
	DATE("Datum"),
	REPEATING("Wiederholend"),
	CATEGORY("Kategorie"),
	NAME("Name"),
	DESCRIPTION("Notiz"), 
	RATING("Bewertung"), 
	AMOUNT("Betrag");
	
	private String name;

	private ColumnType(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}