package de.deadlocker8.budgetmaster.unit.database;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.tags.TagService;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.unit.helpers.LoggerTestUtil;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class DatabaseServiceTest
{
	@Mock
	private AccountService accountService;

	@Mock
	private SettingsService settingsService;

	@Mock
	private CategoryService categoryService;

	@Mock
	private TransactionService transactionService;

	@Mock
	private TagService tagService;

	@InjectMocks
	private DatabaseService databaseService;

	@Test
	void test_determineFilesToDelete_unlimited() throws URISyntaxException
	{
		ListAppender<ILoggingEvent> loggingAppender = LoggerTestUtil.getListAppenderForClass(DatabaseService.class);

		final Settings mockedSettings = Settings.getDefault();
		mockedSettings.setAutoBackupFilesToKeep(0);
		when(settingsService.getSettings()).thenReturn(mockedSettings);

		final Path backupFolder = Paths.get(getClass().getClassLoader().getResource("").toURI()).resolve("backups/empty");
		final List<Path> filesToDelete = databaseService.determineFilesToDelete(backupFolder);

		assertThat(filesToDelete).isEmpty();

		assertThat(loggingAppender.list)
				.extracting(ILoggingEvent::getMessage, ILoggingEvent::getLevel)
				.contains(Tuple.tuple("Skipping backup rotation since number of files to keep is set to unlimited", Level.DEBUG));
	}

	@Test
	void test_determineFilesToDelete_limitNotReached() throws URISyntaxException
	{
		ListAppender<ILoggingEvent> loggingAppender = LoggerTestUtil.getListAppenderForClass(DatabaseService.class);

		final Settings mockedSettings = Settings.getDefault();
		mockedSettings.setAutoBackupFilesToKeep(5);
		when(settingsService.getSettings()).thenReturn(mockedSettings);

		final Path backupFolder = Paths.get(getClass().getClassLoader().getResource("").toURI()).resolve("backups/three");
		final List<Path> filesToDelete = databaseService.determineFilesToDelete(backupFolder);

		assertThat(filesToDelete).isEmpty();

		assertThat(loggingAppender.list)
				.extracting(ILoggingEvent::getMessage, ILoggingEvent::getLevel)
				.contains(Tuple.tuple("Skipping backup rotation (existing backups: 3, files to keep: 5)", Level.DEBUG));
	}

	@Test
	void test_determineFilesToDelete_limitReachedExactly() throws URISyntaxException
	{
		ListAppender<ILoggingEvent> loggingAppender = LoggerTestUtil.getListAppenderForClass(DatabaseService.class);

		final Settings mockedSettings = Settings.getDefault();
		mockedSettings.setAutoBackupFilesToKeep(3);
		when(settingsService.getSettings()).thenReturn(mockedSettings);

		final Path testResources = Paths.get(getClass().getClassLoader().getResource("").toURI());
		final Path backupFolder = testResources.resolve("backups/three");
		final List<Path> filesToDelete = databaseService.determineFilesToDelete(backupFolder);

		assertThat(filesToDelete)
				.hasSize(1)
				.contains(testResources.resolve("backups/three/BudgetMasterDatabase_2020_03_07_14_10_50.json"));

		assertThat(loggingAppender.list)
				.extracting(ILoggingEvent::getMessage, ILoggingEvent::getLevel)
				.contains(Tuple.tuple("Determining old backups (existing backups: 3, files to keep: 3)", Level.DEBUG));
	}

	@Test
	void test_determineFilesToDelete_limitReached() throws URISyntaxException
	{
		ListAppender<ILoggingEvent> loggingAppender = LoggerTestUtil.getListAppenderForClass(DatabaseService.class);

		final Settings mockedSettings = Settings.getDefault();
		mockedSettings.setAutoBackupFilesToKeep(2);
		when(settingsService.getSettings()).thenReturn(mockedSettings);

		final Path testResources = Paths.get(getClass().getClassLoader().getResource("").toURI());
		final Path backupFolder = testResources.resolve("backups/three");
		final List<Path> filesToDelete = databaseService.determineFilesToDelete(backupFolder);

		assertThat(filesToDelete)
				.hasSize(2)
				.contains(testResources.resolve("backups/three/BudgetMasterDatabase_2020_03_07_14_10_50.json"))
				.contains(testResources.resolve("backups/three/BudgetMasterDatabase_2020_03_08_13_10_50.json"));

		assertThat(loggingAppender.list)
				.extracting(ILoggingEvent::getMessage, ILoggingEvent::getLevel)
				.contains(Tuple.tuple("Determining old backups (existing backups: 3, files to keep: 2)", Level.DEBUG));
	}
}