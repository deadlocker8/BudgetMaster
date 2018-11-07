package de.deadlocker8.budgetmaster;

import de.tobias.utils.io.PathUtils;
import de.tobias.utils.util.Localization;
import de.tobias.utils.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

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

		ProgramArgs.setArgs(Arrays.asList(args));

		Path settingsPath = applicationSupportFolder.resolve("settings.properties");
		if(Files.notExists(settingsPath))
		{
			try
			{
				LOGGER.warn("BudgetMaster settings file ({0}) is missing. A default file will be created. Please fill in your settings.", settingsPath);
				Files.copy(Main.class.getClassLoader().getResourceAsStream("config/templates/settings.properties"), settingsPath, StandardCopyOption.REPLACE_EXISTING);
				System.exit(1);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		Path logPath = applicationSupportFolder.resolve("error.log");
		SpringApplication.run(Main.class, "--logging.file=" + logPath.toString());
	}

	@Override
	public void run(ApplicationArguments args)
	{
		Build build = Build.getInstance();
		logAppInfo(build.getAppName(), build.getVersionName(), build.getVersionCode(), build.getVersionDate());
		LOGGER.info("=============================");
		LOGGER.info("+++ BUDGETMASTER STARTED +++");
		LOGGER.info("=============================");
	}

	private static void logAppInfo(String appName, String versionName, String versionCode, String versionDate) {
		LOGGER.info(appName + " - v" + versionName + " - (versioncode: " + versionCode + ") from " + versionDate + ")");
	}
}