package de.deadlocker8.budgetmaster.ui;

import java.io.IOException;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import logger.Logger;
import tools.AlertGenerator;

public class SettingsController
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private TextField textFieldURL;
	@FXML private Label labelURL;
	@FXML private TextField textFieldSecret;
	@FXML private Label labelSecret;
	@FXML private TextField textFieldCurrency;
	@FXML private Label labelCurrency;
	@FXML private Button buttonSave;
	@FXML private RadioButton radioButtonRestActivated;
	@FXML private RadioButton radioButtonRestDeactivated;
	@FXML private TextArea textAreaTrustedHosts;

	private Controller controller;

	public void init(Controller controller)
	{
		this.controller = controller;
		if(controller.getSettings() != null)
		{
			textFieldURL.setText(controller.getSettings().getUrl());
			textFieldSecret.setText(controller.getSettings().getSecret());
			textFieldCurrency.setText(controller.getSettings().getCurrency());
			if(controller.getSettings().isRestActivated())
			{
				radioButtonRestActivated.setSelected(true);
			}
			else
			{
				radioButtonRestDeactivated.setSelected(true);
			}
			setTextAreaTrustedHosts(controller.getSettings().getTrustedHosts());
		}
		
		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");
		labelSecret.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		labelURL.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		labelCurrency.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		buttonSave.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");	
		textFieldURL.setPromptText("z.B. https://yourdomain.de");
		textFieldCurrency.setPromptText("z.B. €, CHF, $");
		textAreaTrustedHosts.setPromptText("z.B. localhost");
		
		ToggleGroup toggleGroup = new ToggleGroup();
		radioButtonRestActivated.setToggleGroup(toggleGroup);
		radioButtonRestDeactivated.setToggleGroup(toggleGroup);
	}
	
	private void setTextAreaTrustedHosts(ArrayList<String> trustedHosts)
	{
		StringBuilder trustedHostsString = new StringBuilder();
		for(String currentHost : trustedHosts)
		{
			trustedHostsString.append(currentHost);
			trustedHostsString.append("\n");
		}
		textAreaTrustedHosts.setText(trustedHostsString.toString());
	}
	
	public void save()
	{
		String url = textFieldURL.getText().trim();
		String secret = textFieldSecret.getText().trim();
		String currency = textFieldCurrency.getText().trim();
		if(url != null && !url.equals(""))
		{
			if(secret != null && !secret.equals(""))
			{
				if(currency != null && !currency.equals(""))
				{
					ArrayList<String> trustedHosts = new ArrayList<>();
					String trustedHostText = textAreaTrustedHosts.getText();
					String[] trustedHostsArray = trustedHostText.split("\n");
					for(String currentHost : trustedHostsArray)
					{
						currentHost = currentHost.trim();
						if(!currentHost.equals(""))
						{
							trustedHosts.add(currentHost);
						}
					}
					setTextAreaTrustedHosts(trustedHosts);
					
					if(controller.getSettings() != null)
					{
						controller.getSettings().setUrl(url);
						controller.getSettings().setSecret(secret);	
						controller.getSettings().setCurrency(currency);
						controller.getSettings().setRestActivated(radioButtonRestActivated.isSelected());
						controller.getSettings().setTrustedHosts(trustedHosts);
					}
					else
					{
						Settings settings = new Settings();
						settings.setUrl(url);
						settings.setSecret(secret);
						settings.setCurrency(currency);
						settings.setRestActivated(radioButtonRestActivated.isSelected());
						settings.setTrustedHosts(trustedHosts);
						controller.setSettings(settings);
					}
					
					try
					{
						Utils.saveSettings(controller.getSettings());
					}
					catch(IOException e)
					{
						Logger.error(e);
						AlertGenerator.showAlert(AlertType.ERROR, "Fehler", "", "Beim Speichern der Einstellungen ist ein Fehler aufgetreten", controller.getIcon(), controller.getStage(), null, false);
					}	
					controller.refresh(controller.getFilterSettings());
					controller.showNotification("Erfolgreich gespeichert");
				}
				else
				{
					AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Bitte gib deine gewünschte Währung ein!", controller.getIcon(), controller.getStage(), null, false);
				}
			}
			else
			{
				AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Das Server Passwortfeld darf nicht leer sein!", controller.getIcon(), controller.getStage(), null, false);
			}
		}
		else
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Das Feld für die Server URL darf nicht leer sein!", controller.getIcon(), controller.getStage(), null, false);
		}
	}
}