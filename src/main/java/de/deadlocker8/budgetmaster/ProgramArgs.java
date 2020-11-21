package de.deadlocker8.budgetmaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProgramArgs
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgramArgs.class);

	private static List<String> args = new ArrayList<>();

	private ProgramArgs()
	{
	}

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

	public static Optional<String> getCustomFolder()
	{
		final Optional<String> customFolderOptional = ProgramArgs.getArgs().stream()
				.filter(arg -> arg.startsWith("--customFolder"))
				.findFirst();

		if(!customFolderOptional.isPresent())
		{
			return Optional.empty();
		}

		final String customFolderArg  = customFolderOptional.get();
		final String[] parts = customFolderArg.split("=");
		if(parts.length != 2)
		{
			LOGGER.error("Wrong format of argument --customFolder (example usage: --customFolder=\"C:/Users/User/BudgetMaster\")");
			return Optional.empty();
		}

		String customFolder = parts[1];
		customFolder = customFolder.replace("\"", "");
		return Optional.of(customFolder);
	}

	public static boolean isTest()
	{
		return RunMode.currentRunMode.equals(RunMode.TEST);
	}
}