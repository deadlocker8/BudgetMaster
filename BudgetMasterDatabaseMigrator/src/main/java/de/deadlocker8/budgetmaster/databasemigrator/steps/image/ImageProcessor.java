package de.deadlocker8.budgetmaster.databasemigrator.steps.image;

import de.deadlocker8.budgetmaster.databasemigrator.destination.image.DestinationImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ImageProcessor implements ItemProcessor<DestinationImage, DestinationImage>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessor.class);

	@Override
	public DestinationImage process(DestinationImage image)
	{
		LOGGER.debug("ImageProcessor: Processing image: {}", image);
		return image;
	}
}
