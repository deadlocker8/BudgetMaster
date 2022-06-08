package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageRepository;
import de.deadlocker8.budgetmaster.services.EntityType;

public class ImageImporter extends ItemImporter<Image>
{
	public ImageImporter(ImageRepository imageRepository)
	{
		super(imageRepository, EntityType.IMAGE);
	}

	@Override
	protected int importSingleItem(Image image) throws ImportException
	{
		if(!(repository instanceof ImageRepository repository))
		{
			throw new IllegalArgumentException("Invalid repository type");
		}

		final Image imageToCreate = new Image(image.getImage(), image.getFileName(), image.getFileExtension());

		final Image savedImage = repository.save(imageToCreate);
		return savedImage.getID();
	}

	@Override
	protected String getNameForItem(Image item)
	{
		return String.valueOf(item.getID());
	}
}
