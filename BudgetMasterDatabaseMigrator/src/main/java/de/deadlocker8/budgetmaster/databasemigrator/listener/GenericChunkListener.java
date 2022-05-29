package de.deadlocker8.budgetmaster.databasemigrator.listener;

import de.deadlocker8.budgetmaster.databasemigrator.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class GenericChunkListener implements ChunkListener
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericChunkListener.class);

	private final String itemName;

	private int numberOfProcessedItems = 0;

	public GenericChunkListener(String itemName)
	{
		this.itemName = itemName;
	}

	@Override
	public void beforeChunk(ChunkContext context)
	{
		// nothing to do
	}

	@Override
	public void afterChunk(ChunkContext context)
	{
		final int count = Utils.getCommitCount(context.getStepContext().getStepExecution());
		if(count > numberOfProcessedItems)
		{
			numberOfProcessedItems++;
			LOGGER.info("Migrating {} {}", itemName, count);
		}
	}

	@Override
	public void afterChunkError(ChunkContext context)
	{
		// nothing to do
	}
}
