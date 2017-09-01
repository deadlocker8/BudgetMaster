package de.deadlocker8.budgetmaster.ui.controller;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.Budget;
import de.deadlocker8.budgetmaster.logic.FilterSettings;
import de.deadlocker8.budgetmaster.logic.NormalPayment;
import de.deadlocker8.budgetmaster.logic.Payment;
import de.deadlocker8.budgetmaster.logic.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.RepeatingPaymentEntry;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmaster.ui.Refreshable;
import de.deadlocker8.budgetmaster.ui.Styleable;
import de.deadlocker8.budgetmaster.ui.cells.PaymentCell;
import fontAwesome.FontIconType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import logger.Logger;
import tools.ConvertTo;
import tools.Localization;

public class PaymentController implements Refreshable, Styleable
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Label labelIncome;
	@FXML private Label labelIncomes;
	@FXML private Label labelPayment;
	@FXML private Label labelPayments;
	@FXML private Label labelFilterActive;
	@FXML private ListView<Payment> listView;
	@FXML private Button buttonNewIncome;
	@FXML private Button buttonFilter;
	@FXML private Button buttonNewPayment;

	private Controller controller;

	public void init(Controller controller)
	{
		this.controller = controller;

		PaymentController thisController = this;
		listView.setCellFactory(new Callback<ListView<Payment>, ListCell<Payment>>()
		{
			@Override
			public ListCell<Payment> call(ListView<Payment> param)
			{
				PaymentCell cell = new PaymentCell(thisController);
				cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
					if(event.getClickCount() == 2)
					{						
						// don't allow editing of payment "rest"
						if(cell.getItem().getCategoryID() != 2)
						{
							payment(!cell.getItem().isIncome(), true, cell.getItem());
						}
					}
				});
				cell.prefWidthProperty().bind(listView.widthProperty().subtract(2));
				return cell;
			}
		});
		
		Label labelPlaceholder = new Label(Localization.getString(Strings.PAYMENTS_PLACEHOLDER));      
        labelPlaceholder.setStyle("-fx-font-size: 16");
        listView.setPlaceholder(labelPlaceholder);

		listView.getSelectionModel().selectedIndexProperty().addListener((ChangeListener<Number>)(observable, oldValue, newValue) -> Platform.runLater(() -> listView.getSelectionModel().select(-1)));

		applyStyle();
	}

	public void newIncome()
	{
		payment(false, false, null);
	}

	public void newPayment()
	{
		payment(true, false, null);
	}

	public void payment(boolean isPayment, boolean edit, Payment payment)
	{		
		new NewPaymentController(controller.getStage(), controller, this, isPayment, edit, payment);		
	}

	private void refreshListView()
	{
		listView.getItems().clear();

		ArrayList<Payment> payments = controller.getPaymentHandler().getPayments();
		if(payments != null)
		{
			listView.getItems().setAll(payments);
		}
	}

	private void refreshCounter()
	{
		Budget budget = new Budget(listView.getItems());
		String currency = "€";
		if(controller.getSettings() != null)
		{
			currency = controller.getSettings().getCurrency();
		}
		labelIncomes.setText(Helpers.getCurrencyString(budget.getIncomeSum(), currency));
		labelPayments.setText(Helpers.getCurrencyString(budget.getPaymentSum(), currency));
	}

	public void deleteNormalPayment(NormalPayment payment)
	{
		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			connection.deleteNormalPayment(payment);
			controller.refresh(controller.getFilterSettings());
		}
		catch(Exception e)
		{
			Logger.error(e);
			controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
		}
	}

	public void deleteRepeatingPayment(RepeatingPaymentEntry payment)
	{
		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			connection.deleteRepeatingPayment(payment);
			controller.refresh(controller.getFilterSettings());
		}
		catch(Exception e)
		{
			Logger.error(e);
			controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
		}
	}

	public void deleteFuturePayments(RepeatingPaymentEntry payment)
	{
		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			RepeatingPayment oldRepeatingPayment = connection.getRepeatingPayment(payment.getRepeatingPaymentID());
			RepeatingPayment newRepeatingPayment = new RepeatingPayment(payment.getID(), payment.getAmount(), oldRepeatingPayment.getDate(), payment.getCategoryID(), payment.getName(), payment.getDescription(), payment.getRepeatInterval(), payment.getDate(), payment.getRepeatMonthDay());
			connection.deleteRepeatingPayment(payment);
			connection.addRepeatingPayment(newRepeatingPayment);

			controller.refresh(controller.getFilterSettings());
		}
		catch(Exception e)
		{
			Logger.error(e);
			controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
		}
	}
	
	public void filter()
	{			
		new FilterController(controller.getStage(), controller, controller.getFilterSettings());	
	}

	public Controller getController()
	{
		return controller;
	}

	@Override
	public void refresh()
	{
		refreshListView();
		refreshCounter();
		
		if(controller.getFilterSettings().equals(new FilterSettings()))
		{
			labelFilterActive.setVisible(false);
		}
		else
		{
			labelFilterActive.setVisible(true);
		}
	}

	@Override
	public void applyStyle()
	{
		buttonNewIncome.setGraphic(Helpers.getFontIcon(FontIconType.DOWNLOAD, 18, Color.WHITE));
		buttonFilter.setGraphic(Helpers.getFontIcon(FontIconType.FILTER, 18, Color.WHITE));
		buttonNewPayment.setGraphic(Helpers.getFontIcon(FontIconType.UPLOAD, 18, Color.WHITE));
		labelFilterActive.setGraphic(Helpers.getFontIcon(FontIconType.WARNING, 13, Colors.TEXT));

		anchorPaneMain.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND));
		labelIncome.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelIncomes.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelPayment.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelPayments.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		labelFilterActive.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		buttonNewIncome.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		buttonFilter.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		buttonNewPayment.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
	}
}