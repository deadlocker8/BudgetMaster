package de.deadlocker8.budgetmaster.logic.chartGenerators;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.Helpers;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tools.ConvertTo;

public class CategoriesChartGenerator
{
	private String title;
	private ArrayList<CategoryInOutSum> categoryInOutSums;
	private boolean useBudgetIN;
	private String currency;
	private double total;

	public CategoriesChartGenerator(String title, ArrayList<CategoryInOutSum> categoryInOutSums, boolean useBudgetIN, String currency)
	{
		this.title = title;
		if(categoryInOutSums == null)
		{
			this.categoryInOutSums = new ArrayList<>();
		}
		else
		{
			this.categoryInOutSums = categoryInOutSums;
		}		
		this.useBudgetIN = useBudgetIN;
		this.currency = currency;
		this.total = getTotal(this.categoryInOutSums, useBudgetIN);
	}	

	public VBox generate()
	{
		VBox generatedChart = new VBox();
		HBox chart = new HBox();
		chart.setMinHeight(50);

		Label labelTitle = new Label(title);
		labelTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
		generatedChart.getChildren().add(labelTitle);
		VBox.setMargin(labelTitle, new Insets(0, 0, 10, 0));

		for(CategoryInOutSum currentItem : categoryInOutSums)
		{
			Label currentPart = new Label();
			currentPart.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(currentItem.getColor()));
			currentPart.prefHeightProperty().bind(chart.heightProperty());
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

			currentPart.prefWidthProperty().bind(chart.widthProperty().multiply(percentage));

			Tooltip tooltip = new Tooltip();
			tooltip.setText(currentItem.getName() + "\n" + Helpers.NUMBER_FORMAT.format(percentage*100) + " %\n" + Helpers.getCurrencyString(value, currency));//
			currentPart.setTooltip(tooltip);
		}

		generatedChart.getChildren().add(chart);

		return generatedChart;
	}

	public GridPane generateLegend()
	{
		GridPane legend = new GridPane();
		legend.setPadding(new Insets(10));
		legend.setHgap(20);
		legend.setVgap(10);
		legend.setAlignment(Pos.CENTER);
		legend.setStyle("-fx-background-color: #EEEEEE; -fx-border-color: #212121; -fx-border-width: 1; -fx-border-radius: 5;");

		if(categoryInOutSums.size() == 0)
		{
			return legend;
		}	
		
		ArrayList<HBox> legendItems = new ArrayList<>();
		for(CategoryInOutSum currentItem : categoryInOutSums)
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
	
	public VBox generateFullLegend()
	{
		VBox legend = new VBox();
		legend.setPadding(new Insets(10));
		legend.setSpacing(10);
		legend.setStyle("-fx-background-color: #EEEEEE; -fx-border-color: #212121; -fx-border-width: 1; -fx-border-radius: 5;");

		if(categoryInOutSums.size() == 0)
		{
			return legend;
		}
		
		double totalIn = getTotal(categoryInOutSums, true);
		double totalOut = getTotal(categoryInOutSums, false);
				
		HBox hboxLegend = new HBox();
		hboxLegend.setSpacing(10);

		VBox vboxCircles = new VBox();
		vboxCircles.setSpacing(10);	
		VBox vboxNames = new VBox();
		vboxNames.setSpacing(10);
		VBox vboxIn = new VBox();
		vboxIn.setSpacing(10);
		VBox vboxOut = new VBox();
		vboxOut.setSpacing(10);
		
		for(CategoryInOutSum currentItem : categoryInOutSums)
		{
			String name = currentItem.getName();
			if(name.equals("NONE"))
			{
				name = "Keine Kategorie";
			}
			
			Label labelCircle = new Label();
			labelCircle.setMinWidth(20);
			labelCircle.setMinHeight(20);
			labelCircle.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(currentItem.getColor()) + "; -fx-background-radius: 50%; -fx-border-width: 1; -fx-border-color: black - fx-border-radius: 50%");
			vboxCircles.getChildren().add(labelCircle);

			Label labelName = new Label(name);
			labelName.setStyle("-fx-font-weight: bold;");
			labelName.setMinHeight(20);
			vboxNames.getChildren().add(labelName);
			
			String percentageIn = Helpers.NUMBER_FORMAT.format((currentItem.getBudgetIN() / totalIn));
			Label labelInSum = new Label("+" + Helpers.getCurrencyString(currentItem.getBudgetIN(), currency) + " (" + percentageIn + "%)");
			labelInSum.setStyle("-fx-font-weight: bold;");
			labelInSum.setMinHeight(20);
			vboxIn.getChildren().add(labelInSum);
			
			String percentageOut = Helpers.NUMBER_FORMAT.format((currentItem.getBudgetOUT() / totalOut));
			Label labelOutSum = new Label(Helpers.getCurrencyString(currentItem.getBudgetOUT(), currency) + " (" + percentageOut + "%)");
			labelOutSum.setStyle("-fx-font-weight: bold;");
			labelOutSum.setMinHeight(20);
			vboxOut.getChildren().add(labelOutSum);
		}
		
		hboxLegend.getChildren().add(vboxCircles);
		hboxLegend.getChildren().add(vboxNames);
		HBox.setHgrow(vboxNames, Priority.ALWAYS);
		hboxLegend.getChildren().add(vboxIn);
		HBox.setHgrow(vboxIn, Priority.ALWAYS);
		hboxLegend.getChildren().add(vboxOut);
		HBox.setHgrow(vboxOut, Priority.ALWAYS);			
		legend.getChildren().add(hboxLegend);		
		
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
}