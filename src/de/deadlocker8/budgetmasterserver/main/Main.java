package de.deadlocker8.budgetmasterserver.main;

public class Main
{
	public static void main(String[] args)
	{
		DatabaseHandler handler = new DatabaseHandler();
		//handler.listTables();
		System.out.println(handler.getCategoryBudget(2017, 1));
	}
}