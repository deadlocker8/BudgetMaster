package de.deadlocker8.budgetmasterclient.main;

import java.io.File;
import java.util.Locale;

import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.utils.FileHelper;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.ShutdownHandler;
import de.deadlocker8.budgetmasterclient.ui.controller.SplashScreenController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logger.FileOutputMode;
import logger.Logger;
import tools.Localization;
import tools.PathUtils;

public class Main extends Application
{
	public static Stage primaryStage;

	@Override
	public void start(Stage stage)
	{
		primaryStage = stage;

		// load correct language
		Settings settings = FileHelper.loadSettings();
		if(settings != null && settings.getLanguage() != null)
		{
			Localization.loadLanguage(settings.getLanguage().getLocale());
		}
		
		ShutdownHandler shutdownHandler = new ShutdownHandler();

		Image icon = new Image("/de/deadlocker8/budgetmaster/icon.png");
		new SplashScreenController(stage, icon, getParameters().getNamed().get("update") != null, shutdownHandler);
	}

	@Override
	public void init() throws Exception
	{
		Localization.init("de/deadlocker8/budgetmaster/languages/");
		Localization.loadLanguage(Locale.ENGLISH);

		Parameters params = getParameters();
		String logLevelParam = params.getNamed().get("loglevel");
		Logger.setLevel(logLevelParam);

		File logFolder = new File(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER));
		PathUtils.checkFolder(logFolder);
		Logger.enableFileOutput(logFolder, System.out, System.err, FileOutputMode.COMBINED);

		Logger.appInfo(Localization.getString(Strings.APP_NAME), Localization.getString(Strings.VERSION_NAME), Localization.getString(Strings.VERSION_CODE), Localization.getString(Strings.VERSION_DATE));
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}