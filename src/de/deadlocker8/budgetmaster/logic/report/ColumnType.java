package de.deadlocker8.budgetmaster.logic.report;

public enum ColumnType
{
	POSITION("Nr.", 1),
	DATE("Datum", 2),
	REPEATING("Wiederholend", 1),
	CATEGORY("Kategorie", 3),
	NAME("Name", 3),
	DESCRIPTION("Notiz", 3), 
	RATING("+/-", 1), 
	AMOUNT("Betrag", 2);
	
	private String name;
	private float proportion;

	private ColumnType(String name, float proportion)
	{
		this.name = name;
		this.proportion = proportion;
	}

	public String getName()
	{
		return name;
	}

	public float getProportion()
	{
		return proportion;
	}
}