package de.deadlocker8.budgetmaster.databasemigrator.listener;

import de.deadlocker8.budgetmaster.databasemigrator.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class GenericStepListener implements StepExecutionListener
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericStepListener.class);

	private final String itemName;

	public GenericStepListener(String itemName)
	{
		this.itemName = itemName;
	}

	@Override
	public void beforeStep(StepExecution stepExecution)
	{
		LOGGER.info("\n");
		LOGGER.info(">>> Migrate {}s...", itemName);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution)
	{
		final int count = Utils.getCommitCount(stepExecution);
		LOGGER.info(">>> Successfully migrated {} {}s\n", count, itemName);
		return null;
	}
}
