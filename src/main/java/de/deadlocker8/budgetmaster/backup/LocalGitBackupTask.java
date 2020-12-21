package de.deadlocker8.budgetmaster.backup;

import de.deadlocker8.budgetmaster.Main;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class LocalGitBackupTask implements Runnable
{
	private static final String DATE_PATTERN = "yyyy-MM-dd_HH-mm-ss";

	private final Path gitFolder;

	public LocalGitBackupTask()
	{
		final Path applicationSupportFolder = Main.getApplicationSupportFolder();
		final Path backupFolder = applicationSupportFolder.resolve("backups");
		this.gitFolder = backupFolder.resolve("git/.git");
	}

	@Override
	public void run()
	{
		try
		{
			if(!Files.exists(gitFolder))
			{
				GitHelper.createNewRepository(gitFolder);
			}

			final Repository repository = GitHelper.openRepository(gitFolder);
			final Git git = new Git(repository);

			git.add().addFilepattern("*.mv.db").call();
			GitHelper.commitChanges(git, DateTime.now().toString(DATE_PATTERN));
		}
		catch(IOException | GitAPIException e)
		{
			// TODO: error handling
			e.printStackTrace();
		}
	}
}

