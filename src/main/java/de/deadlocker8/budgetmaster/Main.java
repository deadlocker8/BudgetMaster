package de.deadlocker8.budgetmaster;

import de.deadlocker8.budgetmaster.utils.Strings;
import de.tobias.logger.*;
import de.tobias.utils.application.ApplicationUtils;
import de.tobias.utils.application.container.PathType;
import de.tobias.utils.util.Localization;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Locale;


@SpringBootApplication
public class Main implements ApplicationRunner
{
	public static void main(String[] args)
	{
		Logger.init(ApplicationUtils.getApplication().getPath(PathType.LOG));

		ProgramArgs.setArgs(Arrays.asList(args));

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

		Logger.appInfo(Localization.getString(Strings.APP_NAME), Localization.getString(Strings.VERSION_NAME), Localization.getString(Strings.VERSION_CODE), Localization.getString(Strings.VERSION_DATE));
	}
}