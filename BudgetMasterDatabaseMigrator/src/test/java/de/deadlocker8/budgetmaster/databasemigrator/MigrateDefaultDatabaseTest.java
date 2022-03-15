package de.deadlocker8.budgetmaster.databasemigrator;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MigrateDefaultDatabaseTest extends MigratorTestBase
{
	@Test
	void test_jobMigrate() throws Exception
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchJob(DEFAULT_JOB_PARAMETERS);

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
	}

	@Test
	void test_stepMigrateImages_noImages()
	{
		final JobExecution jobExecution = jobLauncherTestUtils.launchStep("Migrate images", DEFAULT_JOB_PARAMETERS);
		final List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());

		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

		assertThat(stepExecutions).hasSize(1);
		final StepExecution stepExecution = stepExecutions.get(0);
		assertThat(stepExecution.getReadCount()).isZero();
		assertThat(stepExecution.getCommitCount()).isOne();
	}
}