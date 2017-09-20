package de.deadlocker8.budgetmaster.ui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.updater.Updater;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.FileHelper;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.LanguageType;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmaster.main.Main;
import de.deadlocker8.budgetmaster.ui.Styleable;
import de.deadlocker8.budgetmaster.ui.cells.LanguageCell;
import de.deadlocker8.budgetmasterserver.logic.database.Database;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logger.Logger;
import tools.AlertGenerator;
import tools.BASE58Type;
import tools.ConvertTo;
import tools.HashUtils;
import tools.Localization;
import tools.RandomCreations;
import tools.Worker;

public class SettingsController implements Styleable
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private ScrollPane scrollPane;
	@FXML private HBox hboxSettings;
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
	@FXML private ComboBox<LanguageType> comboBoxLanguage;
	@FXML private CheckBox checkboxEnableAutoUpdate;
	@FXML private Button buttonSearchUpdates;
	@FXML private Label labelCurrentVersion;
	@FXML private Label labelLatestVersion;

	private Controller controller;
	private LanguageType previousLanguage;

	public void init(Controller controller)
	{
		this.controller = controller;
		
		textFieldClientSecret.setText("******");		
		
		comboBoxLanguage.setCellFactory((view) -> {
			return new LanguageCell(true);
		});		
		
		comboBoxLanguage.getItems().addAll(LanguageType.values());		
		comboBoxLanguage.setButtonCell(new LanguageCell(false));
		comboBoxLanguage.setValue(LanguageType.ENGLISH);
		previousLanguage = LanguageType.ENGLISH;
		checkboxEnableAutoUpdate.setSelected(true);
		
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
			
			if(controller.getSettings().getLanguage() != null)
			{
				LanguageType language = controller.getSettings().getLanguage();
				comboBoxLanguage.setValue(language);
				previousLanguage = language;
			}
			
			checkboxEnableAutoUpdate.setSelected(controller.getSettings().isAutoUpdateCheckEnabled());
		}
		else
		{
			radioButtonRestDeactivated.setSelected(true);
		}
		
		applyStyle();

		textFieldURL.setPromptText(Localization.getString(Strings.URL_PLACEHOLDER));
		textFieldCurrency.setPromptText(Localization.getString(Strings.CURRENCY_PLACEHOLDER));
		textAreaTrustedHosts.setPromptText(Localization.getString(Strings.TRUSTED_HOSTS_PLACEHOLDER));

		ToggleGroup toggleGroup = new ToggleGroup();
		radioButtonRestActivated.setToggleGroup(toggleGroup);
		radioButtonRestDeactivated.setToggleGroup(toggleGroup);
		
		hboxSettings.prefWidthProperty().bind(scrollPane.widthProperty().subtract(25));
		
		refreshLabelsUpdate();
	}
	
	private void refreshLabelsUpdate()
	{
		Updater updater = controller.getUpdater();
		labelCurrentVersion.setText(Localization.getString(Strings.VERSION_NAME));
		labelLatestVersion.setText(updater.getLatestVersion().getVersionName());
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
			AlertGenerator.showAlert(AlertType.WARNING, 
									Localization.getString(Strings.TITLE_WARNING), 
									"",
									Localization.getString(Strings.WARNING_EMPTY_SECRET_CLIENT),
									controller.getIcon(), 
									controller.getStage(), 
									null, 
									false);
			return;
		}
		
		if(url == null || url.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, 
									Localization.getString(Strings.TITLE_WARNING), 
									"", 
									Localization.getString(Strings.WARNING_EMPTY_URL),
									controller.getIcon(), 
									controller.getStage(),
									null, 
									false);
			return;
		}

		if(secret == null || secret.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, 
									Localization.getString(Strings.TITLE_WARNING), 
									"", 
									Localization.getString(Strings.WARNING_EMPTY_SECRET_SERVER), 
									controller.getIcon(), 
									controller.getStage(), 
									null, 
									false);
			return;
		}

		if(currency == null || currency.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, 
									Localization.getString(Strings.TITLE_WARNING), 
									"", 
									Localization.getString(Strings.WARNING_EMPTY_CURRENCY),
									controller.getIcon(), 
									controller.getStage(), 
									null, 
									false);
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
			controller.getSettings().setLanguage(comboBoxLanguage.getValue());
			controller.getSettings().setAutoUpdateCheckEnabled(checkboxEnableAutoUpdate.isSelected());
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
			settings.setLanguage(comboBoxLanguage.getValue());
			settings.setAutoUpdateCheckEnabled(checkboxEnableAutoUpdate.isSelected());
			controller.setSettings(settings);
		}

		try
		{
			FileHelper.saveSettings(controller.getSettings());
		}
		catch(IOException e)
		{
			Logger.error(e);
			AlertGenerator.showAlert(AlertType.ERROR, 
									Localization.getString(Strings.TITLE_ERROR), 
									"", 
									Localization.getString(Strings.ERROR_SETTINGS_SAVE),
									controller.getIcon(), 
									controller.getStage(), 
									null, 
									false);
		}

		textFieldClientSecret.setText("******");
		textFieldSecret.setText("******");

		controller.refresh(controller.getFilterSettings());
		controller.showNotification(Localization.getString(Strings.NOTIFICATION_SETTINGS_SAVE));
		
		if(controller.getSettings().isAutoUpdateCheckEnabled())
		{
			controller.checkForUpdates(false);			
			refreshLabelsUpdate();
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
				
			    Image icon = new Image("/de/deadlocker8/budgetmaster/resources/icon.png");
				new SplashScreenController(Main.primaryStage, icon, false);			
			}
			else
			{
				alert.close();
			}
		}
	}

	public void exportDB()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Localization.getString(Strings.TITLE_DATABASE_EXPORT));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON (*.json)", "*.json");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(controller.getStage());
		if(file != null)
		{
			Stage modalStage = Helpers.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_DATABASE_EXPORT), controller.getStage(), controller.getIcon());

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
						AlertGenerator.showAlert(AlertType.INFORMATION, 
												Localization.getString(Strings.INFO_TITLE_DATABASE_EXPORT), 
												"", 
												Localization.getString(Strings.INFO_TEXT_DATABASE_EXPORT), 
												controller.getIcon(), 
												controller.getStage(), 
												null, 
												false);
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
		fileChooser.setTitle(Localization.getString(Strings.TITLE_DATABASE_IMPORT));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON (*.json)", "*.json");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(controller.getStage());
		if(file != null)
		{
			Database database;
			try
			{
				database = FileHelper.loadDatabaseJSON(file);
				if(database.getCategories() == null 
						|| database.getNormalPayments() == null 
						|| database.getRepeatingPayments() == null
						|| database.getTags() == null
						|| database.getTagMatches() == null)
				{
					AlertGenerator.showAlert(AlertType.ERROR, 
											Localization.getString(Strings.TITLE_ERROR), 
											"", 
											Localization.getString(Strings.ERROR_DATABASE_IMPORT_WRONG_FILE), 
											controller.getIcon(), 
											controller.getStage(), 
											null, 
											false);
					return;
				}
			}
			catch(IOException e1)
			{
				Logger.error(e1);
				AlertGenerator.showAlert(AlertType.ERROR, 
										Localization.getString(Strings.TITLE_ERROR), 
										"", 
										Localization.getString(Strings.ERROR_DATABASE_IMPORT), 
										controller.getIcon(), 
										controller.getStage(), 
										null, 
										false);
				return;
			}

			Stage modalStage = Helpers.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_DATABASE_IMPORT), controller.getStage(), controller.getIcon());

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
						
						AlertGenerator.showAlert(AlertType.INFORMATION, 
												Localization.getString(Strings.INFO_TITLE_DATABASE_IMPORT), 
												"", 
												Localization.getString(Strings.INFO_TEXT_DATABASE_IMPORT), 
												controller.getIcon(), 
												controller.getStage(), 
												null, 
												false);
						
						controller.refresh(controller.getFilterSettings());
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
			controller.refresh(controller.getFilterSettings());
		}
	}

	public void importDB()
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(Localization.getString(Strings.INFO_TITLE_DATABASE_IMPORT_DIALOG));
		alert.setHeaderText("");		
		alert.setContentText(Localization.getString(Strings.INFO_TEXT_DATABASE_IMPORT_DIALOG));
		Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(controller.getIcon());
		dialogStage.initOwner(controller.getStage());

		ButtonType buttonTypeDelete = new ButtonType(Localization.getString(Strings.INFO_TEXT_DATABASE_IMPORT_DIALOG_DELETE));
		ButtonType buttonTypeAppend = new ButtonType(Localization.getString(Strings.INFO_TEXT_DATABASE_IMPORT_DIALOG_APPEND));
		ButtonType buttonTypeCancel = new ButtonType(Localization.getString(Strings.CANCEL), ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonTypeDelete, buttonTypeAppend, buttonTypeCancel);
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getButtonTypes().stream().map(dialogPane::lookupButton).forEach(button -> button.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
			if(KeyCode.ENTER.equals(event.getCode()) && event.getTarget() instanceof Button)
			{
				((Button)event.getTarget()).fire();
			}
		}));
		
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
		dialog.setTitle(Localization.getString(Strings.INFO_TITLE_DATABASE_DELETE));
		dialog.setHeaderText(Localization.getString(Strings.INFO_HEADER_TEXT_DATABASE_DELETE));
		dialog.setContentText(Localization.getString(Strings.INFO_TEXT_DATABASE_DELETE, verificationCode));
		Stage dialogStage = (Stage)dialog.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(controller.getIcon());
		dialogStage.initOwner(controller.getStage());

		Optional<String> result = dialog.showAndWait();
		if(result.isPresent())
		{
			if(result.get().equals(verificationCode))
			{
				Stage modalStage = Helpers.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_DATABASE_DELETE), controller.getStage(), controller.getIcon());

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
								else
								{
									controller.refresh(controller.getFilterSettings());
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
				AlertGenerator.showAlert(AlertType.WARNING, 
										Localization.getString(Strings.TITLE_WARNING), 
										"", 
										Localization.getString(Strings.WARNING_WRONG_VERIFICATION_CODE), 
										controller.getIcon(), 
										controller.getStage(), 
										null, 
										false);
				deleteDatabase(importPending);
			}
		}
	}
	
	public void checkForUpdates()
	{
		controller.checkForUpdates(true);		
		refreshLabelsUpdate();
	}

	@Override
	public void applyStyle()
	{
		anchorPaneMain.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND));
		scrollPane.setStyle("-fx-background-color: transparent");
		labelClientSecret.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelSecret.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelURL.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelCurrency.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		buttonSave.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		buttonExportDB.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonImportDB.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonDeleteDB.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_RED) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonSearchUpdates.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
	}
}