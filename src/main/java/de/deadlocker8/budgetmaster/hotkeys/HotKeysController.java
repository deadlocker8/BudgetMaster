package de.deadlocker8.budgetmaster.hotkeys;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HotKeysController extends BaseController
{
	private final SettingsService settingsService;

	@Autowired
	public HotKeysController(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	@RequestMapping(Mappings.HOTKEYS)
	public String index(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("hotkeysGeneral", GeneralHotKey.values());
		model.addAttribute("hotkeysGlobalDatePicker", GlobalDatePickerHotKey.values());
		return "hotkeys";
	}
}