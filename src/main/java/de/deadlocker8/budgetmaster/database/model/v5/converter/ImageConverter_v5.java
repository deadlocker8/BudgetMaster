package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.database.Converter;
import de.deadlocker8.budgetmaster.database.model.v5.BackupImage_v5;
import de.deadlocker8.budgetmaster.images.Image;

public class ImageConverter_v5 implements Converter<Image, BackupImage_v5>
{
	public Image convert(BackupImage_v5 backupImage)
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
}
