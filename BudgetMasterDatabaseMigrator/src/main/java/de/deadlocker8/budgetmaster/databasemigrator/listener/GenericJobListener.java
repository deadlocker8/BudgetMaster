package de.deadlocker8.budgetmaster.databasemigrator.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;

public class GenericJobListener implements JobExecutionListener
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericJobListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution)
	{
		// nothing to do
	}

	@Override
	public void afterJob(JobExecution jobExecution)
	{
		LOGGER.info("\n");
		LOGGER.info("=========================\n");
		LOGGER.info("=== Migration results ===");
		for(StepExecution stepExecution : jobExecution.getStepExecutions())
		{
			LOGGER.info("{}: {}", stepExecution.getStepName().split(" ")[1], stepExecution.getReadCount());
		}
		LOGGER.info("=========================\n");
	}
}
