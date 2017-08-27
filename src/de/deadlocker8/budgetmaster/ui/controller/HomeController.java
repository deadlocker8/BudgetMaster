package de.deadlocker8.budgetmaster.ui.controller;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.Budget;
import de.deadlocker8.budgetmaster.logic.CategoryBudget;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmaster.ui.Refreshable;
import de.deadlocker8.budgetmaster.ui.cells.CategoryBudgetCell;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import tools.ConvertTo;
import tools.Localization;

public class HomeController implements Refreshable
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Label labelBudget;
	@FXML private Label labelStartBudget;
	@FXML private ProgressBar progressBar;
	@FXML private ListView<CategoryBudget> listView;

	private Controller controller;

	public void init(Controller controller)
	{
		this.controller = controller;

		HomeController thisController = this;
		listView.setCellFactory(new Callback<ListView<CategoryBudget>, ListCell<CategoryBudget>>()
		{
			@Override
			public ListCell<CategoryBudget> call(ListView<CategoryBudget> param)
			{
				return new CategoryBudgetCell(thisController);
			}
		});
		
		Label labelPlaceholder = new Label(Localization.getString(Strings.HOME_PLACEHOLDER));          
        labelPlaceholder.setStyle("-fx-font-size: 16");
        listView.setPlaceholder(labelPlaceholder);

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
		
		anchorPaneMain.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND));
	}
	
	private void refreshListView()
	{		
		listView.getItems().clear();
	
		ArrayList<CategoryBudget> categoryBudgets = controller.getCategoryBudgets();
		if(categoryBudgets != null)
		{				
			listView.getItems().setAll(categoryBudgets);
		}		
	}
	
	private void refreshCounter()
	{
		if(controller.getPaymentHandler().getPayments() != null)
		{
			Budget budget = new Budget(controller.getPaymentHandler().getPayments());	
			double remaining = budget.getIncomeSum() + budget.getPaymentSum();
			String currency = "€";
			if(controller.getSettings() != null)
			{
				currency = controller.getSettings().getCurrency();
			}
			labelBudget.setText(Helpers.getCurrencyString(remaining, currency));
			if(remaining <= 0)
			{
				labelBudget.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT_RED));
			}
			else
			{
				labelBudget.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
			}
			labelStartBudget.setText(Localization.getString(Strings.HOME_BUDGET, Helpers.getCurrencyString(budget.getIncomeSum(), currency)));
			
			double factor = remaining / budget.getIncomeSum();
			if(factor < 0)
			{
				factor = 0;
			}
			progressBar.setProgress(factor);
		}
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
	}
}