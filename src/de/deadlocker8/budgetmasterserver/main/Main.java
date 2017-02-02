package de.deadlocker8.budgetmasterserver.main;

public class Main
{
	public static void main(String[] args)
	{
		Settings settings = Utils.loadSettings();
		DatabaseHandler handler = new DatabaseHandler(settings);
		//handler.listTables();
//		System.out.println(handler.getCategoryBudget(2017, 1));
		System.out.println(handler.getPayments(2017, 1));
	}
}