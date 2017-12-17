package de.deadlocker8.budgetmasterclient.ui.controller;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.FilterSettings;
import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerTagConnection;
import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.Styleable;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logger.Logger;
import tools.ConvertTo;
import tools.Localization;

public class FilterController extends BaseController implements Styleable
{
	@FXML private ScrollPane scrollPane;
	@FXML private VBox vboxMain;
	@FXML private CheckBox checkBoxIncome;
	@FXML private CheckBox checkBoxPayment;
	@FXML private CheckBox checkBoxNoRepeating;
	@FXML private CheckBox checkBoxMonthlyRepeating;
	@FXML private CheckBox checkBoxRepeatEveryXDays;
	@FXML private VBox vboxCategories;
	@FXML private TextField textFieldSearch;
	@FXML private VBox vboxTags;
	@FXML private Button buttonCancel;
	@FXML private Button buttonReset;
	@FXML private Button buttonFilter;
	@FXML private Button buttonCategoryAll;
	@FXML private Button buttonCategoryNone;
	@FXML private Button buttonTagsAll;
	@FXML private Button buttonTagsNone;
	@FXML private Label labelSeparator;
	@FXML private Label labelSeparatorHorizontalLeft;
	@FXML private Label labelSeparatorHorizontalRight;

	private Stage parentStage;
	private Controller controller;
	private FilterSettings filterSetttings;
	private ArrayList<Tag> allTags;
	
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
		stage.setResizable(true);
		stage.setMinHeight(600);
		stage.setMinWidth(475);
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
		
		try
		{
			ServerTagConnection connection = new ServerTagConnection(controller.getSettings());
			allTags = connection.getTags();
			for(Tag currentTag : allTags)
			{
				CheckBox newCheckBox = new CheckBox();
				newCheckBox.setText(currentTag.getName());
				newCheckBox.setUserData(currentTag.getID());
				newCheckBox.setStyle("-fx-font-size: 14;");
				vboxTags.getChildren().add(newCheckBox);
			}
		}
		catch(Exception e)
		{
			Logger.error(e);
			controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
			return;
		}
	
		textFieldSearch.setOnKeyPressed((event)->{
            if(event.getCode().equals(KeyCode.ENTER))
            {
            	filter();
            }
	    });
		
		vboxMain.prefWidthProperty().bind(scrollPane.widthProperty().subtract(5));
		vboxMain.prefHeightProperty().bind(scrollPane.heightProperty().subtract(5));
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
		
		ArrayList<Integer> allowedTagIDs = filterSetttings.getAllowedTagIDs();
		for(Node node : vboxTags.getChildren())
		{
			CheckBox currentCheckBox = (CheckBox)node;
			if(allowedTagIDs == null || allowedTagIDs.contains(currentCheckBox.getUserData()))
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
		
		ArrayList<Integer> allowedTagIDs = new ArrayList<>();
		for(Node node : vboxTags.getChildren())
		{
			CheckBox currentCheckBox = (CheckBox)node;
			if(currentCheckBox.isSelected())
			{
				allowedTagIDs.add((int)currentCheckBox.getUserData());
			}
		}

		if(allowedTagIDs.size() == allTags.size())
		{
			allowedTagIDs = null;
		}

		FilterSettings newFilterSettings = new FilterSettings(isIncomeAllowed, isPaymentAllowed, isNoRepeatingAllowed, isMonthlyRepeatingAllowed, isRepeatingEveryXDaysAllowed, allowedCategoryIDs, allowedTagIDs, name);
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
	
	public void enableAllTags()
	{
		for(Node node : vboxTags.getChildren())
		{
			((CheckBox)node).setSelected(true);
		}
	}
	
	public void disableAllTags()
	{
		for(Node node : vboxTags.getChildren())
		{
			((CheckBox)node).setSelected(false);
		}
	}

	@Override
	public void applyStyle()
	{
		buttonCancel.setGraphic(new FontIcon(FontIconType.TIMES, 17, Color.WHITE));
		buttonReset.setGraphic(new FontIcon(FontIconType.UNDO, 17, Color.WHITE));		
		buttonFilter.setGraphic(new FontIcon(FontIconType.FILTER, 17, Color.WHITE));		

		scrollPane.setStyle("-fx-background-color: transparent");
		
		labelSeparator.setStyle("-fx-background-color: #CCCCCC;");
		labelSeparator.setMinWidth(1);
		labelSeparator.setMaxWidth(1);
		
		labelSeparatorHorizontalLeft.setStyle("-fx-background-color: #CCCCCC;");
		labelSeparatorHorizontalLeft.setMinHeight(1);
		labelSeparatorHorizontalLeft.setMaxHeight(1);
		
		labelSeparatorHorizontalRight.setStyle("-fx-background-color: #CCCCCC;");
		labelSeparatorHorizontalRight.setMinHeight(1);
		labelSeparatorHorizontalRight.setMaxHeight(1);
		
		buttonCancel.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonReset.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonFilter.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonCategoryAll.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;");
		buttonCategoryNone.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;");
		buttonTagsAll.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;");
		buttonTagsNone.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;");
	}
}