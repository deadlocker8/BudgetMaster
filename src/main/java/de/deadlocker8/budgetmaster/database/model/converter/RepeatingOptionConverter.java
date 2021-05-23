package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v4.BackupRepeatingOption_v4;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class RepeatingOptionConverter implements Converter<RepeatingOption, BackupRepeatingOption_v4>
{
	@Override
	public RepeatingOption convertToInternalForm(BackupRepeatingOption_v4 backupItem)
	{
		if(backupItem == null)
		{
			return null;
		}

		final RepeatingOption repeatingOption = new RepeatingOption();

		DateTime startDate = DateTime.parse(backupItem.getStartDate(), DateTimeFormat.forPattern("yyyy-MM-dd"));
		startDate = startDate.withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
		repeatingOption.setStartDate(startDate);

		repeatingOption.setModifier(new RepeatingModifierConverter().convertToInternalForm(backupItem.getModifier()));
		repeatingOption.setEndOption(new RepeatingEndOptionConverter().convertToInternalForm(backupItem.getEndOption()));
		return repeatingOption;
	}

	@Override
	public BackupRepeatingOption_v4 convertToExternalForm(RepeatingOption internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupRepeatingOption_v4 repeatingOption = new BackupRepeatingOption_v4();
		repeatingOption.setStartDate(internalItem.getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd")));
		repeatingOption.setModifier(new RepeatingModifierConverter().convertToExternalForm(internalItem.getModifier()));
		repeatingOption.setEndOption(new RepeatingEndOptionConverter().convertToExternalForm(internalItem.getEndOption()));
		return repeatingOption;
	}
}
