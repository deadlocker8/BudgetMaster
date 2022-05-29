package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v4.BackupRepeatingOption_v4;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

		LocalDate startDate = LocalDate.parse(backupItem.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
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
		repeatingOption.setStartDate(internalItem.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		repeatingOption.setModifier(new RepeatingModifierConverter().convertToExternalForm(internalItem.getModifier()));
		repeatingOption.setEndOption(new RepeatingEndOptionConverter().convertToExternalForm(internalItem.getEndOption()));
		return repeatingOption;
	}
}
