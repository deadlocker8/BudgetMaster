package de.deadlocker8.budgetmaster.backup;

import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.thecodelabs.utils.util.Localization;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public enum AutoBackupStrategy
{
	NONE("settings.backup.auto.strategy.none", null),
	LOCAL("settings.backup.auto.strategy.local", LocalBackupTask.class),
	GIT_LOCAL("settings.backup.auto.strategy.git.local", LocalGitBackupTask.class),
	GIT_REMOTE("settings.backup.auto.strategy.git.remote", RemoteGitBackupTask.class);

	private String localizationKey;
	private Class<? extends BackupTask> backupTaskType;

	AutoBackupStrategy(String localizationKey, Class<? extends BackupTask> backupTaskType)
	{
		this.localizationKey = localizationKey;
		this.backupTaskType = backupTaskType;
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}

	public Class<? extends Runnable> getBackupTaskType()
	{
		return backupTaskType;
	}

	public String getName()
	{
		return Localization.getString(localizationKey);
	}

	public Optional<Runnable> getBackupTask(DatabaseService databaseService, SettingsService settingsService)
	{
		if(backupTaskType == null)
		{
			return Optional.empty();
		}

		try
		{
			return Optional.of(backupTaskType.getConstructor(DatabaseService.class, SettingsService.class)
					.newInstance(databaseService, settingsService));
		}
		catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			throw new RuntimeException("Can't instantiate class " + backupTaskType.getName());
		}
	}

	public static AutoBackupStrategy fromName(String name)
	{
		for(AutoBackupStrategy type : values())
		{
			if(type.getName().equals(name))
			{
				return type;
			}
		}

		return null;
	}
}
