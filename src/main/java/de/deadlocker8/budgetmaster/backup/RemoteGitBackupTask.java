package de.deadlocker8.budgetmaster.backup;

import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;


public class RemoteGitBackupTask extends GitBackupTask
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteGitBackupTask.class);

	public RemoteGitBackupTask(DatabaseService databaseService, SettingsService settingsService)
	{
		super(databaseService, settingsService);
	}

	@Override
	protected AutoBackupStrategy getBackupStrategy()
	{
		return AutoBackupStrategy.GIT_REMOTE;
	}

	@Override
	public void run()
	{
		final Settings settings = settingsService.getSettings();
		final UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(settings.getAutoBackupGitUserName(), settings.getAutoBackupGitToken());
		final String remote = settings.getAutoBackupGitUrl();

		try
		{
			if(!Files.exists(gitFolder))
			{
				LOGGER.debug(MessageFormat.format("Cloning repository \"{0}\" to \"{1}\"...", remote, gitFolder.getParent()));
				GitHelper.cloneRepository(remote, credentialsProvider, this.gitFolder.getParent());
			}

			LOGGER.debug("Exporting database...");
			exportDatabase();

			handleChanges(credentialsProvider, remote, settings.getAutoBackupGitBranchName());
		}
		catch(IOException | GitAPIException | URISyntaxException e)
		{
			e.printStackTrace();
			setBackupStatus(BackupStatus.ERROR);
		}
	}

	private void handleChanges(UsernamePasswordCredentialsProvider credentialsProvider, String remote, String branchName) throws GitAPIException, IOException, URISyntaxException
	{
		LOGGER.debug(MessageFormat.format("Using git repository: \"{0}\"", gitFolder));
		try(Repository repository = GitHelper.openRepository(gitFolder))
		{
			final Git git = new Git(repository);

			renameBranch(git, repository.getBranch(), branchName);

			LOGGER.debug(MessageFormat.format("Set remote to \"{0}\"", remote));
			GitHelper.replaceRemote(git, remote);

			final boolean needsPush = addAndCommitChanges(git);
			if(needsPush)
			{
				LOGGER.debug(("Pushing commits to remote..."));
				GitHelper.push(git, credentialsProvider);
			}

			setBackupStatus(BackupStatus.OK);
			LOGGER.debug("Backup DONE");
		}
	}

	private void renameBranch(Git git, String currentBranchName, String newBranchName) throws GitAPIException
	{
		if(currentBranchName.equals(newBranchName))
		{
			return;
		}

		LOGGER.debug(MessageFormat.format("Rename branch from \"{0}\" to \"{1}\"", currentBranchName, newBranchName));
		git.branchRename().setOldName(currentBranchName).setNewName(newBranchName).call();
	}

	@Override
	public void cleanup(Settings previousSettings, Settings newSettings)
	{
		if(!needsCleanup(previousSettings, newSettings))
		{
			return;
		}

		final Path folderToDelete = gitFolder.getParent();
		LOGGER.debug(MessageFormat.format("Deleting folder and all contents: \"{0}\"", folderToDelete));
		try
		{
			FileSystemUtils.deleteRecursively(folderToDelete);
		}
		catch(IOException e)
		{
			LOGGER.error(MessageFormat.format("Error deleting folder: \"{0}\"", folderToDelete), e);
			e.printStackTrace();
		}
	}

	@Override
	public boolean needsCleanup(Settings previousSettings, Settings newSettings)
	{
		if(!previousSettings.getAutoBackupGitUrl().equals(newSettings.getAutoBackupGitUrl()))
		{
			return true;
		}

		if(!previousSettings.getAutoBackupGitBranchName().equals(newSettings.getAutoBackupGitBranchName()))
		{
			return true;
		}

		return false;
	}
}
