package de.deadlocker8.budgetmaster.hotkeys;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HotKeysController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String HOTKEYS_GENERAL = "hotkeysGeneral";
		public static final String HOTKEYS_DATEPICKER = "hotkeysGlobalDatePicker";
		public static final String HOTKEYS_ACCOUNT_SELECT = "hotkeysAccountSelect";
		public static final String HOTKEYS_CHARTS = "hotkeysCharts";
	}

	private static class ReturnValues
	{
		public static final String HOTKEYS = "hotkeys";
	}

	@GetMapping(Mappings.HOTKEYS)
	public String index(Model model)
	{
		model.addAttribute(ModelAttributes.HOTKEYS_GENERAL, GeneralHotKey.values());
		model.addAttribute(ModelAttributes.HOTKEYS_DATEPICKER, GlobalDatePickerHotKey.values());
		model.addAttribute(ModelAttributes.HOTKEYS_ACCOUNT_SELECT, GlobalAccountSelectHotKey.values());
		model.addAttribute(ModelAttributes.HOTKEYS_CHARTS, ChartHotkeys.values());
		return ReturnValues.HOTKEYS;
	}
}