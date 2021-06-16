package de.deadlocker8.budgetmaster.icon;

import java.util.Optional;

public interface Iconizable
{
	void setIconReference(Icon icon);

	Icon getIconReference();

	static void updateIcon(IconService iconService, Integer iconImageID, String builtinIconIdentifier, Iconizable iconizable)
	{
		iconService.deleteIcon(iconizable.getIconReference());

		final Optional<Icon> iconOptional = iconService.createIconReference(iconImageID, builtinIconIdentifier);
		if(iconOptional.isEmpty())
		{
			iconizable.setIconReference(null);
			return;
		}

		final Icon icon = iconOptional.get();
		iconService.getRepository().save(icon);
		iconizable.setIconReference(icon);
	}
}
