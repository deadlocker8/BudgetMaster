package de.deadlocker8.budgetmaster.databasemigrator.steps.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class CategoryStepListener implements StepExecutionListener
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryStepListener.class);

	@Override
	public void beforeStep(StepExecution stepExecution)
	{
		LOGGER.info("\n");
		LOGGER.info(">>> Migrate categories...");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution)
	{
		final int count = stepExecution.getReadCount();
		LOGGER.info(">>> Successfully migrated {} categories\n", count);
		return null;
	}
}
