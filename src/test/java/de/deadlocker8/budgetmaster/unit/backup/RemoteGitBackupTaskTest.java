package de.deadlocker8.budgetmaster.unit.backup;

import de.deadlocker8.budgetmaster.backup.AutoBackupStrategy;
import de.deadlocker8.budgetmaster.backup.BackupStatus;
import de.deadlocker8.budgetmaster.backup.BackupTask;
import de.deadlocker8.budgetmaster.backup.RemoteGitBackupTask;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.database.model.v7.BackupCategory_v7;
import de.deadlocker8.budgetmaster.database.model.v7.BackupDatabase_v7;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.unit.helpers.Helpers;
import de.deadlocker8.budgetmaster.utils.DateHelper;
import de.thecodelabs.utils.util.OS;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class RemoteGitBackupTaskTest
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
	}

	@Test
	void test_needsCleanup_false_everythingEquals()
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_REMOTE);
		previousSettings.setAutoBackupGitUrl("123");
		previousSettings.setAutoBackupGitBranchName("master");
		previousSettings.setAutoBackupGitUserName("deadlocker8");
		previousSettings.setAutoBackupGitToken("0815");

		final RemoteGitBackupTask remoteGitBackupTask = new RemoteGitBackupTask(databaseService, settingsService);

		assertThat(remoteGitBackupTask.needsCleanup(previousSettings, previousSettings))
				.isFalse();
	}

	@Test
	void test_needsCleanup_false_onlyNameChanged()
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_REMOTE);
		previousSettings.setAutoBackupGitUrl("123");
		previousSettings.setAutoBackupGitBranchName("master");
		previousSettings.setAutoBackupGitUserName("deadlocker8");
		previousSettings.setAutoBackupGitToken("0815");

		final Settings newSettings = Settings.getDefault();
		newSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_REMOTE);
		newSettings.setAutoBackupGitUrl("123");
		newSettings.setAutoBackupGitBranchName("master");
		newSettings.setAutoBackupGitUserName("xyz");
		newSettings.setAutoBackupGitToken("0815");

		final RemoteGitBackupTask remoteGitBackupTask = new RemoteGitBackupTask(databaseService, settingsService);

		assertThat(remoteGitBackupTask.needsCleanup(previousSettings, newSettings)).isFalse();
	}

	@Test
	void test_needsCleanup_true_urlChanged()
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_REMOTE);
		previousSettings.setAutoBackupGitUrl("123");
		previousSettings.setAutoBackupGitBranchName("master");
		previousSettings.setAutoBackupGitUserName("deadlocker8");
		previousSettings.setAutoBackupGitToken("0815");

		final Settings newSettings = Settings.getDefault();
		newSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_REMOTE);
		newSettings.setAutoBackupGitUrl("456");
		newSettings.setAutoBackupGitBranchName("master");
		newSettings.setAutoBackupGitUserName("deadlocker8");
		newSettings.setAutoBackupGitToken("0815");

		final RemoteGitBackupTask remoteGitBackupTask = new RemoteGitBackupTask(databaseService, settingsService);

		assertThat(remoteGitBackupTask.needsCleanup(previousSettings, newSettings))
				.isTrue();
	}

	@Test
	void test_needsCleanup_true_branchNameChanged()
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_REMOTE);
		previousSettings.setAutoBackupGitUrl("123");
		previousSettings.setAutoBackupGitBranchName("master");
		previousSettings.setAutoBackupGitUserName("deadlocker8");
		previousSettings.setAutoBackupGitToken("0815");

		final Settings newSettings = Settings.getDefault();
		newSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_REMOTE);
		newSettings.setAutoBackupGitUrl("123");
		newSettings.setAutoBackupGitBranchName("main");
		newSettings.setAutoBackupGitUserName("deadlocker8");
		newSettings.setAutoBackupGitToken("0815");

		final RemoteGitBackupTask remoteGitBackupTask = new RemoteGitBackupTask(databaseService, settingsService);

		assertThat(remoteGitBackupTask.needsCleanup(previousSettings, newSettings))
				.isTrue();
	}

	@Test
	void test_runBackup_firstCommit() throws IOException
	{
		try(MockedStatic<DateHelper> dateHelper = Mockito.mockStatic(DateHelper.class))
		{
			dateHelper.when(DateHelper::getCurrentDateTime).thenReturn(LocalDateTime.of(2021, 1, 30, 12, 0, 0));

			// create fake server
			final Path fakeServerFolder = Files.createDirectory(tempFolder.resolve("server"));
			final Path repositoryFolder = Files.createDirectory(tempFolder.resolve("client")).resolve(".git");

			final RemoteGitBackupTask remoteGitBackupTask = createBackupTask(repositoryFolder, fakeServerFolder);
			remoteGitBackupTask.run();

			// check local git
			assertThat(remoteGitBackupTask.getBackupStatus()).
					isEqualByComparingTo(BackupStatus.OK);
			assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
					gitExecutable, "rev-list", "--all", "--count"))
					.isEqualTo("1");
			assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
					gitExecutable, "log", "--pretty=%B", "-1"))
					.startsWith("2021-01-30");
			assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
					gitExecutable, "show", "--name-only", "--oneline", "HEAD"))
					.contains(BackupTask.DATABASE_FILE_NAME);

			// check remote git
			assertThat(Helpers.runCommand(fakeServerFolder.toFile(),
					gitExecutable, "rev-list", "--all", "--count"))
					.isEqualTo("1");
			assertThat(Helpers.runCommand(fakeServerFolder.toFile(),
					gitExecutable, "log", "--pretty=%B", "-1"))
					.startsWith("2021-01-30");
			assertThat(Helpers.runCommand(fakeServerFolder.toFile(),
					gitExecutable, "show", "--name-only", "--oneline", "HEAD"))
					.contains(BackupTask.DATABASE_FILE_NAME);
		}
	}

	@Test
	void test_runBackup_fileNotChanged() throws IOException
	{
		// create fake server
		final Path fakeServerFolder = Files.createDirectory(tempFolder.resolve("server"));
		final Path repositoryFolder = Files.createDirectory(tempFolder.resolve("client")).resolve(".git");

		final RemoteGitBackupTask remoteGitBackupTask = createBackupTask(repositoryFolder, fakeServerFolder);
		remoteGitBackupTask.run();
		remoteGitBackupTask.run();

		// check local git
		assertThat(remoteGitBackupTask.getBackupStatus())
				.isEqualByComparingTo(BackupStatus.OK);
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
				gitExecutable, "rev-list", "--all", "--count"))
				.isEqualTo("1");

		// check remote git
		assertThat(Helpers.runCommand(fakeServerFolder.toFile(),
				gitExecutable, "rev-list", "--all", "--count"))
				.isEqualTo("1");
	}

	@Test
	void test_runBackup_fileChanged() throws IOException
	{
		try(MockedStatic<DateHelper> dateHelper = Mockito.mockStatic(DateHelper.class))
		{
			dateHelper.when(DateHelper::getCurrentDateTime).thenReturn(LocalDateTime.of(2021, 1, 30, 12, 0, 0));
			// create fake server
			final Path fakeServerFolder = Files.createDirectory(tempFolder.resolve("server"));
			final Path repositoryFolder = Files.createDirectory(tempFolder.resolve("client")).resolve(".git");

			final RemoteGitBackupTask remoteGitBackupTask = createBackupTask(repositoryFolder, fakeServerFolder);
			remoteGitBackupTask.run();

			final BackupDatabase_v7 databaseModified = new BackupDatabase_v7(List.of(new BackupCategory_v7(5, "myCategory", "#FF0000", CategoryType.CUSTOM, null)), List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
			Mockito.when(databaseService.getDatabaseForJsonSerialization()).thenReturn(databaseModified);
			remoteGitBackupTask.run();

			// check local git
			assertThat(remoteGitBackupTask.getBackupStatus()).isEqualByComparingTo(BackupStatus.OK);
			assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
					gitExecutable, "rev-list", "--all", "--count"))
					.isEqualTo("2");
			assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
					gitExecutable, "log", "--pretty=%B", "-1"))
					.startsWith("2021-01-30");
			assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(),
					gitExecutable, "show", "--name-only", "--oneline", "HEAD"))
					.contains(BackupTask.DATABASE_FILE_NAME);

			// check remote git
			assertThat(Helpers.runCommand(fakeServerFolder.toFile(),
					gitExecutable, "rev-list", "--all", "--count"))
					.isEqualTo("2");
			assertThat(Helpers.runCommand(fakeServerFolder.toFile(),
					gitExecutable, "log", "--pretty=%B", "-1"))
					.startsWith("2021-01-30");
			assertThat(Helpers.runCommand(fakeServerFolder.toFile(),
					gitExecutable, "show", "--name-only", "--oneline", "HEAD"))
					.contains(BackupTask.DATABASE_FILE_NAME);
		}
	}

	private RemoteGitBackupTask createBackupTask(Path repositoryFolder, Path fakeServerFolder)
	{
		Helpers.runCommand(fakeServerFolder.toFile(), gitExecutable, "init", "--bare");

		final Settings settings = Settings.getDefault();
		settings.setAutoBackupStrategy(AutoBackupStrategy.GIT_REMOTE);
		settings.setAutoBackupGitUrl("file://" + fakeServerFolder.toString());
		settings.setAutoBackupGitBranchName("master");
		settings.setAutoBackupGitUserName("deadlocker8");
		settings.setAutoBackupGitToken("0815");
		Mockito.when(settingsService.getSettings()).thenReturn(settings);

		final BackupDatabase_v7 database = new BackupDatabase_v7();
		Mockito.when(databaseService.getDatabaseForJsonSerialization()).thenReturn(database);
		Mockito.doCallRealMethod().when(databaseService).exportDatabase(Mockito.any());

		final RemoteGitBackupTask remoteGitBackupTask = new RemoteGitBackupTask(databaseService, settingsService);
		remoteGitBackupTask.setGitFolder(repositoryFolder);
		return remoteGitBackupTask;
	}
}