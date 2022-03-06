package de.deadlocker8.budgetmaster.databasemigrator.steps.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class CategoryChunkListener implements ChunkListener
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryChunkListener.class);

	private int numberOfProcessedItems = 0;

	@Override
	public void beforeChunk(ChunkContext context)
	{
		// nothing to do
	}

	@Override
	public void afterChunk(ChunkContext context)
	{
		final int count = context.getStepContext().getStepExecution().getReadCount();
		if(count > numberOfProcessedItems)
		{
			numberOfProcessedItems++;
			LOGGER.info("Migrating category {}", count);
		}
	}

	@Override
	public void afterChunkError(ChunkContext context)
	{
		// nothing to do
	}
}
