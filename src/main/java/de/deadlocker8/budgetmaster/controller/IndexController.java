package de.deadlocker8.budgetmaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController
{
	@RequestMapping("/")
	public String index()
	{
		return "index";
	}
}