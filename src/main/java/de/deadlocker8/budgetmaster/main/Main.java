package de.deadlocker8.budgetmaster.main;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import logger.FileOutputMode;
import logger.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tools.Localization;
import tools.PathUtils;

import java.io.File;
import java.util.Locale;


@SpringBootApplication
public class Main implements ApplicationRunner
{
	public static void main(String[] args)
	{
		SpringApplication.run(Main.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		Localization.init("de/deadlocker8/budgetmaster/languages/");
		Localization.loadLanguage(Locale.ENGLISH);

		String logLevelParam = args.getOptionValues("loglevel").get(0);
		Logger.setLevel(logLevelParam);

		File logFolder = new File(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER));
		PathUtils.checkFolder(logFolder);
		Logger.enableFileOutput(logFolder, System.out, System.err, FileOutputMode.COMBINED);

		Logger.appInfo(Localization.getString(Strings.APP_NAME), Localization.getString(Strings.VERSION_NAME), Localization.getString(Strings.VERSION_CODE), Localization.getString(Strings.VERSION_DATE));
	}
}