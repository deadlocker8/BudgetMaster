package de.deadlocker8.budgetmaster.ui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.FileHelper;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmasterserver.logic.Database;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logger.Logger;
import tools.AlertGenerator;
import tools.BASE58Type;
import tools.ConvertTo;
import tools.HashUtils;
import tools.RandomCreations;
import tools.Worker;

public class SettingsController
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Label labelClientSecret;
	@FXML private TextField textFieldClientSecret;
	@FXML private TextField textFieldURL;
	@FXML private Label labelURL;
	@FXML private TextField textFieldSecret;
	@FXML private Label labelSecret;
	@FXML private TextField textFieldCurrency;
	@FXML private Label labelCurrency;
	@FXML private Button buttonSave;
	@FXML private Button buttonExportDB;
	@FXML private Button buttonImportDB;
	@FXML private Button buttonDeleteDB;
	@FXML private RadioButton radioButtonRestActivated;
	@FXML private RadioButton radioButtonRestDeactivated;
	@FXML private TextArea textAreaTrustedHosts;

	private Controller controller;

	public void init(Controller controller)
	{
		this.controller = controller;
		
		textFieldClientSecret.setText("******");
		radioButtonRestDeactivated.setSelected(true);
		
		if(controller.getSettings().isComplete())
		{
			textFieldURL.setText(controller.getSettings().getUrl());
			textFieldSecret.setText("******");
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

		anchorPaneMain.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND));
		labelClientSecret.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelSecret.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelURL.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelCurrency.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		buttonSave.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		buttonExportDB.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonImportDB.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonDeleteDB.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_RED) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
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
		if(trustedHosts != null)
		{
			for(String currentHost : trustedHosts)
			{
				trustedHostsString.append(currentHost);
				trustedHostsString.append("\n");
			}
			textAreaTrustedHosts.setText(trustedHostsString.toString());
		}
		else
		{
			textAreaTrustedHosts.setText("");
		}
	}

	public void save()
	{
		String clientSecret = textFieldClientSecret.getText().trim();
		String url = textFieldURL.getText().trim();
		String secret = textFieldSecret.getText().trim();
		String currency = textFieldCurrency.getText().trim();
		
		if(clientSecret == null || clientSecret.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Das Feld für das Client Passwort darf nicht leer sein!", controller.getIcon(), controller.getStage(), null, false);
			return;
		}
		
		if(url == null || url.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Das Feld für die Server URL darf nicht leer sein!", controller.getIcon(), controller.getStage(), null, false);
			return;
		}

		if(secret == null || secret.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Das Server Passwortfeld darf nicht leer sein!", controller.getIcon(), controller.getStage(), null, false);
			return;
		}

		if(currency == null || currency.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Bitte gib deine gewünschte Währung ein!", controller.getIcon(), controller.getStage(), null, false);
			return;
		}

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

		if(controller.getSettings().isComplete())
		{
			if(!clientSecret.equals("******"))
			{
				controller.getSettings().setClientSecret(HashUtils.hash(clientSecret, Helpers.SALT));
			}
			
			if(!secret.equals("******"))
			{
				controller.getSettings().setSecret(HashUtils.hash(secret, Helpers.SALT));
			}
			controller.getSettings().setUrl(url);
			controller.getSettings().setCurrency(currency);
			controller.getSettings().setRestActivated(radioButtonRestActivated.isSelected());
			controller.getSettings().setTrustedHosts(trustedHosts);
		}
		else
		{
			Settings settings = new Settings();
			
			if(!clientSecret.equals("******"))
			{
				settings.setClientSecret(HashUtils.hash(clientSecret, Helpers.SALT));
			}
			else
			{
				settings.setClientSecret(controller.getSettings().getClientSecret());
			}
			
			if(!secret.equals("******"))
			{
				settings.setSecret(HashUtils.hash(secret, Helpers.SALT));
			}
			else
			{
				settings.setSecret(controller.getSettings().getSecret());
			}
			settings.setUrl(url);			
			settings.setCurrency(currency);
			settings.setRestActivated(radioButtonRestActivated.isSelected());
			settings.setTrustedHosts(trustedHosts);
			controller.setSettings(settings);
		}

		try
		{
			FileHelper.saveSettings(controller.getSettings());
		}
		catch(IOException e)
		{
			Logger.error(e);
			AlertGenerator.showAlert(AlertType.ERROR, "Fehler", "", "Beim Speichern der Einstellungen ist ein Fehler aufgetreten", controller.getIcon(), controller.getStage(), null, false);
		}

		textFieldClientSecret.setText("******");
		textFieldSecret.setText("******");

		controller.refresh(controller.getFilterSettings());
		controller.showNotification("Erfolgreich gespeichert");
	}

	public void exportDB()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Datenbank exportieren");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON (*.json)", "*.json");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(controller.getStage());
		if(file != null)
		{
			Stage modalStage = Helpers.showModal("Vorgang läuft", "Die Datenbank wird exportiert, bitte warten...", controller.getStage(), controller.getIcon());

			Worker.runLater(() -> {
				try
				{
					ServerConnection connection = new ServerConnection(controller.getSettings());
					String databaseJSON = connection.exportDatabase();
					FileHelper.saveDatabaseJSON(file, databaseJSON);

					Platform.runLater(() -> {
						if(modalStage != null)
						{
							modalStage.close();
						}
						AlertGenerator.showAlert(AlertType.INFORMATION, "Erfolgreich exportiert", "", "Die Datenbank wurde erfolgreich exportiert.", controller.getIcon(), controller.getStage(), null, false);
					});
				}
				catch(Exception e)
				{
					Logger.error(e);
					Platform.runLater(() -> {
						if(modalStage != null)
						{
							modalStage.close();
						}
						controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));						
					});
				}
			});
		}
	}
	
	private void importDatabase()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Datenbank importieren");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON (*.json)", "*.json");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(controller.getStage());
		if(file != null)
		{
			Database database;
			try
			{
				database = FileHelper.loadDatabaseJSON(file);
				if(database.getCategories() == null || database.getNormalPayments() == null || database.getRepeatingPayments() == null)
				{
					AlertGenerator.showAlert(AlertType.ERROR, "Fehler", "", "Die angegebene Datei enthält kein gültiges BudgetMaster-Datenformat und kann daher nicht importiert werden.", controller.getIcon(), controller.getStage(), null, false);
					return;
				}
			}
			catch(IOException e1)
			{
				Logger.error(e1);
				AlertGenerator.showAlert(AlertType.ERROR, "Fehler", "", "Beim Einlesen der Datei ist ein Fehler aufgetreten.", controller.getIcon(), controller.getStage(), null, false);
				return;
			}

			Stage modalStage = Helpers.showModal("Vorgang läuft", "Die Datenbank wird importiert, bitte warten...", controller.getStage(), controller.getIcon());

			Worker.runLater(() -> {
				try
				{
					ServerConnection connection = new ServerConnection(controller.getSettings());
					connection.importDatabase(database);

					Platform.runLater(() -> {
						if(modalStage != null)
						{
							modalStage.close();
						}
						AlertGenerator.showAlert(AlertType.INFORMATION, "Erfolgreich exportiert", "", "Die Datenbank wurder erfolgreich importiert.", controller.getIcon(), controller.getStage(), null, false);
					});
				}
				catch(Exception e)
				{
					Logger.error(e);
					Platform.runLater(() -> {
						if(modalStage != null)
						{
							modalStage.close();
						}
						controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
					});
				}
			});
		}
	}

	public void importDB()
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Datenbank importieren");
		alert.setHeaderText("");		
		alert.setContentText("Soll die Datenbank vor dem Importieren gelöscht werden?");
		Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(controller.getIcon());
		dialogStage.initOwner(controller.getStage());

		ButtonType buttonTypeDelete = new ButtonType("Ja, Datenbank löschen");
		ButtonType buttonTypeAppend = new ButtonType("Nein, Daten hinzufügen");
		ButtonType buttonTypeCancel = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeDelete, buttonTypeAppend, buttonTypeCancel);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == buttonTypeDelete)
		{
			deleteDatabase(true);
		}	
		else if(result.get() == buttonTypeAppend)
		{	
			importDatabase();
		}
	}
	
	public void deleteDB()
	{
		deleteDatabase(false);
	}

	public void deleteDatabase(boolean importPending)
	{
		String verificationCode = ConvertTo.toBase58(RandomCreations.generateRandomMixedCaseString(4, true), true, BASE58Type.UPPER);

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Datenbank löschen");
		dialog.setHeaderText("Soll die Datenbank wirklich gelöscht werden?");
		dialog.setContentText("Zur Bestätigung gib folgenden Code ein:\t" + verificationCode);
		Stage dialogStage = (Stage)dialog.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(controller.getIcon());
		dialogStage.initOwner(controller.getStage());

		Optional<String> result = dialog.showAndWait();
		if(result.isPresent())
		{
			if(result.get().equals(verificationCode))
			{
				Stage modalStage = Helpers.showModal("Vorgang läuft", "Die Datenbank wird gelöscht, bitte warten...", controller.getStage(), controller.getIcon());

				Worker.runLater(() -> {
					try
					{
						ServerConnection connection = new ServerConnection(controller.getSettings());
						connection.deleteDatabase();
						Platform.runLater(() -> {
							if(modalStage != null)
							{
								modalStage.close();
								if(importPending)
								{
									importDatabase();
								}
							}
						});
					}
					catch(Exception e)
					{
						Logger.error(e);
						Platform.runLater(() -> {
							if(modalStage != null)
							{
								modalStage.close();
							}
							controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
						});
					}
				});
			}
			else
			{
				AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Die Eingabe stimmt nicht mit dem Bestätigungscode überein.", controller.getIcon(), controller.getStage(), null, false);
				deleteDB();
			}
		}
	}
}