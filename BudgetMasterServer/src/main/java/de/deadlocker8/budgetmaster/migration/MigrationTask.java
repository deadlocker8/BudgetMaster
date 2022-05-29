package de.deadlocker8.budgetmaster.migration;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MigrationTask implements Runnable
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MigrationTask.class);
	private static final String BUDGET_MASTER_MIGRATOR_JAR = "BudgetMasterDatabaseMigrator.jar";
	private static final Pattern PATTERN_SUMMARY = Pattern.compile(".*(\\[COMPLETED\\] Migrate .*)");

	private static final int NUMBER_OF_MIGRATION_STEPS = 25;

	private final Path applicationSupportFolder;

	private final SettingsService settingsService;

	private MigrationArguments migrationArguments;
	private MigrationStatus migrationStatus;
	private List<String> collectedStdout;
	private List<String> summary;

	public MigrationTask(Path applicationSupportFolder, SettingsService settingsService)
	{
		this.applicationSupportFolder = applicationSupportFolder;
		this.settingsService = settingsService;
		this.migrationStatus = MigrationStatus.NOT_RUNNING;
	}

	public void setMigrationArguments(MigrationArguments migrationArguments)
	{
		this.migrationArguments = migrationArguments;
		this.collectedStdout = new ArrayList<>();
		this.summary = new ArrayList<>();
	}

	@Override
	public void run()
	{
		if(migrationArguments == null)
		{
			throw new IllegalArgumentException("No migration arguments set!");
		}

		if(migrationStatus != MigrationStatus.NOT_RUNNING)
		{
			this.migrationStatus = MigrationStatus.NOT_RUNNING;
			this.collectedStdout = new ArrayList<>();
			this.summary = new ArrayList<>();
		}

		LOGGER.debug("Start migration...");
		setMigrationStatus(MigrationStatus.RUNNING);

		try
		{
			final Path migratorPath = extractMigrator();
			try
			{
				runMigrator(migratorPath, migrationArguments);
			}
			finally
			{
				// if any error happens during migration the settings table might be still empty and lead to errors
				// and an infinite progress indicator loop during fetch of the migration status due to SettingsAdvice.class
				// Therefore ensure settings exist at this point
				settingsService.createDefaultSettingsIfNotExists();

				Files.deleteIfExists(migratorPath);
			}

			summary = collectSummary();
			if(summary.size() == NUMBER_OF_MIGRATION_STEPS)
			{
				setMigrationStatus(MigrationStatus.SUCCESS);
			}
			else
			{
				setMigrationStatus(MigrationStatus.ERROR);
			}
		}
		catch(MigrationException | IOException e)
		{
			LOGGER.error("Migration failed", e);
			setMigrationStatus(MigrationStatus.ERROR);
		}
	}

	private Path extractMigrator() throws MigrationException
	{
		final Path destinationPath = applicationSupportFolder.resolve(BUDGET_MASTER_MIGRATOR_JAR);

		try
		{
			Files.copy(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(BUDGET_MASTER_MIGRATOR_JAR)), destinationPath, StandardCopyOption.REPLACE_EXISTING);
			return destinationPath;
		}
		catch(IOException e)
		{
			throw new MigrationException(MessageFormat.format("Could not copy migrator to {0}", destinationPath), e);
		}
	}

	private void runMigrator(Path migratorPath, MigrationArguments migrationArguments) throws MigrationException
	{
		final String javaCommand = determineJavaCommand();

		final List<String> command = new ArrayList<>();
		command.add(javaCommand);
		command.add("-jar");
		command.add(migratorPath.toString());
		command.addAll(migrationArguments.getArguments());
		LOGGER.debug("Starting migration with command: {}", command);

		try
		{
			final ProcessBuilder processBuilder = new ProcessBuilder(command).redirectErrorStream(true);
			final Process process = processBuilder.start();

			try(BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream())))
			{
				while(true)
				{
					String line = in.readLine();
					if(line == null)
					{
						break;
					}

					LOGGER.debug("[MIGRATOR] {}", line);
					collectedStdout.add(line);
				}
			}

			LOGGER.debug("Migration process finished");
		}
		catch(IOException e)
		{
			final MigrationException exception = new MigrationException("Error during migration process", e);
			collectedStdout.add(MessageFormat.format("{0}: {1}", e.getMessage(), e.getCause()));
			throw exception;
		}
	}

	private String determineJavaCommand() throws MigrationException
	{
		final Optional<String> commandResultOptional = ProcessHandle.current().info().command();
		if(commandResultOptional.isEmpty())
		{
			throw new MigrationException("Could not determine java executable");
		}

		return commandResultOptional.get().replace("\\", "/");
	}

	public MigrationStatus getMigrationStatus()
	{
		return migrationStatus;
	}

	public void setMigrationStatus(MigrationStatus migrationStatus)
	{
		this.migrationStatus = migrationStatus;
	}

	public List<String> getCollectedStdout()
	{
		return collectedStdout;
	}

	public List<String> getSummary()
	{
		return summary;
	}

	private List<String> collectSummary()
	{
		if(collectedStdout == null)
		{
			return new ArrayList<>();
		}

		LOGGER.debug("Collecting summary...");

		final List<String> matchingLines = new ArrayList<>();
		for(String line : collectedStdout)
		{
			final Matcher matcher = PATTERN_SUMMARY.matcher(line);
			if(matcher.find())
			{
				matchingLines.add(matcher.group(1));
			}
		}

		LOGGER.debug("Summary collected");
		return matchingLines;
	}
}
