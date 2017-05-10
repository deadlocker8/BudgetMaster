package de.deadlocker8.budgetmaster.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import de.deadlocker8.budgetmaster.logic.Helpers;
import de.deadlocker8.budgetmaster.logic.ServerConnection;
import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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
	@FXML private TextField textFieldURL;
	@FXML private Label labelURL;
	@FXML private TextField textFieldSecret;
	@FXML private Label labelSecret;
	@FXML private TextField textFieldCurrency;
	@FXML private Label labelCurrency;
	@FXML private Button buttonSave;
	@FXML private Button buttonBackupDB;
	@FXML private Button buttonDeleteDB;
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

		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");
		labelSecret.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		labelURL.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		labelCurrency.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		buttonSave.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		buttonBackupDB.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		buttonDeleteDB.setStyle("-fx-background-color: #FF5047; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
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
		String url = textFieldURL.getText().trim();
		String secret = textFieldSecret.getText().trim();
		String currency = textFieldCurrency.getText().trim();
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

		if(controller.getSettings() != null)
		{
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
			settings.setUrl(url);
			settings.setSecret(HashUtils.hash(secret, Helpers.SALT));
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

		textFieldSecret.setText("******");

		controller.refresh(controller.getFilterSettings());
		controller.showNotification("Erfolgreich gespeichert");
	}

	public void backupDB()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Datenbank exportieren");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON (*.json)", "*.json");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(controller.getStage());
		if(file != null)
		{
			Stage modalStage = showModal("Vorgang läuft", "Die Datenbank wird exportiert, bitte warten...");

			Worker.runLater(() -> {
				try
				{
					ServerConnection connection = new ServerConnection(controller.getSettings());				
					String databaseJSON = connection.exportDatabase();
					Utils.saveDatabaseJSON(file, databaseJSON);
					
					Platform.runLater(() -> {
						if(modalStage != null)
						{
							modalStage.close();
						}
						AlertGenerator.showAlert(AlertType.INFORMATION, "Erfolgreich exportiert", "", "Die Datenbank wurder erfolgreich exportiert.", controller.getIcon(), controller.getStage(), null, false);
					});
				}
				catch(Exception e)
				{
					Logger.error(e);
					Platform.runLater(() -> {
						controller.showConnectionErrorAlert(e.getMessage());
					});
				}
			});
		}
	}

	public void deleteDB()
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
				Stage modalStage = showModal("Vorgang läuft", "Die Datenbank wird gelöscht, bitte warten...");

				Worker.runLater(() -> {
					try
					{
						ServerConnection connection = new ServerConnection(controller.getSettings());
						connection.deleteDatabase();
						Platform.runLater(() -> {
							if(modalStage != null)
							{
								modalStage.close();
							}
						});
					}
					catch(Exception e)
					{
						Logger.error(e);
						Platform.runLater(() -> {
							controller.showConnectionErrorAlert(e.getMessage());
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

	private Stage showModal(String title, String message)
	{
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/Modal.fxml"));
			Parent root = (Parent)fxmlLoader.load();
			Stage newStage = new Stage();
			newStage.initOwner(controller.getStage());
			newStage.initModality(Modality.APPLICATION_MODAL);
			newStage.setTitle(title);
			newStage.setScene(new Scene(root));
			newStage.getIcons().add(controller.getIcon());
			newStage.setResizable(false);
			ModalController newController = fxmlLoader.getController();
			newController.init(newStage, message);
			newStage.show();

			return newStage;
		}
		catch(IOException e)
		{
			Logger.error(e);
			return null;
		}
	}
}