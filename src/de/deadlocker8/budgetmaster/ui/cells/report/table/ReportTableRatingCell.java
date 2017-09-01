package de.deadlocker8.budgetmaster.ui.cells.report.table;

import de.deadlocker8.budgetmaster.logic.report.ReportItem;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import fontAwesome.FontIconType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;

public class ReportTableRatingCell extends TableCell<ReportItem, Integer>
{
	@Override
	protected void updateItem(Integer item, boolean empty)
	{
		if(!empty)
        {                               
            Label labelRepeating = new Label();
            if(item > 0)
            {
                labelRepeating.setGraphic(Helpers.getFontIcon(FontIconType.PLUS, 14, Colors.TEXT));
            }
            else
            {
                labelRepeating.setGraphic(Helpers.getFontIcon(FontIconType.MINUS, 14, Colors.TEXT));
            }
            labelRepeating.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #212121");
            labelRepeating.setAlignment(Pos.CENTER);
            setGraphic(labelRepeating);
        }
        else
        {
            setGraphic(null);
        }
	}
}