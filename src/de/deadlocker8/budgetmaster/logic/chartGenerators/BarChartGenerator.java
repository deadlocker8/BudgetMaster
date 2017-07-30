package de.deadlocker8.budgetmaster.logic.chartGenerators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import de.deadlocker8.budgetmaster.logic.Helpers;
import de.deadlocker8.budgetmaster.logic.MonthInOutSum;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

@Deprecated
public class BarChartGenerator
{
	private ArrayList<MonthInOutSum> monthInOutSums;
	private String currency;

	public BarChartGenerator(ArrayList<MonthInOutSum> monthInOutSums, String currency)
	{
		this.monthInOutSums = monthInOutSums;
		this.currency = currency;
	}

	public BarChart<String, Number> generate()
	{
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String, Number> generatedChart = new BarChart<>(xAxis, yAxis);
		generatedChart.setTitle(null);

		xAxis.setLabel("");
		yAxis.setLabel("Summe in " + currency);

		XYChart.Series<String, Number> seriesIN = new XYChart.Series<String, Number>();
		seriesIN.setName("Einnahmen");
		XYChart.Series<String, Number> seriesOUT = new XYChart.Series<String, Number>();
		seriesOUT.setName("Ausgaben");

		for(MonthInOutSum currentItem : monthInOutSums)
		{
			String label = currentItem.getDate().toString("MMMM YY");

			seriesIN.getData().add(new XYChart.Data<String, Number>(label, currentItem.getBudgetIN()/100.0));
			seriesOUT.getData().add(new XYChart.Data<String, Number>(label, currentItem.getBudgetOUT()/100.0));
		}

		generatedChart.getData().add(seriesIN);
		generatedChart.getData().add(seriesOUT);

		generatedChart.setLegendVisible(true);
		
		// add tooltip to every segment
		generatedChart.getData().stream().forEach(tool -> {
			for(XYChart.Data<String, Number> data : tool.getData())
			{
				Tooltip tooltip = new Tooltip();

				tooltip.setText(Helpers.getCurrencyString(data.getYValue().doubleValue(), currency));
				Tooltip.install(tool.getNode(), tooltip);
				Node node = data.getNode();
				node.setOnMouseEntered(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent event)
					{
						Point2D p = node.localToScreen(event.getX() + 5, event.getY() + 7);
						tooltip.show(node, p.getX(), p.getY());
					}
				});
				node.setOnMouseExited(new EventHandler<MouseEvent>()
				{

					@Override
					public void handle(MouseEvent event)
					{
						tooltip.hide();
					}
				});
			}
		});
		
		// style bar for incomes
		for(Node n : generatedChart.lookupAll(".default-color0.chart-bar"))
		{
			n.setStyle("-fx-bar-fill: " + Helpers.COLOR_INCOME + ";");
		}		
		
		// style bar for payments
		for(Node n : generatedChart.lookupAll(".default-color1.chart-bar"))
		{
			n.setStyle("-fx-bar-fill: " + Helpers.COLOR_PAYMENT + ";");
		}
		
		 //style legend item according to color
		Set<Node> nodes = generatedChart.lookupAll(".chart-legend-item");
		if(nodes.size() > 0)
		{        	
			Iterator<Node> iterator = nodes.iterator();
			int counter = 0;
			while(iterator.hasNext())
			{
    			Node node = iterator.next();	        			
    			if(node instanceof Label)
    			{
    				Label labelLegendItem = (Label)node;     
    				if(counter == 0)
    				{
    					labelLegendItem.getGraphic().setStyle("-fx-background-color: " + Helpers.COLOR_INCOME + ";");
    				}
    				else
    				{
    					labelLegendItem.getGraphic().setStyle("-fx-background-color: " + Helpers.COLOR_PAYMENT + ";");
    				}
    			}
    			counter++;
			}
		}

		return generatedChart;
	}
}