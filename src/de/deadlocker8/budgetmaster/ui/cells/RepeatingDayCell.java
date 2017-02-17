package de.deadlocker8.budgetmaster.ui.cells;

import javafx.scene.control.ListCell;

public class RepeatingDayCell extends ListCell<Integer>
{			
	@Override
	protected void updateItem(Integer item, boolean empty)
	{
		super.updateItem(item, empty);

		if(!empty && item != 0)
		{			
			setText(String.valueOf(item));
		}
		else
		{			
			setText(null);			
		}
	}
}