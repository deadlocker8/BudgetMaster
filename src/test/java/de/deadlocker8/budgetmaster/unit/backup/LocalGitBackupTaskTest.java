package de.deadlocker8.budgetmaster.unit.backup;

import de.deadlocker8.budgetmaster.backup.AutoBackupStrategy;
import de.deadlocker8.budgetmaster.backup.BackupStatus;
import de.deadlocker8.budgetmaster.backup.LocalGitBackupTask;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
public class LocalGitBackupTaskTest
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
	public void test_needsCleanup()
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_LOCAL);

		final LocalGitBackupTask localGitBackupTask = new LocalGitBackupTask(databaseService, settingsService);

		assertThat(localGitBackupTask.needsCleanup(previousSettings, previousSettings)).isFalse();
	}

	@Test
	public void test_runBackup_repositoryNotExisting() throws IOException
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_LOCAL);

		final Path repositoryFolder = tempFolder.newFolder().toPath().resolve(".git");

		final LocalGitBackupTask localGitBackupTask = new LocalGitBackupTask(databaseService, settingsService);
		localGitBackupTask.setGitFolder(repositoryFolder);
		localGitBackupTask.run();

		assertThat(localGitBackupTask.getBackupStatus()).isEqualByComparingTo(BackupStatus.OK);
		assertThat(Files.exists(repositoryFolder)).isTrue();
	}

	@Test
	public void test_runBackup_firstCommit() throws IOException
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_LOCAL);

		final Path repositoryFolder = tempFolder.newFolder().toPath().resolve(".git");

		final Database database = new Database();
		Mockito.when(databaseService.getDatabaseForJsonSerialization()).thenReturn(database);
		Mockito.doCallRealMethod().when(databaseService).exportDatabase(Mockito.any());

		final LocalGitBackupTask localGitBackupTask = new LocalGitBackupTask(databaseService, settingsService);
		localGitBackupTask.setGitFolder(repositoryFolder);
		localGitBackupTask.run();

		assertThat(localGitBackupTask.getBackupStatus()).isEqualByComparingTo(BackupStatus.OK);
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(), gitExecutable, "rev-list", "--all", "--count")).isEqualTo("1");
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(), gitExecutable, "log", "--pretty=%B", "-1")).startsWith("2021-01-30");
	}

	@Test
	public void test_runBackup_fileNotChanged() throws IOException
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_LOCAL);

		final Path repositoryFolder = tempFolder.newFolder().toPath().resolve(".git");

		final Database database = new Database();
		Mockito.when(databaseService.getDatabaseForJsonSerialization()).thenReturn(database);
		Mockito.doCallRealMethod().when(databaseService).exportDatabase(Mockito.any());

		final LocalGitBackupTask localGitBackupTask = new LocalGitBackupTask(databaseService, settingsService);
		localGitBackupTask.setGitFolder(repositoryFolder);
		localGitBackupTask.run();
		localGitBackupTask.run();

		assertThat(localGitBackupTask.getBackupStatus()).isEqualByComparingTo(BackupStatus.OK);
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(), gitExecutable, "rev-list", "--all", "--count")).isEqualTo("1");
	}

	@Test
	public void test_runBackup_fileChanged() throws IOException
	{
		final Settings previousSettings = Settings.getDefault();
		previousSettings.setAutoBackupStrategy(AutoBackupStrategy.GIT_LOCAL);

		final Path repositoryFolder = tempFolder.newFolder().toPath().resolve(".git");

		final Database database = new Database();
		Mockito.when(databaseService.getDatabaseForJsonSerialization()).thenReturn(database);
		Mockito.doCallRealMethod().when(databaseService).exportDatabase(Mockito.any());

		final LocalGitBackupTask localGitBackupTask = new LocalGitBackupTask(databaseService, settingsService);
		localGitBackupTask.setGitFolder(repositoryFolder);
		localGitBackupTask.run();

		final Database databaseModified = new Database(List.of(new Category("myCategory", "#FF0000", CategoryType.CUSTOM)), List.of(), List.of(), List.of());
		Mockito.when(databaseService.getDatabaseForJsonSerialization()).thenReturn(databaseModified);
		localGitBackupTask.run();

		assertThat(localGitBackupTask.getBackupStatus()).isEqualByComparingTo(BackupStatus.OK);
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(), gitExecutable, "rev-list", "--all", "--count")).isEqualTo("2");
		assertThat(Helpers.runCommand(repositoryFolder.getParent().toFile(), gitExecutable, "log", "--pretty=%B", "-1")).startsWith("2021-01-30");
	}
}