package de.deadlocker8.budgetmaster.ui.controller;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.FilterSettings;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
	@FXML private Button buttonReset;
	@FXML private Button buttonFilter;
	@FXML private Button buttonCategoryAll;
	@FXML private Button buttonCategoryNone;

	private Stage stage;
	private Controller controller;
	private FilterSettings filterSetttings;

	public void init(Stage stage, Controller controller, FilterSettings filterSettings)
	{
		this.stage = stage;
		this.controller = controller;
		this.filterSetttings = filterSettings;

		buttonCancel.setGraphic(Helpers.getFontIcon(FontIconType.TIMES, 17, Color.WHITE));
		buttonReset.setGraphic(Helpers.getFontIcon(FontIconType.UNDO, 17, Color.WHITE));		
		buttonFilter.setGraphic(Helpers.getFontIcon(FontIconType.FILTER, 17, Color.WHITE));

		buttonCancel.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonReset.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonFilter.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonCategoryAll.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;");
		buttonCategoryNone.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;");

		for(Category currentCategory : controller.getCategoryHandler().getCategories())
		{
			CheckBox newCheckBox = new CheckBox();
			newCheckBox.setText(currentCategory.getName());
			newCheckBox.setUserData(currentCategory.getID());
			newCheckBox.setStyle("-fx-font-size: 14;");
			vboxCategories.getChildren().add(newCheckBox);
		}

		preselect();
	}

	private void preselect()
	{
		checkBoxIncome.setSelected(filterSetttings.isIncomeAllowed());
		checkBoxPayment.setSelected(filterSetttings.isPaymentAllowed());
		checkBoxNoRepeating.setSelected(filterSetttings.isNoRepeatingAllowed());
		checkBoxMonthlyRepeating.setSelected(filterSetttings.isMonthlyRepeatingAllowed());
		checkBoxRepeatEveryXDays.setSelected(filterSetttings.isRepeatingEveryXDaysAllowed());

		ArrayList<Integer> allowedCategoryIDs = filterSetttings.getAllowedCategoryIDs();

		for(Node node : vboxCategories.getChildren())
		{
			CheckBox currentCheckBox = (CheckBox)node;
			if(allowedCategoryIDs == null || allowedCategoryIDs.contains(currentCheckBox.getUserData()))
			{
				currentCheckBox.setSelected(true);
			}
		}

		textFieldSearch.setText(filterSetttings.getName());
	}

	public void filter()
	{
		boolean isIncomeAllowed = checkBoxIncome.isSelected();
		boolean isPaymentAllowed = checkBoxPayment.isSelected();

		boolean isNoRepeatingAllowed = checkBoxNoRepeating.isSelected();
		boolean isMonthlyRepeatingAllowed = checkBoxMonthlyRepeating.isSelected();
		boolean isRepeatingEveryXDaysAllowed = checkBoxRepeatEveryXDays.isSelected();

		ArrayList<Integer> allowedCategoryIDs = new ArrayList<>();
		for(Node node : vboxCategories.getChildren())
		{
			CheckBox currentCheckBox = (CheckBox)node;
			if(currentCheckBox.isSelected())
			{
				allowedCategoryIDs.add((int)currentCheckBox.getUserData());
			}
		}

		if(allowedCategoryIDs.size() == controller.getCategoryHandler().getCategories().size())
		{
			allowedCategoryIDs = null;
		}

		String name = textFieldSearch.getText();
		if(name != null && name.equals(""))
		{
			name = null;
		}

		FilterSettings newFilterSettings = new FilterSettings(isIncomeAllowed, isPaymentAllowed, isNoRepeatingAllowed, isMonthlyRepeatingAllowed, isRepeatingEveryXDaysAllowed, allowedCategoryIDs, name);
		controller.setFilterSettings(newFilterSettings);
		controller.refresh(newFilterSettings);		
		stage.close();
	}

	public void reset()
	{
		filterSetttings = new FilterSettings();
		preselect();
		controller.setFilterSettings(filterSetttings);
		controller.refresh(filterSetttings);
	}

	public void cancel()
	{
		stage.close();
	}	

	public void enableAllCategories()
	{
		for(Node node : vboxCategories.getChildren())
		{
			((CheckBox)node).setSelected(true);
		}
	}
	
	public void disableAllCategories()
	{
		for(Node node : vboxCategories.getChildren())
		{
			((CheckBox)node).setSelected(false);
		}
	}
}