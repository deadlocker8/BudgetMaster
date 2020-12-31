package de.deadlocker8.budgetmaster.backup;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Set;

public class RemoteGitBackupTask extends BackupTask
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteGitBackupTask.class);
	private static final String DATE_PATTERN = "yyyy-MM-dd_HH-mm-ss";

	private final Path gitFolder;

	public RemoteGitBackupTask(DatabaseService databaseService, SettingsService settingsService)
	{
		super(databaseService, settingsService);
		this.gitFolder = Main.getApplicationSupportFolder().resolve(".git");
	}

	@Override
	public void run()
	{
		LOGGER.debug(MessageFormat.format("Starting backup with strategy \"{0}\"", AutoBackupStrategy.GIT_REMOTE));

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

			LOGGER.debug(MessageFormat.format("Using git repository: \"{0}\"", gitFolder));
			try(Repository repository = GitHelper.openRepository(gitFolder))
			{
				final Git git = new Git(repository);

				renameBranch(git, repository.getBranch(), settings.getAutoBackupGitBranchName());

				LOGGER.debug(MessageFormat.format("Set remote to \"{0}\"", remote));
				GitHelper.replaceRemote(git, remote);

				LOGGER.debug("Pulling changes from remote...");
				GitHelper.pullLatestChanges(git, credentialsProvider, settings.getAutoBackupGitBranchName());

				// skip if database file is not modified and not (already) staged for commit
				if(!isDatabaseFileModified(git) && !isDatabaseFileAdded(git))
				{
					LOGGER.debug(MessageFormat.format("Skipping commit because \"{0}\" is not modified", DATABASE_FILE_NAME));
					LOGGER.debug("Backup DONE");
					return;
				}

				git.add().addFilepattern(DATABASE_FILE_NAME).call();

				// check if database file is successfully added
				if(!isDatabaseFileAdded(git))
				{
					throw new RuntimeException(MessageFormat.format("Error adding \"{0}\" to git", DATABASE_FILE_NAME));
				}

				LOGGER.debug(("Committing changes..."));
				GitHelper.commitChanges(git, DateTime.now().toString(DATE_PATTERN));

				LOGGER.debug(("Pushing commits to remote..."));
				GitHelper.push(git, credentialsProvider);

				LOGGER.debug("Backup DONE");
			}
		}
		catch(IOException | GitAPIException | URISyntaxException | GitHelper.PullException e)
		{
			// TODO: error handling
			e.printStackTrace();
		}
	}

	private boolean isDatabaseFileModified(Git git) throws GitAPIException
	{
		final Set<String> modifiedFiles = git.status().call().getModified();
		return modifiedFiles.contains(DATABASE_FILE_NAME);
	}

	private boolean isDatabaseFileAdded(Git git) throws GitAPIException
	{
		final Set<String> changedFiles = git.status().call().getChanged();
		return changedFiles.contains(DATABASE_FILE_NAME);
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
}
