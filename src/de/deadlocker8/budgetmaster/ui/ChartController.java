package de.deadlocker8.budgetmaster.ui;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.ServerConnection;
import de.deadlocker8.budgetmaster.logic.chartGenerators.PieChartGenerator;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import logger.Logger;

public class ChartController implements Refreshable
{
	@FXML private AnchorPane anchorPaneMain;

	private Controller controller;

	public void init(Controller controller)
	{
		this.controller = controller;

		//TODO design, chart chooser		
		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");
	}

	@Override
	public void refresh()
	{
		//TODO example	
		//TODO date range chooser
		//TODO check wether starDate and EndDate are included and are working correctly
		try
		{
			ServerConnection connection = new ServerConnection(controller.getSettings());
			ArrayList<CategoryInOutSum> sums = connection.getCategoryInOutSumForMonth(controller.getCurrentDate().withDayOfMonth(1), controller.getCurrentDate().dayOfMonth().withMaximumValue());
			PieChartGenerator generator = new PieChartGenerator("Einnahmen nach Kategorien", sums, false, controller.getSettings().getCurrency());
			anchorPaneMain.getChildren().add(generator.generate());		
		}
		catch(Exception e)
		{
			Logger.error(e);			
		}
	}
}