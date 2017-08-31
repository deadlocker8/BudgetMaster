package de.deadlocker8.budgetmaster.ui.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.joda.time.DateTime;

import de.deadlocker8.budgetmaster.logic.CategoryBudget;
import de.deadlocker8.budgetmaster.logic.CategoryHandler;
import de.deadlocker8.budgetmaster.logic.FilterSettings;
import de.deadlocker8.budgetmaster.logic.NormalPayment;
import de.deadlocker8.budgetmaster.logic.PaymentHandler;
import de.deadlocker8.budgetmaster.logic.Settings;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.updater.Updater;
import de.deadlocker8.budgetmaster.logic.updater.VersionInformation;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import logger.Logger;
import tools.AlertGenerator;
import tools.ConvertTo;
import tools.Localization;
import tools.Worker;

public class Controller
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Label labelMonth;
	@FXML private Button buttonLeft;
	@FXML private Button buttonRight;
	@FXML private Button buttonToday;
	@FXML private Button buttonAbout;
	@FXML private TabPane tabPane;
	@FXML private Tab tabHome;
	@FXML private Tab tabPayments;
	@FXML private Tab tabCategories;
	@FXML private Tab tabCharts;
	@FXML private Tab tabReports;
	@FXML private Tab tabSettings;
	@FXML private Label labelNotification;

	private HomeController homeController;
	private PaymentController paymentController;
	private CategoryController categoryController;
	private ChartController chartController;
	private ReportController reportController;
	private SettingsController settingsController;

	private Stage stage;
	private Image icon = new Image("de/deadlocker8/budgetmaster/resources/icon.png");	
	private Settings settings;
	private DateTime currentDate;
	private ArrayList<CategoryBudget> categoryBudgets;
	private PaymentHandler paymentHandler;
	private CategoryHandler categoryHandler;
	private FilterSettings filterSettings;
	private Updater updater;

	private boolean alertIsShowing = false;

	public void init(Stage stage, Settings settings)
	{
		this.stage = stage;
		this.settings = settings;
		
		stage.setOnCloseRequest((event)->{
			Worker.shutdown();
			System.exit(0);
		});
		
		currentDate = DateTime.now();
		labelMonth.setText(currentDate.toString("MMMM yyyy"));
		
		filterSettings = new FilterSettings();
		paymentHandler = new PaymentHandler();
		updater = new Updater();
		
		if(settings.isAutoUpdateCheckEnabled())
		{			
			checkForUpdates();
		}
				
		initUI();		
	}
	
	private void initUI()
	{
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/fxml/HomeTab.fxml"));
			fxmlLoader.setResources(Localization.getBundle());
			Parent nodeTabHome = (Parent)fxmlLoader.load();
			homeController = fxmlLoader.getController();
			homeController.init(this);
			tabHome.setContent(nodeTabHome);

			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/fxml/PaymentTab.fxml"));
			fxmlLoader.setResources(Localization.getBundle());
			Parent nodeTabPayment = (Parent)fxmlLoader.load();
			paymentController = fxmlLoader.getController();
			paymentController.init(this);
			tabPayments.setContent(nodeTabPayment);

			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/fxml/CategoryTab.fxml"));
			fxmlLoader.setResources(Localization.getBundle());
			Parent nodeTabCategory = (Parent)fxmlLoader.load();
			categoryController = fxmlLoader.getController();
			categoryController.init(this);
			tabCategories.setContent(nodeTabCategory);

			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/fxml/ChartTab.fxml"));
			fxmlLoader.setResources(Localization.getBundle());
			Parent nodeTabChart = (Parent)fxmlLoader.load();
			chartController = fxmlLoader.getController();
			chartController.init(this);
			tabCharts.setContent(nodeTabChart);
			tabCharts.selectedProperty().addListener((a,b,c)->{
				if(c)
				{
					chartController.refresh();
				}
			});
			
			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/fxml/ReportTab.fxml"));
			fxmlLoader.setResources(Localization.getBundle());
			Parent nodeTabReport = (Parent)fxmlLoader.load();
			reportController = fxmlLoader.getController();
			reportController.init(this);
			tabReports.setContent(nodeTabReport);

			fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/fxml/SettingsTab.fxml"));
			fxmlLoader.setResources(Localization.getBundle());
			Parent nodeTabSettings = (Parent)fxmlLoader.load();
			settingsController = fxmlLoader.getController();
			settingsController.init(this);
			tabSettings.setContent(nodeTabSettings);
		}
		catch(IOException e)
		{
			Logger.error(e);
			Platform.runLater(() -> {
				AlertGenerator.showAlert(AlertType.ERROR, Localization.getString(Strings.TITLE_ERROR), "", Localization.getString(Strings.ERROR_CREATE_UI), icon, stage, null, false);
			});			
		}
		
		buttonLeft.setGraphic(Helpers.getFontIcon(FontIconType.CHEVRON_LEFT, 20, Colors.TEXT));		
		buttonRight.setGraphic(Helpers.getFontIcon(FontIconType.CHEVRON_RIGHT, 20, Colors.TEXT));		
		buttonToday.setGraphic(Helpers.getFontIcon(FontIconType.CALENDAR_ALT, 20, Colors.TEXT));		
		buttonAbout.setGraphic(Helpers.getFontIcon(FontIconType.INFO, 20, Colors.TEXT));

		// apply theme
		anchorPaneMain.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_MAIN));
		labelMonth.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelNotification.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16; -fx-font-weight: bold; -fx-background-color: transparent;");
		buttonLeft.setStyle("-fx-background-color: transparent;");
		buttonRight.setStyle("-fx-background-color: transparent;");
		buttonToday.setStyle("-fx-background-color: transparent;");
		buttonAbout.setStyle("-fx-background-color: transparent;");
		
		if(!settings.isComplete())
		{			
			Platform.runLater(() -> {
				toggleAllTabsExceptSettings(true);
				tabPane.getSelectionModel().select(tabSettings);
				AlertGenerator.showAlert(AlertType.INFORMATION, Localization.getString(Strings.TITLE_INFO), "", Localization.getString(Strings.INFO_FIRST_START), icon, stage, null, false);
			});
		}
		else
		{
			refresh(filterSettings);
		}
	}

	public Stage getStage()
	{
		return stage;
	}

	public Image getIcon()
	{
		return icon;
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
		labelNotification.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16; -fx-font-weight: bold; -fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_NOTIFICATION));
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

		refresh(filterSettings);
	}

	public void nextMonth()
	{
		currentDate = currentDate.plusMonths(1);
		labelMonth.setText(currentDate.toString("MMMM yyyy"));

		refresh(filterSettings);
	}
	
	public void today()
	{
		currentDate = DateTime.now();
		labelMonth.setText(currentDate.toString("MMMM yyyy"));

		refresh(filterSettings);
	}

	public DateTime getCurrentDate()
	{
		return currentDate;
	}

	public void showConnectionErrorAlert(String errorMessage)
	{		
		if(!alertIsShowing)
		{
			alertIsShowing = true;
			Platform.runLater(() -> {
				toggleAllTabsExceptSettings(true);
				tabPane.getSelectionModel().select(tabSettings);	
				
				alertIsShowing = true;
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle(Localization.getString(Strings.TITLE_ERROR));
				alert.setHeaderText("");
				if(errorMessage == null)
				{
					alert.setContentText(Localization.getString(Strings.ERROR_SERVER_CONNECTION));
				}
				else
				{
					alert.setContentText(Localization.getString(Strings.ERROR_SERVER_CONNECTION_WITH_DETAILS, errorMessage));
				}
				
				Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
				dialogStage.getIcons().add(icon);
				dialogStage.initOwner(stage);
				alert.showAndWait();
				alertIsShowing = false;
			});
		}
	}

	public void refreshAllTabs()
	{
		homeController.refresh();
		paymentController.refresh();
		categoryController.refresh();
		reportController.refresh();
		if(tabCharts.isSelected())
		{
			chartController.refresh();
		}
	}
	
	public ArrayList<CategoryBudget> getCategoryBudgets()
	{
		return categoryBudgets;
	}

	public PaymentHandler getPaymentHandler()
	{
		return paymentHandler;
	}

	public CategoryHandler getCategoryHandler()
	{
		return categoryHandler;
	}

	public FilterSettings getFilterSettings()
	{
		return filterSettings;
	}
	
	public Updater getUpdater()
	{
		return updater;
	}

	public void setFilterSettings(FilterSettings filterSettings)
	{
		this.filterSettings = filterSettings;
	}
	
	public void toggleAllTabsExceptSettings(boolean disable)
	{
		tabHome.setDisable(disable);
		tabPayments.setDisable(disable);
		tabCategories.setDisable(disable);
		tabCharts.setDisable(disable);
		tabReports.setDisable(disable);
		buttonLeft.setDisable(disable);
		buttonRight.setDisable(disable);
		buttonToday.setDisable(disable);
	}
	
	public void checkForUpdates()
	{
		try
		{
			boolean updateAvailable = updater.isUpdateAvailable(Integer.parseInt(Localization.getString(Strings.VERSION_CODE)));
			String changes = updater.getChangelog(updater.getLatestVersion().getVersionCode());

			if(!updateAvailable)
			{
				showNotification(Localization.getString(Strings.NOTIFICATION_NO_UPDATE_AVAILABLE));
				return;
			}
			
			Platform.runLater(()->{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(Localization.getString(Strings.INFO_TITLE_UPDATE_AVAILABLE));
				alert.setHeaderText("");
				alert.setContentText(Localization.getString(Strings.INFO_TEXT_UPDATE_AVAILABLE,
															updater.getLatestVersion().getVersionName(),
															changes));			
				Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
				dialogStage.getIcons().add(icon);					
				
				ButtonType buttonTypeOne = new ButtonType(Localization.getString(Strings.INFO_TEXT_UPDATE_AVAILABLE_NOW));
				ButtonType buttonTypeTwo = new ButtonType(Localization.getString(Strings.CANCEL));							
				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
				
				Optional<ButtonType> result = alert.showAndWait();						
				if (result.get() == buttonTypeOne)
				{					
					Stage modalStage = Helpers.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_UPDATE), stage, icon);
					
					Worker.runLater(() -> {
						try 
						{
							updater.downloadLatestVersion();
							Platform.runLater(() -> {
								if(modalStage != null)
								{
									modalStage.close();
								}							
							});
						}
						catch(Exception ex)
						{
							Logger.error(ex);
							Platform.runLater(() -> {
								if(modalStage != null)
								{
									modalStage.close();
									AlertGenerator.showAlert(AlertType.ERROR, 
															Localization.getString(Strings.TITLE_ERROR),
															"", 
															Localization.getString(Strings.ERROR_UPDATER_DOWNLOAD_LATEST_VERSION, ex.getMessage()), 
															icon, null, null, true);
								}							
							});
						}
					});
				}
				else
				{
					alert.close();
				}
			});
		}		
		catch(Exception e)
		{
			Logger.error(e);
			AlertGenerator.showAlert(AlertType.ERROR, 
									Localization.getString(Strings.TITLE_ERROR),
									"", 
									Localization.getString(Strings.ERROR_UPDATER_GET_LATEST_VERSION), 
									icon, null, null, true);
		}
	}

	public void about()
	{
		ArrayList<String> creditLines = new ArrayList<>();
		creditLines.add(Localization.getString(Strings.CREDITS));
				
		AlertGenerator.showAboutAlertWithCredits(Localization.getString(Strings.APP_NAME),
												Localization.getString(Strings.VERSION_NAME),
												Localization.getString(Strings.VERSION_CODE),
												Localization.getString(Strings.VERSION_DATE),
												Localization.getString(Strings.AUTHOR),
												creditLines,
												icon, 
												stage, 
												null, 
												false);
	}	
	
	public void refresh(FilterSettings newFilterSettings)
	{
		Stage modalStage = Helpers.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_DATA), stage, icon);

		Worker.runLater(() -> {
			try
			{
				ServerConnection connection = new ServerConnection(settings);
				
				//check if server is compatible with client
				try
				{
					VersionInformation serverVersion = connection.getServerVersion();
					if(serverVersion.getVersionCode() < Integer.parseInt(Localization.getString(Strings.VERSION_CODE)))
					{
						Platform.runLater(()->{
							AlertGenerator.showAlert(AlertType.WARNING,
													Localization.getString(Strings.TITLE_WARNING), 
													"",
													Localization.getString(Strings.WARNING_SERVER_VERSION, serverVersion.getVersionName(), Localization.getString(Strings.VERSION_NAME)), 
													icon, stage, null, false);				
						
							if(modalStage != null)
							{
								modalStage.close();
							};
							categoryHandler = new CategoryHandler(null);					
							toggleAllTabsExceptSettings(true);
							tabPane.getSelectionModel().select(tabSettings);	
						});
						return;
					}
				}
				catch(Exception e1)
				{
					Platform.runLater(()->{
						AlertGenerator.showAlert(AlertType.WARNING,
						Localization.getString(Strings.TITLE_WARNING), 
						"",
						Localization.getString(Strings.WARNING_SERVER_VERSION, Localization.getString(Strings.UNDEFINED), Localization.getString(Strings.VERSION_NAME)), 
						icon, stage, null, false);				
	
						if(modalStage != null)
						{
							modalStage.close();
						};
						categoryHandler = new CategoryHandler(null);					
						toggleAllTabsExceptSettings(true);
						tabPane.getSelectionModel().select(tabSettings);
					});
					return;
				}
				
				
				paymentHandler = new PaymentHandler();
				paymentHandler.getPayments().addAll(connection.getPayments(currentDate.getYear(), currentDate.getMonthOfYear()));
				paymentHandler.getPayments().addAll(connection.getRepeatingPayments(currentDate.getYear(), currentDate.getMonthOfYear()));			
				paymentHandler.sort();
				if(settings.isRestActivated())
				{
					int rest = connection.getRestForAllPreviousMonths(currentDate.getYear(), currentDate.getMonthOfYear());
					//categoryID 2 = Rest
					paymentHandler.getPayments().add(new NormalPayment(-1, rest, currentDate.withDayOfMonth(1).toString("yyyy-MM-dd"), 2, Localization.getString(Strings.CATEGORY_REST), ""));				
				}
				
				categoryHandler = new CategoryHandler(connection.getCategories());
				
				categoryBudgets = connection.getCategoryBudgets(currentDate.getYear(), currentDate.getMonthOfYear());	
				paymentHandler.filter(newFilterSettings);				

				Platform.runLater(() -> {
					if(modalStage != null)
					{
						modalStage.close();
					}
					toggleAllTabsExceptSettings(false);
					refreshAllTabs();
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
					Logger.error(e);
					categoryHandler = new CategoryHandler(null);	
					showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
					refreshAllTabs();
				});
			}
		});	
	}
}