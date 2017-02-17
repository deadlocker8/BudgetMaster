package de.deadlocker8.budgetmaster.ui;

import java.util.ArrayList;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.ServerConnection;
import fontAwesome.FontIcon;
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
		buttonColor.setStyle("-fx-border-color: #000000; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");
		
		buttonColor.prefWidthProperty().bind(textFieldName.widthProperty());
		
		ArrayList<Color> colors = new ArrayList<>();		
		//grey (light to dark)
		colors.add(Color.web("#ecf0f1"));
		colors.add(Color.web("#CCCCCC"));	
		colors.add(Color.web("#888888"));		
		colors.add(Color.web("#333333"));				
		colors.add(Color.rgb(255, 204, 0));		//yellow
		colors.add(Color.rgb(255, 149, 0)); 	//orange
		colors.add(Color.rgb(255, 59, 48));		//red
		colors.add(Color.rgb(169, 3, 41));		//darkred	
		colors.add(Color.rgb(255, 81, 151));	//pink
		colors.add(Color.rgb(155, 89, 182));	//purple
		colors.add(Color.rgb(88, 86, 214));		//darkpurple
		colors.add(Color.rgb(0, 122, 250));		//blue		
		colors.add(Color.rgb(90, 200, 250));	//lightblue
		colors.add(Color.rgb(76, 217, 100));	//lightgreen
		colors.add(Color.rgb(46, 124, 43));		//darkgreen
		
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
			colorView = new ColorView(category.getColor(), colors, this, (finishColor) -> {
				setColor(finishColor);
			});
			setColor(category.getColor());
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
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Das Feld für den Namen darf nicht leer sein.", controller.getIcon(), controller.getStage(), null, false);
			return;
		}
		
		if(edit)
		{
			category.setName(name);
			category.setColor(color);
			ServerConnection connection;
			try
			{
				connection = new ServerConnection(controller.getSettings());
				connection.updateCategory(category);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				controller.showConnectionErrorAlert();
			}			
		}
		else
		{			
			Category newCategory = new Category(name, color);		
			ServerConnection connection;
			try
			{
				connection = new ServerConnection(controller.getSettings());
				connection.addCategory(newCategory);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				controller.showConnectionErrorAlert();
			}	
		}
		
		stage.close();
		categoryController.refreshListView();
	}

	public void cancel()
	{
		stage.close();
	}
}