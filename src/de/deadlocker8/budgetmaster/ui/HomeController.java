package de.deadlocker8.budgetmaster.ui;

import java.util.Locale;
import java.util.ResourceBundle;

import de.deadlocker8.budgetmaster.logic.CategoryBudget;
import de.deadlocker8.budgetmaster.ui.cells.CategoryBudgetCell;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class HomeController
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Label labelBudget;
	@FXML private Label labelStartBudget;
	@FXML private ProgressBar progressBar;
	@FXML private ListView<CategoryBudget> listView;	

	private Controller controller;
	private Image icon = new Image("de/deadlocker8/budgetmaster/resources/icon.png");
	private final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);

	public void init(Controller controller)
	{
		this.controller = controller;
		listView.setCellFactory(new Callback<ListView<CategoryBudget>, ListCell<CategoryBudget>>()
		{
			@Override
			public ListCell<CategoryBudget> call(ListView<CategoryBudget> param)
			{
				return new CategoryBudgetCell();
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
		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");
		// DEBUG
		listView.getItems().add(new CategoryBudget("Auto", Color.RED, 79.56));
		listView.getItems().add(new CategoryBudget("Wohnung", Color.GREEN, 245.));
	}
}