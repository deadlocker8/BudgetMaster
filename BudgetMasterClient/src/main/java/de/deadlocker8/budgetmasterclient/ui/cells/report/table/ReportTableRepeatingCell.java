package de.deadlocker8.budgetmasterclient.ui.cells.report.table;

import de.deadlocker8.budgetmaster.logic.report.ReportItem;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

public class ReportTableRepeatingCell extends TableCell<ReportItem, Boolean>
{
	@Override
	protected void updateItem(Boolean item, boolean empty)
	{
		 if(!empty)
         {   
             Label labelRepeating = new Label();
             if(item)
             {
                 labelRepeating.setGraphic(new FontIcon(FontIconType.CALENDAR, 16, Colors.TEXT));
             }
             else
             {
                 labelRepeating.setGraphic(new FontIcon(FontIconType.CALENDAR, 16, Color.TRANSPARENT));
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