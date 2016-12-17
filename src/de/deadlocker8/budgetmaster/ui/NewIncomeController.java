package de.deadlocker8.budgetmaster.ui;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class NewIncomeController
{
	@FXML private TextField textFieldName;
	@FXML private TextField textFieldAmount;	
	@FXML private DatePicker datePicker;
	
	private Stage stage;
	private Controller controller;
	private PaymentController paymentController;
	private Image icon = new Image("de/deadlocker8/budgetmaster/resources/icon.png");
	private final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);

	public void init(Stage stage, Controller controller, PaymentController paymentController)
	{
		this.stage = stage;
		this.controller = controller;
		this.paymentController = paymentController;
	}
	
	public void save()
	{
		
	}
	
	public void cancel()
	{
		stage.close();
	}
}