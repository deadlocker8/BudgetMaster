package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.migration.MigrationService;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@LocalizedTest
class MigrationServiceTest
{
	@Mock
	private SettingsService settingsService;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private TemplateRepository templateRepository;

	@TempDir
	public Path tempFolder;

	@Test
	void test_needToShowMigrationDialog_migrationPageOpened_returnFalse() throws IOException
	{
		final Settings settings = Settings.getDefault();

		final Path previousDatabase = tempFolder.resolve(MigrationService.PREVIOUS_DATABASE_FILE_NAME);
		Files.createFile(previousDatabase);

		Mockito.when(settingsService.getSettings()).thenReturn(settings);

		final MigrationService migrationService = new MigrationService(settingsService, tempFolder, accountRepository, categoryRepository, transactionRepository, templateRepository);
		assertThat(migrationService.needToShowMigrationDialog(Mappings.MIGRATION)).isFalse();
	}

	@Test
	void test_needToShowMigrationDialog_migrationDeclined_returnFalse() throws IOException
	{
		final Settings settings = Settings.getDefault();
		settings.setMigrationDeclined(true);

		final Path previousDatabase = tempFolder.resolve(MigrationService.PREVIOUS_DATABASE_FILE_NAME);
		Files.createFile(previousDatabase);

		Mockito.when(settingsService.getSettings()).thenReturn(settings);

		final MigrationService migrationService = new MigrationService(settingsService, tempFolder, accountRepository, categoryRepository, transactionRepository, templateRepository);
		assertThat(migrationService.needToShowMigrationDialog(Mappings.TRANSACTIONS)).isFalse();
	}

	@Test
	void test_needToShowMigrationDialog_newDatabaseNotEmpty_returnFalse() throws IOException
	{
		final Settings settings = Settings.getDefault();

		final Path previousDatabase = tempFolder.resolve(MigrationService.PREVIOUS_DATABASE_FILE_NAME);
		Files.createFile(previousDatabase);

		Mockito.when(settingsService.getSettings()).thenReturn(settings);
		Mockito.when(categoryRepository.findAllByTypeOrderByNameAsc(CategoryType.CUSTOM)).thenReturn(List.of(new Category("custom category", "ff0000", CategoryType.CUSTOM)));

		final MigrationService migrationService = new MigrationService(settingsService, tempFolder, accountRepository, categoryRepository, transactionRepository, templateRepository);
		assertThat(migrationService.needToShowMigrationDialog(Mappings.TRANSACTIONS)).isFalse();
	}

	@Test
	void test_needToShowMigrationDialog_noPreviousDatabaseExisting_returnFalse()
	{
		final Settings settings = Settings.getDefault();

		Mockito.when(settingsService.getSettings()).thenReturn(settings);

		final MigrationService migrationService = new MigrationService(settingsService, tempFolder, accountRepository, categoryRepository, transactionRepository, templateRepository);
		assertThat(migrationService.needToShowMigrationDialog(Mappings.TRANSACTIONS)).isFalse();
	}

	@Test
	void test_needToShowMigrationDialog_migrationPageNotOpenend_emptyDestinationDatabase_existingPreviousDatabase_notDeclined_returnTrue() throws IOException
	{
		final Settings settings = Settings.getDefault();

		final Path previousDatabase = tempFolder.resolve(MigrationService.PREVIOUS_DATABASE_FILE_NAME);
		Files.createFile(previousDatabase);

		Mockito.when(settingsService.getSettings()).thenReturn(settings);

		final MigrationService migrationService = new MigrationService(settingsService, tempFolder, accountRepository, categoryRepository, transactionRepository, templateRepository);
		assertThat(migrationService.needToShowMigrationDialog(Mappings.TRANSACTIONS)).isTrue();
	}
}
