package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v8.BackupIcon_v8;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.images.Image;

import java.util.List;

public class IconConverter implements Converter<Icon, BackupIcon_v8>
{
	private final List<Image> availableImages;

	public IconConverter(List<Image> availableImages)
	{
		this.availableImages = availableImages;
	}


	public Icon convertToInternalForm(BackupIcon_v8 backupIcon)
	{
		if(backupIcon == null)
		{
			return null;
		}

		final Icon icon = new Icon();
		icon.setID(backupIcon.getID());
		icon.setImage(getImageById(backupIcon.getImageID()));
		icon.setBuiltinIdentifier(backupIcon.getBuiltinIdentifier());
		icon.setFontColor(backupIcon.getFontColor());

		return icon;
	}

	private Image getImageById(Integer iconID)
	{
		return availableImages.stream()
				.filter(icon -> icon.getID().equals(iconID))
				.findFirst().orElse(null);
	}

	@Override
	public BackupIcon_v8 convertToExternalForm(Icon internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupIcon_v8 icon = new BackupIcon_v8();
		icon.setID(internalItem.getID());

		if(internalItem.getImage() != null)
		{
			icon.setImageID(internalItem.getImage().getID());
		}

		icon.setBuiltinIdentifier(internalItem.getBuiltinIdentifier());
		icon.setFontColor(internalItem.getFontColor());

		return icon;
	}
}
