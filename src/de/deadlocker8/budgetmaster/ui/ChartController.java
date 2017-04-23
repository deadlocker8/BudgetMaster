package de.deadlocker8.budgetmaster.ui;

import java.time.LocalDate;
import java.util.ArrayList;

import org.joda.time.DateTime;

import de.deadlocker8.budgetmaster.logic.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.ServerConnection;
import de.deadlocker8.budgetmaster.logic.chartGenerators.PieChartGenerator;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.fxml.FXML;
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

			accordion.setExpandedPane(accordion.getPanes().get(0));
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
	}
}