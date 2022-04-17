package de.deadlocker8.budgetmaster.migration;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MigrationArguments
{
	static class MigrationArgumentBuilder
	{
		private String sourceUrl;
		private String destinationUrl;
		private String destinationUsername;
		private String destinationPassword;

		public MigrationArgumentBuilder withSourceUrl(String databasePath)
		{
			databasePath = databasePath.replace("\\", "/");
			this.sourceUrl = MessageFormat.format("jdbc:h2:/{0}", databasePath);
			return this;
		}

		public MigrationArgumentBuilder withDestinationUrl(String hostname, Integer port, String databaseName)
		{
			this.destinationUrl = MessageFormat.format("jdbc:postgresql://{0}:{1,number,#}/{2}", hostname, port, databaseName);
			return this;
		}

		public MigrationArgumentBuilder withDestinationCredentials(String username, String password)
		{
			this.destinationUsername = username;
			this.destinationPassword = password;
			return this;
		}

		public MigrationArguments build()
		{
			return new MigrationArguments(sourceUrl, destinationUrl, destinationUsername, destinationPassword);
		}
	}

	private final String sourceUrl;
	private final String destinationUrl;
	private final String destinationUsername;
	private final String destinationPassword;

	private MigrationArguments(String sourceUrl, String destinationUrl, String destinationUsername, String destinationPassword)
	{
		this.sourceUrl = sourceUrl;
		this.destinationUrl = destinationUrl;
		this.destinationUsername = destinationUsername;
		this.destinationPassword = destinationPassword;
	}

	public List<String> getArguments()
	{
		final ArrayList<String> arguments = new ArrayList<>();
		arguments.add(MessageFormat.format("--spring.datasource.jdbc-url={0}", sourceUrl));
		arguments.add(MessageFormat.format("--spring.seconddatasource.jdbc-url={0}", destinationUrl));
		arguments.add(MessageFormat.format("--spring.seconddatasource.username={0}", destinationUsername));
		arguments.add(MessageFormat.format("--spring.seconddatasource.password={0}", destinationPassword));
		return arguments;
	}
}
