package de.deadlocker8.budgetmaster.ui.cells;

import java.text.DecimalFormat;

import de.deadlocker8.budgetmaster.logic.Payment;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import tools.ConvertTo;

public class PaymentCell extends ListCell<Payment>
{		
	private final double HEIGHT = 40.0;
	private final DecimalFormat format = new DecimalFormat("#.00");
	
	@Override
	protected void updateItem(Payment item, boolean empty)
	{
		super.updateItem(item, empty);

		if(!empty)
		{		
			HBox hbox = new HBox();
			
			Label labelCircle = new Label(item.getCategory().getName().substring(0, 1).toUpperCase());
			labelCircle.setPrefWidth(HEIGHT);
			labelCircle.setPrefHeight(HEIGHT);
			labelCircle.setAlignment(Pos.CENTER);
			labelCircle.getStyleClass().add("greylabel");
			labelCircle.setStyle("-fx-background-color: " + ConvertTo.toRGBHex(item.getCategory().getColor()) + "; -fx-background-radius: 50%; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20;");
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
			
			Label labelBudget = new Label(String.valueOf(format.format(item.getAmount())).replace(".", ",") + " €");
			labelBudget.setPrefHeight(HEIGHT);		
			labelBudget.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: #212121");
			labelBudget.setAlignment(Pos.CENTER);
			labelBudget.getStyleClass().add("greylabel");
			hbox.getChildren().add(labelBudget);
			HBox.setMargin(labelBudget, new Insets(0, 0, 0, 20));	
			
			FontIcon icon;			
			
			if(item.isIncome())
			{			
				labelBudget.setText("+" + labelBudget.getText());
				
				icon = new FontIcon(FontIconType.DOWNLOAD);
				icon.setColor(Color.web("#247A2D"));
			}
			else
			{
				labelBudget.setText("-" + labelBudget.getText());
				labelBudget.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: #CC0000");
				
				icon = new FontIcon(FontIconType.UPLOAD);
				icon.setColor(Color.web("#CC0000"));
				
			}			
			
			icon.setSize(20);			
			
			Label labelIcon = new Label();
			labelIcon.setGraphic(icon);
			labelIcon.setPrefHeight(HEIGHT);
			labelIcon.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: #212121");
			labelIcon.setAlignment(Pos.CENTER);
			labelIcon.getStyleClass().add("greylabel");
			hbox.getChildren().add(labelIcon);
			HBox.setMargin(labelIcon, new Insets(0, 0, 0, 25));
			
			Button buttonDelete = new Button();
			FontIcon iconDelete = new FontIcon(FontIconType.TRASH);
			iconDelete.setSize(16);
			buttonDelete.setGraphic(iconDelete);
			buttonDelete.setPrefHeight(HEIGHT);					
			buttonDelete.getStyleClass().add("greylabel");
			buttonDelete.setStyle("-fx-background-color: transparent");
			hbox.getChildren().add(buttonDelete);
			HBox.setMargin(buttonDelete, new Insets(0, 0, 0, 25));	
			
			hbox.setPadding(new Insets(10));
			setStyle("-fx-background: transparent; -fx-border-color: #545454; -fx-border-width: 0 0 1 0");
			setGraphic(hbox);	
			setAlignment(Pos.CENTER);			
		}
		else
		{
			setStyle("-fx-background: transparent");
			setText(null);
		}
	}
}