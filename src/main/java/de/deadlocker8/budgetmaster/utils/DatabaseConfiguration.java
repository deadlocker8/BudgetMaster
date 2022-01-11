package de.deadlocker8.budgetmaster.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.text.MessageFormat;

@Configuration
@Profile("!test")
public class DatabaseConfiguration
{
	DatabaseConfigurationProperties databaseConfig;

	@Autowired
	public DatabaseConfiguration(DatabaseConfigurationProperties databaseConfig)
	{
		this.databaseConfig = databaseConfig;
	}

	@Bean
	@Primary
	public DataSource dataSource()
	{
		final String jdbcString = MessageFormat.format("jdbc:postgresql://{0}:{1}/{2}", databaseConfig.getHostname(), Long.toString(databaseConfig.getPort()), databaseConfig.getDatabaseName());

		return DataSourceBuilder.create()
				.username(databaseConfig.getUsername())
				.password(databaseConfig.getPassword())
				.url(jdbcString)
				.driverClassName("org.postgresql.Driver")
				.build();
	}
}
