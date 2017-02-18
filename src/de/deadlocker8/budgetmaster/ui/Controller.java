package de.deadlocker8.budgetmaster.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.joda.time.DateTime;

import de.deadlocker8.budgetmaster.logic.CategoryBudget;
import de.deadlocker8.budgetmaster.logic.CategoryHandler;
import de.deadlocker8.budgetmaster.logic.Payment;
import de.deadlocker8.budgetmaster.logic.ServerConnection;
import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.Utils;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import logger.LogLevel;
import logger.Logger;
import tools.AlertGenerator;

public class Controller implements Refreshable
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

	private HomeController homeController;
	private PaymentController paymentController;
	private CategoryController categoryController;
	private ChartController chartController;
	private SettingsController settingsController;

	private Stage stage;
	private Image icon = new Image("de/deadlocker8/budgetmaster/resources/icon.png");
	private final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);
	private Settings settings;
	private DateTime currentDate;
	private ArrayList<CategoryBudget> categoryBudgets;
	private ArrayList<Payment> payments;
	private CategoryHandler categoryHandler;

	private boolean alertIsShowing = false;

	public void init(Stage stage)
	{
		this.stage = stage;
		currentDate = DateTime.now();
		labelMonth.setText(currentDate.toString("MMMM yyyy"));

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
			homeController = fxmlLoader.getController();
			homeController.init(this);
			tabHome.setContent(nodeTabHome);

			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/PaymentTab.fxml"));
			Parent nodeTabPayment = (Parent)fxmlLoader.load();
			paymentController = fxmlLoader.getController();
			paymentController.init(this);
			tabPayments.setContent(nodeTabPayment);

			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/CategoryTab.fxml"));
			Parent nodeTabCategory = (Parent)fxmlLoader.load();
			categoryController = fxmlLoader.getController();
			categoryController.init(this);
			tabCategories.setContent(nodeTabCategory);

			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/ChartTab.fxml"));
			Parent nodeTabChart = (Parent)fxmlLoader.load();
			chartController = fxmlLoader.getController();
			chartController.init(this);
			tabCharts.setContent(nodeTabChart);

			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/SettingsTab.fxml"));
			Parent nodeTabSettings = (Parent)fxmlLoader.load();
			settingsController = fxmlLoader.getController();
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
		
		refresh();
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

		SequentialTransition seqT = new SequentialTransition(fadeIn, fadeOut);
		seqT.play();
		seqT.setOnFinished((a) -> {
			labelNotification.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16; -fx-font-weight: bold; -fx-background-color: transparent;");
		});
	}

	public void previousMonth()
	{
		currentDate = currentDate.minusMonths(1);
		labelMonth.setText(currentDate.toString("MMMM yyyy"));

		refresh();
	}

	public void nextMonth()
	{
		currentDate = currentDate.plusMonths(1);
		labelMonth.setText(currentDate.toString("MMMM yyyy"));

		refresh();
	}

	public DateTime getCurrentDate()
	{
		return currentDate;
	}

	public void showConnectionErrorAlert()
	{
		if(!alertIsShowing)
		{
			Platform.runLater(() -> {
				alertIsShowing = true;
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Fehler");
				alert.setHeaderText("");
				alert.setContentText("Beim Herstellen der Verbindung zum Server ist ein Fehler aufgetreten. Bitte überprüfe deine Einstellungen und ob der Server läuft.");
				Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
				dialogStage.getIcons().add(icon);
				dialogStage.initOwner(stage);
				dialogStage.setOnCloseRequest((event) -> {
					alertIsShowing = false;
				});
				alert.showAndWait();
			});
		}
	}

	private void refreshAllTabs()
	{
		homeController.refresh();
		paymentController.refresh();
		categoryController.refresh();
		chartController.refresh();
	}
	
	public ArrayList<CategoryBudget> getCategoryBudgets()
	{
		return categoryBudgets;
	}

	public ArrayList<Payment> getPayments()
	{
		return payments;
	}

	public CategoryHandler getCategoryHandler()
	{
		return categoryHandler;
	}

	public void about()
	{
		AlertGenerator.showAboutAlert(bundle.getString("app.name"), bundle.getString("version.name"), bundle.getString("version.code"), bundle.getString("version.date"), bundle.getString("author"), icon, stage, null, false);
	}

	@Override
	public void refresh()
	{
		try
		{
			ServerConnection connection = new ServerConnection(settings);
			categoryBudgets = connection.getCategoryBudgets(currentDate.getYear(), currentDate.getMonthOfYear());
			payments = connection.getPayments(currentDate.getYear(), currentDate.getMonthOfYear());
			categoryHandler = new CategoryHandler(connection.getCategories());
		}
		catch(Exception e)
		{
			categoryHandler = new CategoryHandler(null);			
			showConnectionErrorAlert();
		}

		refreshAllTabs();
	}
}