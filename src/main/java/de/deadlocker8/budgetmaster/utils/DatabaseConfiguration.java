package de.deadlocker8.budgetmaster.utils;

import de.deadlocker8.budgetmaster.Main;
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
		Path applicationSupportFolder = Main.getApplicationSupportFolder();
		String jdbcString = "jdbc:h2:/" + applicationSupportFolder.toString() + "/" + "budgetmaster;DB_CLOSE_ON_EXIT=TRUE";
		return DataSourceBuilder.create().username("sa").password("").url(jdbcString).driverClassName("org.h2.Driver").build();
	}
}
