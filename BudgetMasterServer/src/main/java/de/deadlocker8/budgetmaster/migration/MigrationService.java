package de.deadlocker8.budgetmaster.migration;

import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import de.deadlocker8.budgetmaster.utils.DatabaseConfigurationProperties;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class MigrationService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MigrationService.class);

	public static final String PREVIOUS_DATABASE_FILE_NAME = "budgetmaster.mv.db";
	public static final String PREVIOUS_DATABASE_FILE_NAME_WITHOUT_EXTENSION = "budgetmaster";
	private static final String BUDGET_MASTER_MIGRATOR_JAR = "BudgetMasterDatabaseMigrator.jar";
	private final SettingsService settingsService;
	private final Path applicationSupportFolder;
	private final AccountRepository accountRepository;
	private final CategoryRepository categoryRepository;
	private final TransactionRepository transactionRepository;
	private final TemplateRepository templateRepository;
	private final DatabaseConfigurationProperties databaseConfig;

	@Autowired
	public MigrationService(SettingsService settingsService, Path applicationSupportFolder, AccountRepository accountRepository, CategoryRepository categoryRepository, TransactionRepository transactionRepository, TemplateRepository templateRepository, DatabaseConfigurationProperties databaseConfig)
	{
		this.settingsService = settingsService;
		this.applicationSupportFolder = applicationSupportFolder;
		this.accountRepository = accountRepository;
		this.categoryRepository = categoryRepository;
		this.transactionRepository = transactionRepository;
		this.templateRepository = templateRepository;
		this.databaseConfig = databaseConfig;
	}

	public MigrationSettings getPrefilledMigrationSettings()
	{
		return new MigrationSettings(databaseConfig.getHostname(), databaseConfig.getPort(), databaseConfig.getDatabaseName(), databaseConfig.getUsername(), databaseConfig.getPassword());
	}

	public boolean needToShowMigrationDialog(String loadedPage)
	{
		loadedPage = "/" + loadedPage;
		if(loadedPage.equals(Mappings.MIGRATION))
		{
			return false;
		}

		if(!isDatabaseEmpty())
		{
			return false;
		}

		if(!isDatabaseFromPreviousVersionExisting())
		{
			return false;
		}

		return !settingsService.getSettings().getMigrationDeclined();
	}

	private boolean isDatabaseEmpty()
	{
		if(accountRepository.findAll().size() > 2)
		{
			return false;
		}

		if(!categoryRepository.findAllByTypeOrderByNameAsc(CategoryType.CUSTOM).isEmpty())
		{
			return false;
		}

		if(!transactionRepository.findAll().isEmpty())
		{
			return false;
		}

		return templateRepository.findAll().isEmpty();
	}

	private Path getDatabaseFromPreviousVersionPath()
	{
		return applicationSupportFolder.resolve(PREVIOUS_DATABASE_FILE_NAME);
	}

	public Path getDatabaseFromPreviousVersionPathWithoutExtension()
	{
		return applicationSupportFolder.resolve(PREVIOUS_DATABASE_FILE_NAME_WITHOUT_EXTENSION);
	}

	private boolean isDatabaseFromPreviousVersionExisting()
	{
		return Files.exists(getDatabaseFromPreviousVersionPath());
	}

	public String runMigration(MigrationArguments migrationArguments) throws MigrationException, IOException
	{
		final Path migratorPath = extractMigrator();
		try
		{
			return runMigrator(migratorPath, migrationArguments);
		}
		finally
		{
			Files.deleteIfExists(migratorPath);
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

	private String runMigrator(Path migratorPath, MigrationArguments migrationArguments) throws MigrationException
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

			final StringBuilder collectedStdout = new StringBuilder();
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

					collectedStdout.append(line);
					collectedStdout.append("\n");
				}
			}

			LOGGER.debug("Migration process finished");
			return collectedStdout.toString();
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
}
