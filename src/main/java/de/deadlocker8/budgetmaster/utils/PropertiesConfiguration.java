package de.deadlocker8.budgetmaster.utils;

import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.SystemUtils;
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
		Path settingsPath = SystemUtils.getApplicationSupportDirectoryPath(Localization.getString("folder"), "settings.properties");
		properties.setLocation(new FileSystemResource(settingsPath.toString()));
		properties.setIgnoreResourceNotFound(false);

		return properties;
	}
}