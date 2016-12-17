package de.deadlocker8.budgetmaster.ui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.Payment;
import de.deadlocker8.budgetmaster.logic.RepeatingType;
import de.deadlocker8.budgetmaster.ui.cells.PaymentCell;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logger.LogLevel;
import logger.Logger;

public class PaymentController
{	
	@FXML private Label labelIncomes;
	@FXML private Label labelPayments;
	@FXML private ListView<Payment> listView;

	private Controller controller;
	private Image icon = new Image("de/deadlocker8/budgetmaster/resources/icon.png");
	private final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);

	public void init(Controller controller)
	{
		this.controller = controller;

		listView.setFixedCellSize(60.0);
		listView.setCellFactory(new Callback<ListView<Payment>, ListCell<Payment>>()
		{
			@Override
			public ListCell<Payment> call(ListView<Payment> param)
			{
				return new PaymentCell();
			}
		});

		listView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				Platform.runLater(new Runnable()
				{
					public void run()
					{
						listView.getSelectionModel().select(-1);
					}
				});
			}
		});

		// DEBUG
		listView.getItems().add(new Payment(false, 50.23, LocalDate.now(), new Category("Auto", Color.RED), "Tanken", RepeatingType.NONE, 0));
		listView.getItems().add(new Payment(true, 50.23, LocalDate.now(), new Category("Einkommen", Color.YELLOW), "Einkommen", RepeatingType.NONE, 0));	
	}	
	
	public void newIncome()
	{
		payment(false);
	}
	
	public void newPayment()
	{
		payment(true);
	}

	public void payment(boolean payment)
	{
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/NewPaymentGUI.fxml"));
			Parent root = (Parent)fxmlLoader.load();
			Stage newStage = new Stage();
			newStage.initOwner(controller.getStage());
			newStage.initModality(Modality.APPLICATION_MODAL);
			if(payment)
			{
				newStage.setTitle("Neue Ausgabe");
			}
			else
			{
				newStage.setTitle("Neue Einnahme");
			}
			newStage.setScene(new Scene(root));
			newStage.getIcons().add(icon);
			newStage.setResizable(false);
			NewPaymentController newController = fxmlLoader.getController();
			newController.init(newStage, controller, this, payment);
			newStage.show();
		}
		catch(IOException e)
		{
			Logger.log(LogLevel.ERROR, Logger.exceptionToString(e));
		}
	}
}