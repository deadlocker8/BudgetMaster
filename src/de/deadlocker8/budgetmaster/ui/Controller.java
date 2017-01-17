package de.deadlocker8.budgetmaster.ui;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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

	private Stage stage;
	private Image icon = new Image("de/deadlocker8/budgetmaster/resources/icon.png");
	private final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);

	//TODO tab blue selection border is too small
	
	public void init(Stage stage)
	{
		this.stage = stage;
		
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
		}
		catch(IOException e)
		{
			//ERRORHANDLING
			Logger.log(LogLevel.ERROR, Logger.exceptionToString(e));
		}
		
		FontIcon iconPrevious = new FontIcon(FontIconType.CHEVRON_LEFT);
		iconPrevious.setSize(20);
		buttonLeft.setGraphic(iconPrevious);
		FontIcon iconNext = new FontIcon(FontIconType.CHEVRON_RIGHT);
		iconNext.setSize(20);
		buttonRight.setGraphic(iconNext);
		
		
		//apply theme
		anchorPaneMain.setStyle("-fx-background-color: #DDDDDD");
		labelMonth.setStyle("-fx-text-fill: " + bundle.getString("color.text"));		
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

	public void about()
	{
		AlertGenerator.showAboutAlert(bundle.getString("app.name"), bundle.getString("version.name"), bundle.getString("version.code"), bundle.getString("version.date"), bundle.getString("author"), icon, stage, null, false);	
	}
}