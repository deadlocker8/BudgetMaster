package de.deadlocker8.budgetmaster.logic.chartGenerators;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.Helpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import tools.ConvertTo;


public class PieChartGenerator
{
    private String title;
    private ArrayList<CategoryInOutSum> categoryInOutSums;
    private boolean useBudgetIN;
    private String currency;

    public PieChartGenerator(String title, ArrayList<CategoryInOutSum> categoryInOutSums, boolean useBudgetIN, String currency)
    {
        this.title = title;
        this.categoryInOutSums = categoryInOutSums;
        this.useBudgetIN = useBudgetIN;
        this.currency = currency;
    }

    /**
     * generates a new pie chart
     * @return PieChart chart
     */
    public PieChart generate()
    {
        ArrayList<PieChart.Data> data = new ArrayList<>();      

        for(CategoryInOutSum currentItem : categoryInOutSums)
        {        	
        	String label = String.valueOf(currentItem.getName()); 
        	if(useBudgetIN)
        	{
        		data.add(new PieChart.Data(label, currentItem.getBudgetIN()/100.0));
        	}
        	else
        	{
        		data.add(new PieChart.Data(label, -currentItem.getBudgetOUT()/100.0));
        	}
        }
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(data);

        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle(title);

        //add tooltip to every segment that shows percentage as double value
        chart.getData().stream().forEach(tool ->
        {
            Tooltip tooltip = new Tooltip();

            double total = 0;
            for(int i = 0; i<chart.getData().size(); i++)
            {
            	PieChart.Data currentData = chart.getData().get(i);
                total += currentData.getPieValue();
                String currentColor = ConvertTo.toRGBHexWithoutOpacity(categoryInOutSums.get(i).getColor());
                currentData.getNode().setStyle("-fx-pie-color: " + currentColor + ";");
                
                //TODO color legend
            }

            double pieValue = tool.getPieValue();
            double percentage = (pieValue / total) * 100;
            String percent = String.valueOf(percentage);
            percent = percent.substring(0, percent.indexOf(".") + 2);

            tooltip.setText(percent + " %\n" + Helpers.NUMBER_FORMAT.format(pieValue).replace(".", ",") + currency);
            Tooltip.install(tool.getNode(), tooltip);
            Node node = tool.getNode();
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
        });

        return chart;
    }
}