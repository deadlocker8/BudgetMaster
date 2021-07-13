package de.deadlocker8.budgetmaster.unit.backup;

import de.deadlocker8.budgetmaster.backup.AutoBackupStrategy;
import de.deadlocker8.budgetmaster.backup.BackupStatus;
import de.deadlocker8.budgetmaster.backup.BackupTask;
import de.deadlocker8.budgetmaster.backup.LocalGitBackupTask;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.database.model.v7.BackupCategory_v7;
import de.deadlocker8.budgetmaster.database.model.v7.BackupDatabase_v7;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.unit.helpers.Helpers;
import de.thecodelabs.utils.util.OS;
import org.joda.time.DateTimeUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
class LocalGitBackupTaskTest
{
	@Mock
	private DatabaseService databaseService;

	@Mock
	private SettingsService settingsService;

	@TempDir
	public Path tempFolder;

	private static String gitExecutable;


	@BeforeAll
	public static void setup()
	{
		if(OS.isWindows())
		{
			gitExecutable = "C:\\Program Files\\Git\\cmd\\git.exe";
		}
		else
		{
			gitExecutable = "/usr/bin/git";
		}

		DateTimeUtils.setCurrentMillisFixed(1612004400000L);
	}

	@AfterAll
	public static void cleanup()
	{
		DateTimeUtils.setCurrentMillisSystem();
	}

	@Test
	void test_needsCleanup()
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_LOCAL);

		final LocalGitBackupTask localGitBackupTask = new LocalGitBackupTask(databaseService, settingsService);

		assertThat(localGitBackupTask.needsCleanup(previousSettings, previousSettings)).isFalse();
	}

	@Test
	void test_runBackup_repositoryNotExisting()
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_LOCAL);

		final Path repositoryFolder = tempFolder.resolve(".git");

		final LocalGitBackupTask localGitBackupTask = new LocalGitBackupTask(databaseService, settingsService);
		localGitBackupTask.setGitFolder(repositoryFolder);
		localGitBackupTask.run();

		assertThat(localGitBackupTask.getBackupStatus())
				.isEqualByComparingTo(BackupStatus.OK);
		assertThat(Files.exists(repositoryFolder)).isTrue();
	}

	@Test
	void test_runBackup_firstCommit()
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_LOCAL);

		final Path repositoryFolder = tempFolder.resolve(".git");

		final BackupDatabase_v7 database = new BackupDatabase_v7();
		Mockito.when(databaseService.getDatabaseForJsonSerialization()).thenReturn(database);
		Mockito.doCallRealMethod().when(databaseService).exportDatabase(Mockito.any());

		final LocalGitBackupTask localGitBackupTask = new LocalGitBackupTask(databaseService, settingsService);
		localGitBackupTask.setGitFolder(repositoryFolder);
		localGitBackupTask.run();

		assertThat(localGitBackupTask.getBackupStatus()).isEqualByComparingTo(BackupStatus.OK);
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
				gitExecutable, "rev-list", "--all", "--count"))
				.isEqualTo("1");
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
				gitExecutable, "log", "--pretty=%B", "-1"))
				.startsWith("2021-01-30");
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
				gitExecutable, "show", "--name-only", "--oneline", "HEAD"))
				.contains(BackupTask.DATABASE_FILE_NAME);
	}

	@Test
	void test_runBackup_fileNotChanged()
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_LOCAL);

		final Path repositoryFolder = tempFolder.resolve(".git");

		final BackupDatabase_v7 database = new BackupDatabase_v7();
		Mockito.when(databaseService.getDatabaseForJsonSerialization()).thenReturn(database);
		Mockito.doCallRealMethod().when(databaseService).exportDatabase(Mockito.any());

		final LocalGitBackupTask localGitBackupTask = new LocalGitBackupTask(databaseService, settingsService);
		localGitBackupTask.setGitFolder(repositoryFolder);
		localGitBackupTask.run();
		localGitBackupTask.run();

		assertThat(localGitBackupTask.getBackupStatus())
				.isEqualByComparingTo(BackupStatus.OK);
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
				gitExecutable, "rev-list", "--all", "--count"))
				.isEqualTo("1");
	}

	@Test
	void test_runBackup_fileChanged()
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_LOCAL);

		final Path repositoryFolder = tempFolder.resolve(".git");

		final BackupDatabase_v7 database = new BackupDatabase_v7();
		Mockito.when(databaseService.getDatabaseForJsonSerialization()).thenReturn(database);
		Mockito.doCallRealMethod().when(databaseService).exportDatabase(Mockito.any());

		final LocalGitBackupTask localGitBackupTask = new LocalGitBackupTask(databaseService, settingsService);
		localGitBackupTask.setGitFolder(repositoryFolder);
		localGitBackupTask.run();

		final BackupDatabase_v7 databaseModified = new BackupDatabase_v7(List.of(new BackupCategory_v7(5, "myCategory", "#FF0000", CategoryType.CUSTOM, null)), List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
		Mockito.when(databaseService.getDatabaseForJsonSerialization()).thenReturn(databaseModified);
		localGitBackupTask.run();

		assertThat(localGitBackupTask.getBackupStatus())
				.isEqualByComparingTo(BackupStatus.OK);
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
				gitExecutable, "rev-list", "--all", "--count"))
				.isEqualTo("2");
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
				gitExecutable, "log", "--pretty=%B", "-1"))
				.startsWith("2021-01-30");
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
				gitExecutable, "show", "--name-only", "--oneline", "HEAD"))
				.contains(BackupTask.DATABASE_FILE_NAME);
	}
}