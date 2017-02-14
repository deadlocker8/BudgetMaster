package de.deadlocker8.budgetmaster.ui;

import java.util.ArrayList;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import de.deadlocker8.budgetmaster.logic.Category;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
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

	public void init(Stage stage, Controller controller, CategoryController categoryController, boolean edit, Category category)
	{
		this.stage = stage;
		this.controller = controller;
		this.categoryController = categoryController;
		this.edit = edit;
		this.color = null;

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

		// DEBUG
		ArrayList<Color> colors = new ArrayList<>();
		colors.add(Color.web("#ecf0f1"));
		colors.add(Color.web("#CCCCCC"));	
		colors.add(Color.web("#888888"));
		colors.add(Color.web("#333333"));
		colors.add(Color.web("#000000"));
		
		colors.add(Color.web("#00FF00"));
		colors.add(Color.web("#64dd17"));		
		colors.add(Color.web("#27ae60"));		
		colors.add(Color.web("#1b5e20"));
		colors.add(Color.web("#00FFFF"));
				
		colors.add(Color.web("#8FC6EA"));		
		colors.add(Color.web("#2980b9"));		
		colors.add(Color.web("#0d47a1"));
		colors.add(Color.web("#0000FF"));
		colors.add(Color.web("#9b59b6"));
		
		colors.add(Color.web("#B200FF"));		
		colors.add(Color.web("#FF00DC"));	
		colors.add(Color.web("#ffff00"));			
		colors.add(Color.web("#e67e22"));
		colors.add(Color.web("#FF7F00"));	
		
		colors.add(Color.web("#EB6C5F"));		
		colors.add(Color.web("#c0392b"));
		colors.add(Color.web("#FF0000"));
		colors.add(Color.web("#7F3300"));
		
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

	}

	public void cancel()
	{
		stage.close();
	}
}