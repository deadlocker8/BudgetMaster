package de.deadlocker8.budgetmaster.ui;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.ServerConnection;
import de.deadlocker8.budgetmaster.logic.chartGenerators.PieChartGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import logger.Logger;

public class ChartController implements Refreshable
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private Accordion accordion;
	@FXML private DatePicker datePickerStart;
	@FXML private HBox hboxChartCategories;
	@FXML private DatePicker datePickerEnd;
	@FXML private AnchorPane anchorPaneChartMonth;

	private Controller controller;

	public void init(Controller controller)
	{
		this.controller = controller;

		// TODO design, chart chooser
		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");
		hboxChartCategories.setStyle("-fx-background-color: #F4F4F4;");
		anchorPaneChartMonth.setStyle("-fx-background-color: #F4F4F4;");		
	}

	@Override
	public void refresh()
	{
		// TODO example
		// TODO date range chooser
		// TODO check wether starDate and EndDate are included and are working correctly
		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			ArrayList<CategoryInOutSum> sums = connection.getCategoryInOutSumForMonth(controller.getCurrentDate().withDayOfMonth(1), controller.getCurrentDate().dayOfMonth().withMaximumValue());

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
		}
	}
}