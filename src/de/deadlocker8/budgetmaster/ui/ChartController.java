package de.deadlocker8.budgetmaster.ui;

import java.time.LocalDate;
import java.util.ArrayList;

import org.joda.time.DateTime;

import de.deadlocker8.budgetmaster.logic.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.MonthInOutSum;
import de.deadlocker8.budgetmaster.logic.ServerConnection;
import de.deadlocker8.budgetmaster.logic.chartGenerators.BarChartGenerator;
import de.deadlocker8.budgetmaster.logic.chartGenerators.LineChartGenerator;
import de.deadlocker8.budgetmaster.logic.chartGenerators.PieChartGenerator;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import logger.Logger;

public class ChartController implements Refreshable
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Accordion accordion;
	@FXML private DatePicker datePickerStart;
	@FXML private HBox hboxChartCategories;
	@FXML private DatePicker datePickerEnd;
	@FXML private AnchorPane anchorPaneChartMonth;
	@FXML private AnchorPane anchorPaneLineChart;
	@FXML private Button buttonChartCategoriesShow;

	private Controller controller;

	public void init(Controller controller)
	{
		this.controller = controller;

		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");
		hboxChartCategories.setStyle("-fx-background-color: #F4F4F4;");
		anchorPaneChartMonth.setStyle("-fx-background-color: #F4F4F4;");	
		FontIcon iconShow = new FontIcon(FontIconType.CHECK);
		iconShow.setSize(16);
		iconShow.setColor(Color.WHITE);
		buttonChartCategoriesShow.setStyle("-fx-background-color: #2E79B9;");	
		buttonChartCategoriesShow.setGraphic(iconShow);
		
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
                         if (item.isBefore(datePickerStart.getValue().plusDays(1)))
                         {
                             setDisable(true);
                             setStyle("-fx-background-color: #ffc0cb;");
                         }   
                     }
                 };
			}
		});
	}
	
	public void chartCategoriesShow()
	{
		DateTime startDate = DateTime.parse(datePickerStart.getValue().toString());
		DateTime endDate = DateTime.parse(datePickerEnd.getValue().toString());		
	
		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			ArrayList<CategoryInOutSum> sums = connection.getCategoryInOutSumForMonth(startDate, endDate);

			hboxChartCategories.getChildren().clear();

			PieChartGenerator generator = new PieChartGenerator("Einnahmen nach Kategorien", sums, true, controller.getSettings().getCurrency());
			hboxChartCategories.getChildren().add(generator.generate());
			generator = new PieChartGenerator("Ausgaben nach Kategorien", sums, false, controller.getSettings().getCurrency());
			hboxChartCategories.getChildren().add(generator.generate());
		}
		catch(Exception e)
		{
			Logger.error(e);
			//TODO
			//controller.showConnectionErrorAlert(e.getMessage());
		}
	}
	
	public void chartMonthShow()
	{
		//DEBUG get date from comboboxes
		DateTime startDate = controller.getCurrentDate().withMonthOfYear(1);
		DateTime endDate =  controller.getCurrentDate().withMonthOfYear(12);	
		
		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			//DEBUG
			ArrayList<MonthInOutSum> sums = connection.getMonthInOutSum(startDate, endDate);

			anchorPaneChartMonth.getChildren().clear();
			BarChartGenerator generator = new BarChartGenerator(sums, controller.getSettings().getCurrency());
			BarChart<String, Number> chartMonth = generator.generate();
			anchorPaneChartMonth.getChildren().add(chartMonth);
			AnchorPane.setTopAnchor(chartMonth, 0.0);
			AnchorPane.setRightAnchor(chartMonth, 0.0);
			AnchorPane.setBottomAnchor(chartMonth, 0.0);
			AnchorPane.setLeftAnchor(chartMonth, 0.0);
		}
		catch(Exception e)
		{
			Logger.error(e);
			//TODO
			//controller.showConnectionErrorAlert(e.getMessage());
		}
	}
	
	public void chartLineChartShow()
	{
		//DEBUG get date from comboboxes
		DateTime startDate = controller.getCurrentDate().withMonthOfYear(1);
		DateTime endDate =  controller.getCurrentDate().withMonthOfYear(12);	
		
		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			//DEBUG
			ArrayList<MonthInOutSum> sums = connection.getMonthInOutSum(startDate, endDate);

			anchorPaneLineChart.getChildren().clear();
			LineChartGenerator generator = new LineChartGenerator(sums, controller.getSettings().getCurrency());
			LineChart<String, Number> chartMonth = generator.generate();
			anchorPaneLineChart.getChildren().add(chartMonth);
			AnchorPane.setTopAnchor(chartMonth, 0.0);
			AnchorPane.setRightAnchor(chartMonth, 0.0);
			AnchorPane.setBottomAnchor(chartMonth, 0.0);
			AnchorPane.setLeftAnchor(chartMonth, 0.0);
		}
		catch(Exception e)
		{
			Logger.error(e);
			//TODO
			//controller.showConnectionErrorAlert(e.getMessage());
		}
	}

	@Override
	public void refresh()
	{
		//chart categories
		LocalDate startDate = LocalDate.parse(controller.getCurrentDate().withDayOfMonth(1).toString("yyyy-MM-dd"));
		LocalDate endDate = LocalDate.parse(controller.getCurrentDate().dayOfMonth().withMaximumValue().toString("yyy-MM-dd"));
		
		datePickerStart.setValue(startDate);
		datePickerEnd.setValue(endDate);
		
		chartCategoriesShow();
		
		//chart month
		chartMonthShow();
		
		chartLineChartShow();
		
		//TODO combine bar und line chart (radio buttons)
		
		accordion.setExpandedPane(accordion.getPanes().get(0));
	}
}