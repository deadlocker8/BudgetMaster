package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String STATISTIC_ITEMS = "statisticItems";
	}

	private static class ReturnValues
	{
		public static final String INDEX = "index";
		public static final String FIRST_USE = "firstUse";
		public static final String STATISTICS = "statistics";
	}

	private final StatisticsService statisticsService;

	@Autowired
	public IndexController(StatisticsService statisticsService)
	{
		this.statisticsService = statisticsService;
	}

	@GetMapping("/")
	public String index()
	{
		return ReturnValues.INDEX;
	}

	@GetMapping("/firstUse")
	public String firstUse()
	{
		return ReturnValues.FIRST_USE;
	}

	@GetMapping("/statistics")
	public String statistics(Model model)
	{
		model.addAttribute(ModelAttributes.STATISTIC_ITEMS, statisticsService.getStatisticItems());
		return ReturnValues.STATISTICS;
	}
}