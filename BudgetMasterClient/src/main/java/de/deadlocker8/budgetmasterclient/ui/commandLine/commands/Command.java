package de.deadlocker8.budgetmasterclient.ui.commandLine.commands;

public abstract class Command
{
	public String keyword;
	public int numberOfParams;
	public String helptText;	
	
	public String getKeyword()
	{
		return keyword;
	}	
	
	public int getNumberOfParams()
	{
		return numberOfParams;
	}
	
	public String getHelpText()
	{
		return keyword;
	}
	
	public boolean isValid(String[] command)
	{
		if((command.length - 1) < numberOfParams || (command.length -1) > numberOfParams)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public abstract void execute(String[] command, CommandBundle bundle);	
}