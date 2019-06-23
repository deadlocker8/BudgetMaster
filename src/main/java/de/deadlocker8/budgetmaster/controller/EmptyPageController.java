package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class EmptyPageController extends BaseController
{
	private final SettingsService settingsService;

	@Autowired
	public EmptyPageController(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}
}