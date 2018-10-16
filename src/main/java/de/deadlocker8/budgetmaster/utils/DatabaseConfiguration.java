package de.deadlocker8.budgetmaster.utils;

import de.tobias.utils.util.Localization;
import de.tobias.utils.util.SystemUtils;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.nio.file.Path;

@Configuration
public class DatabaseConfiguration
{
	@Bean
	@Primary
	public DataSource dataSource()
	{
		Path applicationSupportFolder = SystemUtils.getApplicationSupportDirectoryPath(Localization.getString("folder"));
		String jdbcString = "jdbc:h2:/" + applicationSupportFolder.toString() + "/" + "budgetmaster;DB_CLOSE_ON_EXIT=TRUE";
		return DataSourceBuilder.create().username("sa").password("").url(jdbcString).driverClassName("org.h2.Driver").build();
	}
}
