package de.deadlocker8.budgetmaster;

import de.deadlocker8.budgetmaster.utils.Strings;
import de.tobias.logger.FileOutputOption;
import de.tobias.logger.LogLevel;
import de.tobias.logger.LogLevelFilter;
import de.tobias.logger.Logger;
import de.tobias.utils.io.PathUtils;
import de.tobias.utils.util.Localization;
import de.tobias.utils.util.SystemUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Locale;


@SpringBootApplication
public class Main implements ApplicationRunner
{
	public static void main(String[] args)
	{
		Localization.setDelegate(new Localization.LocalizationDelegate()
		{
			@Override
			public Locale getLocale()
			{
				return Locale.ENGLISH;
			}

			@Override
			public String getBaseResource()
			{
				return "languages/";
			}

			@Override
			public boolean useMessageFormatter()
			{
				return true;
			}
		});
		Localization.load();

		Path applicationSupportFolder = SystemUtils.getApplicationSupportDirectoryPath(Localization.getString("folder"));
		PathUtils.createDirectoriesIfNotExists(applicationSupportFolder);

		Logger.init(applicationSupportFolder);
		Logger.setFileOutput(FileOutputOption.COMBINED);
		Logger.appInfo(Localization.getString(Strings.APP_NAME), Localization.getString(Strings.VERSION_NAME), Localization.getString(Strings.VERSION_CODE), Localization.getString(Strings.VERSION_DATE));

		ProgramArgs.setArgs(Arrays.asList(args));

		Path settingsPath = applicationSupportFolder.resolve("settings.properties");
		if(Files.notExists(settingsPath))
		{
			try
			{
				Logger.warning("BudgetMaster settings file ({0}) is missing. A default file will be created. Please fill in your settings.", settingsPath);
				Files.copy(Main.class.getClassLoader().getResourceAsStream("config/templates/settings.properties"), settingsPath, StandardCopyOption.REPLACE_EXISTING);
				System.exit(1);
			}
			catch(IOException e)
			{
				Logger.error(e);
			}
		}

		SpringApplication.run(Main.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		String logLevelParam = args.getOptionValues("loglevel").get(0);
		if(logLevelParam.equalsIgnoreCase("debug"))
		{
			Logger.setLevelFilter(LogLevelFilter.DEBUG);
			Logger.addFilter(logMessage -> {
				if(logMessage.getCaller().getClassName().contains("deadlocker8"))
				{
					return true;
				}

				if(logMessage.getLevel().equals(LogLevel.DEBUG) || logMessage.getLevel().equals(LogLevel.TRACE))
				{
					return false;
				}

				return true;
			});
		}
		else
		{
			Logger.setLevelFilter(LogLevelFilter.NORMAL);
			Logger.setFileOutput(FileOutputOption.COMBINED);
		}
	}
}