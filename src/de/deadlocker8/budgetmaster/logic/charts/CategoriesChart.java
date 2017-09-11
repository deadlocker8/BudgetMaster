package de.deadlocker8.budgetmaster.logic.charts;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tools.ConvertTo;
import tools.Localization;

public class CategoriesChart extends VBox implements ChartExportable
{
	private String titleIncomes;
	private String titlePayments;
	private ArrayList<CategoryInOutSum> categoryInOutSums;
	private String currency;
	private double totalIncomes;
	private double totalPayments;
	private LegendType legendType;	
	private final double CHART_HEIGHT = 200;
	private final double FULL_LEGEND_ITEM_HEIGHT = 40;

	public CategoriesChart(String titleIncomes, String titlePayments, ArrayList<CategoryInOutSum> categoryInOutSums, String currency, LegendType legendType)
	{
		this.titleIncomes = titleIncomes;
		this.titlePayments = titlePayments;
		if(categoryInOutSums == null)
		{
			this.categoryInOutSums = new ArrayList<>();
		}
		else
		{
			this.categoryInOutSums = categoryInOutSums;
		}
		
		this.currency = currency;
		this.totalIncomes = getTotal(this.categoryInOutSums, true);
		this.totalPayments = getTotal(categoryInOutSums, false);
		this.legendType = legendType;
		
		this.setSpacing(10);
		
		this.getChildren().add(generate(titleIncomes, true));
		this.getChildren().add(generate(titlePayments, false));
		
		Region spacer = new Region();
		this.getChildren().add(spacer);
		VBox.setVgrow(spacer, Priority.ALWAYS);
		
		if(this.legendType == LegendType.NORMAL)
		{
			this.getChildren().add(generateLegend());
		}
		else if(this.legendType == LegendType.FULL)
		{
			this.getChildren().add(generateFullLegend());		
		}
	}	

	private VBox generate(String title, boolean useIncomes)
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
			currentPart.setStyle("-fx-background-color: " + currentItem.getColor());
			currentPart.prefHeightProperty().bind(chart.heightProperty());
			chart.getChildren().add(currentPart);

			double value;
			double percentage;
			if(useIncomes)
			{
				value = currentItem.getBudgetIN() / 100.0;
				percentage = value / totalIncomes;
			}
			else
			{
				value = -currentItem.getBudgetOUT() / 100.0;
				percentage = value / totalPayments;
			}

			currentPart.prefWidthProperty().bind(chart.widthProperty().multiply(percentage));
			
			Tooltip tooltip = new Tooltip();
			tooltip.setText(Localization.getString(Strings.TOOLTIP_CHART_CATEGORIES,
			                                       currentItem.getName(),
			                                       Helpers.NUMBER_FORMAT.format(percentage * 100),
			                                       Helpers.getCurrencyString(value, currency)));
			currentPart.setTooltip(tooltip);
		}

		generatedChart.getChildren().add(chart);

		return generatedChart;
	}

	private GridPane generateLegend()
	{
		GridPane legend = new GridPane();
		legend.setPadding(new Insets(10));
		legend.setHgap(20);
		legend.setVgap(10);
		legend.setAlignment(Pos.CENTER);
		legend.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_CHART_LEGEND) + "; -fx-border-color: #212121; -fx-border-width: 1; -fx-border-radius: 5;");

		if(categoryInOutSums.size() == 0)
		{
			return legend;
		}	
		
		ArrayList<HBox> legendItems = new ArrayList<>();
		for(CategoryInOutSum currentItem : categoryInOutSums)
		{			
			legendItems.add(getLegendItem(currentItem.getName(), Color.web(currentItem.getColor())));
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
	
	private VBox generateFullLegend()
	{
		VBox legend = new VBox();
		legend.setPadding(new Insets(10));
		legend.setSpacing(10);
		legend.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_CHART_LEGEND) + "; -fx-border-color: #212121; -fx-border-width: 1; -fx-border-radius: 5;");

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
		
		Label labelHeaderSpacer = new Label();
		labelHeaderSpacer.setMinWidth(20);
		labelHeaderSpacer.setMinHeight(20);
		vboxCircles.getChildren().add(labelHeaderSpacer);
		
		Label labelHeaderName = new Label(Localization.getString(Strings.TITLE_CATEGORIES));
		labelHeaderName.setStyle("-fx-font-weight: bold; -fx-underline: true;");
		labelHeaderName.setMinHeight(20);
		vboxNames.getChildren().add(labelHeaderName);	
		
		Label labelHeaderIn = new Label(Localization.getString(Strings.TITLE_INCOMES));
		labelHeaderIn.setStyle("-fx-font-weight: bold; -fx-underline: true;");
		labelHeaderIn.setMinHeight(20);
		vboxIn.getChildren().add(labelHeaderIn);		
		
		Label labelHeaderOut = new Label(Localization.getString(Strings.TITLE_PAYMENTS));
		labelHeaderOut.setStyle("-fx-font-weight: bold; -fx-underline: true;");
		labelHeaderOut.setMinHeight(20);
		vboxOut.getChildren().add(labelHeaderOut);		
		
		for(CategoryInOutSum currentItem : categoryInOutSums)
		{	
			Label labelCircle = new Label();
			labelCircle.setMinWidth(20);
			labelCircle.setMinHeight(20);
			labelCircle.setStyle("-fx-background-color: " + currentItem.getColor() + "; -fx-background-radius: 50%; -fx-border-width: 1; -fx-border-color: black - fx-border-radius: 50%");
			vboxCircles.getChildren().add(labelCircle);

			Label labelName = new Label(currentItem.getName());
			labelName.setStyle("-fx-font-weight: bold;");
			labelName.setMinHeight(20);
			vboxNames.getChildren().add(labelName);			
			
			String percentageIn = totalIn != 0 ? Helpers.NUMBER_FORMAT.format((currentItem.getBudgetIN() / totalIn)) : "0,00";
			Label labelInSum = new Label("+" + Helpers.getCurrencyString(currentItem.getBudgetIN(), currency) + " (" + percentageIn + "%)");
			labelInSum.setStyle("-fx-font-weight: bold;");
			labelInSum.setMinHeight(20);
			vboxIn.getChildren().add(labelInSum);
			
			String percentageOut = totalOut != 0 ? Helpers.NUMBER_FORMAT.format((currentItem.getBudgetOUT() / totalOut)) : "0,00";
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

	private double getTotal(ArrayList<CategoryInOutSum> categoryInOutSums, boolean useIncomes)
	{		
		double total = 0;
		for(CategoryInOutSum currentItem : categoryInOutSums)
		{
			if(useIncomes)
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
	
	@Override
	public WritableImage export(int width, int height)
	{
		VBox root = new VBox();

        root.setStyle("-fx-background-color: transparent;");
        root.setPadding(new Insets(25));
        root.setSpacing(10);
        
        root.getChildren().add(generate(titleIncomes, true));
        root.getChildren().add(generate(titlePayments, false));
        
        Region spacer = new Region();
        spacer.setMinHeight(25);
        root.getChildren().add(spacer);
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        root.getChildren().add(generateFullLegend());	    

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
	
	@Override
	public double getSuggestedWidth()
	{
		return getWidth() + 50;
	}

	@Override
	public double getSuggestedHeight()
	{
		return CHART_HEIGHT + categoryInOutSums.size() * FULL_LEGEND_ITEM_HEIGHT + 50;
	}
}