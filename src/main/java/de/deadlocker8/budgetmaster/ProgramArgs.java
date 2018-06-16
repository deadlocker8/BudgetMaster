package de.deadlocker8.budgetmaster;

import java.util.List;

public class ProgramArgs
{
	private static List<String> args;

	public static void setArgs(List<String> args)
	{
		ProgramArgs.args = args;
	}

	public static List<String> getArgs()
	{
		return args;
	}
}