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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public class GitHelper
{
	private static final String REMOTE_NAME = "origin";

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

	public static void replaceRemote(Git git, String remote) throws URISyntaxException, GitAPIException
	{
		git.remoteRemove().setRemoteName(REMOTE_NAME).call();
		git.remoteAdd().setName(REMOTE_NAME).setUri(new URIish(remote)).call();
	}

	public static void pullLatestChanges(Git git, CredentialsProvider credentialsProvider, String branchName) throws GitAPIException, PullException
	{
		final PullResult result = git.pull()
				.setCredentialsProvider(credentialsProvider)
				.setRemote(REMOTE_NAME)
				.setRemoteBranchName(branchName)
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

	public static Optional<String> checkConnection(String uri, CredentialsProvider credentialsProvider)
	{
		try
		{
			final Path tempDirectory = Files.createTempDirectory("TestGitRepo");
			cloneRepository(uri, credentialsProvider, tempDirectory);
		}
		catch(GitAPIException | IOException e)
		{
			e.printStackTrace();
			return Optional.of(e.getMessage());
		}

		return Optional.empty();
	}

	public static boolean isFileModified(Git git, String fileName) throws GitAPIException
	{
		final Set<String> modifiedFiles = git.status().call().getModified();
		return modifiedFiles.contains(fileName);
	}

	public static boolean isFileAddedOrChanged(Git git, String fileName) throws GitAPIException
	{
		final Set<String> changedFiles = git.status().call().getChanged();
		final Set<String> addedFiles = git.status().call().getAdded();
		return changedFiles.contains(fileName) || addedFiles.contains(fileName);
	}

	public static boolean isFileUntracked(Git git, String fileName) throws GitAPIException
	{
		final Set<String> modifiedFiles = git.status().call().getUntracked();
		return modifiedFiles.contains(fileName);
	}
}
