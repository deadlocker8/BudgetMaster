package de.deadlocker8.budgetmaster.databasemigrator;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DatabaseMigratorMain implements CommandLineRunner
{
	private final JobLauncher jobLauncher;

	@Qualifier("migrateJob")
	private final Job migrateJob;

	public DatabaseMigratorMain(JobLauncher jobLauncher, Job migrateJob)
	{
		this.jobLauncher = jobLauncher;
		this.migrateJob = migrateJob;
	}

	public static void main(String[] args)
	{
		SpringApplication.run(DatabaseMigratorMain.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		jobLauncher.run(migrateJob, new JobParametersBuilder().toJobParameters());
	}
}
