package de.deadlocker8.budgetmasterclient.ui.commandLine.commands;

import java.util.ArrayList;
import java.util.Arrays;

public class PossibleCommands
{			
	public static final ArrayList<Command> possibleCommands = new ArrayList<>(Arrays.asList(
				new CommandList(),
				new CommandHelp(),
				new CommandClear(),
				new CommandShortcuts(),
				new CommandOpen()
			));	
}