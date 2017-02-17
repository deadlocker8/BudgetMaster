package de.deadlocker8.budgetmaster.ui;

import java.time.LocalDate;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.RepeatingType;
import de.deadlocker8.budgetmaster.logic.ServerConnection;
import de.deadlocker8.budgetmaster.ui.cells.ButtonCategoryCell;
import de.deadlocker8.budgetmaster.ui.cells.SmallCategoryCell;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import tools.AlertGenerator;
import tools.ConvertTo;

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
	private boolean edit;

	public void init(Stage stage, Controller controller, PaymentController paymentController, boolean payment, boolean edit)
	{
		this.stage = stage;
		this.controller = controller;
		this.paymentController = paymentController;
		this.payment = payment;
		this.edit = edit;

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
		
		comboBoxCategory.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>()
		{			
			@Override
			public ListCell<Category> call(ListView<Category> param)
			{
				return new SmallCategoryCell();
			}
		});	
		
		comboBoxCategory.setButtonCell(new ButtonCategoryCell(Color.WHITE));
		comboBoxCategory.setStyle("-fx-border-color: #000000; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");
		comboBoxCategory.valueProperty().addListener((listener, oldValue, newValue)->{
			comboBoxCategory.setStyle("-fx-background-color: " + ConvertTo.toRGBHex(newValue.getColor()) + "; -fx-border-color: #000000; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");
			comboBoxCategory.setButtonCell(new ButtonCategoryCell(newValue.getColor()));
		});	
		
		comboBoxCategory.getItems().clear();
		try
		{			
			ServerConnection connection = new ServerConnection(controller.getSettings());
			ArrayList<Category> categories = connection.getCategories();
			if(categories != null)
			{
				comboBoxCategory.getItems().addAll(categories);
				comboBoxCategory.getSelectionModel().select(0);
			}
		}
		catch(Exception e)
		{
			//ERRORHANDLING
			e.printStackTrace();
		}		
	}

	public void save()
	{
		String name = textFieldName.getText();
		if(name == null || name.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Das Feld für den Namen darf nicht leer sein.", controller.getIcon(), controller.getStage(), null, false);
			return;
		}
		
		String amountText = textFieldAmount.getText();
		if(!amountText.matches("^-?\\d+(,\\d+)*(\\.\\d+(e\\d+)?)?$"))
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Gib eine gültige Zahl für den Betrag ein." , controller.getIcon(), controller.getStage(), null, false);
			return;
		}
		
		LocalDate date = datePicker.getValue();
		if(date == null)
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Bitte wähle ein Datum aus." , controller.getIcon(), controller.getStage(), null, false);
			return;
		}
		
		int amount = 0;
		amount = (int)(Double.parseDouble(amountText) * 100);
		if(payment)
		{
			amount = -amount;
		}
		
		//TODO use ServerConnection to save
		if(edit)
		{
			
		}
		else
		{
			
		}
		
		paymentController.refresh();
	}

	public void cancel()
	{
		stage.close();
	}
}