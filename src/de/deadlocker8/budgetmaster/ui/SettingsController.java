package de.deadlocker8.budgetmaster.ui;

import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import tools.AlertGenerator;

public class SettingsController
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private TextField textFieldSecret;
	@FXML private Label labelSecret;
	@FXML private Button buttonSave;

	private Controller controller;

	public void init(Controller controller)
	{
		this.controller = controller;
		if(controller.getSettings() != null)
		{
			textFieldSecret.setText(controller.getSettings().getSecret());
		}
		
		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");
		labelSecret.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		buttonSave.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");	
	}
	
	public void save()
	{
		String secret = textFieldSecret.getText().trim();
		if(secret != null && !secret.equals(""))
		{
			if(controller.getSettings() != null)
			{
				controller.getSettings().setSecret(secret);				
			}
			else
			{
				Settings settings = new Settings();
				settings.setSecret(secret);
				controller.setSettings(settings);
			}
			Utils.saveSettings(controller.getSettings());	
			controller.showNotification("Erfolgreich gespeichert");
		}
		else
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Das Server Passwortfel darf nicht leer sein!", controller.getIcon(), controller.getStage(), null, false);
		}
	}
}