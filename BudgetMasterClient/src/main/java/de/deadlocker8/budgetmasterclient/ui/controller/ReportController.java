package de.deadlocker8.budgetmasterclient.ui.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.joda.time.DateTime;

import de.deadlocker8.budgetmaster.logic.Budget;
import de.deadlocker8.budgetmaster.logic.FilterSettings;
import de.deadlocker8.budgetmaster.logic.comparators.DateComparator;
import de.deadlocker8.budgetmaster.logic.comparators.RatingComparator;
import de.deadlocker8.budgetmaster.logic.payment.Payment;
import de.deadlocker8.budgetmaster.logic.payment.RepeatingPaymentEntry;
import de.deadlocker8.budgetmaster.logic.report.ColumnFilter;
import de.deadlocker8.budgetmaster.logic.report.ColumnOrder;
import de.deadlocker8.budgetmaster.logic.report.ColumnType;
import de.deadlocker8.budgetmaster.logic.report.ReportGenerator;
import de.deadlocker8.budgetmaster.logic.report.ReportItem;
import de.deadlocker8.budgetmaster.logic.report.ReportPreferences;
import de.deadlocker8.budgetmaster.logic.report.ReportSorting;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.tag.TagHandler;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.Refreshable;
import de.deadlocker8.budgetmasterclient.ui.Styleable;
import de.deadlocker8.budgetmasterclient.ui.cells.report.table.ReportTableRatingCell;
import de.deadlocker8.budgetmasterclient.ui.cells.report.table.ReportTableRepeatingCell;
import de.deadlocker8.budgetmasterclient.utils.UIHelpers;
import fontAwesome.FontIconType;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logger.Logger;
import tools.AlertGenerator;
import tools.ConvertTo;
import tools.Localization;
import tools.ObjectJSONHandler;
import tools.Worker;

