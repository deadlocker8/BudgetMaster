package de.deadlocker8.budgetmaster.backup;

import de.deadlocker8.budgetmaster.Main;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class RemoteGitBackupTask implements Runnable
{
	private static final String DATE_PATTERN = "yyyy-MM-dd_HH-mm-ss";

	private final Path gitFolder;
	private final UsernamePasswordCredentialsProvider credentialsProvider;
	private final String remote;

	public RemoteGitBackupTask()
	{
		final Path applicationSupportFolder = Main.getApplicationSupportFolder();
		final Path backupFolder = applicationSupportFolder.resolve("backups");
		this.gitFolder = backupFolder.resolve("git/.git");

		this.credentialsProvider = new UsernamePasswordCredentialsProvider("", "");
		this.remote = "https://thecodelabs.de/deadlocker8/bm_test.git";
	}

	@Override
	public void run()
	{
		try
		{
			if(!Files.exists(gitFolder))
			{
				GitHelper.cloneRepository(this.remote, this.credentialsProvider, this.gitFolder.getParent());
			}

			final Repository repository = GitHelper.openRepository(gitFolder);
			final Git git = new Git(repository);

			GitHelper.setRemote(git, this.remote);
			GitHelper.pullLatestChanges(git, credentialsProvider);

			git.add().addFilepattern("*.mv.db").call();
			GitHelper.commitChanges(git, DateTime.now().toString(DATE_PATTERN));
			GitHelper.push(git, this.credentialsProvider);
		}
		catch(IOException | GitAPIException | URISyntaxException | GitHelper.PullException e)
		{
			e.printStackTrace();
		}
	}
}

