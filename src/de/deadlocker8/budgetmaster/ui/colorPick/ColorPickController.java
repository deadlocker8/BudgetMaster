package de.deadlocker8.budgetmaster.ui.colorPick;

import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tools.ConvertTo;

public class ColorPickController
{
	@FXML private Label labelColor;
	@FXML private Slider sliderRed;
	@FXML private TextField textFieldRed;
	@FXML private Slider sliderGreen;
	@FXML private TextField textFieldGreen;
	@FXML private Slider sliderBlue;
	@FXML private TextField textFieldBlue;
	@FXML private TextField textFieldHex;
	@FXML private Button buttonSave;
	@FXML private Button buttonCancel;	

	private Stage stage;
	private Consumer<Color> finish;

	public void init(Stage stage, Consumer<Color> finish)
	{
		this.stage = stage;
		this.finish = finish;
		
		initializeSliderAndTextField(sliderRed, textFieldRed);
		initializeSliderAndTextField(sliderGreen, textFieldGreen);
		initializeSliderAndTextField(sliderBlue, textFieldBlue);
		
		initializeTextFieldHex();
		
		updatePreview();
	}
	
	private void initializeSliderAndTextField(Slider slider, TextField textField)
	{
		initializeSlider(slider, textField);
		initializeTextField(slider, textField);
	}
	
	private void initializeSlider(Slider slider, TextField textField)
	{
		slider.valueProperty().addListener((observer, oldValue, newValue)->{
			textField.setText(String.valueOf(newValue.intValue()));			
			textFieldHex.setText(ConvertTo.toRGBHexWithoutOpacity(getColor()));
			updatePreview();
		});
	}
	
	private void initializeTextField(Slider slider, TextField textField)
	{
		textField.setTextFormatter(new TextFormatter<>(c -> {
			if(c.getControlNewText().isEmpty())
			{
				return c;
			}

			if(c.getControlNewText().matches("[0-9]*"))
			{
				if(Double.parseDouble(c.getControlNewText()) > 255)
				{
					return null;
				}
				else
				{
					return c;
				}				
			}
			else
			{
				return null;
			}
		}));		
		
		textField.textProperty().addListener((observer, oldValue, newValue)->{
			double value;
			if(newValue.isEmpty())
			{
				value = 0;
			}
			else
			{
				value = Double.parseDouble(newValue);
			}
			slider.setValue(value);				
			textFieldHex.setText(ConvertTo.toRGBHexWithoutOpacity(getColor()));
		});
	}
	
	private void initializeTextFieldHex()
	{		
		textFieldHex.textProperty().addListener((observer, oldValue, newValue)->{			
			try
			{				
				Color hexColor = Color.web(newValue);
				
				sliderRed.setValue((int)(hexColor.getRed()*255));
				sliderGreen.setValue((int)(hexColor.getGreen()*255));
				sliderBlue.setValue((int)(hexColor.getBlue()*255));
			}
			catch(Exception e)
			{
				
			}
		});
	}

	private Color getColor()
	{
		return Color.rgb((int)sliderRed.getValue(), (int)sliderGreen.getValue(), (int)sliderBlue.getValue());
	}
	
	private void updatePreview()
	{
		labelColor.setStyle("-fx-border-color: #000000; -fx-border-width: 1; -fx-background-color: " + ConvertTo.toRGBHex(getColor()));
	}
	
	@FXML
	void cancel(ActionEvent event)
	{
		stage.close();		
	}

	@FXML
	void save(ActionEvent event)
	{
		finish.accept(getColor());
		stage.close();
	}
}