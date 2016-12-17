package de.deadlocker8.budgetmaster.ui.cells;

import java.text.DecimalFormat;

import de.deadlocker8.budgetmaster.logic.CategoryBudget;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import tools.ConvertTo;

public class CategoryBudgetCell extends ListCell<CategoryBudget>
{		
	private final double HEIGHT = 40.0;
	private final DecimalFormat format = new DecimalFormat("#.00");
	
	@Override
	protected void updateItem(CategoryBudget item, boolean empty)
	{
		super.updateItem(item, empty);

		if(!empty)
		{		
			HBox hbox = new HBox();
			
			Label labelCircle = new Label(item.getName().substring(0, 1).toUpperCase());
			labelCircle.setPrefWidth(HEIGHT);
			labelCircle.setPrefHeight(HEIGHT);
			labelCircle.setAlignment(Pos.CENTER);
			labelCircle.getStyleClass().add("greylabel");
			labelCircle.setStyle("-fx-background-color: " + ConvertTo.toRGBHex(item.getColor()) + "; -fx-background-radius: 50%; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20;");
			hbox.getChildren().add(labelCircle);
			
			Label labelName = new Label(item.getName());
			labelName.setPrefHeight(HEIGHT);
			labelName.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: #212121");
			labelName.setAlignment(Pos.CENTER);
			labelName.getStyleClass().add("greylabel");
			hbox.getChildren().add(labelName);
			HBox.setMargin(labelName, new Insets(0, 0, 0, 20));
			
			Region r = new Region();
			hbox.getChildren().add(r);
			HBox.setHgrow(r, Priority.ALWAYS);
			
			Label labelBudget = new Label(String.valueOf(format.format(item.getBudget())).replace(".", ",") + " �");
			labelBudget.setPrefHeight(HEIGHT);
			labelBudget.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: #212121");
			labelBudget.setAlignment(Pos.CENTER);
			labelBudget.getStyleClass().add("greylabel");
			hbox.getChildren().add(labelBudget);
			HBox.setMargin(labelBudget, new Insets(0, 0, 0, 20));		
			
			ProgressIndicator progressIndicator = new ProgressIndicator();
			progressIndicator.setPrefHeight(HEIGHT);	
			progressIndicator.setStyle("-fx-progress-color: " + ConvertTo.toRGBHex(item.getColor()));
			//DEBUG replace 560.00 with startBudget
			progressIndicator.setProgress(item.getBudget() / 560.00);
			hbox.getChildren().add(progressIndicator);
			HBox.setMargin(progressIndicator, new Insets(0, 0, 0, 25));
			
			setPadding(new Insets(10));
			setStyle("-fx-background: transparent; -fx-border-color: #333333");
			setGraphic(hbox);	
		}
		else
		{
			setStyle("-fx-background: transparent");
			setText(null);
		}
	}
}