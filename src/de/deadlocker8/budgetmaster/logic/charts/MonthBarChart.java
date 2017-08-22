package de.deadlocker8.budgetmaster.logic.charts;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.CategoryInOutSum;
import de.deadlocker8.budgetmaster.logic.MonthInOutSum;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tools.ConvertTo;
import tools.Localization;

public class MonthBarChart extends VBox implements ChartExportable
{
	private ArrayList<MonthInOutSum> monthInOutSums;
	private String currency;

	public MonthBarChart(ArrayList<MonthInOutSum> monthInOutSums, String currency)
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
		
		ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setFocusTraversable(false);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-border-color: transparent; -fx-border-width: 0; -fx-border-insets: 0;");
        scrollPane.setPadding(new Insets(0, 0, 10, 0));
       
        HBox generatedChart = generate();              
        scrollPane.setContent(generatedChart);
        generatedChart.prefHeightProperty().bind(scrollPane.heightProperty().subtract(30));
        this.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        this.getChildren().add(generateLegend());
	}

	private HBox generate()
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
			currentPart.setStyle("-fx-background-color: " + currentItem.getColor());
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

			Tooltip tooltip = new Tooltip();
			tooltip.setText(Localization.getString(Strings.TOOLTIP_CHART_CATEGORIES,
                                                    currentItem.getName(),
                                                    Helpers.NUMBER_FORMAT.format(percentage * 100),
                                                    Helpers.getCurrencyString(value, currency)));
			currentPart.setTooltip(tooltip);
		}

		result.getChildren().add(chart);
		VBox.setVgrow(chart, Priority.ALWAYS);

		return result;
	}

	private GridPane generateLegend()
	{
		GridPane legend = new GridPane();
		legend.setPadding(new Insets(10));
		legend.setHgap(20);
		legend.setVgap(10);
		legend.setAlignment(Pos.CENTER);
		legend.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_CHART_LEGEND) + "; -fx-border-color: #212121; -fx-border-width: 1; -fx-border-radius: 5;");
		
		if(monthInOutSums.size() == 0)
		{
			return legend;
		}		
		
		ArrayList<HBox> legendItems = new ArrayList<>();
		for(CategoryInOutSum currentItem : monthInOutSums.get(0).getSums())
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

    @Override
    public WritableImage export(int width, int height) 
    {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: transparent;");
        root.setPadding(new Insets(25));
        
        HBox generatedChart = generate();
        root.getChildren().add(generatedChart);        
        VBox.setVgrow(generatedChart, Priority.ALWAYS);
        
        Region spacer = new Region();
        spacer.setMinHeight(30);
		root.getChildren().add(spacer);
        
        root.getChildren().add(generateLegend());       
        
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
    	if(this.getChildren().size() < 2)
    	{
    		return 0;
    	}
    	
    	Node currentNode = this.getChildren().get(0);
    	
    	if(!(currentNode instanceof ScrollPane))
    	{
    		return 0;
    	}
    	
    	ScrollPane scrollPane = (ScrollPane)currentNode; 
    	Node content = scrollPane.getContent();
    	if(content == null)
    	{
    		return 0;
    	}    	
    	
    	return ((Region)content).getWidth() + 50;
	}

	@Override
	public double getSuggestedHeight()
	{
		return getHeight() + 50;
	}
}