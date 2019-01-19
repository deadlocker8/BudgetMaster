package de.deadlocker8.budgetmaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProgramArgs
{
	private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

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
}