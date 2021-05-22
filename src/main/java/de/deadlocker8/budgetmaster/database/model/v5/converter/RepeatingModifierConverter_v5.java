package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.database.Converter;
import de.deadlocker8.budgetmaster.database.model.v5.BackupRepeatingModifier_v5;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifier;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierType;
import de.thecodelabs.utils.util.Localization;

public class RepeatingModifierConverter_v5 implements Converter<RepeatingModifier, BackupRepeatingModifier_v5>
{
	@Override
	public RepeatingModifier convert(BackupRepeatingModifier_v5 backupItem)
	{
		if(backupItem == null)
		{
			return null;
		}

		RepeatingModifierType type = RepeatingModifierType.getByLocalization(Localization.getString(backupItem.getLocalizationKey()));
		return RepeatingModifier.fromModifierType(type, backupItem.getQuantity());
	}
}
