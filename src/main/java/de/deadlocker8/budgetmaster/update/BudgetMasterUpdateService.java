package de.deadlocker8.budgetmaster.update;

import de.deadlocker8.budgetmaster.Build;
import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.thecodelabs.storage.settings.Storage;
import de.thecodelabs.storage.settings.StorageTypes;
import de.thecodelabs.utils.util.SystemUtils;
import de.thecodelabs.versionizer.VersionizerItem;
import de.thecodelabs.versionizer.config.Artifact;
import de.thecodelabs.versionizer.config.Repository;
import de.thecodelabs.versionizer.model.RemoteFile;
import de.thecodelabs.versionizer.model.Version;
import de.thecodelabs.versionizer.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.text.MessageFormat;

@Service
public class BudgetMasterUpdateService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BudgetMasterUpdateService.class);

	@Autowired
	private Artifact artifact;

	@Autowired
	private UpdateService updateService;

	@Autowired
	private SettingsService settingsService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private boolean isRunningFromSource = true;
	private String executablePath;
	private UpdateService.Strategy updateStrategy;
	private RemoteFile.FileType fileType;

	public BudgetMasterUpdateService()
	{
		File source = new ApplicationHome().getSource();
		executablePath = null;
		updateStrategy = UpdateService.Strategy.JAR;
		fileType = RemoteFile.FileType.JAR;

		SystemUtils.setIsJarHook(new IsJarFileHook());
		SystemUtils.setIsExeHook(new IsExeFileHook());

		if(source != null)
		{
			isRunningFromSource = false;
			executablePath = source.getAbsolutePath();
			if(executablePath.toLowerCase().endsWith(".exe"))
			{
				updateStrategy = UpdateService.Strategy.EXE;
				fileType = RemoteFile.FileType.EXE;
			}
		}
	}

	@Scheduled(cron = "${versionizer.service.cron}")
	public void updateSearchTask()
	{
		if(settingsService.getSettings().isAutoUpdateCheckEnabled())
		{
			LOGGER.info("Performing update check");
			updateService.fetchCurrentVersion();
			if(updateService.isUpdateAvailable())
			{
				UpdateAvailableEvent customSpringEvent = new UpdateAvailableEvent(this, updateService);
				applicationEventPublisher.publishEvent(customSpringEvent);
				LOGGER.info(MessageFormat.format("Update available (installed: v{0}, available: {1})", Build.getInstance().getVersionName(), getAvailableVersionString()));
			}
		}
	}

	@Bean
	public Artifact artifact()
	{
		Artifact artifact = new Artifact();
		artifact.setVersion(Build.getInstance().getVersionName());
		artifact.setGroupId("de.deadlocker8");
		artifact.setArtifactId("BudgetMaster");
		artifact.setArtifactType(Artifact.ArtifactType.RUNTIME);
		return artifact;
	}

	@Bean
	public UpdateService updateService()
	{
		ClassLoader classLoader = Main.class.getClassLoader();
		Repository repository = Storage.load(classLoader.getResourceAsStream("repositories.json"), StorageTypes.JSON, Repository.class);

		VersionizerItem versionizerItem = new VersionizerItem(repository, executablePath);
		UpdateService updateService = UpdateService.startVersionizer(versionizerItem, updateStrategy, UpdateService.InteractionType.HEADLESS, UpdateService.RepositoryType.RELEASE);
		if(executablePath != null)
		{
			updateService.addArtifact(artifact, Paths.get(executablePath));
		}
		return updateService;
	}

	public UpdateService getUpdateService()
	{
		return updateService;
	}

	public String getAvailableVersionString()
	{
		if(getAvailableVersion() == null)
		{
			return "-";
		}
		else
		{
			return "v" + getAvailableVersion().toVersionString();
		}
	}

	public Version getAvailableVersion()
	{
		return updateService.getRemoteVersionForArtifact(artifact);
	}

	public String getExecutablePath()
	{
		return executablePath;
	}

	public RemoteFile.FileType getFileType()
	{
		return fileType;
	}

	public boolean isRunningFromSource()
	{
		return isRunningFromSource;
	}
}
