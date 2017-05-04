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
		this.categoryInOutSums = categoryInOutSums;
		this.useBudgetIN = useBudgetIN;
		this.currency = currency;
		this.total = getTotal(categoryInOutSums, useBudgetIN);
	}

	public VBox generate()
	{
		VBox chartWithLegend = new VBox();
		HBox chart = new HBox();
		chart.setMinHeight(30);

		Label labelTitle = new Label(title);
		labelTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
		chartWithLegend.getChildren().add(labelTitle);
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
			tooltip.setText(Helpers.NUMBER_FORMAT.format(percentage*100) + " %\n" + Helpers.NUMBER_FORMAT.format(value).replace(".", ",") + currency);//
			currentPart.setTooltip(tooltip);
		}

		chartWithLegend.getChildren().add(chart);

		return chartWithLegend;
	}

	public GridPane generateLegend()
	{
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

		int legendWidth = (int)Math.ceil(Math.sqrt(legendItems.size()));
		GridPane legend = new GridPane();
		legend.setPadding(new Insets(10));
		legend.setHgap(20);
		legend.setVgap(10);
		legend.setAlignment(Pos.CENTER);
		legend.setStyle("-fx-background-color: #EEEEEE; -fx-border-color: #212121; -fx-border-width: 1; -fx-border-radius: 5;");

		for(int i = 0; i < legendItems.size(); i++)
		{
			int columnIndex = i % legendWidth;
			int rowIndex = i / 4;
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
}