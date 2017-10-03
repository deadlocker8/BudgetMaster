package de.deadlocker8.budgetmasterclient.ui.controller;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logger.Logger;

public class BaseController
{
	private Parent parent;
	private Stage stage;
	
	public void load(String fxmlFileName, ResourceBundle resourceBundle)
	{		
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
			if(resourceBundle != null)
			{
				fxmlLoader.setResources(resourceBundle);
			}
			fxmlLoader.setController(this);
			parent = (Parent)fxmlLoader.load();
		}
		catch(IOException e)
		{
			Logger.error(e);
		}
	
		createNewStage();
		initStage(stage);
		init();
	}	
	
	public Stage getStage()
	{
		return stage;
	}

	public void createNewStage()
	{
		Scene scene = new Scene(parent);
		stage = new Stage();
		stage.setScene(scene);
	}
	
	public void initStage(Stage stage){};
	
	public void init() {};
}
