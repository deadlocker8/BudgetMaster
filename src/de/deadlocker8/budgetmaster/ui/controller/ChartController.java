package de.deadlocker8.budgetmaster.ui.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import de.deadlocker8.budgetmaster.logic.charts.CategoriesChart;
import de.deadlocker8.budgetmaster.logic.charts.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.charts.ChartExportable;
import de.deadlocker8.budgetmaster.logic.charts.LegendType;
import de.deadlocker8.budgetmaster.logic.charts.MonthBarChart;
import de.deadlocker8.budgetmaster.logic.charts.MonthInOutSum;
import de.deadlocker8.budgetmaster.logic.charts.MonthLineChart;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmaster.ui.Refreshable;
import de.deadlocker8.budgetmaster.ui.Styleable;
import fontAwesome.FontIconType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logger.Logger;
import tools.AlertGenerator;
import tools.ConvertTo;
import tools.Localization;
import tools.Worker;

public class ChartController implements Refreshable, Styleable
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
	@FXML private Button buttonChartMonthExport;
	@FXML private RadioButton radioButtonBars;
	@FXML private RadioButton radioButtonLines;

	private Controller controller;
	private File lastExportPath;
	
	private CategoriesChart categoriesChart;
	private MonthLineChart monthLineChart;
	private MonthBarChart monthBarChart;

	public void init(Controller controller)
	{
		this.controller = controller;
	
		datePickerEnd.setDayCellFactory(param -> new DateCell()
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
		
		applyStyle();
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
				categoriesChart = new CategoriesChart(Localization.getString(Strings.CHART_CATEGORIES_TITLE_INCOMES), 
													  Localization.getString(Strings.CHART_CATEGORIES_TITLE_PAYMENTS),
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
	
	public void chartMonthExport()
	{
	    if(radioButtonLines.isSelected())
        {
            if(monthLineChart != null)
            {
                export(monthLineChart);
            }
        }
        else
        {
            if(monthBarChart != null)
            {
                export(monthBarChart);
            }
        }
	}

	public void export(ChartExportable chart)
	{
		Worker.runLater(() -> {
			Platform.runLater(() -> {
				new ExportChartController(controller.getStage(), this, chart);
			});
		});
	}

	public void chartMonthShow()
	{
		Platform.runLater(() -> {
			vboxChartMonth.getChildren().clear();
		});

		String startMonth = comboBoxStartMonth.getValue();
		String startYear = comboBoxStartYear.getValue();
		String endMonth = comboBoxEndMonth.getValue();
		String endYear = comboBoxEndYear.getValue();

		String startDateString = "01-" + startMonth + "-" + startYear;
		DateTime startDate = DateTime.parse(startDateString, DateTimeFormat.forPattern("dd-MMMM-YYYY").withLocale(controller.getSettings().getLanguage().getLocale()));

		String endDateString = "01-" + endMonth + "-" + endYear;
		DateTime endDate = DateTime.parse(endDateString, DateTimeFormat.forPattern("dd-MMMM-YYYY").withLocale(controller.getSettings().getLanguage().getLocale()));

		if(endDate.isBefore(startDate))
		{
			Platform.runLater(() -> {
				AlertGenerator.showAlert(AlertType.WARNING, Localization.getString(Strings.TITLE_WARNING), "", Localization.getString(Strings.WARNING_ENDDATE_BEFORE_STARTDATE), controller.getIcon(), controller.getStage(), null, false);
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
				    monthBarChart = new MonthBarChart(sums, controller.getSettings().getCurrency());
				    vboxChartMonth.getChildren().add(monthBarChart);
				    VBox.setVgrow(monthBarChart, Priority.ALWAYS);
				}
				else
				{
					monthLineChart = new MonthLineChart(sums, controller.getSettings().getCurrency());					
					vboxChartMonth.getChildren().add(monthLineChart);
					VBox.setVgrow(monthLineChart, Priority.ALWAYS);
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

	public Controller getController()
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
		Stage modalStage = Helpers.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_CHARTS), controller.getStage(), controller.getIcon());

		// prepare chart categories
		LocalDate startDate = LocalDate.parse(controller.getCurrentDate().withDayOfMonth(1).toString("yyyy-MM-dd"));
		LocalDate endDate = LocalDate.parse(controller.getCurrentDate().dayOfMonth().withMaximumValue().toString("yyyy-MM-dd"));

		datePickerStart.setValue(startDate);
		datePickerEnd.setValue(endDate);

		// chart month
		System.out.println(controller.getCurrentDate().minusMonths(5).monthOfYear().getAsText(controller.getSettings().getLanguage().getLocale()));
		comboBoxStartMonth.setValue(controller.getCurrentDate().minusMonths(5).monthOfYear().getAsText(controller.getSettings().getLanguage().getLocale()));
		comboBoxStartYear.setValue(String.valueOf(controller.getCurrentDate().minusMonths(5).getYear()));

		comboBoxEndMonth.setValue(controller.getCurrentDate().plusMonths(6).monthOfYear().getAsText(controller.getSettings().getLanguage().getLocale()));
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

	@Override
	public void applyStyle()
	{
		anchorPaneMain.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND));
		vboxChartCategories.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND));
		vboxChartCategories.setSpacing(20);
		vboxChartMonth.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND));
		
		buttonChartCategoriesShow.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE));
		buttonChartCategoriesShow.setGraphic(Helpers.getFontIcon(FontIconType.CHECK, 16, Color.WHITE));

		buttonChartCategoriesExport.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE));
		buttonChartCategoriesExport.setGraphic(Helpers.getFontIcon(FontIconType.SAVE, 16, Color.WHITE));

		buttonChartMonthShow.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE));
		buttonChartMonthShow.setGraphic(Helpers.getFontIcon(FontIconType.CHECK, 16, Color.WHITE));

		buttonChartMonthExport.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE));
		buttonChartMonthExport.setGraphic(Helpers.getFontIcon(FontIconType.SAVE, 16, Color.WHITE));		
	}
}