package de.deadlocker8.budgetmaster.logic.chartGenerators;

import javafx.scene.image.WritableImage;

public interface ChartExportable
{
	WritableImage export(int width, int height);
}
