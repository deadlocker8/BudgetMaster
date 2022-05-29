package de.deadlocker8.budgetmaster.databasemigrator.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class GenericDoNothingProcessor<T> implements ItemProcessor<T, T>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericDoNothingProcessor.class);

	@Override
	public T process(T item)
	{
		LOGGER.debug("GenericDoNothingProcessor: Processing item: {}", item);
		return item;
	}
}