public class ReportController implements Refreshable, Styleable
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Label labelPayments;
	@FXML private Label labelFilterActive;
	@FXML private CheckBox checkBoxIncludeBudget;
	@FXML private CheckBox checkBoxSplitTable;
	@FXML private CheckBox checkBoxIncludeCategoryBudgets;
	@FXML private Button buttonFilter;
	@FXML private Button buttonGenerate;
	@FXML private TableView<ReportItem> tableView;

	private Controller controller;
	private ColumnFilter columnFilter;
	private String initialReportFileName;
	private ReportPreferences reportPreferences;
	
	private TableColumn<ReportItem, Integer> columnPosition;
	private TableColumn<ReportItem, String> columnDate;
	private TableColumn<ReportItem, Boolean> columnIsRepeating;
	private TableColumn<ReportItem, String> columnCategory;
	private TableColumn<ReportItem, Integer> columnName;
	private TableColumn<ReportItem, String> columnDescription;
	private TableColumn<ReportItem, String> columnTags;
	private TableColumn<ReportItem, Integer> columnRating;
	private TableColumn<ReportItem, String> columnAmount;

	public void init(Controller controller)
	{
		this.controller = controller;
		initTable();
		applyStyle();			
		applyReportPreferences();
	}
	
	private void initColumn(ColumnType columnType, boolean activated, SortType sortType)
	{
		switch(columnType)
		{
			case AMOUNT:
				initColumnAmount(activated);
				toggleColumn(columnAmount, activated);
				if(sortType != null)
				{
					columnAmount.setSortType(sortType);
					tableView.getSortOrder().add(columnAmount);
				}
				break;
			case CATEGORY:
				initColumnCategory(activated);
				toggleColumn(columnCategory, activated);
				if(sortType != null)
				{
					columnCategory.setSortType(sortType);
					tableView.getSortOrder().add(columnCategory);
				}
				break;
			case DATE:
				initColumnDate(activated);
				toggleColumn(columnDate, activated);
				if(sortType != null)
				{
					columnDate.setSortType(sortType);
					tableView.getSortOrder().add(columnDate);
				}
				break;
			case DESCRIPTION:
				initColumnDescription(activated);
				toggleColumn(columnDescription, activated);
				if(sortType != null)
				{
					columnDescription.setSortType(sortType);
					tableView.getSortOrder().add(columnDescription);
				}
				break;
			case NAME:
				initColumnName(activated);
				toggleColumn(columnName, activated);
				if(sortType != null)
				{
					columnName.setSortType(sortType);
					tableView.getSortOrder().add(columnName);
				}
				break;
			case POSITION:
				initColumnPosition(activated);
				toggleColumn(columnPosition, activated);
				if(sortType != null)
				{
					columnPosition.setSortType(sortType);
					tableView.getSortOrder().add(columnPosition);
				}
				break;
			case RATING:
				initColumnRating(activated);
				toggleColumn(columnRating, activated);
				if(sortType != null)
				{
					columnRating.setSortType(sortType);
					tableView.getSortOrder().add(columnRating);
				}
				break;
			case REPEATING:
				initColumnIsRepeating(activated);
				toggleColumn(columnIsRepeating, activated);
				if(sortType != null)
				{
					columnIsRepeating.setSortType(sortType);
					tableView.getSortOrder().add(columnIsRepeating);
				}
				break;
			case TAGS:
				initColumnTags(activated);
				toggleColumn(columnTags, activated);
				if(sortType != null)
				{
					columnTags.setSortType(sortType);
					tableView.getSortOrder().add(columnTags);
				}
				break;
			default:
				break;					
		}
	}
	
	private void applyReportPreferences()
	{
		tableView.getColumns().clear();
		
		Object loadedObject = ObjectJSONHandler.loadObjectFromJSON(Localization.getString(Strings.FOLDER), "reportPreferences", new ReportPreferences());
		if(loadedObject != null)
		{
			reportPreferences = (ReportPreferences)loadedObject;
			checkBoxIncludeBudget.setSelected(reportPreferences.isIncludeBudget());
			checkBoxSplitTable.setSelected(reportPreferences.isSplitTable());
			checkBoxIncludeCategoryBudgets.setSelected(reportPreferences.isIncludeCategoryBudgets());
			
			ReportSorting reportSorting = reportPreferences.getReportSorting();
			
			ArrayList<ColumnType> allColumns = new ArrayList<>(Arrays.asList(ColumnType.values()));			

			for(ColumnType currentType : reportPreferences.getColumnOrder().getColumns())
			{
				if(currentType == reportSorting.getColumnType())
				{
					initColumn(currentType, true, reportSorting.getSortType());
				}
				else
				{
					initColumn(currentType, true, null);
				}
				allColumns.remove(currentType);
			}
			
			for(ColumnType currentColumn : allColumns)
			{
				initColumn(currentColumn, false, null);
			}
		}
		else
		{
			for(ColumnType currentType : ColumnType.values())
			{
				initColumn(currentType, true, null);
			}
		}
	}
	
	private void initColumnPosition(boolean activated)
	{
		columnPosition = new TableColumn<>();
        columnPosition.setUserData(ColumnType.POSITION);
        columnPosition.setCellValueFactory(new PropertyValueFactory<ReportItem, Integer>("position"));
        columnPosition.setStyle("-fx-alignment: CENTER;");
        
        HBox hboxColumnPosition = new HBox();
        hboxColumnPosition.setAlignment(Pos.CENTER);
        hboxColumnPosition.setSpacing(3);
        
        CheckBox checkBoxPositions = new CheckBox();
        hboxColumnPosition.getChildren().add(checkBoxPositions);

        Label labelColumnPosition = new Label(Localization.getString(Strings.REPORT_POSITION));      
        hboxColumnPosition.getChildren().add(labelColumnPosition);
        
        checkBoxPositions.selectedProperty().addListener((a, b, c)->{
        	 toggleColumn(columnPosition, c);
        });
        checkBoxPositions.setSelected(activated);
        columnPosition.setGraphic(hboxColumnPosition);
        tableView.getColumns().add(columnPosition);
	}
	
	private void initColumnDate(boolean activated)
	{
	    columnDate = new TableColumn<>();
        columnDate.setUserData(ColumnType.DATE);
        columnDate.setCellValueFactory(param -> {               
		    String dateString = param.getValue().getDate();
		    try
		    {
		        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		        Date date = format.parse(dateString);
		        DateFormat finalFormat = new SimpleDateFormat("dd.MM.yy");
		        dateString = finalFormat.format(date);
		        return new SimpleStringProperty(dateString);
		    }
		    catch(Exception e)
		    {
		        Logger.error(e);
		        return null;
		    }
		});
        columnDate.setStyle("-fx-alignment: CENTER;");
        
        HBox hboxColumnDate = new HBox();
        hboxColumnDate.setAlignment(Pos.CENTER);
        hboxColumnDate.setSpacing(3);
        
        CheckBox checkBoxDate = new CheckBox();
        hboxColumnDate.getChildren().add(checkBoxDate);

        Label labelComlumnDate = new Label(Localization.getString(Strings.REPORT_DATE));
        hboxColumnDate.getChildren().add(labelComlumnDate);        
        
        checkBoxDate.selectedProperty().addListener((a, b, c)->{
        	 toggleColumn(columnDate, c);
        });
        checkBoxDate.setSelected(activated);
        columnDate.setGraphic(hboxColumnDate);
        columnDate.setComparator(new DateComparator());
        tableView.getColumns().add(columnDate);
	}
	
	private void initColumnIsRepeating(boolean activated)
	{
	    columnIsRepeating = new TableColumn<>();
        columnIsRepeating.setUserData(ColumnType.REPEATING);
        columnIsRepeating.setCellValueFactory(new PropertyValueFactory<ReportItem, Boolean>("repeating"));
        columnIsRepeating.setCellFactory(param -> {
		    return new ReportTableRepeatingCell();
		});
        columnIsRepeating.setStyle("-fx-alignment: CENTER;");
        
        HBox hboxColumnIsRepeating = new HBox();
        hboxColumnIsRepeating.setAlignment(Pos.CENTER);
        hboxColumnIsRepeating.setSpacing(3);
        
        CheckBox checkBoxRepeating = new CheckBox();
        hboxColumnIsRepeating.getChildren().add(checkBoxRepeating);
        
        Label labelColumnIsRepeating = new Label(Localization.getString(Strings.REPORT_REPEATING));
        hboxColumnIsRepeating.getChildren().add(labelColumnIsRepeating);
        
        checkBoxRepeating.selectedProperty().addListener((a, b, c)->{
        	 toggleColumn(columnIsRepeating, c);
        });
        checkBoxRepeating.setSelected(activated);
        
        columnIsRepeating.setGraphic(hboxColumnIsRepeating);        
        tableView.getColumns().add(columnIsRepeating);
	}
	
	private void initColumnCategory(boolean activated)
	{
	    columnCategory = new TableColumn<>();
        columnCategory.setUserData(ColumnType.CATEGORY);
        columnCategory.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCategory().getName()));
        columnCategory.setStyle("-fx-alignment: CENTER;");
        
        HBox hboxColumnCategory = new HBox();
        hboxColumnCategory.setAlignment(Pos.CENTER);
        hboxColumnCategory.setSpacing(3);
        
        CheckBox checkBoxCategory = new CheckBox();
        hboxColumnCategory.getChildren().add(checkBoxCategory);
        
        Label labelColumnCategory = new Label(Localization.getString(Strings.REPORT_CATEGORY));
        hboxColumnCategory.getChildren().add(labelColumnCategory);
        
        checkBoxCategory.selectedProperty().addListener((a, b, c)->{
        	 toggleColumn(columnCategory, c);
        });
        checkBoxCategory.setSelected(activated);
        columnCategory.setGraphic(hboxColumnCategory);    
        tableView.getColumns().add(columnCategory);
	}
	
	private void initColumnName(boolean activated)
	{
	    columnName = new TableColumn<>();
        columnName.setUserData(ColumnType.NAME);
        columnName.setCellValueFactory(new PropertyValueFactory<ReportItem, Integer>("name"));
        columnName.setStyle("-fx-alignment: CENTER;");
        
        HBox hboxColumnName = new HBox();
        hboxColumnName.setAlignment(Pos.CENTER);
        hboxColumnName.setSpacing(3); 
        
        CheckBox checkBoxName = new CheckBox();
        hboxColumnName.getChildren().add(checkBoxName);
        
        Label labelColumnName = new Label(Localization.getString(Strings.REPORT_NAME));
        hboxColumnName.getChildren().add(labelColumnName);        
        
        checkBoxName.selectedProperty().addListener((a, b, c)->{
        	 toggleColumn(columnName, c);
        });
        checkBoxName.setSelected(activated);
        columnName.setGraphic(hboxColumnName);
        tableView.getColumns().add(columnName);
	}
	
	private void initColumnDescription(boolean activated)
	{
	    columnDescription = new TableColumn<>();
        columnDescription.setUserData(ColumnType.DESCRIPTION);
        columnDescription.setCellValueFactory(new PropertyValueFactory<ReportItem, String>("description"));
        columnDescription.setStyle("-fx-alignment: CENTER;");
        
        HBox hboxColumnDescription = new HBox();
        hboxColumnDescription.setAlignment(Pos.CENTER);
        hboxColumnDescription.setSpacing(3); 
        
        CheckBox checkBoxDescription = new CheckBox();
        hboxColumnDescription.getChildren().add(checkBoxDescription);
        
        Label labelColumnDescription = new Label(Localization.getString(Strings.REPORT_DESCRIPTION));
        hboxColumnDescription.getChildren().add(labelColumnDescription);
        
        checkBoxDescription.selectedProperty().addListener((a, b, c)->{
        	 toggleColumn(columnDescription, c);
        });
        checkBoxDescription.setSelected(activated);
        columnDescription.setGraphic(hboxColumnDescription);
        tableView.getColumns().add(columnDescription);
	}

	private void initColumnTags(boolean activated)
	{
	    columnTags = new TableColumn<>();
	    columnTags.setUserData(ColumnType.TAGS);
	    columnTags.setCellValueFactory(new PropertyValueFactory<ReportItem, String>("tags"));
	    columnTags.setStyle("-fx-alignment: CENTER;");
        
        HBox hboxColumnTags = new HBox();
        hboxColumnTags.setAlignment(Pos.CENTER);
        hboxColumnTags.setSpacing(3); 
        
        CheckBox checkBoxTags = new CheckBox();
        hboxColumnTags.getChildren().add(checkBoxTags);
        
        Label labelColumnTags = new Label(Localization.getString(Strings.REPORT_TAGS));
        hboxColumnTags.getChildren().add(labelColumnTags);        
        
        checkBoxTags.selectedProperty().addListener((a, b, c)->{
        	 toggleColumn(columnTags, c);
        });
        checkBoxTags.setSelected(activated);
        columnTags.setGraphic(hboxColumnTags);
        tableView.getColumns().add(columnTags);
	}
	
	private void initColumnRating(boolean activated)
	{
	    columnRating = new TableColumn<>();
        columnRating.setUserData(ColumnType.RATING);
        columnRating.setCellValueFactory(new PropertyValueFactory<ReportItem, Integer>("amount"));
        columnRating.setCellFactory(param -> {		    
		    return new ReportTableRatingCell();
		});
        columnRating.setStyle("-fx-alignment: CENTER;");
        
        HBox hboxColumnRating = new HBox();
        hboxColumnRating.setAlignment(Pos.CENTER);
        hboxColumnRating.setSpacing(3);         
        
        CheckBox checkBoxRating = new CheckBox();
        hboxColumnRating.getChildren().add(checkBoxRating);
        
        Label labelColumnRating = new Label(Localization.getString(Strings.REPORT_RATING));
        hboxColumnRating.getChildren().add(labelColumnRating);
        
        checkBoxRating.selectedProperty().addListener((a, b, c)->{
        	 toggleColumn(columnRating, c);
        });
        checkBoxRating.setSelected(activated);
        columnRating.setGraphic(hboxColumnRating);
        columnRating.setComparator(new RatingComparator());
        tableView.getColumns().add(columnRating);
	}
	
	private void toggleColumn(TableColumn<ReportItem, ?> column, boolean activated)
	{		
		String style = activated ? "" : "-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_REPORT_TABLE_HEADER_DISABLED);
		Node graphic = column.getGraphic();
		if(graphic != null)
		{
			graphic.setStyle(style);
		}
        columnFilter.toggleColumn((ColumnType)column.getUserData(), activated);       
	}
	
	private void initColumnAmount(boolean activated)
	{
	    columnAmount = new TableColumn<>();
        columnAmount.setUserData(ColumnType.AMOUNT);
        columnAmount.setCellValueFactory(param -> {
		    StringProperty value = new SimpleStringProperty();
		    double amount = param.getValue().getAmount() / 100.0;
		    value.set(Helpers.getCurrencyString(amount, controller.getSettings().getCurrency()));
		    return value;
		});
        columnAmount.setStyle("-fx-alignment: CENTER;");
        
        HBox hboxColumnAmount = new HBox();
        hboxColumnAmount.setAlignment(Pos.CENTER);
        hboxColumnAmount.setSpacing(3);
        
        CheckBox checkBoxAmount = new CheckBox();
        hboxColumnAmount.getChildren().add(checkBoxAmount);
        
        Label labelColumnAmount = new Label(Localization.getString(Strings.REPORT_AMOUNT));
        hboxColumnAmount.getChildren().add(labelColumnAmount);
        
        checkBoxAmount.selectedProperty().addListener((a, b, c)->{
            toggleColumn(columnAmount, c);
        });
        checkBoxAmount.setSelected(activated);
        columnAmount.setGraphic(hboxColumnAmount);
        tableView.getColumns().add(columnAmount);               
	}

	private void initTable()
	{
		columnFilter = new ColumnFilter();
		for(ColumnType type : ColumnType.values())
		{
			columnFilter.addColumn(type);
		}
		
		Label labelPlaceholder = new Label(Localization.getString(Strings.PAYMENTS_PLACEHOLDER));
		labelPlaceholder.setStyle("-fx-font-size: 16");
		tableView.setPlaceholder(labelPlaceholder);		

		tableView.setFixedCellSize(26);		
	}

	public void filter()
	{
		new FilterController(controller.getStage(), controller, controller.getFilterSettings());	
	}

	private ArrayList<ReportItem> createReportItems(ArrayList<Payment> payments)
	{
		ArrayList<ReportItem> reportItems = new ArrayList<>();
		TagHandler tagHander = new TagHandler(controller.getSettings());
		
		for(int i = 0; i < payments.size(); i++)
		{
			Payment currentPayment = payments.get(i);
			ReportItem reportItem = new ReportItem();
			reportItem.setPosition(i + 1);
			reportItem.setDate(currentPayment.getDate());
			reportItem.setAmount(currentPayment.getAmount());
			reportItem.setName(currentPayment.getName());
			reportItem.setDescription(currentPayment.getDescription());
			reportItem.setRepeating(currentPayment instanceof RepeatingPaymentEntry);
			reportItem.setCategory(controller.getCategoryHandler().getCategory(currentPayment.getCategoryID()));
			
			try
			{
				reportItem.setTags(tagHander.getTagsAsString(currentPayment));
			}
			catch(Exception e)
			{
				Logger.error(e);
				controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
			}

			reportItems.add(reportItem);
		}

		return reportItems;
	}

	private void refreshTableView()
	{
		Platform.runLater(()->{tableView.getItems().clear();});

		ArrayList<Payment> payments = controller.getPaymentHandler().getPayments();		
		if(payments != null)
		{
			ArrayList<ReportItem> reportItems = createReportItems(payments);
			Platform.runLater(()->{;
				ObservableList<ReportItem> objectsForTable = FXCollections.observableArrayList(reportItems);
				tableView.setItems(objectsForTable);
			});
		}
	}
	
	private ReportPreferences getReportPreferences()
	{
		ColumnOrder columnOrder = new ColumnOrder();
		for(TableColumn<ReportItem, ?> currentColumn : tableView.getColumns())
		{
			ColumnType currentType = (ColumnType)currentColumn.getUserData();			
			if(columnFilter.containsColumn(currentType))
			{
				columnOrder.addColumn(currentType);
			}
		}
		
		ReportSorting reportSorting = new ReportSorting();
		ObservableList<TableColumn<ReportItem, ?>> sortOrder = tableView.getSortOrder();
		if(sortOrder.size() >  0)
		{
			reportSorting.setColumnType((ColumnType)sortOrder.get(0).getUserData());
			reportSorting.setSortType(sortOrder.get(0).getSortType());
		}
		else
		{
			reportSorting.setColumnType(ColumnType.DATE);
			reportSorting.setSortType(SortType.DESCENDING);
		}
		
		String reportFolderPath = null;
		if(reportPreferences != null)
		{
			reportFolderPath = reportPreferences.getReportFolderPath();
		}
		
		return new ReportPreferences(columnOrder, 
									checkBoxIncludeBudget.isSelected(),
									checkBoxSplitTable.isSelected(),
									checkBoxIncludeCategoryBudgets.isSelected(),
									reportSorting, 
									reportFolderPath);
	}
	
	private void saveReportPreferences() 
	{
		try
		{
			ObjectJSONHandler.saveObjectToJSON(Localization.getString(Strings.FOLDER), "reportPreferences", reportPreferences);
		}
		catch(IOException e)
		{
			Logger.error(e);
		}	
	}

	public void generate()
	{		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Localization.getString(Strings.TITLE_REPORT_SAVE));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf");
		fileChooser.setInitialFileName(initialReportFileName);
		fileChooser.getExtensionFilters().add(extFilter);		
		
		String initialReportFolder = reportPreferences.getReportFolderPath();
		if(initialReportFolder != null)
		{
			fileChooser.setInitialDirectory(new File(initialReportFolder));
		}			
		
		File file = fileChooser.showSaveDialog(controller.getStage());	
		if(file != null)
		{				
			Budget budget = new Budget(controller.getPaymentHandler().getPayments());		
			
			reportPreferences = getReportPreferences();
			reportPreferences.setReportFolderPath(file.getParentFile().getAbsolutePath());
			saveReportPreferences();
			
			ReportGenerator reportGenerator = new ReportGenerator(new ArrayList<ReportItem>(tableView.getItems()),
																controller.getCategoryBudgets(),
																reportPreferences,														
																file,
																controller.getSettings().getCurrency(),
																controller.getCurrentDate(),
																budget);
			
			Stage modalStage = UIHelpers.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_REPORT), controller.getStage(), controller.getIcon());

			Worker.runLater(() -> {
				try
				{
					reportGenerator.generate();					

					Platform.runLater(() -> {
						if(modalStage != null)
						{
							modalStage.close();
						}
						
						controller.showNotification(Localization.getString(Strings.NOTIFICATION_REPORT_SAVE));	
						
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle(Localization.getString(Strings.INFO_TITLE_REPORT_SAVE));
						alert.setHeaderText("");
						alert.initOwner(controller.getStage());
						alert.setContentText(Localization.getString(Strings.INFO_TEXT_REPORT_SAVE));			
						Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
						dialogStage.getIcons().add(controller.getIcon());						
						
						ButtonType buttonTypeOne = new ButtonType(Localization.getString(Strings.INFO_TEXT_REPORT_SAVE_OPEN_FOLDER));
						ButtonType buttonTypeTwo = new ButtonType(Localization.getString(Strings.INFO_TEXT_REPORT_SAVE_OPEN_REPORT));
						ButtonType buttonTypeThree = new ButtonType(Localization.getString(Strings.OK));						
						alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);
						
						DialogPane dialogPane = alert.getDialogPane();
						dialogPane.getButtonTypes().stream().map(dialogPane::lookupButton).forEach(button -> button.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
							if(KeyCode.ENTER.equals(event.getCode()) && event.getTarget() instanceof Button)
							{
								((Button)event.getTarget()).fire();
							}
						}));
						
						Optional<ButtonType> result = alert.showAndWait();						
						if (result.get() == buttonTypeOne)
						{
							try
							{
								Desktop.getDesktop().open(new File(file.getParent().replace("\\", "/")));
							}
							catch(IOException e1)
							{
								Logger.error(e1);
								AlertGenerator.showAlert(AlertType.ERROR, 
														Localization.getString(Strings.TITLE_ERROR), 
														"", 
														Localization.getString(Strings.ERROR_OPEN_FOLDER, e1.getMessage()),
														controller.getIcon(), 
														controller.getStage(), 
														null, 
														false);
							}
						}
						else if (result.get() == buttonTypeTwo)
						{
							try
							{
								Desktop.getDesktop().open(new File(file.getAbsolutePath().replace("\\", "/")));
							}
							catch(IOException e1)
							{
								Logger.error(e1);
								AlertGenerator.showAlert(AlertType.ERROR, 
														Localization.getString(Strings.TITLE_ERROR), 
														"", 
														Localization.getString(Strings.ERROR_OPEN_REPORT, e1.getMessage()), 
														controller.getIcon(), 
														controller.getStage(), 
														null, 
														false);
							}
						}
						else
						{
							alert.close();
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
						AlertGenerator.showAlert(AlertType.ERROR, 
												Localization.getString(Strings.TITLE_ERROR), 
												"", 
												Localization.getString(Strings.ERROR_REPORT_SAVE, e.getMessage()), 
												controller.getIcon(), 
												controller.getStage(), 
												null, 
												false);
					});
				}
			});			
		}
	}

	public Controller getController()
	{
		return controller;
	}

	@Override
	public void refresh()
	{
		Stage modalStage = UIHelpers.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_REPORT_TAB), controller.getStage(), controller.getIcon());
		
		if(controller.getFilterSettings().equals(new FilterSettings()))
		{
			labelFilterActive.setVisible(false);
		}
		else
		{
			labelFilterActive.setVisible(true);
		}
		
		DateTime currentDate = controller.getCurrentDate();
		String currentMonth = currentDate.toString("MM");
	    String currentYear = currentDate.toString("YYYY");
	   
	    initialReportFileName = Localization.getString(Strings.REPORT_INITIAL_FILENAME, currentYear, currentMonth);
		
		reportPreferences = getReportPreferences();
		saveReportPreferences();
		
		Worker.runLater(() -> {
			refreshTableView();

			Platform.runLater(() -> {
				if(modalStage != null)
				{
					modalStage.close();
				}
				
				applyReportPreferences();
				tableView.refresh();
			});
		});
	}

	@Override
	public void applyStyle()
	{
		buttonFilter.setGraphic(Helpers.getFontIcon(FontIconType.FILTER, 18, Color.WHITE));		
		buttonGenerate.setGraphic(Helpers.getFontIcon(FontIconType.COGS, 18, Color.WHITE));	
		labelFilterActive.setGraphic(Helpers.getFontIcon(FontIconType.WARNING, 16, Colors.TEXT));
		
		anchorPaneMain.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND));
		labelFilterActive.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));
		buttonFilter.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		buttonGenerate.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		checkBoxIncludeBudget.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT) + "; -fx-font-size: 14;");
		checkBoxSplitTable.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT) + "; -fx-font-size: 14;");
		checkBoxIncludeCategoryBudgets.setStyle("-fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT) + "; -fx-font-size: 14;");
	}
}