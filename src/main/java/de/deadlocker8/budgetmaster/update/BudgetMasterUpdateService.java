package de.deadlocker8.budgetmaster.update;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.services.SettingsService;
import de.thecodelabs.storage.settings.Storage;
import de.thecodelabs.storage.settings.StorageTypes;
import de.thecodelabs.versionizer.VersionizerItem;
import de.thecodelabs.versionizer.config.Artifact;
import de.thecodelabs.versionizer.config.Repository;
import de.thecodelabs.versionizer.model.Version;
import de.thecodelabs.versionizer.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationHome;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class BudgetMasterUpdateService
{
	private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

	@Autowired
	private Artifact artifact;

	@Autowired
	private UpdateService updateService;

	@Autowired
	private SettingsService settingsService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Scheduled(cron = "${versionizer.service.cron}")
	public void updateSearchTask()
	{
		if(settingsService.getSettings().isAutoUpdateCheckEnabled())
		{
			updateService.fetchCurrentVersion();
			if(updateService.isUpdateAvailable())
			{
				UpdateAvailableEvent customSpringEvent = new UpdateAvailableEvent(this, updateService);
				applicationEventPublisher.publishEvent(customSpringEvent);
			}
		}
	}

	@Bean
	public Artifact artifact()
	{
		Artifact artifact = new Artifact();
//		artifact.setVersion(Build.getInstance().getVersionName());
		artifact.setVersion("0.0.0");
		artifact.setGroupId("de.deadlocker8");
		artifact.setArtifactId("BudgetMaster");
		return artifact;
	}

	@Bean
	public UpdateService updateService()
	{
		ClassLoader classLoader = Main.class.getClassLoader();
		Repository repository = Storage.load(classLoader.getResourceAsStream("repositories.json"), StorageTypes.JSON, Repository.class);

		File source = new ApplicationHome().getSource();
		String executablePath = null;
		if(source == null)
		{
			LOGGER.debug("Running from source code: Skipping update check");
		}
		else
		{
			executablePath = source.getAbsolutePath();
		}

		VersionizerItem versionizerItem = new VersionizerItem(repository, artifact, executablePath);
		return UpdateService.startVersionizer(versionizerItem, UpdateService.Strategy.JAR, UpdateService.InteractionType.HEADLESS);
	}

	@Component
	public class UpdateEventListener implements ApplicationListener<UpdateAvailableEvent>
	{
		@Override
		public void onApplicationEvent(UpdateAvailableEvent updateAvailableEvent)
		{
			UpdateService updateService = updateAvailableEvent.getUpdateService();
			Version version = updateService.getRemoteVersionForArtifact(artifact);

			System.out.println(version);
		}
	}

	public UpdateService getUpdateService()
	{
		return updateService;
	}

	public String getAvailableVersion()
	{
		if(updateService.getRemoteVersionForArtifact(artifact) == null)
		{
			return "-";
		}
		else
		{
			return "v" + updateService.getRemoteVersionForArtifact(artifact).toVersionString();
		}
	}
}
