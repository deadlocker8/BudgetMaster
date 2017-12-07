package de.deadlocker8.budgetmasterclient.ui.controller.settings;

import java.io.IOException;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.ServerType;
import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.localserver.LocalServerException;
import de.deadlocker8.budgetmaster.logic.localserver.LocalServerHandler;
import de.deadlocker8.budgetmaster.logic.localserver.LocalServerStatus;
import de.deadlocker8.budgetmaster.logic.updater.Updater;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.FileHelper;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.LanguageType;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.RestartHandler;
import de.deadlocker8.budgetmasterclient.ui.cells.LanguageCell;
import de.deadlocker8.budgetmasterclient.ui.controller.Controller;
import de.deadlocker8.budgetmasterclient.utils.LoadingModal;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import logger.Logger;
import tools.AlertGenerator;
import tools.ConvertTo;
import tools.HashUtils;
import tools.Localization;
import tools.Worker;

public class LocalServerSettingsController extends SettingsController
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private ScrollPane scrollPane;
	@FXML private HBox hboxSettings;
	@FXML private ToggleButton toggleButtonOnline;
	@FXML private ToggleButton toggleButtonLocal;
	@FXML private Label labelStatus;
	@FXML private Label labelLocalServerStatus;
	@FXML private Button buttonLocalServerAction;
	@FXML private Label labelClientSecret;
	@FXML private TextField textFieldClientSecret;
	@FXML private TextField textFieldCurrency;
	@FXML private Label labelCurrency;
	@FXML private Button buttonSave;
	@FXML private Button buttonExportDB;
	@FXML private Button buttonImportDB;
	@FXML private Button buttonDeleteDB;
	@FXML private RadioButton radioButtonRestActivated;
	@FXML private RadioButton radioButtonRestDeactivated;
	@FXML private ComboBox<LanguageType> comboBoxLanguage;
	@FXML private CheckBox checkboxEnableAutoUpdate;
	@FXML private Button buttonSearchUpdates;
	@FXML private Label labelCurrentVersion;
	@FXML private Label labelLatestVersion;

	private LanguageType previousLanguage;
	
	private final int MILLIS_UNTIL_NEXT_RETRY = 2000;
	private final int MAX_NUMBER_OF_RETRIES = 5;

	@Override
	public void init(Controller controller)
	{
		super.controller = controller;

		ToggleGroup toggleGroupServerType = new ToggleGroup();
		toggleButtonOnline.setToggleGroup(toggleGroupServerType);
		toggleButtonLocal.setToggleGroup(toggleGroupServerType);
		toggleButtonOnline.setOnAction((event) -> {
			controller.getSettings().setServerType(ServerType.ONLINE);
			controller.loadSettingsTab();
		});

		textFieldClientSecret.setText("******");

		comboBoxLanguage.setCellFactory((view) -> {
			return new LanguageCell(true);
		});

		comboBoxLanguage.getItems().addAll(LanguageType.values());
		comboBoxLanguage.setButtonCell(new LanguageCell(false));
		comboBoxLanguage.setValue(LanguageType.ENGLISH);
		previousLanguage = LanguageType.ENGLISH;
		checkboxEnableAutoUpdate.setSelected(true);

		applyStyle();

		textFieldCurrency.setPromptText(Localization.getString(Strings.CURRENCY_PLACEHOLDER));

		ToggleGroup toggleGroup = new ToggleGroup();
		radioButtonRestActivated.setToggleGroup(toggleGroup);
		radioButtonRestDeactivated.setToggleGroup(toggleGroup);

		hboxSettings.prefWidthProperty().bind(scrollPane.widthProperty().subtract(25));

		refreshLabelsUpdate();
		
		prefill();
		checkServerStatus();
	}

	@Override
	public void prefill()
	{
		textFieldCurrency.setText(controller.getSettings().getCurrency());

		if(controller.getSettings().isRestActivated())
		{
			radioButtonRestActivated.setSelected(true);
		}
		else
		{
			radioButtonRestDeactivated.setSelected(true);
		}

		if(controller.getSettings().getLanguage() != null)
		{
			LanguageType language = controller.getSettings().getLanguage();
			comboBoxLanguage.setValue(language);
			previousLanguage = language;
		}

		checkboxEnableAutoUpdate.setSelected(controller.getSettings().isAutoUpdateCheckEnabled());
	}

	private void checkServerStatus()
	{
		LocalServerHandler serverHandler = new LocalServerHandler();
		switch(serverHandler.getServerStatus())
		{
			case ACTIVE:
				labelLocalServerStatus.setText(Localization.getString(Strings.LOCAL_SERVER_STATUS_OK));
				buttonLocalServerAction.setVisible(false);				
				refreshLabelsUpdate();
				save();
				break;
			case INACTIVE:
				Platform.runLater(()->{
					LoadingModal.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_LOCAL_SERVER), controller.getStage(), controller.getIcon());
				});
				labelLocalServerStatus.setText(Localization.getString(Strings.LOCAL_SERVER_STATUS_NOT_STARTED));
				buttonLocalServerAction.setVisible(false);
				Worker.runLater(() -> {
					try
					{
						Logger.debug("Starting local Server...");
						serverHandler.createServerSettingsIfNotExists();
						serverHandler.startServer();
						
						Logger.debug("Trying to connect to local server...");
						Platform.runLater(()->{LoadingModal.setMessage(Localization.getString(Strings.LOAD_LOCAL_SERVER_CONNECT));});
						int retryCount = 1;
						while(retryCount <= MAX_NUMBER_OF_RETRIES)
						{
							final int retries = retryCount;
							boolean isActive = serverHandler.getServerStatus().equals(LocalServerStatus.ACTIVE);
							if(isActive)
							{
								Logger.debug("Connected to local server");
								break;
							}
								
							if(retryCount == MAX_NUMBER_OF_RETRIES)
							{
								Logger.debug("Couldn't connect to local server. Giving up after " + retryCount + " retries.");
								throw new LocalServerException("");
							}
							else
							{
								Logger.debug("Couldn't connect to local server. Retry " + retryCount + "/" + MAX_NUMBER_OF_RETRIES + ". Next Retry in " + MILLIS_UNTIL_NEXT_RETRY/1000 + " Seconds...");							
								Platform.runLater(()->{LoadingModal.setMessage(Localization.getString(Strings.LOAD_LOCAL_SERVER_RETRY, retries, MAX_NUMBER_OF_RETRIES));});
								retryCount++;
								try
								{
									Thread.sleep(MILLIS_UNTIL_NEXT_RETRY);
								}
								catch(InterruptedException e)
								{
								}
							}							
						}
					}
					catch(IOException e)
					{
						Logger.debug("Error while starting local server");
						Logger.error(e);
						Platform.runLater(()->{
							LoadingModal.closeModal();
							AlertGenerator.showAlert(AlertType.ERROR, Localization.getString(Strings.TITLE_ERROR), "", Localization.getString(Strings.ERROR_LOCAL_SERVER_START, e.getMessage()), controller.getIcon(), controller.getStage(), null, false);
							controller.forceSettingsTab();
						});
						return;
					}
					catch(LocalServerException ex)
					{
						Logger.debug("Error while starting local server");
						Platform.runLater(()->{
							LoadingModal.closeModal();
							AlertGenerator.showAlert(AlertType.ERROR, Localization.getString(Strings.TITLE_ERROR), "", Localization.getString(Strings.ERROR_LOCAL_SERVER_START, ""), controller.getIcon(), controller.getStage(), null, false);
							controller.refresh(controller.getFilterSettings());
							buttonLocalServerAction.setText(Localization.getString(Strings.LOCAL_SERVER_ACTION_NOT_STARTED));
							buttonLocalServerAction.setVisible(true);
							buttonLocalServerAction.setDisable(false);

							buttonLocalServerAction.setOnAction((event) -> {
								buttonLocalServerAction.setDisable(true);
								checkServerStatus();
							});						
						});
						return;
					}
					
					Platform.runLater(()->{
						checkServerStatus();
						LoadingModal.closeModal();
					});
				});
				break;
			case MISSING:
				controller.forceSettingsTab();
				labelLocalServerStatus.setText(Localization.getString(Strings.LOCAL_SERVER_STATUS_NOT_PRESENT));
				buttonLocalServerAction.setText(Localization.getString(Strings.LOCAL_SERVER_ACTION_NOT_PRESENT));
				buttonLocalServerAction.setVisible(true);
				buttonLocalServerAction.setDisable(false);

				buttonLocalServerAction.setOnAction((event) -> {
					buttonLocalServerAction.setDisable(true);
					LoadingModal.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_DOWNLOAD_LOCAL_SERVER), controller.getStage(), controller.getIcon());

					Worker.runLater(() -> {
						try
						{							
							serverHandler.downloadServer(Localization.getString(Strings.VERSION_NAME));
							serverHandler.createServerSettingsIfNotExists();
							Platform.runLater(()->{
								checkServerStatus();
								LoadingModal.closeModal();
							});
						}
						catch(Exception e)
						{
							Logger.error(e);
							Platform.runLater(()->{
								LoadingModal.closeModal();
								AlertGenerator.showAlert(AlertType.ERROR, Localization.getString(Strings.TITLE_ERROR), "", Localization.getString(Strings.ERROR_LOCAL_SERVER_DOWNLOAD, e.getMessage()), controller.getIcon(), controller.getStage(), null, false);
								buttonLocalServerAction.setDisable(false);
							});
						}
					});
				});
				break;
			default:
				break;
		}
	}

	@Override
	void refreshLabelsUpdate()
	{
		Updater updater = controller.getUpdater();
		labelCurrentVersion.setText(Localization.getString(Strings.VERSION_NAME));
		labelLatestVersion.setText(updater.getLatestVersion().getVersionName());
	}

	@Override
	public void save()
	{
		String clientSecret = textFieldClientSecret.getText().trim();
		String currency = textFieldCurrency.getText().trim();

		if(clientSecret == null || clientSecret.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, Localization.getString(Strings.TITLE_WARNING), "", Localization.getString(Strings.WARNING_EMPTY_SECRET_CLIENT), controller.getIcon(), controller.getStage(), null, false);
			return;
		}

		if(currency == null || currency.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, Localization.getString(Strings.TITLE_WARNING), "", Localization.getString(Strings.WARNING_EMPTY_CURRENCY), controller.getIcon(), controller.getStage(), null, false);
			return;
		}

		if(controller.getSettings().isComplete())
		{
			if(!clientSecret.equals("******"))
			{
				controller.getSettings().setClientSecret(HashUtils.hash(clientSecret, Helpers.SALT));
			}

			controller.getSettings().setCurrency(currency);
			controller.getSettings().setRestActivated(radioButtonRestActivated.isSelected());
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

			settings.setCurrency(currency);
			settings.setRestActivated(radioButtonRestActivated.isSelected());
			settings.setLanguage(comboBoxLanguage.getValue());
			settings.setAutoUpdateCheckEnabled(checkboxEnableAutoUpdate.isSelected());
			controller.setSettings(settings);
		}

		controller.getSettings().setSecret(HashUtils.hash("BudgetMaster", Helpers.SALT));
		controller.getSettings().setUrl("https://localhost:9000");
		ArrayList<String> trustedHosts = new ArrayList<>();
		trustedHosts.add("localhost");
		controller.getSettings().setTrustedHosts(trustedHosts);

		try
		{
			FileHelper.saveSettings(controller.getSettings());
		}
		catch(IOException e)
		{
			Logger.error(e);
			AlertGenerator.showAlert(AlertType.ERROR, Localization.getString(Strings.TITLE_ERROR), "", Localization.getString(Strings.ERROR_SETTINGS_SAVE), controller.getIcon(), controller.getStage(), null, false);
		}

		textFieldClientSecret.setText("******");

		RestartHandler restartHandler = new RestartHandler(controller);
		restartHandler.handleRestart(previousLanguage);
		refreshLabelsUpdate();
	}
	
	public void handleIncompatibleServer()
	{
		labelLocalServerStatus.setText(Localization.getString(Strings.LOCAL_SERVER_STATUS_INCOMPATIBLE));
		buttonLocalServerAction.setText(Localization.getString(Strings.LOCAL_SERVER_ACTION_INCOMPATIBLE));
		buttonLocalServerAction.setVisible(true);
		buttonLocalServerAction.setDisable(false);

		buttonLocalServerAction.setOnAction((event) -> {
			buttonLocalServerAction.setDisable(true);
			LoadingModal.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_DOWNLOAD_LOCAL_SERVER), controller.getStage(), controller.getIcon());

			Worker.runLater(() -> {
				try
				{					
					LocalServerHandler serverHandler = new LocalServerHandler();
					serverHandler.shutdownServer();
					Thread.sleep(3000);
					serverHandler.downloadServer(Localization.getString(Strings.VERSION_NAME));
					serverHandler.createServerSettingsIfNotExists();
					Platform.runLater(()->{
						checkServerStatus();
						LoadingModal.closeModal();
					});
				}
				catch(Exception e)
				{
					Logger.error(e);
					Platform.runLater(()->{
						LoadingModal.closeModal();
						AlertGenerator.showAlert(AlertType.ERROR, Localization.getString(Strings.TITLE_ERROR), "", Localization.getString(Strings.ERROR_LOCAL_SERVER_DOWNLOAD, e.getMessage()), controller.getIcon(), controller.getStage(), null, false);
						buttonLocalServerAction.setDisable(false);
					});
				}
			});
		});
	}

	@Override
	public void applyStyle()
	{
		anchorPaneMain.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND));
		scrollPane.setStyle("-fx-background-color: transparent");
		labelClientSecret.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelStatus.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelCurrency.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		toggleButtonOnline.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14; -fx-background-radius: 3 0 0 3");
		toggleButtonLocal.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_DARK_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14; -fx-background-radius: 0 3 3 0; -fx-effect: innershadow(gaussian, rgba(0,0,0,0.7), 10,0,0,0);");
		buttonSave.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		buttonLocalServerAction.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonExportDB.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonImportDB.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonDeleteDB.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_RED) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonSearchUpdates.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
	}
}