package de.deadlocker8.budgetmaster.ui.controller;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.FilterSettings;
import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmaster.ui.Styleable;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tools.ConvertTo;
import tools.Localization;

public class FilterController extends BaseController implements Styleable
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

	private Stage parentStage;
	private Controller controller;
	private FilterSettings filterSetttings;
	
	public FilterController(Stage parentStage, Controller controller, FilterSettings filterSettings)
	{
		this.parentStage = parentStage;
		this.controller = controller;
		this.filterSetttings = filterSettings;
		load("/de/deadlocker8/budgetmaster/ui/fxml/FilterGUI.fxml", Localization.getBundle());
		getStage().showAndWait();
	}	
	
	@Override
	public void initStage(Stage stage)
	{		
		stage.initOwner(parentStage);
		stage.initModality(Modality.APPLICATION_MODAL);	
		stage.setTitle(Localization.getString(Strings.TITLE_FILTER));
		stage.getIcons().add(controller.getIcon());
		stage.setResizable(false);		
	}

	@Override
	public void init()
	{
		applyStyle();

		for(Category currentCategory : controller.getCategoryHandler().getCategories())
		{
			CheckBox newCheckBox = new CheckBox();
			newCheckBox.setText(currentCategory.getName());
			newCheckBox.setUserData(currentCategory.getID());
			newCheckBox.setStyle("-fx-font-size: 14;");
			vboxCategories.getChildren().add(newCheckBox);
		}
		
		textFieldSearch.setOnKeyPressed((event)->{
            if(event.getCode().equals(KeyCode.ENTER))
            {
            	filter();
            }
	    });

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
		getStage().close();
	}

	public void reset()
	{
		filterSetttings = new FilterSettings();	
		getStage().close();
		controller.setFilterSettings(filterSetttings);
		controller.refresh(filterSetttings);
	}

	public void cancel()
	{
		getStage().close();
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

	@Override
	public void applyStyle()
	{
		buttonCancel.setGraphic(Helpers.getFontIcon(FontIconType.TIMES, 17, Color.WHITE));
		buttonReset.setGraphic(Helpers.getFontIcon(FontIconType.UNDO, 17, Color.WHITE));		
		buttonFilter.setGraphic(Helpers.getFontIcon(FontIconType.FILTER, 17, Color.WHITE));

		buttonCancel.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonReset.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonFilter.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonCategoryAll.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;");
		buttonCategoryNone.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;");
	}
}