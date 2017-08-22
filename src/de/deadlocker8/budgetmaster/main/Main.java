package de.deadlocker8.budgetmaster.main;

import java.io.File;
import java.util.Locale;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmaster.ui.controller.SplashScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logger.FileOutputMode;
import logger.Logger;
import tools.Localization;
import tools.PathUtils;

public class Main extends Application
{
	@Override
	public void start(Stage stage)
	{
		try
		{			
		    Image icon = new Image("/de/deadlocker8/budgetmaster/resources/icon.png");
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("de/deadlocker8/budgetmaster/ui/fxml/SplashScreen.fxml"));
			loader.setResources(Localization.getBundle());
			Parent root = (Parent)loader.load();
			
			Scene scene = new Scene(root, 450, 230);

			((SplashScreenController)loader.getController()).init(stage, icon);

			stage.setResizable(false);			
			stage.getIcons().add(icon);
			stage.setTitle(Localization.getString(Strings.APP_NAME));
			stage.setScene(scene);			
			stage.show();
		}
		catch(Exception e)
		{
			Logger.error(e);
		}
	}

	@Override
	public void init() throws Exception
	{
		Localization.init("de/deadlocker8/budgetmaster/resources/languages/");
		Localization.loadLanguage(Locale.GERMANY);
		
		Parameters params = getParameters();
		String logLevelParam = params.getNamed().get("loglevel");
		Logger.setLevel(logLevelParam);
		
		File logFolder = new File(PathUtils.getOSindependentPath() + Localization.getString(Strings.FOLDER));
		PathUtils.checkFolder(logFolder);
		Logger.enableFileOutput(logFolder, System.out, System.err, FileOutputMode.COMBINED);

		Logger.appInfo(Localization.getString(Strings.APP_NAME),
						Localization.getString(Strings.VERSION_NAME),
						Localization.getString(Strings.VERSION_CODE),
						Localization.getString(Strings.VERSION_DATE));
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}