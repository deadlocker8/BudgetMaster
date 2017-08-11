package de.deadlocker8.budgetmaster.ui.controller;


import java.io.IOException;
import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.ui.Refreshable;
import de.deadlocker8.budgetmaster.ui.cells.CategoryCell;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logger.Logger;
import tools.ConvertTo;

public class CategoryController implements Refreshable
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Button buttonCategory;
	@FXML private ListView<Category> listView;

	private Controller controller;

	public void init(Controller controller)
	{
		this.controller = controller;
		
		CategoryController thisController = this;
		listView.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>()
		{
			@Override
			public ListCell<Category> call(ListView<Category> param)
			{
				CategoryCell cell = new  CategoryCell(thisController);
				cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent event)
					{
						if(event.getClickCount() == 2)
						{							
							// don't allow editing of category "none"
							if(cell.getItem().getID() != 1)
							{
								newCategory(true, cell.getItem());
							}
						}
					}
				});
				return cell;
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
		
		Label labelPlaceholder = new Label("Keine Kategorien verfügbar");
		labelPlaceholder.setStyle("-fx-font-size: 16");
		listView.setPlaceholder(labelPlaceholder);

		buttonCategory.setGraphic(Helpers.getFontIcon(FontIconType.PLUS, 18, Color.WHITE));

		//apply theme
		anchorPaneMain.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND));
		buttonCategory.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");

		refreshListView();
	}
	
	public void refreshListView()
	{		
		listView.getItems().clear();
		
		if(controller.getCategoryHandler() != null)
		{
			ArrayList<Category> categories = controller.getCategoryHandler().getCategories();	
			if(categories != null && categories.size() > 0)
			{				
				//remove category NONE (not editable)
				categories.remove(0);				
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
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/fxml/NewCategoryGUI.fxml"));
			Parent root = (Parent)fxmlLoader.load();
			Stage newStage = new Stage();
			newStage.initOwner(controller.getStage());
			newStage.initModality(Modality.APPLICATION_MODAL);			
			
			if(edit)
			{
				newStage.setTitle("Kategorie bearbeiten");
			}
			else
			{
				newStage.setTitle("Neue Kategorie");
			}
			
			newStage.setScene(new Scene(root));
			newStage.getIcons().add(controller.getIcon());
			newStage.setResizable(false);
			newStage.getScene().getStylesheets().add("/de/deadlocker8/budgetmaster/ui/style.css");
			NewCategoryController newController = fxmlLoader.getController();
			newController.init(newStage, controller, this, edit, category);
			newStage.show();
		}
		catch(IOException e)
		{
			Logger.error(e);
		}
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
}