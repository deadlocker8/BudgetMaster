package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v4.BackupRepeatingEndOption_v4;
import de.deadlocker8.budgetmaster.repeating.endoption.*;
import de.thecodelabs.utils.util.Localization;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RepeatingEndOptionConverter implements Converter<RepeatingEnd, BackupRepeatingEndOption_v4>
{
	@Override
	public RepeatingEnd convertToInternalForm(BackupRepeatingEndOption_v4 backupItem)
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
				LocalDate endDate = LocalDate.parse(backupItem.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				endOption = new RepeatingEndDate(endDate);
				break;
		}

		return endOption;
	}

	@Override
	public BackupRepeatingEndOption_v4 convertToExternalForm(RepeatingEnd internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupRepeatingEndOption_v4 repeatingEndOption = new BackupRepeatingEndOption_v4();
		repeatingEndOption.setLocalizationKey(internalItem.getLocalizationKey());

		if(internalItem instanceof RepeatingEndDate repeatingEndDate)
		{
			final LocalDate endDate = (LocalDate) repeatingEndDate.getValue();
			repeatingEndOption.setEndDate(endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		}

		if(internalItem instanceof RepeatingEndAfterXTimes repeatingEndAfterXTimes)
		{
			repeatingEndOption.setTimes((int) repeatingEndAfterXTimes.getValue());
		}

		return repeatingEndOption;
	}
}
