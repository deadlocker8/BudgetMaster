package de.deadlocker8.budgetmasterclient.ui.commandLine.commands;

import de.deadlocker8.budgetmasterclient.ui.commandLine.CommandBundle;

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
		if((command.length - 1) == numberOfParams)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public abstract void execute(String[] command, CommandBundle bundle);	
}