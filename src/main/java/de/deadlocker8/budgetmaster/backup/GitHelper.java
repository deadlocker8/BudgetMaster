package de.deadlocker8.budgetmaster.backup;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class GitHelper
{
	static class PullException extends Exception
	{
		public PullException(String message)
		{
			super(message);
		}
	}

	private GitHelper()
	{
	}

	public static void cloneRepository(String uri, CredentialsProvider credentialsProvider, Path targetFolder) throws GitAPIException
	{
		Git.cloneRepository()
				.setURI(uri)
				.setCredentialsProvider(credentialsProvider)
				.setDirectory(targetFolder.toFile())
				.call();
	}

	public static void createNewRepository(Path gitFolder) throws IOException
	{
		try(Repository newRepo = FileRepositoryBuilder.create(gitFolder.toFile()))
		{
			newRepo.create();
		}
	}

	public static Repository openRepository(Path gitFolder) throws IOException
	{
		return new FileRepositoryBuilder()
				.setGitDir(gitFolder.toFile())
				.build();
	}

	public static void setRemote(Git git, String remote) throws URISyntaxException, GitAPIException
	{
		git.remoteAdd().setName("origin").setUri(new URIish(remote)).call();
	}

	public static void pullLatestChanges(Git git, CredentialsProvider credentialsProvider) throws GitAPIException, PullException
	{
		final PullResult result = git.pull()
				.setCredentialsProvider(credentialsProvider)
				.setRemote("origin")
				.setRemoteBranchName("master")
				.call();

		if(!result.isSuccessful())
		{
			throw new PullException("Error while pulling changes.");
		}
	}

	public static void commitChanges(Git git, String message) throws GitAPIException
	{
		final CommitCommand commit = git.commit();
		commit.setMessage(message).call();
	}

	public static void push(Git git, CredentialsProvider credentialsProvider) throws GitAPIException
	{
		final PushCommand pushCommand = git.push();
		pushCommand.setCredentialsProvider(credentialsProvider);
		pushCommand.call();
	}
}

