package de.deadlocker8.budgetmaster.ui;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.FilterSettings;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FilterController
{
	@FXML private CheckBox checkBoxIncome;
	@FXML private CheckBox checkBoxPayment;
	@FXML private CheckBox checkBoxNoRepeating;
	@FXML private CheckBox checkBoxMonthlyRepeating;
	@FXML private CheckBox checkBoxRepeatEveryXDays;
	@FXML private VBox vboxCategories;
	@FXML private TextField textFieldSearch;
	@FXML private Button buttonCancel;
	@FXML private Button buttonFilter;	

	private Stage stage;
	private Controller controller;
	private PaymentController paymentController;

	public void init(Stage stage, Controller controller, PaymentController paymentController)
	{
		this.stage = stage;
		this.controller = controller;
		this.paymentController = paymentController;

		FontIcon iconCancel = new FontIcon(FontIconType.TIMES);
		iconCancel.setSize(17);
		iconCancel.setStyle("-fx-text-fill: white");
		buttonCancel.setGraphic(iconCancel);
		FontIcon iconSave = new FontIcon(FontIconType.SAVE);
		iconSave.setSize(17);
		iconSave.setStyle("-fx-text-fill: white");
		buttonFilter.setGraphic(iconSave);
		
		buttonCancel.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonFilter.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");	
	}

	public void filter()
	{
		boolean isIncomeAllowed = checkBoxIncome.isSelected();
		boolean isPaymentAllowed = checkBoxPayment.isSelected();
		
		boolean isNoRepeatingAllowed = checkBoxNoRepeating.isSelected();
		boolean isMonthlyRepeatingAllowed = checkBoxNoRepeating.isSelected();
		boolean isRepeatingEveryXDaysAllowed = checkBoxNoRepeating.isSelected();
		
		ArrayList<Integer> allowedCategoryIDs = new ArrayList<>(); 
		for(Node node : vboxCategories.getChildren())
		{
			CheckBox currentCheckBox = (CheckBox)node;
			if(currentCheckBox.isSelected())				
			{
				allowedCategoryIDs.add((int)currentCheckBox.getUserData());
			}
		}
		
		String name = textFieldSearch.getText();
		if(name.equals(""))
		{
			name = null;
		}
		
		//TODO get new list from server first
		controller.getPaymentHandler().filter(new FilterSettings(isIncomeAllowed, 
																isPaymentAllowed, 
																isNoRepeatingAllowed,
																isMonthlyRepeatingAllowed, 
																isRepeatingEveryXDaysAllowed, 
																allowedCategoryIDs, 
																name));
		
		stage.close();
		paymentController.getController().refresh();
		
		//TODO button reset
		//TODO set userData for category checkboxes
	}

	public void cancel()
	{
		stage.close();
	}
}