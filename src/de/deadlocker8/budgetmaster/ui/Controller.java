package de.deadlocker8.budgetmaster.ui;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logger.LogLevel;
import logger.Logger;

public class Controller
{
	@FXML private Label labelMonth;
	@FXML private Button buttonLeft;
	@FXML private Button buttonRight;
	@FXML private Tab tabHome;
	@FXML private Tab tabPayments;
	@FXML private Tab tabCategories;
	@FXML private Tab tabCharts;

	private Stage stage;
	private Image icon = new Image("de/deadlocker8/budgetmaster/resources/icon.png");
	private final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);

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
		}
		catch(IOException e)
		{
			//ERRORHANDLING
			Logger.log(LogLevel.ERROR, Logger.exceptionToString(e));
		}
	}
	
	public Stage getStage()
	{
		return stage;
	}

	public void about()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Über " + bundle.getString("app.name"));
		alert.setHeaderText(bundle.getString("app.name"));
		alert.setContentText("Version:     " + bundle.getString("version.name") + "\r\nDatum:      " + bundle.getString("version.date") + "\r\nAutor:        Robert Goldmann\r\n");
		Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(icon);
		dialogStage.centerOnScreen();
		alert.showAndWait();
	}
}