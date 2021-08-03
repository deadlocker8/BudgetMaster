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
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.chartChooser.headline", "news.chartChooser.description"));
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.jumpToSearch.headline", "news.jumpToSearch.description"));
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.fontAwesomeIcons.headline", "news.fontAwesomeIcons.description"));
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.nonSquareImages.headline", "news.nonSquareImages.description"));
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.hints.headline", "news.hints.description"));
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.tagOverview.headline", "news.tagOverview.description"));
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.accountImport.headline", "news.accountImport.description"));
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.performance.headline", "news.performance.description"));
		newsEntries.add(NewsEntry.createWithLocalizationKeys("news.fix.transfersFromHiddenAccounts.headline", "news.fix.transfersFromHiddenAccounts.description"));

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