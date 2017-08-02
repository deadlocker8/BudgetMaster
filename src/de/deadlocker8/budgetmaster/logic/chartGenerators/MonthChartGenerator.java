package de.deadlocker8.budgetmaster.logic.chartGenerators;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.Helpers;
import de.deadlocker8.budgetmaster.logic.MonthInOutSum;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import tools.ConvertTo;

public class MonthChartGenerator
{
	private ArrayList<MonthInOutSum> monthInOutSums;
	private String currency;

	public MonthChartGenerator(ArrayList<MonthInOutSum> monthInOutSums, String currency)
	{
		if(monthInOutSums == null)
		{
			this.monthInOutSums = new ArrayList<>();
		}
		else
		{
			this.monthInOutSums = monthInOutSums;
		}
		this.currency = currency;
	}

	public HBox generate()
	{
		HBox generatedChart = new HBox();
		generatedChart.setAlignment(Pos.TOP_CENTER);
		generatedChart.setSpacing(25);

		double total = getMaximum(monthInOutSums);
			
		for(MonthInOutSum currentMonthSum : monthInOutSums)
		{
			VBox chartPart = new VBox();
			chartPart.setAlignment(Pos.TOP_CENTER);		

			HBox hboxChart = new HBox();
			hboxChart.setAlignment(Pos.BOTTOM_CENTER);
			hboxChart.setSpacing(10);
			VBox chartIncome = generateChart(currentMonthSum.getSums(), total, true);
			hboxChart.getChildren().add(chartIncome);
			HBox.setHgrow(chartIncome, Priority.ALWAYS);
			VBox chartPayment = generateChart(currentMonthSum.getSums(), total, false);
			hboxChart.getChildren().add(chartPayment);
			HBox.setHgrow(chartPayment, Priority.ALWAYS);

			chartPart.getChildren().add(hboxChart);
			VBox.setVgrow(hboxChart, Priority.ALWAYS);

			Label labelTitle = new Label(currentMonthSum.getDate().toString("MMMM \nYY"));
			labelTitle.setStyle("-fx-font-size: 12;");
			labelTitle.setTextAlignment(TextAlignment.CENTER);
			chartPart.getChildren().add(labelTitle);
			VBox.setMargin(labelTitle, new Insets(10, 0, 0, 0));

			generatedChart.getChildren().add(chartPart);			
			generatedChart.getChildren().add(new Separator(Orientation.VERTICAL));
		}

		return generatedChart;
	}

	private VBox generateChart(ArrayList<CategoryInOutSum> categoryInOutSums, double total, boolean useBudgetIN)
	{
		VBox result = new VBox();		
		
		Label labelAmount = new Label(Helpers.getCurrencyString(getTotal(categoryInOutSums, useBudgetIN), currency));
		labelAmount.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
		result.getChildren().add(labelAmount);
		VBox.setMargin(labelAmount, new Insets(0, 0, 10, 0));

		VBox chart = new VBox();
		chart.setAlignment(Pos.BOTTOM_CENTER);
		
		for(CategoryInOutSum currentItem : categoryInOutSums)
		{
			Label currentPart = new Label();
			currentPart.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(currentItem.getColor()));
			currentPart.prefWidthProperty().bind(chart.widthProperty());
			chart.getChildren().add(currentPart);

			double value;
			if(useBudgetIN)
			{
				value = currentItem.getBudgetIN() / 100.0;
			}
			else
			{
				value = -currentItem.getBudgetOUT() / 100.0;
			}

			double percentage = value / total;	
			
			currentPart.setMinHeight(0);
			currentPart.prefHeightProperty().bind(chart.heightProperty().multiply(percentage));	

			String categoryName = currentItem.getName();
			if(categoryName.equals("NONE"))
			{
				categoryName = "Keine Kategorie";
			}
			
			Tooltip tooltip = new Tooltip();
			tooltip.setText(categoryName + "\n"+ Helpers.NUMBER_FORMAT.format(percentage * 100) + " %\n" + Helpers.NUMBER_FORMAT.format(value).replace(".", ",") + currency);//
			currentPart.setTooltip(tooltip);
		}

		result.getChildren().add(chart);
		VBox.setVgrow(chart, Priority.ALWAYS);

		return result;
	}

	public GridPane generateLegend()
	{
		GridPane legend = new GridPane();
		legend.setPadding(new Insets(10));
		legend.setHgap(20);
		legend.setVgap(10);
		legend.setAlignment(Pos.CENTER);
		legend.setStyle("-fx-background-color: #EEEEEE; -fx-border-color: #212121; -fx-border-width: 1; -fx-border-radius: 5;");
		
		if(monthInOutSums.size() == 0)
		{
			return legend;
		}		
		
		ArrayList<HBox> legendItems = new ArrayList<>();
		for(CategoryInOutSum currentItem : monthInOutSums.get(0).getSums())
		{
			String label = currentItem.getName();
			if(label.equals("NONE"))
			{
				label = "Keine Kategorie";
			}
			legendItems.add(getLegendItem(label, currentItem.getColor()));
		}

		int legendWidth;
		int numberOfItems = legendItems.size();
		if(numberOfItems <= 3)
		{
			legendWidth = numberOfItems;
		}
		else
		{
			legendWidth = (int)Math.ceil(Math.sqrt(numberOfItems));
		}
		
		for(int i = 0; i < numberOfItems; i++)
		{
			int columnIndex = i % legendWidth;
			int rowIndex = i / legendWidth;			
			legend.add(legendItems.get(i), columnIndex, rowIndex);
		}

		return legend;
	}

	private HBox getLegendItem(String name, Color color)
	{
		HBox legendItem = new HBox();
		Label labelCircle = new Label();
		labelCircle.setMinWidth(20);
		labelCircle.setMinHeight(20);
		labelCircle.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(color) + "; -fx-background-radius: 50%; -fx-border-width: 1; -fx-border-color: black - fx-border-radius: 50%");

		Label labelText = new Label(name);
		labelText.setStyle("-fx-font-weight: bold;");

		legendItem.getChildren().add(labelCircle);
		legendItem.getChildren().add(labelText);
		HBox.setMargin(labelText, new Insets(0, 0, 0, 5));

		return legendItem;
	}

	private double getTotal(ArrayList<CategoryInOutSum> categoryInOutSums, boolean useBudgetIN)
	{
		double total = 0;
		for(CategoryInOutSum currentItem : categoryInOutSums)
		{
			if(useBudgetIN)
			{
				total += currentItem.getBudgetIN() / 100.0;
			}
			else
			{
				total += -currentItem.getBudgetOUT() / 100.0;
			}
		}
		return total;
	}

	private double getMaximum(ArrayList<MonthInOutSum> monthInOutSums)
	{		
		double maximum = 0;
		for(MonthInOutSum currentItem : monthInOutSums)
		{
			if(currentItem.getBudgetIN() > maximum)
			{
				maximum = currentItem.getBudgetIN();
			}

			if(Math.abs(currentItem.getBudgetOUT()) > maximum)
			{
				maximum = Math.abs(currentItem.getBudgetOUT());
			}
		}
		return maximum / 100.0;
	}
}