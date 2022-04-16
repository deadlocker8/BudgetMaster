package de.deadlocker8.budgetmaster.migration;

import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class MigrationService
{
	public static final String PREVIOUS_DATABASE_FILE_NAME = "budgetmaster.mv.db";
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

	private boolean isDatabaseFromPreviousVersionExisting()
	{
		final Path previousDatabasePath = applicationSupportFolder.resolve(PREVIOUS_DATABASE_FILE_NAME);
		return Files.exists(previousDatabasePath);
	}
}
