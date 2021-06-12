package de.deadlocker8.budgetmaster.tags;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(Mappings.TAGS)
public class TagController extends BaseController
{
	private final TagService tagService;

	@Autowired
	public TagController(TagService tagService)
	{
		this.tagService = tagService;
	}

	@GetMapping
	public String tags(Model model)
	{
		model.addAttribute("tagUsages", tagService.getUsageCounts());
		return "tags/tags";
	}
}