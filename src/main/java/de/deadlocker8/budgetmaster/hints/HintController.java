package de.deadlocker8.budgetmaster.hints;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(Mappings.HINTS)
public class HintController extends BaseController
{
	private final HintService hintService;

	@Autowired
	public HintController(HintService hintService)
	{
		this.hintService = hintService;
	}

	@RequestMapping("/dismiss/{ID}")
	public String dismissHint(@PathVariable("ID") Integer ID)
	{
		hintService.dismiss(ID);
		return "";
	}
}