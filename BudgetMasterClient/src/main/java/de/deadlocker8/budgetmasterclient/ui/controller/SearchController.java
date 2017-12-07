package de.deadlocker8.budgetmasterclient.ui.controller;

import java.util.ArrayList;

import org.controlsfx.control.RangeSlider;

import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmaster.logic.search.SearchPreferences;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.Styleable;
import de.deadlocker8.budgetmasterclient.ui.cells.SearchCell;
import de.deadlocker8.budgetmasterclient.utils.LoadingModal;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
	@FXML private CheckBox checkBoxTags;
	@FXML private TextField textFieldSearch;
	@FXML private CheckBox checkBoxSearchByAmount;
	@FXML private TextField textFieldAmountMin;
	@FXML private TextField textFieldAmountMax;
	@FXML private HBox hboxRangeSlider;
	@FXML private Label labelSeparator;
	@FXML private HBox hboxSearchByAmount;
	@FXML private Button buttonCancel;
	@FXML private Button buttonSearch;
	@FXML private ListView<Payment> listView;

	private Stage parentStage;
	private Controller controller;
	private RangeSlider rangeSlider;
	
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
							search();
						}
					}
				});
				cell.prefWidthProperty().bind(listView.widthProperty().subtract(4));
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
		
		checkBoxSearchByAmount.selectedProperty().addListener((a, b, c)->{
			hboxSearchByAmount.setDisable(!c);
		});
		
		hboxSearchByAmount.setDisable(true);		

		int maximum;
		try
		{
			maximum = getMaxAmountFromServer();
		}
		catch(Exception e)
		{			
			Logger.error(e);
			controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
			return;
		}
		
		rangeSlider = new RangeSlider();
		rangeSlider.setMin(0);
		rangeSlider.setMax(maximum);
		rangeSlider.setLowValue(rangeSlider.getMin());
		rangeSlider.setHighValue(rangeSlider.getMax());		
		rangeSlider.setShowTickMarks(true);
		rangeSlider.setShowTickLabels(true);
		rangeSlider.setMajorTickUnit(getMayorTickUnit(maximum));
		rangeSlider.setMinorTickCount(0);
		rangeSlider.lowValueProperty().addListener((a, b, c)->{
			textFieldAmountMin.setText(String.valueOf(c.intValue()));
		});
		rangeSlider.highValueProperty().addListener((a, b, c)->{
			textFieldAmountMax.setText(String.valueOf(c.intValue()));
		});
		hboxRangeSlider.getChildren().add(rangeSlider);
		hboxRangeSlider.setAlignment(Pos.CENTER);
		HBox.setHgrow(rangeSlider, Priority.ALWAYS);
		
		textFieldAmountMin.setTextFormatter(new TextFormatter<>(c -> {
			if(c.getControlNewText().isEmpty() || c.getControlNewText().matches("[0-9]*"))
			{
				return c;
			}
			else
			{
				return null;
			}
		}));
		
		textFieldAmountMax.setTextFormatter(new TextFormatter<>(c -> {
			if(c.getControlNewText().isEmpty() || c.getControlNewText().matches("[0-9]*"))
			{
				return c;
			}
			else
			{
				return null;
			}
		}));	
		
		textFieldAmountMin.textProperty().addListener((a, b, c)->{
			setRangeSliderAmountMin();
		});
		
		textFieldAmountMax.textProperty().addListener((a, b, c)->{
			setRangeSliderAmountMax();
		});
		
		textFieldAmountMin.setText("0");
		textFieldAmountMax.setText(String.valueOf(maximum));
		
		//prefill
		SearchPreferences searchPreferences = controller.getSearchPreferences();
		if(controller.getSearchPreferences() != null)
		{
			textFieldSearch.setText(searchPreferences.getLastQuery());
			checkBoxName.setSelected(searchPreferences.isSearchName());
			checkBoxDescription.setSelected(searchPreferences.isSearchDescription());
			checkBoxCategoryName.setSelected(searchPreferences.isSearchCategorNames());
			checkBoxTags.setSelected(searchPreferences.isSearchTags());
			checkBoxSearchByAmount.setSelected(searchPreferences.isSearchAmount());
			rangeSlider.setLowValue(searchPreferences.getMinAmount());
			rangeSlider.setHighValue(searchPreferences.getMaxAmount());
		}
		
		applyStyle();	
	}
	
	private void setRangeSliderAmountMin()
	{
		String text = textFieldAmountMin.getText();
		if(text != null && !text.equals(""))
		{
			rangeSlider.setLowValue(Integer.parseInt(text));
		}
	}
	
	private void setRangeSliderAmountMax()
	{
		String text = textFieldAmountMax.getText();
		if(text != null && !text.equals(""))
		{
			rangeSlider.setHighValue(Integer.parseInt(text));
		}
	}
	
	private int getMaxAmountFromServer() throws Exception
	{						
		ServerConnection connection = new ServerConnection(controller.getSettings());			
		return connection.getMaxAmount();		
	}

	private int getMayorTickUnit(int maximum)
	{
		if(maximum < 10)
			return 1;
	
		if(maximum < 100)
			return 5;
		
		int length = String.valueOf(maximum).length();
		return (int)Math.pow(10, length-2);
	}
	
	public void search()
	{
		String query = textFieldSearch.getText().trim();
		if(controller.getSearchPreferences() == null)
		{
			controller.setSearchPreferences(new SearchPreferences());
		}
		SearchPreferences searchPreferences = controller.getSearchPreferences();
		searchPreferences.setLastQuery(query);
		searchPreferences.setSearchName(checkBoxName.isSelected());
		searchPreferences.setSearchDescription(checkBoxDescription.isSelected());
		searchPreferences.setSearchCategorNames(checkBoxCategoryName.isSelected());
		searchPreferences.setSearchTags(checkBoxTags.isSelected());
		searchPreferences.setSearchAmount(checkBoxSearchByAmount.isSelected());
		searchPreferences.setMinAmount((int)rangeSlider.getLowValue());
		searchPreferences.setMaxAmount((int)rangeSlider.getHighValue());
		
		LoadingModal.showModal(controller, Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_SEARCH), getStage(), controller.getIcon());
		
		Worker.runLater(() -> {
			try 
			{				
				ServerConnection connection = new ServerConnection(controller.getSettings());
				ArrayList<Payment> payments = connection.getPaymentsForSearch(query, 
																			checkBoxName.isSelected(), 
																			checkBoxDescription.isSelected(), 
																			checkBoxCategoryName.isSelected(),
																			checkBoxTags.isSelected(),
																			checkBoxSearchByAmount.isSelected(),
																			(int)rangeSlider.getLowValue()*100,
																			(int)rangeSlider.getHighValue()*100);
				
				Platform.runLater(() -> {
					listView.getItems().clear();
					if(payments != null)
					{
						listView.getItems().setAll(payments);
					}
				
					LoadingModal.closeModal();							
				});
			}
			catch(Exception e)
			{
				Logger.error(e);
				Platform.runLater(() -> {
					LoadingModal.closeModal();
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
		labelSeparator.setStyle("-fx-background-color: #CCCCCC;");
		labelSeparator.setMinHeight(1);
		labelSeparator.setMaxHeight(1);
		
		buttonCancel.setGraphic(new FontIcon(FontIconType.TIMES, 17, Color.WHITE));	
		buttonSearch.setGraphic(new FontIcon(FontIconType.SEARCH, 17, Color.WHITE));

		buttonCancel.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
		buttonSearch.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;");
	}
}