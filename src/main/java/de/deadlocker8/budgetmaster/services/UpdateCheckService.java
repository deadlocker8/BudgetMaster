package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.update.BudgetMasterUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateCheckService
{
	private final BudgetMasterUpdateService budgetMasterUpdateService;

	@Autowired
	public UpdateCheckService(BudgetMasterUpdateService budgetMasterUpdateService)
	{
		this.budgetMasterUpdateService = budgetMasterUpdateService;
	}

	public boolean isUpdateAvailable()
	{
		try
		{
			return budgetMasterUpdateService.getUpdateService().isUpdateAvailable();
		}
		catch(NullPointerException e)
		{
			return false;
		}
	}

	public String getAvailableVersionString()
	{
		return budgetMasterUpdateService.getAvailableVersionString();
	}
}
