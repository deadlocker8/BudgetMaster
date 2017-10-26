package de.deadlocker8.budgetmasterclient.ui.commandLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CommandLine
{
	private Stage owner;
	private Stage newStage;
	private Image icon;
	private ResourceBundle languageBundle;
	public ArrayList<HistoryEntry> globalHistory = new ArrayList<>();
	public int lastShownCommand = 1;
	public ArrayList<HistoryEntry> history = new ArrayList<>();
	private final String promptText = ">>>";	
	private CommandBundle bundle;
	
	public CommandLine(Stage owner, Image icon, ResourceBundle languageBundle, CommandBundle commandBundle)
	{
		this.owner = owner;
		this.icon = icon;
		this.languageBundle = languageBundle;
		this.bundle = commandBundle;
	}	
	
	public Stage getOwner()
	{
		return owner;
	}

	public Stage getStage()
	{
		return newStage;
	}

	public Image getIcon()
	{
		return icon;
	}	

	public int getLastShwonCommand()
	{
		return lastShownCommand;
	}

	public String getPromptText()
	{
		return promptText;
	}

	public ResourceBundle getLanguageBundle()
	{
		return languageBundle;
	}
	
	public CommandBundle getBundle()
	{
		return bundle;
	}

	public void showCommandLine(String title, double width, double height, double minWidth, double minHeight, double positionX, double positionY, boolean dark) throws IOException
	{		
		if(newStage != null)
		{
			if(newStage.isShowing())
			{
				return;
			}
		}		
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/commandLine/CommandLineGUI.fxml"));

		Parent root = (Parent)fxmlLoader.load();
		newStage = new Stage();
		newStage.setScene(new Scene(root, width, height));
		newStage.setResizable(true);
		newStage.setTitle(title);	
		newStage.initOwner(owner);
		
		newStage.setMinWidth(minWidth);
		newStage.setMinHeight(minHeight);	
		if(positionX != -1)
		{
			newStage.setX(positionX);
		}
		if(positionY != -1)
		{
			newStage.setY(positionY);
		}

		if(dark)
		{
			root.setStyle("-fx-base: rgb(50, 50, 50); -fx-background: rgb(50, 50, 50); -fx-control-inner-background:  rgb(10, 10, 10);");
		}
		
		if(icon != null)
		{
			newStage.getIcons().add(icon);
		}

		CommandLineController newController = fxmlLoader.getController();
		newController.init(this);	

		newStage.initModality(Modality.NONE);		
		newStage.show();
	}
	
	public void closeCommandLine()
	{
		if(newStage != null)
		{
			if(newStage.isShowing())
			{
				newStage.close();
			}
		}		
	}
}