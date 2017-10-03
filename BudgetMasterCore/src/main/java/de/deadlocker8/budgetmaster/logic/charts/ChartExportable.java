package de.deadlocker8.budgetmaster.logic.charts;

import javafx.scene.image.WritableImage;

public interface ChartExportable
{
	WritableImage export(int width, int height);
	
	double getWidth();
	
	double getHeight();
	
	double getSuggestedWidth();
	
	double getSuggestedHeight();
}
