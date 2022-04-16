package de.deadlocker8.budgetmaster.databasemigrator;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMigratorMain.class);

	private final JobLauncher jobLauncher;

	@Qualifier("migrateJob")
	private final Job migrateJob;

	public DatabaseMigratorMain(JobLauncher jobLauncher, Job migrateJob)
	{
		this.jobLauncher = jobLauncher;
		this.migrateJob = migrateJob;
	}

	public static void main(String[] args) throws ParseException
	{
		final Options commandLineOptions = new Options();
		for(CommandLineOptions option : CommandLineOptions.values())
		{
			commandLineOptions.addRequiredOption(null, option.getName(), true, option.getDescription());
		}

		showHelpIfRequested(args, commandLineOptions);

		final CommandLineParser parser = new DefaultParser();
		final CommandLine cmd = parser.parse(commandLineOptions, args);

		for(CommandLineOptions option : CommandLineOptions.values())
		{
			LOGGER.debug("{}={}", option.getName(), cmd.getOptionValue(option.getName()));
		}

		SpringApplication.run(DatabaseMigratorMain.class, args);
	}

	private static void showHelpIfRequested(String[] args, Options availableCommandLineOptions) throws ParseException
	{
		final Options commandLineOptions = new Options();
		commandLineOptions.addOption("h", "help", false, "Print this help");

		final CommandLineParser parser = new DefaultParser();
		final CommandLine cmd = parser.parse(commandLineOptions, args, true);

		if(cmd.hasOption("help"))
		{
			final HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(200);
			formatter.printHelp("BudgetMasterMigrator", availableCommandLineOptions);
			System.exit(0);
		}
	}

	@Override
	public void run(String... args) throws Exception
	{
		jobLauncher.run(migrateJob, new JobParametersBuilder().toJobParameters());
	}
}
