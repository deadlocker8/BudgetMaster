package de.deadlocker8.budgetmaster.ui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import de.deadlocker8.budgetmaster.logic.FilterSettings;
import de.deadlocker8.budgetmaster.logic.Helpers;
import de.deadlocker8.budgetmaster.logic.Payment;
import de.deadlocker8.budgetmaster.logic.RepeatingPaymentEntry;
import de.deadlocker8.budgetmaster.logic.report.ColumnFilter;
import de.deadlocker8.budgetmaster.logic.report.ColumnOrder;
import de.deadlocker8.budgetmaster.logic.report.ColumnType;
import de.deadlocker8.budgetmaster.logic.report.ReportGenerator;
import de.deadlocker8.budgetmaster.logic.report.ReportItem;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logger.Logger;
import tools.AlertGenerator;
import tools.Worker;

public class ReportController implements Refreshable
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Label labelPayments;
	@FXML private Label labelFilterActive;
	@FXML private CheckBox checkBoxSplitTable;
	@FXML private CheckBox checkBoxIncludeCategoryBudgets;
	@FXML private CheckBox checkBoxDescending;
	@FXML private Button buttonFilter;
	@FXML private Button buttonGenerate;
	@FXML private TableView<ReportItem> tableView;

	private Controller controller;
	private ColumnFilter columnFilter;

	public void init(Controller controller)
	{
		this.controller = controller;

		FontIcon iconFilter = new FontIcon(FontIconType.FILTER);
		iconFilter.setSize(18);
		iconFilter.setStyle("-fx-text-fill: white");
		buttonFilter.setGraphic(iconFilter);
		FontIcon iconPayment = new FontIcon(FontIconType.COGS);
		iconPayment.setSize(18);
		iconPayment.setStyle("-fx-text-fill: white");
		buttonGenerate.setGraphic(iconPayment);
		FontIcon iconWarning = new FontIcon(FontIconType.WARNING);
		iconWarning.setSize(13);
		iconWarning.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		labelFilterActive.setGraphic(iconWarning);
		
		checkBoxDescending.setSelected(true);	
		checkBoxDescending.selectedProperty().addListener((a, b, c)->{
			refreshTableView(c);
		});

		initTable();

		// apply theme
		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");
		labelFilterActive.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		buttonFilter.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		buttonGenerate.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		checkBoxSplitTable.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text") + "; -fx-font-size: 14;");
		checkBoxIncludeCategoryBudgets.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text") + "; -fx-font-size: 14;");
		checkBoxDescending.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text") + "; -fx-font-size: 14;");

		refresh();
	}

	private void initTable()
	{
		columnFilter = new ColumnFilter();
		for(ColumnType type : ColumnType.values())
		{
			columnFilter.addColumn(type);
		}
		
		Label labelPlaceholder = new Label("Keine Daten verfügbar");
		labelPlaceholder.setStyle("-fx-font-size: 16");
		tableView.setPlaceholder(labelPlaceholder);

		TableColumn<ReportItem, Integer> columnPosition = new TableColumn<>("Nr.");
		columnPosition.setUserData(ColumnType.POSITION);
		columnPosition.setCellValueFactory(new PropertyValueFactory<ReportItem, Integer>("position"));
		columnPosition.setStyle("-fx-alignment: CENTER;");
		CheckBox checkBoxPositions = new CheckBox();
		checkBoxPositions.setSelected(true);
		checkBoxPositions.selectedProperty().addListener((a, b, c)->{
			String style = c ? "" : "-fx-background-color: salmon";			
			columnPosition.setStyle(style);
			columnFilter.toggleColumn(ColumnType.POSITION, c);
		});
		columnPosition.setGraphic(checkBoxPositions);
		columnPosition.setSortable(false);
		tableView.getColumns().add(columnPosition);

		TableColumn<ReportItem, String> columnDate = new TableColumn<>("Datum");
		columnDate.setUserData(ColumnType.DATE);
		columnDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReportItem, String>, ObservableValue<String>>()
		{
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportItem, String> param)
			{				
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
			}
		});
		columnDate.setStyle("-fx-alignment: CENTER;");
		CheckBox checkBoxDate = new CheckBox();
		checkBoxDate.setSelected(true);
		checkBoxDate.selectedProperty().addListener((a, b, c)->{
			String style = c ? "" : "-fx-background-color: salmon";			
			columnDate.setStyle(style);
			columnFilter.toggleColumn(ColumnType.DATE, c);
		});
		columnDate.setGraphic(checkBoxDate);
		columnDate.setSortable(false);
		tableView.getColumns().add(columnDate);
			
		TableColumn<ReportItem, Boolean> columnIsRepeating = new TableColumn<>("Wiederholend");
		columnIsRepeating.setUserData(ColumnType.REPEATING);
		columnIsRepeating.setCellValueFactory(new PropertyValueFactory<ReportItem, Boolean>("repeating"));
		columnIsRepeating.setCellFactory(new Callback<TableColumn<ReportItem, Boolean>, TableCell<ReportItem, Boolean>>()
		{
			@Override
			public TableCell<ReportItem, Boolean> call(TableColumn<ReportItem, Boolean> param)
			{
				TableCell<ReportItem, Boolean> cell = new TableCell<ReportItem, Boolean>()
				{
					@Override
					public void updateItem(Boolean item, boolean empty)
					{
						if(!empty)
						{							
							FontIcon iconRepeating = new FontIcon(FontIconType.CALENDAR);
							iconRepeating.setSize(16);							
							Color color = item ? Color.web("#212121") : Color.TRANSPARENT;
							iconRepeating.setColor(color);
							
							Label labelRepeating = new Label();
							labelRepeating.setGraphic(iconRepeating);
							labelRepeating.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #212121");
							labelRepeating.setAlignment(Pos.CENTER);
							setGraphic(labelRepeating);
						}
						else
						{
							setGraphic(null);
						}
					}
				};
				return cell;
			}
		});
		columnIsRepeating.setStyle("-fx-alignment: CENTER;");
		CheckBox checkBoxRepeating = new CheckBox();
		checkBoxRepeating.setSelected(true);
		checkBoxRepeating.selectedProperty().addListener((a, b, c)->{
			String style = c ? "" : "-fx-background-color: salmon";			
			columnIsRepeating.setStyle(style);
			columnFilter.toggleColumn(ColumnType.REPEATING, c);
		});
		columnIsRepeating.setGraphic(checkBoxRepeating);
		columnIsRepeating.setSortable(false);
		tableView.getColumns().add(columnIsRepeating);

		TableColumn<ReportItem, String> columnCategory = new TableColumn<>("Kategorie");
		columnCategory.setUserData(ColumnType.CATEGORY);
		columnCategory.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReportItem, String>, ObservableValue<String>>()
		{
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportItem, String> param)
			{
				String categoryName = param.getValue().getCategory().getName();
				if(categoryName.equals("NONE"))
				{
					categoryName = "";
				}
				return new SimpleStringProperty(categoryName);
			}
		});
		columnCategory.setStyle("-fx-alignment: CENTER;");
		CheckBox checkBoxCategory = new CheckBox();
		checkBoxCategory.setSelected(true);
		checkBoxCategory.selectedProperty().addListener((a, b, c)->{
			String style = c ? "" : "-fx-background-color: salmon";			
			columnCategory.setStyle(style);
			columnFilter.toggleColumn(ColumnType.CATEGORY, c);
		});
		columnCategory.setGraphic(checkBoxCategory);
		columnCategory.setSortable(false);
		tableView.getColumns().add(columnCategory);

		TableColumn<ReportItem, Integer> columnName = new TableColumn<>("Name");
		columnName.setUserData(ColumnType.NAME);
		columnName.setCellValueFactory(new PropertyValueFactory<ReportItem, Integer>("name"));
		columnName.setStyle("-fx-alignment: CENTER;");
		CheckBox checkBoxName = new CheckBox();
		checkBoxName.setSelected(true);
		checkBoxName.selectedProperty().addListener((a, b, c)->{
			String style = c ? "" : "-fx-background-color: salmon";			
			columnName.setStyle(style);
			columnFilter.toggleColumn(ColumnType.NAME, c);
		});
		columnName.setGraphic(checkBoxName);
		columnName.setSortable(false);
		tableView.getColumns().add(columnName);

		TableColumn<ReportItem, Integer> columnDescription = new TableColumn<>("Notiz");
		columnDescription.setUserData(ColumnType.DESCRIPTION);
		columnDescription.setCellValueFactory(new PropertyValueFactory<ReportItem, Integer>("description"));
		columnDescription.setStyle("-fx-alignment: CENTER;");
		CheckBox checkBoxDescription = new CheckBox();
		checkBoxDescription.setSelected(true);
		checkBoxDescription.selectedProperty().addListener((a, b, c)->{
			String style = c ? "" : "-fx-background-color: salmon";			
			columnDescription.setStyle(style);
			columnFilter.toggleColumn(ColumnType.DESCRIPTION, c);
		});
		columnDescription.setGraphic(checkBoxDescription);
		columnDescription.setSortable(false);
		tableView.getColumns().add(columnDescription);
		
		TableColumn<ReportItem, Integer> columnRating = new TableColumn<>("Bewertung");
		columnRating.setUserData(ColumnType.RATING);
		columnRating.setCellValueFactory(new PropertyValueFactory<ReportItem, Integer>("amount"));
		columnRating.setCellFactory(new Callback<TableColumn<ReportItem, Integer>, TableCell<ReportItem, Integer>>()
		{
			@Override
			public TableCell<ReportItem, Integer> call(TableColumn<ReportItem, Integer> param)
			{
				TableCell<ReportItem, Integer> cell = new TableCell<ReportItem, Integer>()
				{
					@Override
					public void updateItem(Integer item, boolean empty)
					{
						if(!empty)
						{								
							FontIcon iconRepeating = item > 0 ? new FontIcon(FontIconType.PLUS) : new FontIcon(FontIconType.MINUS);						
							iconRepeating.setSize(14);
							iconRepeating.setColor(Color.web("#212121"));
							
							Label labelRepeating = new Label();
							labelRepeating.setGraphic(iconRepeating);
							labelRepeating.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #212121");
							labelRepeating.setAlignment(Pos.CENTER);
							setGraphic(labelRepeating);
						}
						else
						{
							setGraphic(null);
						}
					}
				};
				return cell;
			}
		});
		columnRating.setStyle("-fx-alignment: CENTER;");
		CheckBox checkBoxRating = new CheckBox();
		checkBoxRating.setSelected(true);
		checkBoxRating.selectedProperty().addListener((a, b, c)->{
			String style = c ? "" : "-fx-background-color: salmon";			
			columnRating.setStyle(style);
			columnFilter.toggleColumn(ColumnType.RATING, c);
		});
		columnRating.setGraphic(checkBoxRating);
		columnRating.setSortable(false);
		tableView.getColumns().add(columnRating);

		TableColumn<ReportItem, String> columnAmount = new TableColumn<>("Betrag");
		columnAmount.setUserData(ColumnType.AMOUNT);
		columnAmount.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReportItem, String>, ObservableValue<String>>()
		{
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportItem, String> param)
			{
				StringProperty value = new SimpleStringProperty();
				double amount = param.getValue().getAmount() / 100.0;
				value.set(String.valueOf(Helpers.NUMBER_FORMAT.format(amount).replace(".", ",")) + " " + controller.getSettings().getCurrency());
				return value;
			}
		});
		columnAmount.setStyle("-fx-alignment: CENTER;");
		CheckBox checkBoxAmount = new CheckBox();
		checkBoxAmount.setSelected(true);
		checkBoxAmount.selectedProperty().addListener((a, b, c)->{
			String style = c ? "" : "-fx-background-color: salmon";			
			columnAmount.setStyle(style);
			columnFilter.toggleColumn(ColumnType.AMOUNT, c);
		});
		columnAmount.setGraphic(checkBoxAmount);
		columnAmount.setSortable(false);
		tableView.getColumns().add(columnAmount);

		tableView.setFixedCellSize(26);
	}

	public void filter()
	{
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/FilterGUI.fxml"));
			Parent root = (Parent)fxmlLoader.load();
			Stage newStage = new Stage();
			newStage.initOwner(controller.getStage());
			newStage.initModality(Modality.APPLICATION_MODAL);
			newStage.setTitle("Filter");
			newStage.setScene(new Scene(root));
			newStage.getIcons().add(controller.getIcon());
			newStage.setResizable(false);
			FilterController newController = fxmlLoader.getController();
			newController.init(newStage, controller, controller.getFilterSettings());
			newStage.show();
		}
		catch(IOException e)
		{
			Logger.error(e);
		}
	}

	private ArrayList<ReportItem> createReportItems(ArrayList<Payment> payments, boolean descending)
	{
		ArrayList<ReportItem> reportItems = new ArrayList<>();
		for(int i = 0; i < payments.size(); i++)
		{
			Payment currentPayment = payments.get(i);
			ReportItem reportItem = new ReportItem();
			reportItem.setPosition(i + 1);
			reportItem.setDate(currentPayment.getDate());
			reportItem.setAmount(currentPayment.getAmount());
			reportItem.setDescription(currentPayment.getDescription());
			reportItem.setName(currentPayment.getName());
			reportItem.setRepeating(currentPayment instanceof RepeatingPaymentEntry);
			reportItem.setCategory(controller.getCategoryHandler().getCategory(currentPayment.getCategoryID()));

			reportItems.add(reportItem);
		}

		if(!descending)
		{
			Collections.reverse(reportItems);
		}
		return reportItems;
	}

	private void refreshTableView(boolean descending)
	{
		tableView.getItems().clear();

		ArrayList<Payment> payments = controller.getPaymentHandler().getPayments();		
		if(payments != null)
		{
			ArrayList<ReportItem> reportItems = createReportItems(payments, descending);
			ObservableList<ReportItem> objectsForTable = FXCollections.observableArrayList(reportItems);
			tableView.setItems(objectsForTable);
		}
	}

	@SuppressWarnings("rawtypes")
	public void generate()
	{
		ColumnOrder columnOrder = new ColumnOrder();
		for(TableColumn currentColumn : tableView.getColumns())
		{
			ColumnType currentType = (ColumnType)currentColumn.getUserData();			
			if(columnFilter.containsColumn(currentType))
			{
				columnOrder.addColumn(currentType);
			}
		}		
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Bericht speichern");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(controller.getStage());		
		if(file != null)
		{		
			ReportGenerator reportGenerator = new ReportGenerator(new ArrayList<ReportItem>(tableView.getItems()),																
																columnOrder,
																checkBoxSplitTable.isSelected(), 
																checkBoxIncludeCategoryBudgets.isSelected(),																
																file,
																controller.getSettings().getCurrency(),
																controller.getCurrentDate());
			
			Stage modalStage = Helpers.showModal("Vorgang läuft", "Der Monatsbericht wird erstellt, bitte warten...", controller.getStage(), controller.getIcon());

			Worker.runLater(() -> {
				try
				{
					reportGenerator.generate();					

					Platform.runLater(() -> {
						if(modalStage != null)
						{
							modalStage.close();
						}
						
						controller.showNotification("Bericht erfolgreich gespeichert");	
						
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Erfolgreich erstellt");
						alert.setHeaderText("");
						alert.setContentText("Der Monatsbericht wurde erfolgreich erstellt");			
						Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
						dialogStage.getIcons().add(controller.getIcon());						
						
						ButtonType buttonTypeOne = new ButtonType("Ordner öffnen");
						ButtonType buttonTypeTwo = new ButtonType("Bericht öffnen");
						ButtonType buttonTypeThree = new ButtonType("OK");						
						alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);
						
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
								AlertGenerator.showAlert(AlertType.ERROR, "Fehler", "", "Der Ordner konnte nicht geöffnet werden\n\n" + e1.getMessage(), controller.getIcon(), controller.getStage(), null, false);
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
								AlertGenerator.showAlert(AlertType.ERROR, "Fehler", "", "Der Bericht konnte nicht geöffnet werden\n\n" + e1.getMessage(), controller.getIcon(), controller.getStage(), null, false);
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
						AlertGenerator.showAlert(AlertType.ERROR, "Fehler", "", "Beim Erstellen des Monatsberichts ist ein Fehler aufgetreten:\n\n" + e.getMessage(), controller.getIcon(), controller.getStage(), null, false);
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
		if(controller.getFilterSettings().equals(new FilterSettings()))
		{
			labelFilterActive.setVisible(false);
		}
		else
		{
			labelFilterActive.setVisible(true);
		}

		refreshTableView(checkBoxDescending.isSelected());
	}
}