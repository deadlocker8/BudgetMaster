package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class TeapotController extends BaseController
{
	@GetMapping(Mappings.TEAPOT)
	public String index()
	{
		return "error/418";
	}
}