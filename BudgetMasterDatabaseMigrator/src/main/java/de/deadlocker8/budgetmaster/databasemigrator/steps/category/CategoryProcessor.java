package de.deadlocker8.budgetmaster.databasemigrator.steps.category;

import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategory;
import de.deadlocker8.budgetmaster.databasemigrator.source.category.SourceCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CategoryProcessor implements ItemProcessor<SourceCategory, DestinationCategory>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryProcessor.class);

	@Override
	public DestinationCategory process(SourceCategory category)
	{
		LOGGER.debug("CategoryProcessor: Processing category: {}", category);

		final DestinationCategory destinationCategory = new DestinationCategory();
		destinationCategory.setName(category.getName());
		destinationCategory.setColor(category.getColor());
		destinationCategory.setType(category.getType());
		return destinationCategory;
	}
}
