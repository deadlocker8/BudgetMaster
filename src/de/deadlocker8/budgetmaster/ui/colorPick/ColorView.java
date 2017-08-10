package de.deadlocker8.budgetmaster.ui.colorPick;

import java.util.ArrayList;
import java.util.function.Consumer;

import de.deadlocker8.budgetmaster.ui.controller.NewCategoryController;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logger.Logger;
import tools.ConvertTo;

public class ColorView extends GridPane
{
	private Node lastSelectedNode;
	private Color colorPickerColor;
	private Button buttonColorPicker;
	private FontIcon icon;
	
	public ColorView(Color startColor, ArrayList<Color> colors, NewCategoryController controller, Consumer<Color> finish)
	{		
		colors.add(Color.TRANSPARENT);

		double size = Math.sqrt(colors.size());
		int iSize = (int)size;
		if(size != iSize)
		{
			iSize++;
		}

		setVgap(5);
		setHgap(5);
		setPadding(new Insets(5));
		
		colorPickerColor = Color.WHITE;

		int index = 0;
		for(int y = 0; y < iSize; y++)
		{
			for(int x = 0; x < iSize; x++)
			{
				if(index < colors.size())
				{
					Color color = colors.get(index++);

					if(color == Color.TRANSPARENT)
					{						
						buttonColorPicker = new Button();	
						buttonColorPicker.setPrefHeight(40);
						buttonColorPicker.setPrefWidth(40);
												
						if(!colors.contains(startColor))
						{								
							colorPickerColor = startColor;
							updateColorPickerCSS(buttonColorPicker, true, startColor);
						}	
						
						icon = new FontIcon(FontIconType.PLUS);
						icon.setSize(20);			
						icon.setStyle("-fx-text-fill: " + ConvertTo.toRGBHex(ConvertTo.getAppropriateTextColor(colorPickerColor)));						
						buttonColorPicker.setGraphic(icon);

						buttonColorPicker.setOnAction((event)->{
							try
							{
								FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("de/deadlocker8/budgetmaster/ui/colorPick/ColorPickGUI.fxml"));
								Parent root = (Parent)loader.load();

								Scene scene = new Scene(root, 500, 225);
								Stage stage = new Stage();
								
								((ColorPickController)loader.getController()).init(stage, (finishColor)->{
									colorPickerColor = finishColor;
									updateColorPickerCSS(buttonColorPicker, true, finishColor);
									if(lastSelectedNode instanceof Rectangle)
									{
										((Rectangle)lastSelectedNode).getStrokeDashArray().clear();
									}
									lastSelectedNode = buttonColorPicker;
									icon.setStyle("-fx-text-fill: " + ConvertTo.toRGBHex(ConvertTo.getAppropriateTextColor(finishColor)));
									finish.accept(finishColor);
								});
								
								stage.setResizable(false);	
								stage.initModality(Modality.APPLICATION_MODAL);
								stage.setScene(scene);
								stage.getIcons().add(new Image("/de/deadlocker8/budgetmaster/resources/icon.png"));
								stage.show();
							}
							catch(Exception e)
							{
								Logger.error(e);
							}
						});
						
						add(buttonColorPicker, x, y);
					}
					else
					{
						Rectangle rectangle = createRectangle(startColor, color);

						// EventHandler
						rectangle.setOnMouseReleased(event -> {
							if(lastSelectedNode instanceof Rectangle)
							{
								((Rectangle)lastSelectedNode).getStrokeDashArray().clear();
							}
							else
							{
								updateColorPickerCSS(buttonColorPicker, false, Color.WHITE);
								icon.setStyle("-fx-text-fill: " + ConvertTo.toRGBHex(ConvertTo.getAppropriateTextColor(Color.WHITE)));
							}
							rectangle.getStrokeDashArray().add(3.0);
							lastSelectedNode = rectangle;
							
							finish.accept(color);
						});

						add(rectangle, x, y);
					}
				}
			}
		}
		
		updateColorPickerCSS(buttonColorPicker, true, colorPickerColor);
	}

	private Rectangle createRectangle(Color startColor, Color color)
	{
		Rectangle rectangle = new Rectangle(40, 40);
		rectangle.setFill(color);
		rectangle.getStyleClass().add("color-view-item");

		// dotted border
		if(color == startColor)
		{			
			lastSelectedNode = rectangle;
			rectangle.getStrokeDashArray().add(3.0);
		}

		return rectangle;
	}
	
	private void updateColorPickerCSS(Node button, boolean dashed, Color backgroundColor)
	{
		String css = "-fx-background-radius: 4; -fx-border-width: 1.8; -fx-border-color: black; -fx-border-radius: 4; -fx-background-color: " + ConvertTo.toRGBHex(backgroundColor) + ";";
		if(dashed)
		{
			css += " -fx-border-style: dashed;";
		}
		
		button.setStyle(css);
	}
}