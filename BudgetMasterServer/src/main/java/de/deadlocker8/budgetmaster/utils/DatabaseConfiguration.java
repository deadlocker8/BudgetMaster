package de.deadlocker8.budgetmaster.utils;

import de.deadlocker8.budgetmaster.BudgetMasterServerMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.List;

@Configuration
@Profile("!test")
public class DatabaseConfiguration
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfiguration.class);

	private final DatabaseConfigurationProperties databaseConfig;

	@Autowired
	public DatabaseConfiguration(DatabaseConfigurationProperties databaseConfig)
	{
		this.databaseConfig = databaseConfig;
	}

	@Bean
	@Primary
	public DataSource dataSource()
	{
		final List<String> missingAttributes = databaseConfig.getMissingAttributes();
		if(missingAttributes.isEmpty())
		{
			final String jdbcString = MessageFormat.format("jdbc:{0}://{1}:{2}/{3}", databaseConfig.getType(), databaseConfig.getHostname(), Long.toString(databaseConfig.getPort()), databaseConfig.getDatabaseName());

			return DataSourceBuilder.create()
					.username(databaseConfig.getUsername())
					.password(databaseConfig.getPassword())
					.url(jdbcString)
					.driverClassName(databaseConfig.getDatabaseType().getDriverClassName())
					.build();
		}
		else
		{
			final StringBuilder errorMessageBuilder = new StringBuilder("BudgetMaster failed to start due to missing database settings!");
			errorMessageBuilder.append("\n");
			errorMessageBuilder.append("Your settings.properties file in \"");
			errorMessageBuilder.append(BudgetMasterServerMain.getApplicationSupportFolder(true));
			errorMessageBuilder.append("\" is missing the following settings:");
			errorMessageBuilder.append("\n");

			for(String missingAttribute : missingAttributes)
			{
				errorMessageBuilder.append(missingAttribute);
				errorMessageBuilder.append("\n");
			}

			LOGGER.error(errorMessageBuilder.toString());
			System.exit(1);
			return null;
		}
	}
}
