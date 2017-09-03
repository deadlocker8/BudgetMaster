package de.deadlocker8.budgetmaster.ui.controller;

import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmaster.ui.Styleable;
import de.deadlocker8.budgetmaster.ui.cells.SearchCell;
import fontAwesome.FontIconType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logger.Logger;
import tools.ConvertTo;
import tools.Localization;

public class SearchController extends BaseController implements Styleable
{
	@FXML private CheckBox checkBoxName;
	@FXML private CheckBox checkBoxDescription;
	@FXML private CheckBox checkBoxCategoryName;
	@FXML private TextField textFieldSearch;
	@FXML private Button buttonCancel;
	@FXML private Button buttonSearch;
	@FXML private ListView<Payment> listView;

	private Stage parentStage;
	private Controller controller;
	
	public SearchController(Stage parentStage, Controller controller)
	{
		this.parentStage = parentStage;
		this.controller = controller;
		load("/de/deadlocker8/budgetmaster/ui/fxml/SearchGUI.fxml", Localization.getBundle());
		getStage().showAndWait();
	}	
	
	@Override
	public void initStage(Stage stage)
	{		
		stage.initOwner(parentStage);
		stage.initModality(Modality.APPLICATION_MODAL);	
		stage.setTitle(Localization.getString(Strings.TITLE_SEARCH));
		stage.getIcons().add(controller.getIcon());
		stage.setResizable(false);		
	}

	@Override
	public void init()
	{
		SearchController thisController = this;
		listView.setCellFactory(new Callback<ListView<Payment>, ListCell<Payment>>()
		{
			@Override
			public ListCell<Payment> call(ListView<Payment> param)
			{
				SearchCell cell = new SearchCell(thisController);
				cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
					if(event.getClickCount() == 2)
					{						
						//TODO open new/edit payment ui
					}
				});
				cell.prefWidthProperty().bind(listView.widthProperty().subtract(2));
				return cell;
			}
		});
		
		Label labelPlaceholder = new Label(Localization.getString(Strings.PAYMENTS_PLACEHOLDER));      
        labelPlaceholder.setStyle("-fx-font-size: 16");
        listView.setPlaceholder(labelPlaceholder);

		listView.getSelectionModel().selectedIndexProperty().addListener((ChangeListener<Number>)(observable, oldValue, newValue) -> Platform.runLater(() -> listView.getSelectionModel().select(-1)));

		applyStyle();	
	}
	
	public void search()
	{
		String query = textFieldSearch.getText().trim();		
		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			//DEBUG
			System.out.println(connection.getPaymentForSearch(query, 
																checkBoxName.isSelected(), 
																checkBoxDescription.isSelected(), 
																checkBoxCategoryName.isSelected()));
		}
		catch(Exception e)
		{
			//ERRORHANDLING
			Logger.error(e);
		}
		
	}
	
	public void cancel()
	{
		getStage().close();
	}
	
	public Controller getController()
	{
		return controller;
	}

	@Override
	public void applyStyle()
	{
		buttonCancel.setGraphic(Helpers.getFontIcon(FontIconType.TIMES, 17, Color.WHITE));	
		buttonSearch.setGraphic(Helpers.getFontIcon(FontIconType.SEARCH, 17, Color.WHITE));

		buttonCancel.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonSearch.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
	}
}