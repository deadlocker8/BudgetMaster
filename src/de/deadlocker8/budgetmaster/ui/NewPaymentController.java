package de.deadlocker8.budgetmaster.ui;

import java.util.Locale;
import java.util.ResourceBundle;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.RepeatingType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class NewPaymentController
{
	@FXML private TextField textFieldName;
	@FXML private TextField textFieldAmount;
	@FXML private ComboBox<Category> comboBoxCategory;
	@FXML private DatePicker datePicker;
	@FXML private ComboBox<RepeatingType> comboBoxRepeatingPeriod;
	@FXML private ComboBox<Double> comboBoxRepeatingType;
	
	private Stage stage;
	private Controller controller;
	private PaymentController paymentController;
	private Image icon = new Image("de/deadlocker8/budgetmaster/resources/icon.png");
	private final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);
	private boolean payment;

	public void init(Stage stage, Controller controller, PaymentController paymentController, boolean payment)
	{
		this.stage = stage;
		this.controller = controller;
		this.paymentController = paymentController;
		this.payment = payment;
	}
	
	public void save()
	{
		
	}
	
	public void cancel()
	{
		stage.close();
	}
}