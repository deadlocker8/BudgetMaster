package de.deadlocker8.budgetmaster.unit.backup;

import de.deadlocker8.budgetmaster.backup.AutoBackupStrategy;
import de.deadlocker8.budgetmaster.backup.RemoteGitBackupTask;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RemoteGitBackupTaskTest
{
	@Mock
	private DatabaseService databaseService;

	@Mock
	private SettingsService settingsService;

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
}