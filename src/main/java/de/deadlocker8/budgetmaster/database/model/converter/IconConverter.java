package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v7.BackupIcon_v7;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.images.Image;

import java.util.List;

public class IconConverter implements Converter<Icon, BackupIcon_v7>
{
	private final List<Image> availableImages;

	public IconConverter(List<Image> availableImages)
	{
		this.availableImages = availableImages;
	}


	public Icon convertToInternalForm(BackupIcon_v7 backupIcon)
	{
		if(backupIcon == null)
		{
			return null;
		}

		final Icon icon = new Icon();
		icon.setID(backupIcon.getID());
		icon.setImage(getImageById(backupIcon.getImageID()));
		icon.setBuiltinIdentifier(backupIcon.getBuiltinIdentifier());

		return icon;
	}

	private Image getImageById(Integer iconID)
	{
		return availableImages.stream()
				.filter(icon -> icon.getID().equals(iconID))
				.findFirst().orElse(null);
	}

	@Override
	public BackupIcon_v7 convertToExternalForm(Icon internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupIcon_v7 icon = new BackupIcon_v7();
		icon.setID(internalItem.getID());

		if(internalItem.getImage() != null)
		{
			icon.setImageID(internalItem.getImage().getID());
		}

		icon.setBuiltinIdentifier(internalItem.getBuiltinIdentifier());

		return icon;
	}
}
