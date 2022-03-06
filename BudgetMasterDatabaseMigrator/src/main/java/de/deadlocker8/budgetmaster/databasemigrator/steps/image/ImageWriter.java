package de.deadlocker8.budgetmaster.databasemigrator.steps.image;

import de.deadlocker8.budgetmaster.databasemigrator.destination.image.DestinationImage;
import de.deadlocker8.budgetmaster.databasemigrator.destination.image.DestinationImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageWriter implements ItemWriter<DestinationImage>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageWriter.class);

	final DestinationImageRepository destinationImageRepository;

	public ImageWriter(DestinationImageRepository destinationImageRepository)
	{
		this.destinationImageRepository = destinationImageRepository;
	}

	@Override
	public void write(List<? extends DestinationImage> list)
	{
		for(DestinationImage data : list)
		{
			LOGGER.debug("ImageWriter: Writing image: {}", data);
			destinationImageRepository.save(data);
		}
	}
}
