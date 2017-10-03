package de.deadlocker8.budgetmasterclient.ui.controller;


import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.category.Category;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.Refreshable;
import de.deadlocker8.budgetmasterclient.ui.Styleable;
import de.deadlocker8.budgetmasterclient.ui.cells.CategoryCell;
import fontAwesome.FontIconType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import logger.Logger;
import tools.ConvertTo;
import tools.Localization;

public class CategoryController extends BaseController implements Refreshable, Styleable
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Button buttonCategory;
	@FXML private ListView<Category> listView;

	private Controller controller;

	public void init(Controller controller)
	{
		this.controller = controller;
		
		CategoryController thisController = this;
		listView.setCellFactory(param -> {
			CategoryCell cell = new  CategoryCell(thisController);
			cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				if(event.getClickCount() == 2)
				{							
					// don't allow editing of category "none"
					if(cell.getItem().getID() != 1)
					{
						newCategory(true, cell.getItem());
					}
				}
			});
			return cell;
		});

		listView.getSelectionModel().selectedIndexProperty().addListener((ChangeListener<Number>)(observable, oldValue, newValue) -> Platform.runLater(() -> listView.getSelectionModel().select(-1)));
		
		Label labelPlaceholder = new Label(Localization.getString(Strings.CATEGORIES_PLACEHOLDER));
		labelPlaceholder.setStyle("-fx-font-size: 16");
		listView.setPlaceholder(labelPlaceholder);

		applyStyle();
		
		refreshListView();
	}
	
	public void refreshListView()
	{		
		listView.getItems().clear();
		
		if(controller.getCategoryHandler() != null)
		{
			ArrayList<Category> categories = controller.getCategoryHandler().getCategoriesWithoutNone();	
			if(categories != null && categories.size() > 0)
			{				
				listView.getItems().setAll(categories);
			}	
		}
	}
	
	public void createNewCategory()
	{
		newCategory(false, null);
	}

	public void newCategory(boolean edit, Category category)
	{		
		new NewCategoryController(controller.getStage(), controller, this, edit, category);		
	}
	
	public void deleteCategory(int ID)
	{		
		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			connection.deleteCategory(ID);
			controller.refresh(controller.getFilterSettings());
		}
		catch(Exception e)
		{
			Logger.error(e);
			controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
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
	}

	@Override
	public void applyStyle()
	{
		buttonCategory.setGraphic(Helpers.getFontIcon(FontIconType.PLUS, 18, Color.WHITE));
		
		anchorPaneMain.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND));
		buttonCategory.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");		
	}
}