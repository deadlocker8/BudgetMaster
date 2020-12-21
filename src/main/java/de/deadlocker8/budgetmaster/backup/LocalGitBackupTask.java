package de.deadlocker8.budgetmaster.backup;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Repository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

public class LocalGitBackupTask extends BackupTask
{
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalGitBackupTask.class);
	private static final String DATE_PATTERN = "yyyy-MM-dd_HH-mm-ss";

	private final Path gitFolder;

	public LocalGitBackupTask(DatabaseService databaseService, SettingsService settingsService)
	{
		super(databaseService, settingsService);
		this.gitFolder = Main.getApplicationSupportFolder().resolve(".git");
	}

	@Override
	public void run()
	{
		LOGGER.debug(MessageFormat.format("Starting backup with strategy \"{0}\"", AutoBackupStrategy.GIT_LOCAL));

		try
		{
			if(!Files.exists(gitFolder))
			{
				LOGGER.debug(MessageFormat.format("Creating new repository: \"{0}\"", gitFolder));
				GitHelper.createNewRepository(gitFolder);
			}

			LOGGER.debug(MessageFormat.format("Using git repository: \"{0}\"", gitFolder));
			try(Repository repository = GitHelper.openRepository(gitFolder))
			{
				final Git git = new Git(repository);
				final DirCache dirCache = git.add().addFilepattern(DATABASE_FILE_NAME).call();
				if(dirCache.getEntryCount() != 1)
				{
					throw new RuntimeException(MessageFormat.format("Error adding \"{0}\" to git", DATABASE_FILE_NAME));
				}

				LOGGER.debug(("Committing changes..."));
				GitHelper.commitChanges(git, DateTime.now().toString(DATE_PATTERN));

				LOGGER.debug("Backup DONE");
			}
		}
		catch(IOException | GitAPIException e)
		{
			// TODO: error handling
			e.printStackTrace();
		}
	}
}

