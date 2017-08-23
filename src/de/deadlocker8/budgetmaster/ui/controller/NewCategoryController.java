package de.deadlocker8.budgetmaster.ui.controller;

import java.util.ArrayList;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmaster.ui.colorPick.ColorView;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import tools.AlertGenerator;
import tools.ConvertTo;
import tools.Localization;

public class NewCategoryController
{
	@FXML private TextField textFieldName;
	@FXML private Button buttonColor;
	@FXML private Button buttonCancel;
	@FXML private Button buttonSave;

	private Stage stage;
	private Controller controller;
	private CategoryController categoryController;
	private boolean edit;
	private Color color;
	private PopOver colorChooser;
	private ColorView colorView;
	private Category category;

	public void init(Stage stage, Controller controller, CategoryController categoryController, boolean edit, Category category)
	{
		this.stage = stage;
		this.controller = controller;
		this.categoryController = categoryController;
		this.edit = edit;
		this.color = null;
		this.category = category;
	
		buttonCancel.setGraphic(Helpers.getFontIcon(FontIconType.TIMES, 17, Color.WHITE));		
		buttonSave.setGraphic(Helpers.getFontIcon(FontIconType.SAVE, 17, Color.WHITE));

		buttonCancel.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonSave.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonColor.setStyle("-fx-border-color: #000000; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");
		
		buttonColor.prefWidthProperty().bind(textFieldName.widthProperty());
		
		ArrayList<Color> colors = Helpers.getCategoryColorList();
		
		buttonColor.setOnMouseClicked((e) -> {

			if(colorChooser == null || !colorChooser.isShowing())
			{
				colorChooser = new PopOver();
				colorChooser.setContentNode(colorView);
				colorChooser.setDetachable(false);
				colorChooser.setAutoHide(true);				
				colorChooser.setCornerRadius(5);
				colorChooser.setArrowLocation(ArrowLocation.LEFT_CENTER);
				colorChooser.setOnHiding(event -> colorChooser = null);			
				colorChooser.show(buttonColor);				
			}
		});

		stage.setOnCloseRequest(event -> {
			if(colorChooser != null)
			{
				colorChooser.hide(Duration.millis(0));
			}
		});

		if(edit)
		{
			textFieldName.setText(category.getName());
			colorView = new ColorView(Color.web(category.getColor()), colors, this, (finishColor) -> {
				setColor(finishColor);
			});
			setColor(Color.web(category.getColor()));
		}
		else
		{
			colorView = new ColorView(colors.get(0), colors, this, (finishColor) -> {
				setColor(finishColor);
			});
			setColor(colors.get(0));
		}
	}

	private void setColor(Color color)
	{
		this.color = color;
		buttonColor.setStyle("-fx-border-color: #000000; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: " + ConvertTo.toRGBHex(color));
		if(colorChooser != null)
		{
			colorChooser.hide(Duration.millis(0));
		}
	}

	public void save()
	{
		String name = textFieldName.getText();
		if(name == null || name.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING,
			                        Localization.getString(Strings.TITLE_WARNING),
                        	        "",
                        	        Localization.getString(Strings.WARNING_EMPTY_CATEGORY_NAME),
                        	        controller.getIcon(), 
                        	        controller.getStage(), 
                        	        null, 
                        	        false);
			return;
		}
		
		if(name.length() > 45)
		{
			AlertGenerator.showAlert(AlertType.WARNING,
			                        Localization.getString(Strings.TITLE_WARNING),
		                            "", 
		                            Localization.getString(Strings.WARNING_NAME_CHARACTER_LIMIT_REACHED_45), 
		                            controller.getIcon(), 
		                            controller.getStage(), 
		                            null, 
		                            false);
			return;
		}
		
		if(edit)
		{
			category.setName(name);
			category.setColor(ConvertTo.toRGBHexWithoutOpacity(color));
			ServerConnection connection;
			try
			{
				connection = new ServerConnection(controller.getSettings());
				connection.updateCategory(category);
			}
			catch(Exception e)
			{
				controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
			}			
		}
		else
		{			
			Category newCategory = new Category(name, ConvertTo.toRGBHexWithoutOpacity(color));		
			ServerConnection connection;
			try
			{
				connection = new ServerConnection(controller.getSettings());
				connection.addCategory(newCategory);
			}
			catch(Exception e)
			{				
				controller.showConnectionErrorAlert(e.getMessage());
			}	
		}
		
		stage.close();
		categoryController.getController().refresh(controller.getFilterSettings());
	}

	public void cancel()
	{
		stage.close();
	}
}