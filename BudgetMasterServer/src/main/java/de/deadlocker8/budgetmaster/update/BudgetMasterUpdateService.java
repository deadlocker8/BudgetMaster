package de.deadlocker8.budgetmaster.update;

import de.deadlocker8.budgetmaster.Build;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.thecodelabs.versionizer.config.Artifact;
import de.thecodelabs.versionizer.model.RemoteFile;
import de.thecodelabs.versionizer.model.Version;
import de.thecodelabs.versionizer.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

	@Autowired(required = false)
	@Qualifier("executablePath")
	private String executablePath;

	@Autowired
	private RemoteFile.FileType fileType;

	@Autowired
	@Qualifier("runningFromSource")
	private boolean isRunningFromSource;

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

	public UpdateService getUpdateService()
	{
		return updateService;
	}

	public boolean isUpdateAvailable()
	{
		try
		{
			return updateService.isUpdateAvailable();
		}
		catch(NullPointerException e)
		{
			return false;
		}
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
