package de.deadlocker8.budgetmaster.ui.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmaster.ui.Styleable;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tools.AlertGenerator;
import tools.ConvertTo;
import tools.Localization;

public class DatePickerController extends BaseController implements Styleable
{
	@FXML private ComboBox<String> comboBoxMonth;
	@FXML private Spinner<Integer> spinnerYear;
	@FXML private Button buttonCancel;
	@FXML private Button buttonConfirm;
	
	private Stage parentStage;
	private Controller controller;
	private DateTime currentDate;
	
	public DatePickerController(Stage parentStage, Controller controller, DateTime currentDate)
	{
		this.parentStage = parentStage;
		this.controller = controller;
		this.currentDate = currentDate;
		load("/de/deadlocker8/budgetmaster/ui/fxml/DatePickerGUI.fxml", Localization.getBundle());
		getStage().showAndWait();
	}	
	
	@Override
	public void initStage(Stage stage)
	{
		stage.initOwner(parentStage);
		stage.initModality(Modality.APPLICATION_MODAL);			
		stage.setTitle(Localization.getString(Strings.TITLE_DATEPICKER));
		stage.getIcons().add(controller.getIcon());
		stage.setResizable(false);
	}	
	
	@Override
	public void init()
	{
		SpinnerValueFactory<Integer> spinnerYearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3000, currentDate.getYear()); 
		spinnerYear.setValueFactory(spinnerYearValueFactory);
		spinnerYear.setEditable(false);
		spinnerYear.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if(!newValue)
			{
				spinnerYear.increment(0); // won't change value, but will commit editor
			}
		});
		
		comboBoxMonth.getItems().addAll(Helpers.getMonthList());
		comboBoxMonth.setValue(Helpers.getMonthList().get(currentDate.getMonthOfYear()-1));
		
		applyStyle();
	}

	public void confirm()
	{
		String year = String.valueOf(spinnerYear.getValue());
		if(year == null || year.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING,
				                    Localization.getString(Strings.TITLE_WARNING),
				        	        "",
				        	        Localization.getString(Strings.WARNING_EMPTY_YEAR),
				        	        controller.getIcon(), 
				        	        getStage(), 
				        	        null, 
			        	        	false);
			return;
		}
		
		if(year.length() > 4)
		{
			AlertGenerator.showAlert(AlertType.WARNING,
				                    Localization.getString(Strings.TITLE_WARNING),
				        	        "",
				        	        Localization.getString(Strings.WARNING_WRONG_YEAR),
				        	        controller.getIcon(), 
				        	        getStage(), 
				        	        null, 
				    	        	false);
			return;
		}
		
		String dateString = year + "-" + (Helpers.getMonthList().indexOf(comboBoxMonth.getValue()) + 1);		
		DateTime newDate = DateTime.parse(dateString, DateTimeFormat.forPattern("YYYY-MM"));	
		
		getStage().close();
		controller.setDate(newDate);
	}

	public void cancel()
	{
		getStage().close();
	}

	@Override
	public void applyStyle()
	{
		buttonCancel.setGraphic(Helpers.getFontIcon(FontIconType.TIMES, 17, Color.WHITE));		
		buttonConfirm.setGraphic(Helpers.getFontIcon(FontIconType.SAVE, 17, Color.WHITE));

		buttonCancel.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonConfirm.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
	}
}