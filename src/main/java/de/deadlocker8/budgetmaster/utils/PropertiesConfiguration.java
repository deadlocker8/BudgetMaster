package de.deadlocker8.budgetmaster.utils;

import de.deadlocker8.budgetmaster.Main;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Path;

@Configuration
public class PropertiesConfiguration
{
	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
	{
		PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
		Path settingsPath = Main.getApplicationSupportFolder().resolve("settings.properties");
		properties.setLocation(new FileSystemResource(settingsPath.toString()));
		properties.setIgnoreResourceNotFound(false);

		return properties;
	}
}