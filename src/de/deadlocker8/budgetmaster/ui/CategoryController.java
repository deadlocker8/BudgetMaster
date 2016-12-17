package de.deadlocker8.budgetmaster.ui;

import java.util.Locale;
import java.util.ResourceBundle;

import de.deadlocker8.budgetmaster.logic.Category;
import de.deadlocker8.budgetmaster.ui.cells.CategoryCell;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class CategoryController
{	
	@FXML private Label labelIncomes;
	@FXML private Label labelPayments;
	@FXML private ListView<Category> listView;

	private Controller controller;
	private Image icon = new Image("de/deadlocker8/budgetmaster/resources/icon.png");
	private final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);

	public void init(Controller controller)
	{
		this.controller = controller;

		listView.setFixedCellSize(60.0);
		listView.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>()
		{
			@Override
			public ListCell<Category> call(ListView<Category> param)
			{
				return new CategoryCell();
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

		// DEBUG
		listView.getItems().add(new Category("Auto", Color.RED));
		listView.getItems().add(new Category("Wohnung", Color.GREEN));	
	}

	public void newCategory()
	{
//		try
//		{
//			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/NewCategoryGUI.fxml"));
//			Parent root = (Parent)fxmlLoader.load();
//			Stage newStage = new Stage();
//			newStage.initOwner((controller.getStage()));
//			newStage.initModality(Modality.APPLICATION_MODAL);
//			newStage.setTitle("Neue Einnahme");
//			newStage.setScene(new Scene(root));
//			newStage.getIcons().add(icon);
//			newStage.setResizable(false);
//			NewIncomeController newController = fxmlLoader.getController();
//			newController.init(newStage, controller, this);
//			newStage.show();
//		}
//		catch(IOException e)
//		{
//			Logger.log(LogLevel.ERROR, Logger.exceptionToString(e));
//		}
	}
}