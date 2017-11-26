package de.deadlocker8.budgetmasterclient.ui.controller;

import java.io.IOException;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.ServerType;
import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.updater.Updater;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.FileHelper;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.LanguageType;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.RestartHandler;
import de.deadlocker8.budgetmasterclient.ui.cells.LanguageCell;
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

	@Override
	public void init(Controller controller)
	{
		super.controller = controller;
		
		ToggleGroup toggleGroupServerType = new ToggleGroup();
		toggleButtonOnline.setToggleGroup(toggleGroupServerType);
		toggleButtonLocal.setToggleGroup(toggleGroupServerType);
		toggleButtonOnline.setOnAction((event)->{
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
		
		prefill();
		
		applyStyle();

		textFieldCurrency.setPromptText(Localization.getString(Strings.CURRENCY_PLACEHOLDER));

		ToggleGroup toggleGroup = new ToggleGroup();
		radioButtonRestActivated.setToggleGroup(toggleGroup);
		radioButtonRestDeactivated.setToggleGroup(toggleGroup);
		
		hboxSettings.prefWidthProperty().bind(scrollPane.widthProperty().subtract(25));
		
		refreshLabelsUpdate();
	}
	
	public void prefill()
	{
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

		//TODO
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
		controller.getSettings().setUrl("localhost:9000");
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

		RestartHandler restartHandler = new RestartHandler(controller);
		restartHandler.handleRestart(previousLanguage);
		refreshLabelsUpdate();
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
		buttonExportDB.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonImportDB.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonDeleteDB.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_RED) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonSearchUpdates.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
	}
}