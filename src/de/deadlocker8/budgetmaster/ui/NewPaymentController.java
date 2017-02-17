package de.deadlocker8.budgetmaster.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.Payment;
import de.deadlocker8.budgetmaster.logic.ServerConnection;
import de.deadlocker8.budgetmaster.ui.cells.ButtonCategoryCell;
import de.deadlocker8.budgetmaster.ui.cells.RepeatingDayCell;
import de.deadlocker8.budgetmaster.ui.cells.SmallCategoryCell;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
	@FXML private DatePicker datePickerEnddate;
	@FXML private Spinner<Integer> spinnerRepeatingPeriod;	
	@FXML private ComboBox<Integer> comboBoxRepeatingDay;
	@FXML private CheckBox checkBoxRepeat;

	private Stage stage;
	private Controller controller;
	private PaymentController paymentController;
	private boolean isPayment;
	private boolean edit;
	private Payment payment;

	public void init(Stage stage, Controller controller, PaymentController paymentController, boolean isPayment, boolean edit, Payment payment)
	{
		this.stage = stage;
		this.controller = controller;
		this.paymentController = paymentController;
		this.isPayment = isPayment;
		this.edit = edit;
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

		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0);
		spinnerRepeatingPeriod.setValueFactory(valueFactory);
		
		comboBoxRepeatingDay.setCellFactory(new Callback<ListView<Integer>, ListCell<Integer>>()
		{
			@Override
			public ListCell<Integer> call(ListView<Integer> param)
			{
				return new RepeatingDayCell();
			}
		});
		ArrayList<Integer> days = new ArrayList<>();
		for(int i = 0; i <= 31; i++)
		{
			days.add(i);
		}
		comboBoxRepeatingDay.getItems().addAll(days);

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
		comboBoxCategory.valueProperty().addListener((listener, oldValue, newValue) -> {
			comboBoxCategory.setStyle("-fx-background-color: " + ConvertTo.toRGBHex(newValue.getColor()) + "; -fx-border-color: #000000; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");
			comboBoxCategory.setButtonCell(new ButtonCategoryCell(newValue.getColor()));
		});

		checkBoxRepeat.selectedProperty().addListener((listener, oldValue, newValue) -> {
			toggleRepeatingArea(newValue);
		});

		comboBoxCategory.getItems().clear();
		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			ArrayList<Category> categories = connection.getCategories();
			if(categories != null)
			{
				comboBoxCategory.getItems().addAll(categories);
			}
		}
		catch(Exception e)
		{
			// ERRORHANDLING
			e.printStackTrace();
		}

		if(edit)
		{
			// TODO prefill
		}
		else
		{
			comboBoxCategory.getSelectionModel().select(0);
			checkBoxRepeat.setSelected(false);
			toggleRepeatingArea(false);
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
		if(isPayment)
		{
			amount = -amount;
		}
		
		int repeatingInterval = 0;					
		int repeatingDay = 0;
		if(checkBoxRepeat.isSelected())
		{
			repeatingInterval = spinnerRepeatingPeriod.getValue();		
			
			if(repeatingInterval != 0 && repeatingDay != 0)
			{
				AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Es kann nur eine von beiden Wiederholungsarten verwendet werden." , controller.getIcon(), controller.getStage(), null, false);
				return;
			}			
		}		
		
		if(edit)
		{
			Payment newPayment = new Payment(payment.getID(), amount, getDateString(date), comboBoxCategory.getValue().getID(), name, repeatingInterval, getDateString(datePickerEnddate.getValue()), repeatingDay);
			
			try
			{
				ServerConnection connection = new ServerConnection(controller.getSettings());
				connection.updatePayment(newPayment, payment );
			}
			catch(Exception e)
			{				
				controller.showConnectionErrorAlert();
			}
		}
		else
		{
			Payment newPayment = new Payment(-1, amount, getDateString(date), comboBoxCategory.getValue().getID(), name, repeatingInterval, getDateString(datePickerEnddate.getValue()), repeatingDay);
			
			try
			{
				ServerConnection connection = new ServerConnection(controller.getSettings());
				connection.addPayment(newPayment);
			}
			catch(Exception e)
			{				
				controller.showConnectionErrorAlert();
			}
		}	
		
		paymentController.refresh();
	}

	public void cancel()
	{
		stage.close();
	}

	private void toggleRepeatingArea(boolean selected)
	{
		spinnerRepeatingPeriod.setDisable(!selected);		
		comboBoxRepeatingDay.setDisable(!selected);
		datePickerEnddate.setDisable(!selected);
	}
	
	private String getDateString(LocalDate date)
	{		
		if(date == null)
		{
			return "";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return date.format(formatter);
	}
}