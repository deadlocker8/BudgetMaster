package de.deadlocker8.budgetmaster.ui.cells;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.ui.controller.SearchController;
import fontAwesome.FontIconType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import logger.Logger;
import tools.ConvertTo;

public class SearchCell extends ListCell<Payment>
{
	private final double HEIGHT = 30.0;
	private SearchController searchController;

	public SearchCell(SearchController searchController)
	{
		super();
		this.searchController = searchController;
	}

	@Override
	protected void updateItem(Payment item, boolean empty)
	{
		super.updateItem(item, empty);

		if(!empty)
		{
			Category category = searchController.getController().getCategoryHandler().getCategory(item.getCategoryID());

			HBox hbox = new HBox();

			String dateString = item.getDate();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try
			{
				Date date = format.parse(dateString);
				DateFormat finalFormat = new SimpleDateFormat("dd.MM.yy");
				dateString = finalFormat.format(date);
			}
			catch(ParseException e)
			{
				Logger.error(e);
			}
			Label labelDate = new Label(dateString);
			labelDate.setPrefHeight(HEIGHT);
			labelDate.setAlignment(Pos.CENTER);
			labelDate.getStyleClass().add("greylabel");
			labelDate.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #212121");
			labelDate.setMinWidth(60);
			hbox.getChildren().add(labelDate);

			Label labelRepeating = new Label();
			if(item instanceof RepeatingPayment)
			{				
				labelRepeating.setGraphic(Helpers.getFontIcon(FontIconType.CALENDAR, 18, Color.web("#212121")));
			}
			else
			{
			    labelRepeating.setGraphic(Helpers.getFontIcon(FontIconType.CALENDAR, 18, Color.TRANSPARENT));
			}
			labelRepeating.setPrefHeight(HEIGHT);
			labelRepeating.setStyle("-fx-font-size: 15; -fx-text-fill: #212121");
			labelRepeating.setAlignment(Pos.CENTER);
			labelRepeating.getStyleClass().add("greylabel");
			hbox.getChildren().add(labelRepeating);
			HBox.setMargin(labelRepeating, new Insets(0, 20, 0, 15));

			Label labelCircle = new Label(category.getName().substring(0, 1).toUpperCase());
			labelCircle.setMinWidth(HEIGHT);
			labelCircle.setMinHeight(HEIGHT);
			labelCircle.setAlignment(Pos.CENTER);
			labelCircle.getStyleClass().add("greylabel");
			String textColor = ConvertTo.toRGBHex(ConvertTo.getAppropriateTextColor(Color.web(category.getColor())));
			labelCircle.setStyle("-fx-background-color: " + category.getColor() + "; -fx-background-radius: 50%; -fx-text-fill: " + textColor + "; -fx-font-weight: bold; -fx-font-size: 18;");
			Tooltip tooltip = new Tooltip(category.getName());
			tooltip.setStyle("-fx-font-size: 14");
			labelCircle.setTooltip(tooltip);
			hbox.getChildren().add(labelCircle);	
			
			VBox vboxNameAndDescription = new VBox();
			vboxNameAndDescription.setSpacing(2);
			vboxNameAndDescription.setAlignment(Pos.CENTER_LEFT);
			vboxNameAndDescription.setMinHeight(HEIGHT + 12);

			Label labelName = new Label(item.getName());			
			labelName.setStyle("-fx-font-size: 15; -fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
			labelName.setAlignment(Pos.CENTER_LEFT);
			labelName.getStyleClass().add("greylabel");
			vboxNameAndDescription.getChildren().add(labelName);
			
			if(item.getDescription() != null && !item.getDescription().equals(""))
			{				
				Label labelDescription = new Label(item.getDescription());
				labelDescription.setStyle("-fx-font-size: 14; -fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT) + "; -fx-font-style: italic");
				labelDescription.setAlignment(Pos.CENTER_LEFT);
				labelDescription.getStyleClass().add("greylabel");			
				vboxNameAndDescription.getChildren().add(labelDescription);
			}
			
			hbox.getChildren().add(vboxNameAndDescription);
			HBox.setMargin(vboxNameAndDescription, new Insets(0, 0, 0, 20));

			Region r = new Region();
			hbox.getChildren().add(r);
			HBox.setHgrow(r, Priority.ALWAYS);

			Label labelBudget = new Label(Helpers.getCurrencyString(item.getAmount(), searchController.getController().getSettings().getCurrency()));
			labelBudget.setPrefHeight(HEIGHT);
			labelBudget.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #247A2D");
			labelBudget.setAlignment(Pos.CENTER);
			labelBudget.getStyleClass().add("greylabel");
			labelBudget.setMinWidth(90);
			hbox.getChildren().add(labelBudget);
			HBox.setMargin(labelBudget, new Insets(0, 0, 0, 20));

			if(item.isIncome())
			{
				labelBudget.setText("+" + labelBudget.getText());
			}
			else
			{
				labelBudget.setText(labelBudget.getText());
				labelBudget.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #CC0000");
			}

			Button buttonGoto = new Button();			
			buttonGoto.setGraphic(Helpers.getFontIcon(FontIconType.EXTERNAL_LINK_SQUARE, 16, Color.web("#212121")));
			buttonGoto.setPrefHeight(HEIGHT);
			buttonGoto.getStyleClass().add("greylabel");
			buttonGoto.setStyle("-fx-background-color: transparent");			
			buttonGoto.setOnAction((event) -> {
				//TODO goto month and payment
			});
			hbox.getChildren().add(buttonGoto);
			HBox.setMargin(buttonGoto, new Insets(0, 0, 0, 10));
			
			hbox.setPadding(new Insets(8, 8, 8, 5));
			hbox.setAlignment(Pos.CENTER_LEFT);
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