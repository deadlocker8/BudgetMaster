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
	private static class ModelAttributes
	{
		public static final String NEWS_ENTRIES = "newsEntries";
	}

	private static class ReturnValues
	{
		public static final String ABOUT = "about";
		public static final String WHATS_NEW = "whatsNewModal";
	}

	private final SettingsService settingsService;

	@Autowired
	public AboutController(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	@GetMapping
	public String index(Model model)
	{
		return ReturnValues.ABOUT;
	}

	@GetMapping("/whatsNewModal")
	public String whatsNewModal(Model model)
	{
		final List<NewsEntry> newsEntries = new ArrayList<>();
		newsEntries.add(NewsEntry.createWithLocalizationKey("databaseBackend"));
		newsEntries.add(NewsEntry.createWithLocalizationKey("linkToItem"));
		newsEntries.add(NewsEntry.createWithLocalizationKey("newHotkey"));
		newsEntries.add(NewsEntry.createWithLocalizationKey("suggestions"));
		newsEntries.add(NewsEntry.createWithLocalizationKey("chartsShowDateRange"));
		newsEntries.add(NewsEntry.createWithLocalizationKey("fix.sortTags"));
		newsEntries.add(NewsEntry.createWithLocalizationKey("fix.deleteAccounts"));

		model.addAttribute(ModelAttributes.NEWS_ENTRIES, newsEntries);
		return ReturnValues.WHATS_NEW;
	}

	@GetMapping("/whatsNewModal/close")
	@Transactional
	public String whatsNewModalClose(HttpServletRequest request)
	{
		settingsService.getSettings().setWhatsNewShownForCurrentVersion(true);
		return "redirect:" + request.getHeader("Referer");
	}
}