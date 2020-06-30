package de.deadlocker8.budgetmaster.utils.eventlistener;

import de.deadlocker8.budgetmaster.Build;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Component
public class UpdateInstalledVersion
{
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateInstalledVersion.class);

	private final SettingsService settingsService;

	@Autowired
	public UpdateInstalledVersion(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	@EventListener
	@Transactional
	@Order(2)
	public void onApplicationEvent(ApplicationStartedEvent event)
	{
		final Build build = Build.getInstance();
		final int runningVersionCode = Integer.parseInt(build.getVersionCode());

		LOGGER.debug(MessageFormat.format("Updated installedVersionCode to {0}", runningVersionCode));
		settingsService.getSettings().setInstalledVersionCode(runningVersionCode);
	}
}