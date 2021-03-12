package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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
	public String whatsNewModal(Model model)
	{
		final List<NewsEntry> newsEntries =  new ArrayList<>();
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.bugfix.headline", "news.bugfix.description"));

		model.addAttribute("newsEntries", newsEntries);
		return "whatsNewModal";
	}

	@RequestMapping("/whatsNewModal/close")
	@Transactional
	public String whatsNewModalClose(HttpServletRequest request, Model model)
	{
		settingsService.getSettings().setWhatsNewShownForCurrentVersion(true);
		model.addAttribute("settings", settingsService.getSettings());
		return "redirect:" + request.getHeader("Referer");
	}
}