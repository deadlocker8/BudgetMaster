package de.deadlocker8.budgetmaster.hints;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


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
	@ResponseStatus(value = HttpStatus.OK)
	public void dismissHint(@PathVariable("ID") Integer ID)
	{
		hintService.dismiss(ID);
	}
}