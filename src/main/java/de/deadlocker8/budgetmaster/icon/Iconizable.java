package de.deadlocker8.budgetmaster.icon;

import de.deadlocker8.budgetmaster.services.AccessEntityByID;
import de.deadlocker8.budgetmaster.utils.ProvidesID;

import java.util.Optional;

public interface Iconizable extends ProvidesID
{
	void setIconReference(Icon icon);

	Icon getIconReference();

	default <T extends Iconizable> void updateIcon(IconService iconService, Integer iconImageID, String builtinIconIdentifier, String fontColor, AccessEntityByID<T> itemService)
	{
		// the reference to an existing icon may already be null in the item that should be updated
		// (e.g. user deletes the icon for the item and hit the save button)
		// therefore the existing item must be retrieved from database by id
		if(this.getID() != null)
		{
			final Optional<T> existingItemOptional = itemService.findById(this.getID());
			if(existingItemOptional.isPresent())
			{
				final Iconizable existingItem = existingItemOptional.get();
				iconService.deleteIcon(existingItem.getIconReference());
			}
		}

		final Optional<Icon> iconOptional = iconService.createIconReference(iconImageID, builtinIconIdentifier, fontColor);
		if(iconOptional.isEmpty())
		{
			this.setIconReference(null);
			return;
		}

		final Icon icon = iconOptional.get();
		iconService.getRepository().save(icon);
		this.setIconReference(icon);
	}

	String getFontColor();
}
