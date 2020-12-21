package de.deadlocker8.budgetmaster.backup;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
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

public class RemoteGitBackupTask extends BackupTask
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteGitBackupTask.class);
	private static final String DATE_PATTERN = "yyyy-MM-dd_HH-mm-ss";

	private final Path gitFolder;
	private final UsernamePasswordCredentialsProvider credentialsProvider;
	private final String remote;

	public RemoteGitBackupTask(DatabaseService databaseService, SettingsService settingsService)
	{
		super(databaseService, settingsService);
		this.gitFolder = Main.getApplicationSupportFolder().resolve(".git");

		final Settings settings = settingsService.getSettings();

		this.credentialsProvider = new UsernamePasswordCredentialsProvider(settings.getAutoBackupGitUserName(), settings.getAutoBackupGitPassword());
		this.remote = settings.getAutoBackupGitUrl();
	}

	@Override
	public void run()
	{
		LOGGER.debug(MessageFormat.format("Starting backup with strategy \"{0}\"", AutoBackupStrategy.GIT_REMOTE));

		try
		{
			if(!Files.exists(gitFolder))
			{
				LOGGER.debug(MessageFormat.format("Cloning repository \"{0}\" to \"{1}\"...", remote, gitFolder.getParent()));
				GitHelper.cloneRepository(this.remote, this.credentialsProvider, this.gitFolder.getParent());
			}

			LOGGER.debug(MessageFormat.format("Using git repository: \"{0}\"", gitFolder));
			try(Repository repository = GitHelper.openRepository(gitFolder))
			{
				final Git git = new Git(repository);

				LOGGER.debug(MessageFormat.format("Set remote to \"{0}\"", remote));
				GitHelper.setRemote(git, this.remote);

				LOGGER.debug("Pulling changes from remote...");
				GitHelper.pullLatestChanges(git, credentialsProvider);

				final DirCache dirCache = git.add().addFilepattern(DATABASE_FILE_NAME).call();
				if(dirCache.getEntryCount() != 1)
				{
					throw new RuntimeException(MessageFormat.format("Error adding \"{0}\" to git", DATABASE_FILE_NAME));
				}

				LOGGER.debug(("Committing changes..."));
				GitHelper.commitChanges(git, DateTime.now().toString(DATE_PATTERN));

				LOGGER.debug(("Pushing commits to remote..."));
				GitHelper.push(git, this.credentialsProvider);

				LOGGER.debug("Backup DONE");
			}
		}
		catch(IOException | GitAPIException | URISyntaxException | GitHelper.PullException e)
		{
			// TODO: error handling
			e.printStackTrace();
		}
	}
}
