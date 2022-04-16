package de.deadlocker8.budgetmaster.migration;

import de.deadlocker8.budgetmaster.Main;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class ApplicationSupportFolderProvider
{
	@Bean("applicationSupportFolder")
	public Path applicationSupportFolder()
	{
		return Main.getApplicationSupportFolder();
	}
}
