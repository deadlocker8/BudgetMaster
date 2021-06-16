package de.deadlocker8.budgetmaster.icon;

public interface Iconizable
{
	void setIconReference(Icon icon);

	Icon getIconReference();

	static void updateIcon(IconService iconService, Integer iconImageID, String builtinIconIdentifier, Iconizable iconizable)
	{
		final Icon iconReference = iconService.createIconReference(iconImageID, builtinIconIdentifier);
		if(iconReference != null)
		{
			iconService.getRepository().save(iconReference);
		}

		iconService.deleteIcon(iconizable.getIconReference());
		iconizable.setIconReference(iconReference);
	}
}
