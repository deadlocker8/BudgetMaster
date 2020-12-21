package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.backup.AutoBackupTime;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Service
public class BackupService
{
	private final SettingsService settingsService;

	private final BackupTask backupTask;

	private final TaskScheduler scheduler;
	private final Map<Class<? extends Runnable>, ScheduledFuture<?>> jobsMap = new HashMap<>();

	@Autowired
	public BackupService(SettingsService settingsService, BackupTask backupTask, TaskScheduler scheduler)
	{
		this.settingsService = settingsService;
		this.backupTask = backupTask;
		this.scheduler = scheduler;
	}

	public void startBackupCron(String cron)
	{
		stopBackupCron();

		ScheduledFuture<?> scheduledTask = scheduler.schedule(backupTask, new CronTrigger(cron, TimeZone.getDefault()));
		jobsMap.put(backupTask.getClass(), scheduledTask);
	}

	public void stopBackupCron()
	{
		ScheduledFuture<?> scheduledTask = jobsMap.get(backupTask.getClass());
		if(scheduledTask != null)
		{
			scheduledTask.cancel(true);
			jobsMap.remove(backupTask.getClass());
		}
	}

	@EventListener({ContextRefreshedEvent.class})
	public void contextRefreshedEvent()
	{
		final Settings settings = settingsService.getSettings();
		if(settings.isAutoBackupActive())
		{
			startBackupCron(computeCron(settings.getAutoBackupTime(), settings.getAutoBackupDays()));
		}
	}

	public String computeCron(AutoBackupTime time, Integer days)
	{
		int hour = time.getCronTime();
		// seconds minutes hours day_of_month month day_of_week
		return String.format("0 0 %d */%d * *", hour, days);
	}

	public Optional<DateTime> getNextRun()
	{
		final Settings settings = settingsService.getSettings();
		if(settings.isAutoBackupActive())
		{
			final String cron = computeCron(settings.getAutoBackupTime(), settings.getAutoBackupDays());
			CronSequenceGenerator cronTrigger = new CronSequenceGenerator(cron);
			Date next = cronTrigger.next(new Date());

			return Optional.of(new DateTime(next));
		}
		return Optional.empty();
	}
}