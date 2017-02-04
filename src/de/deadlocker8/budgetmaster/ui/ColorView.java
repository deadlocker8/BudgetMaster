package de.deadlocker8.budgetmaster.ui;

import java.util.ArrayList;
import java.util.function.Consumer;

import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import tools.ConvertTo;

public class ColorView extends GridPane
{
	private Node lastSelectedNode;
	
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
						StackPane stackPane = new StackPane();				
						
						ColorPicker picker = new ColorPicker();	
						picker.setPrefHeight(40);
						picker.setPrefWidth(40);
						picker.setMaxWidth(40);
						updateColorPickerCSS(picker, false, Color.TRANSPARENT);
						stackPane.getChildren().add(picker);					
						
						if(!colors.contains(startColor))
						{
							picker.setValue(startColor);
							updateColorPickerCSS(picker, true, startColor);
						}
						
						FontIcon icon = new FontIcon(FontIconType.PLUS);
						icon.setSize(20);			
						icon.setStyle("-fx-text-fill: " + ConvertTo.toRGBHex(ConvertTo.getAppropriateTextColor(picker.getValue())));
						stackPane.getChildren().add(icon);						

						picker.valueProperty().addListener((observable, oldValue, newValue)->{
							updateColorPickerCSS(picker, true, newValue);
							if(lastSelectedNode instanceof Rectangle)
							{
								((Rectangle)lastSelectedNode).getStrokeDashArray().clear();
							}
							lastSelectedNode = picker;
							icon.setStyle("-fx-text-fill: " + ConvertTo.toRGBHex(ConvertTo.getAppropriateTextColor(newValue)));
							finish.accept(newValue);
						});						
						
						icon.setOnMouseClicked((e)-> picker.show());
						
						add(stackPane, x, y);
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
								updateColorPickerCSS(lastSelectedNode, false, ((ColorPicker)lastSelectedNode).getValue());
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
	
	private void updateColorPickerCSS(Node colorPicker, boolean dashed, Color backgroundColor)
	{
		String css = "-fx-background-radius: 4; -fx-border-width: 1.8; -fx-border-color: black; -fx-border-radius: 4; -fx-background-color: " + ConvertTo.toRGBHex(backgroundColor) + ";";
		if(dashed)
		{
			css += " -fx-border-style: dashed;";
		}
		
		colorPicker.setStyle(css);
	}
}