package de.deadlocker8.budgetmaster.databasemigrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableScheduling
public class SchedulerConfig
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerConfig.class);

	final JobLauncher jobLauncher;
	final JobOperator jobOperator;
	final Job job;

	@Autowired
	public SchedulerConfig(JobLauncher jobLauncher, JobOperator jobOperator, Job job)
	{
		this.jobLauncher = jobLauncher;
		this.jobOperator = jobOperator;
		this.job = job;
	}

	@Scheduled(fixedDelay = Long.MAX_VALUE, initialDelay = 1000)
	public void scheduleByFixedRate() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException
	{
		LOGGER.info("Starting migration...");
		final JobParameters jobParameters = new JobParametersBuilder()
				.addString("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
				.toJobParameters();
		final JobExecution execution = jobLauncher.run(job, jobParameters);

		LOGGER.info("Migration DONE");

		System.exit(0);
	}
}
