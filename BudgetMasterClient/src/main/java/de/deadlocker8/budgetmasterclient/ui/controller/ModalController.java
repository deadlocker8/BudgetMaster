package de.deadlocker8.budgetmasterclient.ui.controller;

import java.util.Optional;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logger.Logger;
import tools.Localization;

public class ModalController
{
	@FXML private Label labelMessage;	
	private Alert alert;
	
	public void init(Controller controller, Stage stage, String message)
	{
		labelMessage.setText(message);
		stage.setOnCloseRequest((e)->{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle(Localization.getString(Strings.INFO_TITLE_SHUTDOWN));
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(controller.getStage());
			alert.setHeaderText("");
			alert.setContentText(Localization.getString(Strings.INFO_TEXT_SHUTDOWN));
			Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
			dialogStage.getIcons().add(controller.getIcon());						
			
			ButtonType buttonTypeOne = new ButtonType(Localization.getString(Strings.CANCEL));
			ButtonType buttonTypeTwo = new ButtonType(Localization.getString(Strings.OK));						
			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
			
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getButtonTypes().stream().map(dialogPane::lookupButton).forEach(button -> button.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
				if(KeyCode.ENTER.equals(event.getCode()) && event.getTarget() instanceof Button)
				{
					((Button)event.getTarget()).fire();
				}
			}));
			
			Optional<ButtonType> result = alert.showAndWait();						
			if (result.get() == buttonTypeTwo)
			{
				Logger.debug("Shutting down during operation due to client request...");
				controller.getStage().fireEvent(new WindowEvent(controller.getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
			}			
			
			e.consume();
		});
	}
	
	public void setMessage(String message)
	{
		labelMessage.setText(message);
	}
	
	public void closeAlert()
	{
		if(alert != null && alert.isShowing())
		{
			Button cancelButton = ( Button ) alert.getDialogPane().lookupButton(alert.getButtonTypes().get(0));
			cancelButton.fire();
		}
	}
}