package de.deadlocker8.budgetmaster.databasemigrator.steps.category;

import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CategoryProcessor implements ItemProcessor<DestinationCategory, DestinationCategory>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryProcessor.class);

	@Override
	public DestinationCategory process(DestinationCategory category)
	{
		LOGGER.debug("CategoryProcessor: Processing category: {}", category);
		return category;
	}
}
