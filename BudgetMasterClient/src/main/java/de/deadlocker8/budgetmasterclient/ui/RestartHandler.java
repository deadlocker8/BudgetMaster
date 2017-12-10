package de.deadlocker8.budgetmasterclient.ui;

import java.util.Optional;

import de.deadlocker8.budgetmaster.logic.utils.LanguageType;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.main.Main;
import de.deadlocker8.budgetmasterclient.ui.controller.Controller;
import de.deadlocker8.budgetmasterclient.ui.controller.SplashScreenController;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import tools.Localization;

public class RestartHandler
{
	private Controller controller;
	
	public RestartHandler(Controller controller)
	{
		this.controller = controller;
	}

	public void handleRestart(LanguageType previousLanguage)
	{
		controller.refresh(controller.getFilterSettings());
		controller.showNotification(Localization.getString(Strings.NOTIFICATION_SETTINGS_SAVE));
		
		if(controller.getSettings().isAutoUpdateCheckEnabled())
		{
			controller.checkForUpdates(false);
		}
		
		//restart application if language has changed
		if(controller.getSettings().getLanguage() != previousLanguage)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(Localization.getString(Strings.INFO_TITLE_LANGUAGE_CHANGED));
			alert.setHeaderText("");
			alert.setContentText(Localization.getString(Strings.INFO_TEXT_LANGUAGE_CHANGED));			
			Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
			dialogStage.getIcons().add(controller.getIcon());					
			
			ButtonType buttonTypeOne = new ButtonType(Localization.getString(Strings.INFO_TEXT_LANGUAGE_CHANGED_RESTART_NOW));
			ButtonType buttonTypeTwo = new ButtonType(Localization.getString(Strings.INFO_TEXT_LANGUAGE_CHANGED_RESTART_LATER));							
			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
			
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getButtonTypes().stream().map(dialogPane::lookupButton).forEach(button -> button.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
				if(KeyCode.ENTER.equals(event.getCode()) && event.getTarget() instanceof Button)
				{
					((Button)event.getTarget()).fire();
				}
			}));
			
			Optional<ButtonType> result = alert.showAndWait();						
			if (result.get() == buttonTypeOne)
			{				
				controller.getStage().close();
				
				Localization.loadLanguage(controller.getSettings().getLanguage().getLocale());
				
			    Image icon = new Image("/de/deadlocker8/budgetmaster/icon.png");
				new SplashScreenController(Main.primaryStage, icon, false, controller.getShutdownHandler());			
			}
			else
			{
				alert.close();
			}
		}
	}
}
