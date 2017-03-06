package de.deadlocker8.budgetmaster.main;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import de.deadlocker8.budgetmaster.ui.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logger.LogLevel;
import logger.Logger;

public class Main extends Application
{
	public static ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);
	
	@Override
	public void start(Stage stage)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("de/deadlocker8/budgetmaster/ui/GUI.fxml"));
			Parent root = (Parent)loader.load();

			Scene scene = new Scene(root, 600, 600);			

			((Controller)loader.getController()).init(stage);

			stage.setResizable(true);
			stage.setMinHeight(600);
			stage.setMinWidth(600);
			stage.getIcons().add(new Image("/de/deadlocker8/budgetmaster/resources/icon.png"));
			stage.setTitle(bundle.getString("app.name"));
			stage.setScene(scene);
			stage.getScene().getStylesheets().add("/de/deadlocker8/budgetmaster/ui/style.css");
			stage.show();
		}
		catch(Exception e)
		{
			Logger.error(e);
		}
	}

	public static void main(String[] args)
	{
		if(Arrays.asList(args).contains("debug"))
		{
			Logger.setLevel(LogLevel.ALL);
			Logger.info("Running in Debug Mode");
		}
		else
		{
			Logger.setLevel(LogLevel.ERROR);
		}
	
		Logger.appInfo(bundle.getString("app.name"), bundle.getString("version.name"), bundle.getString("version.code"), bundle.getString("version.date"));
		
		launch(args);
	}
	
}