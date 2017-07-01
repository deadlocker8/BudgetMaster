package de.deadlocker8.budgetmaster.ui;

import java.io.IOException;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.Budget;
import de.deadlocker8.budgetmaster.logic.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.FilterSettings;
import de.deadlocker8.budgetmaster.logic.Helpers;
import de.deadlocker8.budgetmaster.logic.NormalPayment;
import de.deadlocker8.budgetmaster.logic.Payment;
import de.deadlocker8.budgetmaster.logic.RepeatingPayment;
import de.deadlocker8.budgetmaster.logic.RepeatingPaymentEntry;
import de.deadlocker8.budgetmaster.logic.ServerConnection;
import de.deadlocker8.budgetmaster.ui.cells.PaymentCell;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logger.Logger;

public class ReportController implements Refreshable
{
	@FXML private AnchorPane anchorPaneMain;	
	@FXML private Label labelPayments;
	@FXML private Label labelFilterActive;
	@FXML private CheckBox checkBoxSplitTable;
	@FXML private CheckBox checkBoxIncludeCharts;
	@FXML private Button buttonFilter;
	@FXML private Button buttonGenerate;

	private Controller controller;

	public void init(Controller controller)
	{
		this.controller = controller;
		
		FontIcon iconFilter = new FontIcon(FontIconType.FILTER);
		iconFilter.setSize(18);
		iconFilter.setStyle("-fx-text-fill: white");
		buttonFilter.setGraphic(iconFilter);
		FontIcon iconPayment = new FontIcon(FontIconType.COGS);
		iconPayment.setSize(18);
		iconPayment.setStyle("-fx-text-fill: white");
		buttonGenerate.setGraphic(iconPayment);
		
		// apply theme
		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");		
		labelFilterActive.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));		
		buttonFilter.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		buttonGenerate.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");

		refresh();
	}
	
	public void filter()
	{
//		try
//		{
//			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/FilterGUI.fxml"));
//			Parent root = (Parent)fxmlLoader.load();
//			Stage newStage = new Stage();
//			newStage.initOwner(controller.getStage());
//			newStage.initModality(Modality.APPLICATION_MODAL);	
//			newStage.setTitle("Filter");
//			newStage.setScene(new Scene(root));
//			newStage.getIcons().add(controller.getIcon());
//			newStage.setResizable(false);
//			FilterController newController = fxmlLoader.getController();			
//			newController.init(newStage, controller, this, controller.getFilterSettings());
//			newStage.show();
//		}
//		catch(IOException e)
//		{
//			Logger.error(e);
//		}
	}
	
	public void generate()
	{
		
	}

	public Controller getController()
	{
		return controller;
	}

	@Override
	public void refresh()
	{		
//		if(controller.getFilterSettings().equals(new FilterSettings()))
//		{
//			labelFilterActive.setVisible(false);
//		}
//		else
//		{
//			labelFilterActive.setVisible(true);
//		}
	}
}