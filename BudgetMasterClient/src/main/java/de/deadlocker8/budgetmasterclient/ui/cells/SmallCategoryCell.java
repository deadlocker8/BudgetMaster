package de.deadlocker8.budgetmasterclient.ui.cells;

import de.deadlocker8.budgetmaster.logic.category.Category;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import tools.ConvertTo;

public class SmallCategoryCell extends ListCell<Category>
{		
	private final double HEIGHT = 30.0;
	
	@Override
	protected void updateItem(Category item, boolean empty)
	{
		super.updateItem(item, empty);

		if(!empty)
		{		
			HBox hbox = new HBox();
			
			Label labelCircle = new Label(item.getName().substring(0, 1).toUpperCase());
			labelCircle.setPrefWidth(HEIGHT);
			labelCircle.setPrefHeight(HEIGHT);
			labelCircle.setAlignment(Pos.CENTER);
			String textColor = ConvertTo.toRGBHex(ConvertTo.getAppropriateTextColor(Color.web(item.getColor())));
			labelCircle.setStyle("-fx-background-color: " + item.getColor() + "; -fx-background-radius: 50%; -fx-text-fill: " + textColor + "; -fx-font-weight: bold; -fx-font-size: 15;");
			hbox.getChildren().add(labelCircle);			
			
			Label labelName = new Label(item.getName());
			labelName.setPrefHeight(HEIGHT);
			labelName.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #212121");
			labelName.setAlignment(Pos.CENTER);
			hbox.getChildren().add(labelName);
			HBox.setMargin(labelName, new Insets(0, 0, 0, 20));
			
			hbox.setPadding(new Insets(3));
			setStyle("-fx-background: transparent; -fx-border-color: #545454; -fx-border-width: 0 0 1 0");
			setGraphic(hbox);	
			setAlignment(Pos.CENTER);
		}
		else
		{
			setStyle("-fx-background: transparent");
			setText(null);
			setGraphic(null);
		}
	}
}