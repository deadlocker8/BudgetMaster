package de.deadlocker8.budgetmasterclient.ui.commandLine.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.deadlocker8.budgetmaster.logic.utils.FileHelper;
import de.deadlocker8.budgetmaster.logic.utils.LanguageType;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.RestartHandler;
import de.deadlocker8.budgetmasterclient.ui.commandLine.CommandBundle;
import tools.Localization;
import tools.PathUtils;

public class CommandSwitch extends Command
{
	public CommandSwitch()
	{		
		super.keyword = "switch";		
		super.numberOfParams = 0;
		super.helptText = "help.switch";
	}

	@Override
	public void execute(String[] command, CommandBundle bundle)
	{		
		if(!isValid(command))
		{			
			bundle.getController().print(bundle.getString("error.invalid.arguments"));
			return;
		}	
	
		Path currentSettingsFile = Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/settings.json");
		Path secondSettingsFile = Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/_settings.json");
		
		if(secondSettingsFile.toFile().exists())
		{
			try
			{
				Path temp = Paths.get(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER) + "/__settings.json");
				Files.move(currentSettingsFile, temp);
				Files.move(secondSettingsFile, currentSettingsFile);
				Files.move(temp, secondSettingsFile);
			}
			catch(IOException e)
			{
				bundle.getController().print(bundle.getString("switch.error"));				
			}
		}
		else
		{
			bundle.getController().print(bundle.getString("switch.new"));
			try
			{
				Files.copy(currentSettingsFile, secondSettingsFile);
			}
			catch(IOException e)
			{
				bundle.getController().print(bundle.getString("switch.error"));	
			}
		}
		
		LanguageType previousLanguage = bundle.getParentController().getSettings().getLanguage();
		bundle.getParentController().setSettings(FileHelper.loadSettings());
		RestartHandler restartHandler = new RestartHandler(bundle.getParentController());
		restartHandler.handleRestart(previousLanguage);
		bundle.getController().print(bundle.getString("switch.success"));
	}
}