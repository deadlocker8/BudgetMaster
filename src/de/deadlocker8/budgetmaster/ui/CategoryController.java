package de.deadlocker8.budgetmaster.ui;

import java.io.IOException;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.ui.cells.CategoryCell;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logger.LogLevel;
import logger.Logger;

public class CategoryController
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
				return new CategoryCell(thisController);
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

		FontIcon iconCategory = new FontIcon(FontIconType.PLUS);
		iconCategory.setSize(18);
		iconCategory.setStyle("-fx-text-fill: white");
		buttonCategory.setGraphic(iconCategory);

		//apply theme
		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");
		buttonCategory.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");

		//DEBUG
		listView.getItems().add(new Category("Auto", Color.RED));
		listView.getItems().add(new Category("Wohnung", Color.GREEN));
	}
	
	public void createNewCategory()
	{
		newCategory(false, null);
	}

	public void newCategory(boolean edit, Category category)
	{
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/NewCategoryGUI.fxml"));
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
			Logger.log(LogLevel.ERROR, Logger.exceptionToString(e));
		}
	}
}