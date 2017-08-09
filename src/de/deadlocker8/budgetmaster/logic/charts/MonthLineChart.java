package de.deadlocker8.budgetmaster.logic.charts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import de.deadlocker8.budgetmaster.logic.MonthInOutSum;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Transform;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MonthLineChart extends VBox implements ChartExportable
{
	private ArrayList<MonthInOutSum> monthInOutSums;
	private String currency;

	public MonthLineChart(ArrayList<MonthInOutSum> monthInOutSums, String currency)
	{
		this.monthInOutSums = monthInOutSums;
		this.currency = currency;
		
		this.setSpacing(10);
		this.getChildren().add(generate());
	}

	private LineChart<String, Number> generate()
	{
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final LineChart<String, Number> generatedChart = new LineChart<>(xAxis, yAxis);
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

			seriesIN.getData().add(new XYChart.Data<String, Number>(label, currentItem.getBudgetIN() / 100.0));
			seriesOUT.getData().add(new XYChart.Data<String, Number>(label, -currentItem.getBudgetOUT() / 100.0));
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

		// style line for incomes
		for(Node n : generatedChart.lookupAll(".default-color0.chart-series-line"))
		{
			n.setStyle("-fx-stroke: " + Helpers.COLOR_INCOME + ";");
		}
		
		// style line dots for incomes
		for(Node n : generatedChart.lookupAll(".default-color0.chart-line-symbol"))
		{
			n.setStyle("-fx-background-color: " + Helpers.COLOR_INCOME + ", white;");
		}

		// style line for payments
		for(Node n : generatedChart.lookupAll(".default-color1.chart-series-line"))
		{
			n.setStyle("-fx-stroke: " + Helpers.COLOR_PAYMENT + ";");
		}
		
		// style line dots for payments
		for(Node n : generatedChart.lookupAll(".default-color1.chart-line-symbol"))
		{
			n.setStyle("-fx-background-color: " + Helpers.COLOR_PAYMENT + ", white;");
		}

		// style legend item according to color
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

    @Override
    public WritableImage export(int width, int height) {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: transparent;");
        root.setPadding(new Insets(25));
        
        root.getChildren().add(generate());         
        
        Stage newStage = new Stage();
        newStage.initModality(Modality.NONE);
        newStage.setScene(new Scene(root, width, height));
        newStage.setResizable(false);       
        newStage.show();       
        
        SnapshotParameters sp = new SnapshotParameters();
        sp.setTransform(Transform.scale(width / root.getWidth(), height / root.getHeight()));
        newStage.close();
        
        return root.snapshot(sp, null);
    }
}