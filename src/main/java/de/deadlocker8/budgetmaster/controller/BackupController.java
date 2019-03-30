package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
public class BackupController extends BaseController
{
	@Autowired
	private SettingsService settingsService;

	@RequestMapping("/backupReminder/cancel")
	public String cancel(HttpServletRequest request, Model model)
	{
		settingsService.updateLastBackupReminderDate();
		model.addAttribute("settings", settingsService.getSettings());
		return "redirect:" + request.getHeader("Referer");
	}

	@RequestMapping("/backupReminder/settings")
	public String settings(Model model)
	{
		settingsService.updateLastBackupReminderDate();
		return "redirect:/settings";
	}
}