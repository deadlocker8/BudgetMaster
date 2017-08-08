package de.deadlocker8.budgetmaster.ui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import de.deadlocker8.budgetmaster.logic.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.Helpers;
import de.deadlocker8.budgetmaster.logic.MonthInOutSum;
import de.deadlocker8.budgetmaster.logic.ServerConnection;
import de.deadlocker8.budgetmaster.logic.chartGenerators.CategoriesChart;
import de.deadlocker8.budgetmaster.logic.chartGenerators.ChartExportable;
import de.deadlocker8.budgetmaster.logic.chartGenerators.LegendType;
import de.deadlocker8.budgetmaster.logic.chartGenerators.LineChartGenerator;
import de.deadlocker8.budgetmaster.logic.chartGenerators.MonthChartGenerator;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logger.Logger;
import tools.AlertGenerator;
import tools.Worker;

public class ChartController implements Refreshable
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Accordion accordion;
	@FXML private DatePicker datePickerStart;
	@FXML private VBox vboxChartCategories;
	@FXML private DatePicker datePickerEnd;
	@FXML private VBox vboxChartMonth;
	@FXML private Button buttonChartCategoriesShow;
	@FXML private Button buttonChartCategoriesExport;
	@FXML private ComboBox<String> comboBoxStartMonth;
	@FXML private ComboBox<String> comboBoxStartYear;
	@FXML private ComboBox<String> comboBoxEndMonth;
	@FXML private ComboBox<String> comboBoxEndYear;
	@FXML private Button buttonChartMonthShow;
	@FXML private RadioButton radioButtonBars;
	@FXML private RadioButton radioButtonLines;
	@FXML private HBox hboxChartMonthButtons;

	private Controller controller;
	private Button buttonChartMonthExport;
	private File lastExportPath;
	
	private CategoriesChart categoriesChart;

	public void init(Controller controller)
	{
		this.controller = controller;

		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");
		vboxChartCategories.setStyle("-fx-background-color: #F4F4F4;");
		vboxChartCategories.setSpacing(20);
		vboxChartMonth.setStyle("-fx-background-color: #F4F4F4;");
		FontIcon iconShow = new FontIcon(FontIconType.CHECK);
		iconShow.setSize(16);
		iconShow.setColor(Color.WHITE);
		buttonChartCategoriesShow.setStyle("-fx-background-color: #2E79B9;");
		buttonChartCategoriesShow.setGraphic(iconShow);

		FontIcon iconShow2 = new FontIcon(FontIconType.SAVE);
		iconShow2.setSize(16);
		iconShow2.setColor(Color.WHITE);
		buttonChartCategoriesExport.setStyle("-fx-background-color: #2E79B9;");
		buttonChartCategoriesExport.setGraphic(iconShow2);

		FontIcon iconShow3 = new FontIcon(FontIconType.CHECK);
		iconShow3.setSize(16);
		iconShow3.setColor(Color.WHITE);
		buttonChartMonthShow.setStyle("-fx-background-color: #2E79B9;");
		buttonChartMonthShow.setGraphic(iconShow3);

		buttonChartMonthExport = new Button();
		//DEBUG
//		buttonChartMonthExport.setOnAction((event) -> {
//			export(vboxChartMonth);
//		});

		FontIcon iconShow4 = new FontIcon(FontIconType.SAVE);
		iconShow4.setSize(16);
		iconShow4.setColor(Color.WHITE);
		buttonChartMonthExport.setStyle("-fx-background-color: #2E79B9;");
		buttonChartMonthExport.setGraphic(iconShow4);

		datePickerEnd.setDayCellFactory(new Callback<DatePicker, DateCell>()
		{
			@Override
			public DateCell call(DatePicker param)
			{
				return new DateCell()
				{
					@Override
					public void updateItem(LocalDate item, boolean empty)
					{
						super.updateItem(item, empty);
						if(item.isBefore(datePickerStart.getValue().plusDays(1)))
						{
							setDisable(true);
							setStyle("-fx-background-color: #ffc0cb;");
						}
					}
				};
			}
		});

		comboBoxStartMonth.setItems(FXCollections.observableArrayList(Helpers.getMonthList()));
		comboBoxStartYear.setItems(FXCollections.observableArrayList(Helpers.getYearList()));
		comboBoxEndMonth.setItems(FXCollections.observableArrayList(Helpers.getMonthList()));
		comboBoxEndYear.setItems(FXCollections.observableArrayList(Helpers.getYearList()));

		final ToggleGroup toggleGroup = new ToggleGroup();
		radioButtonBars.setToggleGroup(toggleGroup);
		radioButtonBars.setSelected(true);
		radioButtonLines.setToggleGroup(toggleGroup);

		accordion.setExpandedPane(accordion.getPanes().get(0));
		vboxChartMonth.setSpacing(15);
	}

	public void buttonChartCategoriesShow()
	{
		chartCategoriesShow(LegendType.NORMAL);
	}

	public void chartCategoriesShow(LegendType legendType)
	{
		DateTime startDate = DateTime.parse(datePickerStart.getValue().toString());
		DateTime endDate = DateTime.parse(datePickerEnd.getValue().toString());
		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			ArrayList<CategoryInOutSum> sums = connection.getCategoryInOutSumForMonth(startDate, endDate);

			Platform.runLater(()->{;
				vboxChartCategories.getChildren().clear();
				categoriesChart = new CategoriesChart("Einnahmen nach Kategorien", 
																	  "Ausgaben nach Kategorien",
																	  sums,
																	  controller.getSettings().getCurrency(),
																	  legendType);
				vboxChartCategories.getChildren().add(categoriesChart);
				VBox.setVgrow(categoriesChart, Priority.ALWAYS);
			});
		}
		catch(Exception e)
		{
			Logger.error(e);
			Platform.runLater(() -> {
				controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
			});
		}
	}
	
	public void chartCategoriesExport()
	{
		if(categoriesChart != null)
		{
			export(categoriesChart);
		}
	}

	public void export(ChartExportable chart)
	{
		Worker.runLater(() -> {
			Platform.runLater(() -> {
				try
				{
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/budgetmaster/ui/ExportChartGUI.fxml"));
					Parent root = (Parent)fxmlLoader.load();
					Stage newStage = new Stage();
					newStage.initOwner(controller.getStage());
					newStage.initModality(Modality.APPLICATION_MODAL);
					newStage.setTitle("Diagramm exportieren");
					newStage.setScene(new Scene(root));
					newStage.getIcons().add(controller.getIcon());
					newStage.setResizable(false);
					ExportChartController newController = fxmlLoader.getController();
					newController.init(newStage, this, chart);
					newStage.show();
				}
				catch(IOException e)
				{
					Logger.error(e);
				}
			});
		});
	}

	public void chartMonthShow()
	{
		if(radioButtonLines.isSelected())
		{
			if(!hboxChartMonthButtons.getChildren().contains(buttonChartMonthExport))
			{
				hboxChartMonthButtons.getChildren().add(buttonChartMonthExport);
			}
		}
		else
		{
			if(hboxChartMonthButtons.getChildren().contains(buttonChartMonthExport))
			{
				hboxChartMonthButtons.getChildren().remove(buttonChartMonthExport);
			}
		}

		Platform.runLater(() -> {
			vboxChartMonth.getChildren().clear();
		});

		String startMonth = comboBoxStartMonth.getValue();
		String startYear = comboBoxStartYear.getValue();
		String endMonth = comboBoxEndMonth.getValue();
		String endYear = comboBoxEndYear.getValue();

		String startDateString = "01-" + startMonth + "-" + startYear;
		DateTime startDate = DateTime.parse(startDateString, DateTimeFormat.forPattern("dd-MMMM-YYYY"));

		String endDateString = "01-" + endMonth + "-" + endYear;
		DateTime endDate = DateTime.parse(endDateString, DateTimeFormat.forPattern("dd-MMMM-YYYY"));

		if(endDate.isBefore(startDate))
		{
			Platform.runLater(() -> {
				AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Das Enddatum darf nicht vor dem Startdatum liegen.", controller.getIcon(), controller.getStage(), null, false);
			});
			return;
		}

		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			ArrayList<MonthInOutSum> sums = connection.getMonthInOutSum(startDate, endDate);

			Platform.runLater(() -> {
				vboxChartMonth.getChildren().clear();

				if(radioButtonBars.isSelected())
				{
					ScrollPane scrollPane = new ScrollPane();
					scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
					scrollPane.setFocusTraversable(false);
					scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-border-color: transparent; -fx-border-width: 0; -fx-border-insets: 0;");
					scrollPane.setPadding(new Insets(0, 0, 10, 0));

					MonthChartGenerator generator = new MonthChartGenerator(sums, controller.getSettings().getCurrency());
					HBox generatedChart = generator.generate();
					scrollPane.setContent(generatedChart);
					generatedChart.prefHeightProperty().bind(scrollPane.heightProperty().subtract(30));
					vboxChartMonth.getChildren().add(scrollPane);
					VBox.setVgrow(scrollPane, Priority.ALWAYS);
					vboxChartMonth.getChildren().add(generator.generateLegend());
				}
				else
				{
					LineChartGenerator generator = new LineChartGenerator(sums, controller.getSettings().getCurrency());
					LineChart<String, Number> chartMonth = generator.generate();
					vboxChartMonth.getChildren().add(chartMonth);
					VBox.setVgrow(chartMonth, Priority.ALWAYS);
				}
			});
		}
		catch(Exception e)
		{
			Logger.error(e);
			Platform.runLater(() -> {
				controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
			});
		}
	}

	public Controller getControlle()
	{
		return controller;
	}

	public void setLastExportPath(File lastExportPath)
	{
		this.lastExportPath = lastExportPath;
	}

	public File getLastExportPath()
	{
		return lastExportPath;
	}

	@Override
	public void refresh()
	{
		Stage modalStage = Helpers.showModal("Vorgang läuft", "Lade Diagramme...", controller.getStage(), controller.getIcon());

		// prepare chart categories
		LocalDate startDate = LocalDate.parse(controller.getCurrentDate().withDayOfMonth(1).toString("yyyy-MM-dd"));
		LocalDate endDate = LocalDate.parse(controller.getCurrentDate().dayOfMonth().withMaximumValue().toString("yyy-MM-dd"));

		datePickerStart.setValue(startDate);
		datePickerEnd.setValue(endDate);

		// chart month
		comboBoxStartMonth.setValue(controller.getCurrentDate().minusMonths(5).toString("MMMM"));
		comboBoxStartYear.setValue(String.valueOf(controller.getCurrentDate().minusMonths(5).getYear()));

		comboBoxEndMonth.setValue(controller.getCurrentDate().plusMonths(6).toString("MMMM"));
		comboBoxEndYear.setValue(String.valueOf(controller.getCurrentDate().plusMonths(6).getYear()));

		Worker.runLater(() -> {
			chartCategoriesShow(LegendType.NORMAL);
			chartMonthShow();

			Platform.runLater(() -> {
				if(modalStage != null)
				{
					modalStage.close();
				}
			});
		});
	}
}