package de.deadlocker8.budgetmaster.ui.controller;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logger.Logger;
import tools.ConvertTo;
import tools.Localization;
import tools.Worker;

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
		stage.setResizable(true);
		stage.setMinWidth(500);
		stage.setMinHeight(500);
		stage.setWidth(650);
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
						// don't allow editing of payment "rest"
						if(cell.getItem().getCategoryID() != 2)
						{
							controller.getPaymentController().payment(!cell.getItem().isIncome(), true, cell.getItem());
						}
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
		
		checkBoxName.setSelected(true);
		
		textFieldSearch.setOnKeyPressed((event)->{
            if(event.getCode().equals(KeyCode.ENTER))
            {
            	search();
            }	        
	    });
		
		if(controller.getLastSearchQuery() != null)
		{
			textFieldSearch.setText(controller.getLastSearchQuery());
		}
		
		applyStyle();	
	}
	
	public void search()
	{
		String query = textFieldSearch.getText().trim();
		// only perform search if query differs from last query (reduce server connections)
		if(controller.getLastSearchQuery() != null && controller.getLastSearchQuery().equalsIgnoreCase(query))
		{
			textFieldSearch.requestFocus();
			textFieldSearch.positionCaret(textFieldSearch.getText().length());
			return;
		}
		
		controller.setLastSearchQuery(query);
		
		Stage modalStage = Helpers.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_SEARCH), getStage(), controller.getIcon());
		
		Worker.runLater(() -> {
			try 
			{
				
				ServerConnection connection = new ServerConnection(controller.getSettings());
				ArrayList<Payment> payments = connection.getPaymentForSearch(query, 
																			checkBoxName.isSelected(), 
																			checkBoxDescription.isSelected(), 
																			checkBoxCategoryName.isSelected());
				Platform.runLater(() -> {
					listView.getItems().clear();
					if(payments != null)
					{
						listView.getItems().setAll(payments);
					}
				
					if(modalStage != null)
					{
						modalStage.close();
					}							
				});
			}
			catch(Exception e)
			{
				Logger.error(e);
				Platform.runLater(() -> {
					if(modalStage != null)
					{
						modalStage.close();
					}
					controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
				});
			}
		});
		
		textFieldSearch.requestFocus();
		textFieldSearch.positionCaret(textFieldSearch.getText().length());
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