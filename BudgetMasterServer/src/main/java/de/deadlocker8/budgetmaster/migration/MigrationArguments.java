package de.deadlocker8.budgetmaster.migration;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MigrationArguments
{
	static class MigrationArgumentBuilder
	{
		private String sourceUrl;
		private String destinationUrl;
		private String destinationDriverClassName;
		private String destinationUsername;
		private String destinationPassword;

		public MigrationArgumentBuilder withSourceUrl(String databasePath)
		{
			databasePath = databasePath.replace("\\", "/");
			this.sourceUrl = MessageFormat.format("jdbc:h2:/{0}", databasePath);
			return this;
		}

		public MigrationArgumentBuilder withDestinationUrl(DatabaseType databaseType, String hostname, Integer port, String databaseName)
		{
			this.destinationDriverClassName = databaseType.getDriverClassName();
			this.destinationUrl = MessageFormat.format("jdbc:{0}://{1}:{2,number,#}/{3}", databaseType.getName().toLowerCase(), hostname, port, databaseName);
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
			return new MigrationArguments(sourceUrl, destinationUrl, destinationDriverClassName, destinationUsername, destinationPassword);
		}
	}

	private final String sourceUrl;
	private final String destinationUrl;
	private final String destinationDriverClassName;
	private final String destinationUsername;
	private final String destinationPassword;

	private MigrationArguments(String sourceUrl, String destinationUrl, String driverClassName, String destinationUsername, String destinationPassword)
	{
		this.sourceUrl = sourceUrl;
		this.destinationUrl = destinationUrl;
		this.destinationDriverClassName = driverClassName;
		this.destinationUsername = destinationUsername;
		this.destinationPassword = destinationPassword;
	}

	public List<String> getArguments()
	{
		final ArrayList<String> arguments = new ArrayList<>();
		arguments.add(MessageFormat.format("--spring.datasource.jdbc-url={0}", sourceUrl));
		arguments.add(MessageFormat.format("--spring.seconddatasource.jdbc-url={0}", destinationUrl));
		arguments.add(MessageFormat.format("--spring.seconddatasource.driver-class-name={0}", destinationDriverClassName));
		arguments.add(MessageFormat.format("--spring.seconddatasource.username={0}", destinationUsername));
		arguments.add(MessageFormat.format("--spring.seconddatasource.password={0}", destinationPassword));
		return arguments;
	}
}
