package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.database.Converter;
import de.deadlocker8.budgetmaster.database.model.v5.BackupRepeatingEndOption_v5;
import de.deadlocker8.budgetmaster.repeating.endoption.*;
import de.thecodelabs.utils.util.Localization;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class RepeatingEndOptionConverter_v5 implements Converter<RepeatingEnd, BackupRepeatingEndOption_v5>
{
	@Override
	public RepeatingEnd convert(BackupRepeatingEndOption_v5 backupItem)
	{
		if(backupItem == null)
		{
			return null;
		}

		RepeatingEnd endOption = null;
		RepeatingEndType endType = RepeatingEndType.getByLocalization(Localization.getString(backupItem.getLocalizationKey()));
		switch(endType)
		{
			case NEVER:
				endOption = new RepeatingEndNever();
				break;
			case AFTER_X_TIMES:
				endOption = new RepeatingEndAfterXTimes(backupItem.getTimes());
				break;
			case DATE:
				DateTime endDate = DateTime.parse(backupItem.getEndDate(), DateTimeFormat.forPattern("yyyy-MM-dd"));
				endOption = new RepeatingEndDate(endDate);
				break;
		}

		return endOption;
	}
}
