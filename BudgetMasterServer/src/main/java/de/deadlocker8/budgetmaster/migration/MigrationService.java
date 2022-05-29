package de.deadlocker8.budgetmaster.migration;

import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import de.deadlocker8.budgetmaster.utils.DatabaseConfigurationProperties;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

@Service
public class MigrationService
{
	public static final String PREVIOUS_DATABASE_FILE_NAME = "budgetmaster.mv.db";
	public static final String PREVIOUS_DATABASE_FILE_NAME_WITHOUT_EXTENSION = "budgetmaster";

	private final SettingsService settingsService;
	private final Path applicationSupportFolder;
	private final AccountRepository accountRepository;
	private final CategoryRepository categoryRepository;
	private final TransactionRepository transactionRepository;
	private final TemplateRepository templateRepository;
	private final DatabaseConfigurationProperties databaseConfig;
	private final TaskScheduler scheduler;

	private final MigrationTask migrationTask;

	@Autowired
	public MigrationService(SettingsService settingsService, Path applicationSupportFolder, AccountRepository accountRepository, CategoryRepository categoryRepository, TransactionRepository transactionRepository, TemplateRepository templateRepository, DatabaseConfigurationProperties databaseConfig, TaskScheduler scheduler)
	{
		this.settingsService = settingsService;
		this.applicationSupportFolder = applicationSupportFolder;
		this.accountRepository = accountRepository;
		this.categoryRepository = categoryRepository;
		this.transactionRepository = transactionRepository;
		this.templateRepository = templateRepository;
		this.databaseConfig = databaseConfig;
		this.migrationTask = new MigrationTask(applicationSupportFolder, settingsService);
		this.scheduler = scheduler;
	}

	public MigrationSettings getPrefilledMigrationSettings()
	{
		return new MigrationSettings(databaseConfig.getHostname(), databaseConfig.getPort(), databaseConfig.getDatabaseName(), databaseConfig.getUsername(), databaseConfig.getPassword(), databaseConfig.getDatabaseType());
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

	public void startMigration(MigrationArguments migrationArguments)
	{
		migrationTask.setMigrationArguments(migrationArguments);
		scheduler.schedule(migrationTask, new Date());
	}

	public MigrationStatus getMigrationStatus()
	{
		return migrationTask.getMigrationStatus();
	}

	public List<String> getSummary()
	{
		return migrationTask.getSummary();
	}

	public List<String> getCollectedStdout()
	{
		return migrationTask.getCollectedStdout();
	}
}
