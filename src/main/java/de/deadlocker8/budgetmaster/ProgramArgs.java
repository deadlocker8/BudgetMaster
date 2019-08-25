package de.deadlocker8.budgetmaster;

import java.util.ArrayList;
import java.util.List;

public class ProgramArgs
{
	private static List<String> args = new ArrayList<>();

	public static void setArgs(List<String> args)
	{
		ProgramArgs.args = args;
	}

	public static List<String> getArgs()
	{
		return args;
	}

	public static boolean isDebug()
	{
		return ProgramArgs.getArgs().contains("--debugFolder");
	}

	public static boolean isTest()
	{
		return RunMode.currentRunMode.equals(RunMode.TEST);
	}
}