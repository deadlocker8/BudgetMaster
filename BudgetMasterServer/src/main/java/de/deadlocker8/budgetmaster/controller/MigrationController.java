package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(Mappings.MIGRATION)
public class MigrationController extends BaseController
{
	private final SettingsService settingsService;

	@Autowired
	public MigrationController(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	@GetMapping("/cancel")
	public String cancel(HttpServletRequest request)
	{
		settingsService.updateMigrationDeclined(true);
		return "redirect:" + request.getHeader("Referer");
	}

	@GetMapping
	public String migrate()
	{
		return "migration";
	}
}