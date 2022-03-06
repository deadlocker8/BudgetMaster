package de.deadlocker8.budgetmaster.databasemigrator.steps.image;

import de.deadlocker8.budgetmaster.databasemigrator.destination.image.DestinationImage;
import de.deadlocker8.budgetmaster.databasemigrator.source.image.SourceImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ImageProcessor implements ItemProcessor<SourceImage, DestinationImage>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessor.class);

	@Override
	public DestinationImage process(SourceImage image)
	{
		LOGGER.debug("ImageProcessor: Processing image: {}", image);

		final DestinationImage destinationImage = new DestinationImage();
		destinationImage.setFileExtension(image.getFileExtension());
		destinationImage.setFileName(image.getFileName());
		destinationImage.setImage(image.getImage());
		return destinationImage;
	}
}
