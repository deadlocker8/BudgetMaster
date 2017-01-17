package de.deadlocker8.budgetmaster.ui;

import java.io.IOException;
import java.time.LocalDate;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.Payment;
import de.deadlocker8.budgetmaster.logic.RepeatingType;
import de.deadlocker8.budgetmaster.ui.cells.PaymentCell;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logger.LogLevel;
import logger.Logger;

public class PaymentController
{	
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Label labelIncome;
	@FXML private Label labelIncomes;
	@FXML private Label labelPayment;
	@FXML private Label labelPayments;
	@FXML private ListView<Payment> listView;
	@FXML private Button buttonNewIncome;
	@FXML private Button buttonNewPayment;

	private Controller controller;	

	public void init(Controller controller)
	{
		this.controller = controller;
		
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
		
		FontIcon iconIncome = new FontIcon(FontIconType.DOWNLOAD);
		iconIncome.setSize(18);
		iconIncome.setStyle("-fx-text-fill: white");
		buttonNewIncome.setGraphic(iconIncome);
		FontIcon iconPayment = new FontIcon(FontIconType.UPLOAD);
		iconPayment.setSize(18);
		iconPayment.setStyle("-fx-text-fill: white");
		buttonNewPayment.setGraphic(iconPayment);
		
		//apply theme
		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");		
		labelIncome.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		labelIncomes.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		labelPayment.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		labelPayments.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		buttonNewIncome.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		buttonNewPayment.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");

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
			newStage.getIcons().add(controller.getIcon());
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