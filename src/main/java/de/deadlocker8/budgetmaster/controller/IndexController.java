package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController extends BaseController
{
	private final StatisticsService statisticsService;

	@Autowired
	public IndexController(StatisticsService statisticsService)
	{
		this.statisticsService = statisticsService;
	}

	@RequestMapping
	public String index()
	{
		return "index";
	}

	@GetMapping("/firstUse")
	public String firstUse()
	{
		return "firstUse";
	}

	@GetMapping("/statistics")
	public String statistics(Model model)
	{
		model.addAttribute("statisticItems", statisticsService.getStatisticItems());
		return "statistics";
	}
}