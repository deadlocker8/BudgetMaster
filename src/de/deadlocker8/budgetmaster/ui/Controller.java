package de.deadlocker8.budgetmaster.ui;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.Utils;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import logger.LogLevel;
import logger.Logger;
import tools.AlertGenerator;

public class Controller
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Label labelMonth;
	@FXML private Button buttonLeft;
	@FXML private Button buttonRight;
	@FXML private TabPane tabPane;
	@FXML private Tab tabHome;
	@FXML private Tab tabPayments;
	@FXML private Tab tabCategories;
	@FXML private Tab tabCharts;
	@FXML private Tab tabSettings;
	@FXML private Label labelNotification;

	private Stage stage;
	private Image icon = new Image("de/deadlocker8/budgetmaster/resources/icon.png");
	private final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);
	private Settings settings;

	public void init(Stage stage)
	{
		this.stage = stage;

		settings = Utils.loadSettings();

		if(settings == null)
		{
			// TODO dont't load other tabs!
			Platform.runLater(() -> {
				AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Bitte gibt zuerst deine Serverdaten ein!", icon, stage, null, false);
				tabPane.getSelectionModel().select(tabSettings);
			});
		}

		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/HomeTab.fxml"));
			Parent nodeTabHome = (Parent)fxmlLoader.load();
			HomeController newController = fxmlLoader.getController();
			newController.init(this);
			tabHome.setContent(nodeTabHome);

			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/PaymentTab.fxml"));
			Parent nodeTabPayment = (Parent)fxmlLoader.load();
			PaymentController paymentController = fxmlLoader.getController();
			paymentController.init(this);
			tabPayments.setContent(nodeTabPayment);

			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/CategoryTab.fxml"));
			Parent nodeTabCategory = (Parent)fxmlLoader.load();
			CategoryController categoryController = fxmlLoader.getController();
			categoryController.init(this);
			tabCategories.setContent(nodeTabCategory);

			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/ChartTab.fxml"));
			Parent nodeTabChart = (Parent)fxmlLoader.load();
			ChartController chartController = fxmlLoader.getController();
			chartController.init(this);
			tabCharts.setContent(nodeTabChart);

			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/SettingsTab.fxml"));
			Parent nodeTabSettings = (Parent)fxmlLoader.load();
			SettingsController settingsController = fxmlLoader.getController();
			settingsController.init(this);
			tabSettings.setContent(nodeTabSettings);
		}
		catch(IOException e)
		{
			// ERRORHANDLING
			Logger.log(LogLevel.ERROR, Logger.exceptionToString(e));
		}

		FontIcon iconPrevious = new FontIcon(FontIconType.CHEVRON_LEFT);
		iconPrevious.setSize(20);
		buttonLeft.setGraphic(iconPrevious);
		FontIcon iconNext = new FontIcon(FontIconType.CHEVRON_RIGHT);
		iconNext.setSize(20);
		buttonRight.setGraphic(iconNext);

		// apply theme
		anchorPaneMain.setStyle("-fx-background-color: #DDDDDD");
		labelMonth.setStyle("-fx-text-fill: " + bundle.getString("color.text"));
		labelNotification.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16; -fx-font-weight: bold; -fx-background-color: transparent;");
		buttonLeft.setStyle("-fx-background-color: transparent;");
		buttonRight.setStyle("-fx-background-color: transparent;");
	}

	public Stage getStage()
	{
		return stage;
	}

	public Image getIcon()
	{
		return icon;
	}

	public ResourceBundle getBundle()
	{
		return bundle;
	}

	public Settings getSettings()
	{
		return settings;
	}

	public void setSettings(Settings settings)
	{
		this.settings = settings;
	}

	public void showNotification(String text)
	{
		labelNotification.setText(text);
		labelNotification.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16; -fx-font-weight: bold; -fx-background-color: #323232;");
		FadeTransition fadeIn = new FadeTransition(Duration.millis(200), labelNotification);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		
		FadeTransition fadeOut = new FadeTransition(Duration.millis(400), labelNotification);
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		fadeOut.setDelay(Duration.millis(3000));
		fadeOut.play();
		
		SequentialTransition seqT = new SequentialTransition (fadeIn, fadeOut);
	    seqT.play();
	    seqT.setOnFinished((a)->{
	    	labelNotification.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16; -fx-font-weight: bold; -fx-background-color: transparent;");	    	
	    });
	}

	public void about()
	{
		AlertGenerator.showAboutAlert(bundle.getString("app.name"), bundle.getString("version.name"), bundle.getString("version.code"), bundle.getString("version.date"), bundle.getString("author"), icon, stage, null, false);
	}
}