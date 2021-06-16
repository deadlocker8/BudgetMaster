package de.deadlocker8.budgetmaster.icon;

import java.util.Optional;

public interface Iconizable
{
	void setIconReference(Icon icon);

	Icon getIconReference();

	default void updateIcon(IconService iconService, Integer iconImageID, String builtinIconIdentifier)
	{
		iconService.deleteIcon(this.getIconReference());

		final Optional<Icon> iconOptional = iconService.createIconReference(iconImageID, builtinIconIdentifier);
		if(iconOptional.isEmpty())
		{
			this.setIconReference(null);
			return;
		}

		final Icon icon = iconOptional.get();
		iconService.getRepository().save(icon);
		this.setIconReference(icon);
	}
}
