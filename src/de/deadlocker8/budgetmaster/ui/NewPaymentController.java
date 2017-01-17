package de.deadlocker8.budgetmaster.ui;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.RepeatingType;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewPaymentController
{
	@FXML private TextField textFieldName;
	@FXML private TextField textFieldAmount;
	@FXML private Button buttonCancel;
	@FXML private Button buttonSave;
	@FXML private ComboBox<Category> comboBoxCategory;
	@FXML private DatePicker datePicker;
	@FXML private ComboBox<RepeatingType> comboBoxRepeatingPeriod;
	@FXML private ComboBox<Double> comboBoxRepeatingType;
	
	private Stage stage;
	private Controller controller;
	private PaymentController paymentController;	
	private boolean payment;

	public void init(Stage stage, Controller controller, PaymentController paymentController, boolean payment)
	{
		this.stage = stage;
		this.controller = controller;
		this.paymentController = paymentController;
		this.payment = payment;
		
		FontIcon iconCancel = new FontIcon(FontIconType.TIMES);
		iconCancel.setSize(17);
		iconCancel.setStyle("-fx-text-fill: white");	
		buttonCancel.setGraphic(iconCancel);
		FontIcon iconSave = new FontIcon(FontIconType.SAVE);
		iconSave.setSize(17);
		iconSave.setStyle("-fx-text-fill: white");
		buttonSave.setGraphic(iconSave);			
		
		buttonCancel.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonSave.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
	}
	
	public void save()
	{
		
	}
	
	public void cancel()
	{
		stage.close();
	}
}