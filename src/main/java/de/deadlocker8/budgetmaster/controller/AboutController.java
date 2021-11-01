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
		return "about";
	}

	@GetMapping("/whatsNewModal")
	public String whatsNewModal(Model model)
	{
		final List<NewsEntry> newsEntries = new ArrayList<>();
		newsEntries.add(NewsEntry.createWithLocalizationKey("recurringTransfers"));
		newsEntries.add(NewsEntry.createWithLocalizationKey("newFromExisting"));
		newsEntries.add(NewsEntry.createWithLocalizationKey("imagePerformance"));
		newsEntries.add(NewsEntry.createWithLocalizationKey("searchOption"));
		newsEntries.add(NewsEntry.createWithLocalizationKey("newHotkey"));
		newsEntries.add(NewsEntry.createWithLocalizationKey("trimInputs"));
		newsEntries.add(NewsEntry.createWithLocalizationKey("fix.sortTemplates"));

		model.addAttribute("newsEntries", newsEntries);
		return "whatsNewModal";
	}

	@RequestMapping("/whatsNewModal/close")
	@Transactional
	public String whatsNewModalClose(HttpServletRequest request)
	{
		settingsService.getSettings().setWhatsNewShownForCurrentVersion(true);
		return "redirect:" + request.getHeader("Referer");
	}
}