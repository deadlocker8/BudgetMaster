package de.deadlocker8.budgetmaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class TeapotController extends BaseController
{
	@RequestMapping("/418")
	public String index()
	{
		return "error/418";
	}
}