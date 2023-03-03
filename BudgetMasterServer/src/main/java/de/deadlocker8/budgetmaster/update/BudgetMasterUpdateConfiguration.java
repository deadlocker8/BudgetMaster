package de.deadlocker8.budgetmaster.update;

import de.deadlocker8.budgetmaster.BudgetMasterServerMain;
import de.thecodelabs.storage.settings.Storage;
import de.thecodelabs.storage.settings.StorageTypes;
import de.thecodelabs.utils.util.SystemUtils;
import de.thecodelabs.versionizer.VersionizerItem;
import de.thecodelabs.versionizer.config.Artifact;
import de.thecodelabs.versionizer.config.Repository;
import de.thecodelabs.versionizer.model.RemoteFile;
import de.thecodelabs.versionizer.service.UpdateService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;

@Component
public class BudgetMasterUpdateConfiguration
{
	private UpdateService.Strategy updateStrategy;
	private String executablePath;
	private RemoteFile.FileType fileType;
	private boolean isRunningFromSource = true;

	private final Artifact artifact;

	@Autowired
	public BudgetMasterUpdateConfiguration(Artifact artifact)
	{
		this.artifact = artifact;
	}

	@PostConstruct
	private void postInit()
	{
		executablePath = null;
		updateStrategy = UpdateService.Strategy.JAR;
		fileType = RemoteFile.FileType.JAR;

		SystemUtils.setIsJarHook(new IsJarFileHook());
		SystemUtils.setIsExeHook(new IsExeFileHook());

		final ApplicationHome applicationHome = new ApplicationHome();
		final File source = applicationHome.getSource();
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

	@Bean
	public UpdateService updateService()
	{
		ClassLoader classLoader = BudgetMasterServerMain.class.getClassLoader();
		Repository repository = Storage.load(classLoader.getResourceAsStream("repositories.json"), StorageTypes.JSON, Repository.class);

		VersionizerItem versionizerItem = new VersionizerItem(repository, executablePath);
		UpdateService versionizerUpdateService = UpdateService.startVersionizer(versionizerItem, updateStrategy, UpdateService.InteractionType.HEADLESS, UpdateService.RepositoryType.RELEASE);
		if(!isRunningFromSource)
		{
			versionizerUpdateService.addArtifact(artifact, Paths.get(executablePath));
		}
		return versionizerUpdateService;
	}

	@Bean
	public String executablePath()
	{
		return executablePath;
	}

	@Bean
	public RemoteFile.FileType fileType()
	{
		return fileType;
	}

	@Bean
	public boolean runningFromSource()
	{
		return isRunningFromSource;
	}
}
