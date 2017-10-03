package de.deadlocker8.budgetmasterclient.ui.cells;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.payment.NormalPayment;
import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPaymentEntry;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.controller.PaymentController;
import fontAwesome.FontIconType;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import logger.Logger;
import tools.ConvertTo;
import tools.Localization;

public class PaymentCell extends ListCell<Payment>
{
	private final double HEIGHT = 30.0;
	private PaymentController paymentController;

	public PaymentCell(PaymentController paymentController)
	{
		super();
		this.paymentController = paymentController;
	}

	@Override
	protected void updateItem(Payment item, boolean empty)
	{
		super.updateItem(item, empty);

		if(!empty)
		{
			Category category = paymentController.getController().getCategoryHandler().getCategory(item.getCategoryID());

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
			if(item instanceof RepeatingPaymentEntry)
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
				Label labelDescription = new Label(Helpers.getFlatText(item.getDescription()));
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

			Label labelBudget = new Label(Helpers.getCurrencyString(item.getAmount(), paymentController.getController().getSettings().getCurrency()));
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
			
			Button buttonDelete = new Button();			
			buttonDelete.setGraphic(Helpers.getFontIcon(FontIconType.TRASH, 16, Color.web("#212121")));
			buttonDelete.setPrefHeight(HEIGHT);
			buttonDelete.getStyleClass().add("greylabel");
			buttonDelete.setStyle("-fx-background-color: transparent");			
			buttonDelete.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle(Localization.getString(Strings.INFO_TITLE_PAYMENT_DELETE));
				alert.setHeaderText("");
				alert.setContentText(Localization.getString(Strings.INFO_TEXT_PAYMENT_DELETE));
				Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
				dialogStage.getIcons().add(paymentController.getController().getIcon());
				dialogStage.centerOnScreen();

				if(item instanceof RepeatingPaymentEntry)
				{
					alert.setContentText(Localization.getString(Strings.INFO_TEXT_PAYMENT_REPEATING_DELETE));
					
					ButtonType buttonTypeOne = new ButtonType(Localization.getString(Strings.INFO_TEXT_PAYMENT_REPEATING_DELETE_ALL));
					ButtonType buttonTypeTwo = new ButtonType(Localization.getString(Strings.INFO_TEXT_PAYMENT_REPEATING_DELETE_FUTURES));			
					ButtonType buttonTypeCancel = new ButtonType(Localization.getString(Strings.CANCEL), ButtonData.CANCEL_CLOSE);

					alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
					
					DialogPane dialogPane = alert.getDialogPane();
					dialogPane.getButtonTypes().stream().map(dialogPane::lookupButton).forEach(button -> button.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
						if(KeyCode.ENTER.equals(e.getCode()) && e.getTarget() instanceof Button)
						{
							((Button)e.getTarget()).fire();
						}
					}));

					Optional<ButtonType> result = alert.showAndWait();
					if(result.get() == buttonTypeOne)
					{
						paymentController.deleteRepeatingPayment((RepeatingPaymentEntry)item);
					}
					else if(result.get() == buttonTypeTwo)
					{
						paymentController.deleteFuturePayments((RepeatingPaymentEntry)item);
					}
				}
				else
				{
					Optional<ButtonType> result = alert.showAndWait();
					if(result.get() == ButtonType.OK)
					{
						paymentController.deleteNormalPayment((NormalPayment)item);
					}
				}
			});
			hbox.getChildren().add(buttonDelete);
			HBox.setMargin(buttonDelete, new Insets(0, 0, 0, 10));
			// don't allow "Übertrag" to be deleted
			if(item.getID() == -1)
			{
				buttonDelete.setVisible(false);
			}

			hbox.setPadding(new Insets(10, 8, 10, 5));
			
			// payment is selected after search
			Payment selectedPayment = paymentController.getController().getSelectedPayment();
			selectPayment(selectedPayment, item, hbox);
			
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
	
	private void selectPayment(Payment selectedPayment, Payment item, Node noteToFade)
	{
		if(selectedPayment == null)
			return;

		if(selectedPayment instanceof NormalPayment)
		{
			if(item instanceof NormalPayment)
			{
				if(item.getID() != selectedPayment.getID())
				{
					return;
				}
			}
			else
			{
				return;
			}
		}
		
		if(selectedPayment instanceof RepeatingPayment)
		{				
			if(item instanceof RepeatingPaymentEntry)
			{		
				RepeatingPaymentEntry itemRepeating = (RepeatingPaymentEntry)item;
				if(itemRepeating.getRepeatingPaymentID() != selectedPayment.getID())
				{
					return;
				}
			}
			else
			{
				return;
			}
		}
								
		FadeTransition ft = new FadeTransition(Duration.millis(750), noteToFade);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        ft.play();
        ft.setOnFinished((event)->{	            	
        	paymentController.getController().setSelectedPayment(null);	            	
        });		
	}
}