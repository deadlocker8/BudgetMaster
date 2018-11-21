package de.deadlocker8.budgetmaster.update;

import de.thecodelabs.versionizer.service.UpdateService;
import org.springframework.context.ApplicationEvent;

public class UpdateAvailableEvent extends ApplicationEvent
{
	private final UpdateService updateService;

	UpdateAvailableEvent(Object source, UpdateService updateService)
	{
		super(source);
		this.updateService = updateService;
	}

	public UpdateService getUpdateService()
	{
		return updateService;
	}
}
