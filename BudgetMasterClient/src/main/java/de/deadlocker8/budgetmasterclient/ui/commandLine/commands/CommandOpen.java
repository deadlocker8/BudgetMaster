package de.deadlocker8.budgetmasterclient.ui.commandLine.commands;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import tools.Localization;
import tools.PathUtils;

public class CommandOpen extends Command
{
	public CommandOpen()
	{		
		super.keyword = "open";		
		super.numberOfParams = 1;
		super.helptText = "help.open";
	}

	@Override
	public void execute(String[] command, CommandBundle bundle)
	{		
		if(!isValid(command))
		{			
			bundle.getController().print(bundle.getLanguageBundle().getString("error.invalid.arguments"));
			return;
		}
		
		if(!command[1].equals("settings"))
		{			
			bundle.getController().print(bundle.getLanguageBundle().getString("error.invalid.arguments"));
			return;
		}
		
		try
		{	
			File folder = Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER)).toFile();
			Desktop.getDesktop().open(folder);
			bundle.getController().print(bundle.getLanguageBundle().getString("help.success") + " " +  folder.getAbsolutePath());
		}
		catch(IOException e)
		{
			bundle.getController().print(e.getMessage());
		}
	}
}