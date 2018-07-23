package de.deadlocker8.budgetmaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class EmptyPageController extends BaseController
{
	@RequestMapping("/reports")
	public String reports(Model model)
	{
		model.addAttribute("active", "reports");
		return "comingSoon";
	}

	@RequestMapping("/charts")
	public String charts(Model model)
	{
		model.addAttribute("active", "charts");
		return "comingSoon";
	}
}