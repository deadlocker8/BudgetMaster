package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.database.Converter;
import de.deadlocker8.budgetmaster.database.model.v4.BackupRepeatingOption_v4;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class RepeatingOptionConverter_v5 implements Converter<RepeatingOption, BackupRepeatingOption_v4>
{
	@Override
	public RepeatingOption convert(BackupRepeatingOption_v4 backupItem)
	{
		if(backupItem == null)
		{
			return null;
		}

		final RepeatingOption repeatingOption = new RepeatingOption();
		repeatingOption.setStartDate(DateTime.parse(backupItem.getStartDate(), DateTimeFormat.forPattern("yyyy-MM-dd")));
		repeatingOption.setModifier(new RepeatingModifierConverter_v5().convert(backupItem.getModifier()));
		repeatingOption.setEndOption(new RepeatingEndOptionConverter_v5().convert(backupItem.getEndOption()));
		return repeatingOption;
	}
}
