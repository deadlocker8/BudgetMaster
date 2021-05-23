package de.deadlocker8.budgetmaster.database.model.v6.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v5.BackupImage_v5;
import de.deadlocker8.budgetmaster.images.Image;

public class ImageConverter implements Converter<Image, BackupImage_v5>
{
	public Image convertToInternalForm(BackupImage_v5 backupImage)
	{
		if(backupImage == null)
		{
			return null;
		}

		final Image image = new Image();
		image.setID(backupImage.getID());
		image.setFileName(backupImage.getFileName());
		image.setFileExtension(backupImage.getFileExtension());
		image.setImage(backupImage.getImage());
		return image;
	}

	@Override
	public BackupImage_v5 convertToExternalForm(Image internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupImage_v5 image = new BackupImage_v5();
		image.setID(internalItem.getID());
		image.setFileName(internalItem.getFileName());
		image.setFileExtension(internalItem.getFileExtension());
		image.setImage(internalItem.getImage());
		return image;
	}
}
