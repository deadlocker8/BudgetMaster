package de.deadlocker8.budgetmaster;

import de.deadlocker8.budgetmaster.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tools.Localization;


@SpringBootApplication
public class Main implements ApplicationRunner
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args)
	{
		SpringApplication.run(Main.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		//TODO set loglevel
//		String logLevelParam = args.getOptionValues("loglevel").get(0);

		logAppInfo(Localization.getString(Strings.APP_NAME), Localization.getString(Strings.VERSION_NAME), Localization.getString(Strings.VERSION_CODE), Localization.getString(Strings.VERSION_DATE));
	}

	private void logAppInfo(String appName, String versionName, String versionCode, String versionDate) {
		LOGGER.info(appName + " - v" + versionName + " - (versioncode: " + versionCode + ") from " + versionDate + ")");
	}
}