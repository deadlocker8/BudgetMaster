package de.deadlocker8.budgetmaster.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.*;

public class MigrationTask implements Runnable
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MigrationTask.class);
	private static final String BUDGET_MASTER_MIGRATOR_JAR = "BudgetMasterDatabaseMigrator.jar";

	private final Path applicationSupportFolder;

	private MigrationArguments migrationArguments;
	private MigrationStatus migrationStatus;
	private List<String> collectedStdout;

	public MigrationTask(Path applicationSupportFolder)
	{
		this.applicationSupportFolder = applicationSupportFolder;
		this.migrationStatus = MigrationStatus.NOT_RUNNING;
	}

	public void setMigrationArguments(MigrationArguments migrationArguments)
	{
		this.migrationArguments = migrationArguments;
		this.collectedStdout = new ArrayList<>();
	}

	@Override
	public void run()
	{
		if(migrationArguments == null)
		{
			throw new RuntimeException("No migration arguments set!");
		}

		if(migrationStatus != MigrationStatus.NOT_RUNNING)
		{
			throw new RuntimeException("Migration already performed!");
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
				Files.deleteIfExists(migratorPath);
			}

			setMigrationStatus(MigrationStatus.SUCCESS);
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
		command.add(MessageFormat.format("\"{0}\"", javaCommand));
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
			throw new MigrationException("Error during migration process", e);
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
}
