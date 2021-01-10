package de.deadlocker8.budgetmaster.backup;

import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;

public class LocalGitBackupTask extends GitBackupTask
{
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalGitBackupTask.class);

	public LocalGitBackupTask(DatabaseService databaseService, SettingsService settingsService)
	{
		super(databaseService, settingsService);
	}

	@Override
	protected AutoBackupStrategy getBackupStrategy()
	{
		return AutoBackupStrategy.GIT_LOCAL;
	}

	@Override
	public void run()
	{
		try
		{
			if(!Files.exists(gitFolder))
			{
				LOGGER.debug(MessageFormat.format("Creating new repository: \"{0}\"", gitFolder));
				GitHelper.createNewRepository(gitFolder);
			}

			LOGGER.debug("Exporting database...");
			exportDatabase();

			LOGGER.debug(MessageFormat.format("Using git repository: \"{0}\"", gitFolder));
			try(Repository repository = GitHelper.openRepository(gitFolder))
			{
				final Git git = new Git(repository);
				addAndCommitChanges(git);

				setHasErrors(false);
				LOGGER.debug("Backup DONE");
			}
		}
		catch(IOException | GitAPIException e)
		{
			e.printStackTrace();
			setHasErrors(true);
		}
	}
}

