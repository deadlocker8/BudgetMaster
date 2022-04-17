package de.deadlocker8.budgetmaster.migration;

import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MigrationService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MigrationService.class);

	public static final String PREVIOUS_DATABASE_FILE_NAME = "budgetmaster.mv.db";
	public static final String PREVIOUS_DATABASE_FILE_NAME_WITHOUT_EXTENSION = "budgetmaster";
	private final SettingsService settingsService;
	private final Path applicationSupportFolder;
	private final AccountRepository accountRepository;
	private final CategoryRepository categoryRepository;
	private final TransactionRepository transactionRepository;
	private final TemplateRepository templateRepository;

	@Autowired
	public MigrationService(SettingsService settingsService, Path applicationSupportFolder, AccountRepository accountRepository, CategoryRepository categoryRepository, TransactionRepository transactionRepository, TemplateRepository templateRepository)
	{
		this.settingsService = settingsService;
		this.applicationSupportFolder = applicationSupportFolder;
		this.accountRepository = accountRepository;
		this.categoryRepository = categoryRepository;
		this.transactionRepository = transactionRepository;
		this.templateRepository = templateRepository;
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

	public String runMigration(MigrationArguments migrationArguments) throws MigrationException
	{
		// TODO: extract BudgetMasterMigrator.jar from resources to tmp folder

		return runMigrator(migrationArguments);
	}

	private String runMigrator(MigrationArguments migrationArguments) throws MigrationException
	{
		final String javaCommand = determineJavaCommand();

		final List<String> command = new ArrayList<>();
		command.add(MessageFormat.format("\"{0}\"", javaCommand));
		command.add("-jar");
		command.add("C:/Programmierung/BudgetMaster/BudgetMasterDatabaseMigrator/target/BudgetMasterDatabaseMigrator-v2.10.0.jar");
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
