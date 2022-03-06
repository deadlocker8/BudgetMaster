package de.deadlocker8.budgetmaster.databasemigrator.steps.category;

import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategory;
import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Component
public class CategoryWriter implements ItemWriter<DestinationCategory>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryWriter.class);

	final DestinationCategoryRepository destinationCategoryRepository;

	public CategoryWriter(DestinationCategoryRepository destinationCategoryRepository)
	{
		this.destinationCategoryRepository = destinationCategoryRepository;
	}

	@Override
	public void write(List<? extends DestinationCategory> list) throws Exception
	{
		for(DestinationCategory data : list)
		{
			LOGGER.debug("CategoryWriter: Writing category: {}", data);
			destinationCategoryRepository.save(data);
		}
	}
}
