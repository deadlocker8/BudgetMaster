package de.deadlocker8.budgetmaster.databasemigrator.listener;

import de.deadlocker8.budgetmaster.databasemigrator.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
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
		LOGGER.info("==================================================");
		LOGGER.info("### Migration results ###");
		for(StepExecution stepExecution : jobExecution.getStepExecutions())
		{
			final BatchStatus status = stepExecution.getStatus();
			final String name = stepExecution.getStepName();
			final int commitCount = Utils.getCommitCount(stepExecution);
			final int readCount = stepExecution.getReadCount();
			LOGGER.info("[{}] {}: {}/{}", status, name, commitCount, readCount);
		}
		LOGGER.info("==================================================\n");
	}
}
