package de.deadlocker8.budgetmaster.ui.cells;

import de.deadlocker8.budgetmaster.logic.utils.LanguageType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class LanguageCell extends ListCell<LanguageType>
{		
	private final double HEIGHT = 20.0;
	private boolean useBorder;
	
	public LanguageCell(boolean useBorder)
	{
		this.useBorder = useBorder;
	}
	
	@Override
	protected void updateItem(LanguageType item, boolean empty)
	{
		super.updateItem(item, empty);

		if(!empty)
		{		
			HBox hbox = new HBox();
			
			Image image = new Image("de/deadlocker8/budgetmaster/resources/flags/" + item.getIconName() + ".png");
			ImageView imageView = new ImageView(image);
			imageView.setFitWidth(HEIGHT);
			imageView.setFitHeight(HEIGHT);
			hbox.getChildren().add(imageView);			
			
			Label labelName = new Label(item.getName());
			labelName.setPrefHeight(HEIGHT);
			labelName.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #212121");
			labelName.setAlignment(Pos.CENTER);
			hbox.getChildren().add(labelName);
			HBox.setMargin(labelName, new Insets(0, 0, 0, 20));
			
			hbox.setPadding(new Insets(0));
			if(useBorder)
			{
				setStyle("-fx-background: transparent; -fx-border-color: #545454; -fx-border-width: 0 0 1 0");
			}
			else
			{
				setStyle("-fx-background: transparent;");
			}
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