package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(Mappings.ABOUT)
public class AboutController extends BaseController
{
	private final SettingsService settingsService;

	@Autowired
	public AboutController(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	@GetMapping
	public String index(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		return "about";
	}

	@GetMapping("/whatsNewModal")
	public String fromTransactionModal(Model model)
	{
		final List<NewsEntry> newsEntries =  new ArrayList<>();
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.templates.headline", "news.templates.description"));
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.backup.headline", "news.backup.description"));
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.suggestions.headline", "news.suggestions.description"));
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.charts.headline", "news.charts.description"));
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.commandline.headline", "news.commandline.description"));

		model.addAttribute("newsEntries", newsEntries);
		return "whatsNewModal";
	}
}