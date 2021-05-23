package de.deadlocker8.budgetmaster.database.model.v6.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v4.BackupRepeatingModifier_v4;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifier;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierType;
import de.thecodelabs.utils.util.Localization;

public class RepeatingModifierConverter implements Converter<RepeatingModifier, BackupRepeatingModifier_v4>
{
	@Override
	public RepeatingModifier convertToInternalForm(BackupRepeatingModifier_v4 backupItem)
	{
		if(backupItem == null)
		{
			return null;
		}

		RepeatingModifierType type = RepeatingModifierType.getByLocalization(Localization.getString(backupItem.getLocalizationKey()));
		return RepeatingModifier.fromModifierType(type, backupItem.getQuantity());
	}

	@Override
	public BackupRepeatingModifier_v4 convertToExternalForm(RepeatingModifier internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		return new BackupRepeatingModifier_v4(internalItem.getQuantity(), internalItem.getLocalizationKey());
	}
}
