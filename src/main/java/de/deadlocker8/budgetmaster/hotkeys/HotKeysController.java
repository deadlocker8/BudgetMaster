package de.deadlocker8.budgetmaster.hotkeys;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HotKeysController extends BaseController
{
	@RequestMapping(Mappings.HOTKEYS)
	public String index(Model model)
	{
		model.addAttribute("hotkeysGeneral", GeneralHotKey.values());
		model.addAttribute("hotkeysGlobalDatePicker", GlobalDatePickerHotKey.values());
		return "hotkeys";
	}
}