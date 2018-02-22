package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.Category;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController
{
	@Autowired
	private CategoryRepository categoryRepository;

	@RequestMapping("/")
	public String index()
	{
		Category n = new Category();
		n.setName("Ausgaben");
		n.setColor("#FF0000");
		categoryRepository.save(n);

		return "index";
	}
}