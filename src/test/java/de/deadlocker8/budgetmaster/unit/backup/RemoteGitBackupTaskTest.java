package de.deadlocker8.budgetmaster.unit.backup;

import de.deadlocker8.budgetmaster.backup.*;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.unit.helpers.Helpers;
import de.thecodelabs.utils.util.OS;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class RemoteGitBackupTaskTest
{
	@Mock
	private DatabaseService databaseService;

	@Mock
	private SettingsService settingsService;

	@Rule
	public final TemporaryFolder tempFolder = new TemporaryFolder();

	private static String gitExecutable;


	@BeforeClass
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
	public void test_needsCleanup_false_everythingEquals()
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_REMOTE);
		previousSettings.setAutoBackupGitUrl("123");
		previousSettings.setAutoBackupGitBranchName("master");
		previousSettings.setAutoBackupGitUserName("deadlocker8");
		previousSettings.setAutoBackupGitToken("0815");

		final RemoteGitBackupTask remoteGitBackupTask = new RemoteGitBackupTask(databaseService, settingsService);

		assertFalse(remoteGitBackupTask.needsCleanup(previousSettings, previousSettings));
	}

	@Test
	public void test_needsCleanup_false_onlyNameChanged()
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

		assertFalse(remoteGitBackupTask.needsCleanup(previousSettings, newSettings));
	}

	@Test
	public void test_needsCleanup_true_urlChanged()
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

		assertTrue(remoteGitBackupTask.needsCleanup(previousSettings, newSettings));
	}

	@Test
	public void test_needsCleanup_true_branchNameChanged()
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

		assertTrue(remoteGitBackupTask.needsCleanup(previousSettings, newSettings));
	}

	@Test
	public void test_runBackup_firstCommit() throws IOException
	{
		// create fake server
		final Path fakeServerFolder = tempFolder.newFolder("server").toPath();
		final Path repositoryFolder = tempFolder.newFolder().toPath().resolve(".git");

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

	@Test
	public void test_runBackup_fileNotChanged() throws IOException
	{
		// create fake server
		final Path fakeServerFolder = tempFolder.newFolder("server").toPath();
		final Path repositoryFolder = tempFolder.newFolder().toPath().resolve(".git");

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
	public void test_runBackup_fileChanged() throws IOException
	{
		// create fake server
		final Path fakeServerFolder = tempFolder.newFolder("server").toPath();
		final Path repositoryFolder = tempFolder.newFolder().toPath().resolve(".git");

		final RemoteGitBackupTask remoteGitBackupTask = createBackupTask(repositoryFolder, fakeServerFolder);
		remoteGitBackupTask.run();

		final Database databaseModified = new Database(List.of(new Category("myCategory", "#FF0000", CategoryType.CUSTOM)), List.of(), List.of(), List.of());
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

		final Database database = new Database();
		Mockito.when(databaseService.getDatabaseForJsonSerialization()).thenReturn(database);
		Mockito.doCallRealMethod().when(databaseService).exportDatabase(Mockito.any());

		final RemoteGitBackupTask remoteGitBackupTask = new RemoteGitBackupTask(databaseService, settingsService);
		remoteGitBackupTask.setGitFolder(repositoryFolder);
		return remoteGitBackupTask;
	}
}