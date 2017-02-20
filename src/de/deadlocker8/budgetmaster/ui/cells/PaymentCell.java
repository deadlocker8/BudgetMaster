package de.deadlocker8.budgetmaster.ui.cells;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.Helpers;
import de.deadlocker8.budgetmaster.logic.NormalPayment;
import de.deadlocker8.budgetmaster.logic.Payment;
import de.deadlocker8.budgetmaster.logic.RepeatingPaymentEntry;
import de.deadlocker8.budgetmaster.ui.PaymentController;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tools.ConvertTo;

public class PaymentCell extends ListCell<Payment>
{
	private final double HEIGHT = 40.0;
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
				e.printStackTrace();
			}
			Label labelDate = new Label(dateString);
			labelDate.setPrefHeight(HEIGHT);
			labelDate.setAlignment(Pos.CENTER);
			labelDate.getStyleClass().add("greylabel");
			labelDate.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: #212121");
			hbox.getChildren().add(labelDate);

			FontIcon iconRepeating = new FontIcon(FontIconType.CALENDAR);
			iconRepeating.setSize(20);
			if(item instanceof RepeatingPaymentEntry)
			{
				iconRepeating.setColor(Color.web("#212121"));
			}
			else
			{
				iconRepeating.setColor(Color.TRANSPARENT);
			}
			Label labelRepeating = new Label();
			labelRepeating.setGraphic(iconRepeating);
			labelRepeating.setPrefHeight(HEIGHT);
			labelRepeating.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: #212121");
			labelRepeating.setAlignment(Pos.CENTER);
			labelRepeating.getStyleClass().add("greylabel");
			hbox.getChildren().add(labelRepeating);
			HBox.setMargin(labelRepeating, new Insets(0, 30, 0, 15));

			String categoryName = category.getName();
			if(categoryName.equals("NONE"))
			{
				categoryName = "Keine Kategorie";
			}

			Label labelCircle = new Label(categoryName.substring(0, 1).toUpperCase());
			labelCircle.setPrefWidth(HEIGHT);
			labelCircle.setPrefHeight(HEIGHT);
			labelCircle.setAlignment(Pos.CENTER);
			labelCircle.getStyleClass().add("greylabel");
			String textColor = ConvertTo.toRGBHex(ConvertTo.getAppropriateTextColor(category.getColor()));
			labelCircle.setStyle("-fx-background-color: " + ConvertTo.toRGBHex(category.getColor()) + "; -fx-background-radius: 50%; -fx-text-fill: " + textColor + "; -fx-font-weight: bold; -fx-font-size: 20;");
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

			Label labelBudget = new Label(String.valueOf(Helpers.NUMBER_FORMAT.format(item.getAmount() / 100.0)).replace(".", ",") + " €");
			labelBudget.setPrefHeight(HEIGHT);
			labelBudget.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: #247A2D");
			labelBudget.setAlignment(Pos.CENTER);
			labelBudget.getStyleClass().add("greylabel");
			hbox.getChildren().add(labelBudget);
			HBox.setMargin(labelBudget, new Insets(0, 0, 0, 20));

			if(item.isIncome())
			{
				labelBudget.setText("+" + labelBudget.getText());
			}
			else
			{
				labelBudget.setText(labelBudget.getText());
				labelBudget.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: #CC0000");
			}

			Button buttonDelete = new Button();
			FontIcon iconDelete = new FontIcon(FontIconType.TRASH);
			iconDelete.setSize(16);
			buttonDelete.setGraphic(iconDelete);
			buttonDelete.setPrefHeight(HEIGHT);
			buttonDelete.getStyleClass().add("greylabel");
			buttonDelete.setStyle("-fx-background-color: transparent");
			// TODO advanced deleting alert for repeating payments
			buttonDelete.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Zahlung löschen");
				alert.setHeaderText("");
				alert.setContentText("Diese Zahlung wirklich unwiederruflich löschen?");
				Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
				dialogStage.getIcons().add(paymentController.getController().getIcon());
				dialogStage.centerOnScreen();

				if(item instanceof RepeatingPaymentEntry)
				{
					alert.setContentText("Es handelt sich um eine wiederkehrende Zahlung. Wie soll gelöscht werden?");
					
					ButtonType buttonTypeOne = new ButtonType("Komplett löschen");
					ButtonType buttonTypeTwo = new ButtonType("Alle zukünftigen Löschen");				
					ButtonType buttonTypeCancel = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);

					alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

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
			HBox.setMargin(buttonDelete, new Insets(0, 0, 0, 25));
			// don't allow "Übertrag" to be deleted
			if(item.getID() == -1)
			{
				buttonDelete.setVisible(false);
			}

			hbox.setPadding(new Insets(10));
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