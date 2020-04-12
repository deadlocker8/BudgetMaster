package de.deadlocker8.budgetmaster.templates;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class TemplateController extends BaseController
{
	private final TemplateService templateService;
	private final SettingsService settingsService;
	private final TransactionService transactionService;

	@Autowired
	public TemplateController(TemplateService templateService, SettingsService settingsService, TransactionService transactionService)
	{
		this.templateService = templateService;
		this.settingsService = settingsService;
		this.transactionService = transactionService;
	}

	@GetMapping("/templates")
	public String showTemplates(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("templates", templateService.getRepository().findAllByOrderByTemplateNameAsc());
		return "templates/templates";
	}

	@GetMapping("/templates/select")
	public String manage(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("templates", templateService.getRepository().findAllByOrderByTemplateNameAsc());
		return "templates/selectTemplate";
	}

	@GetMapping("/templates/fromTransactionModal")
	public String fromTransactionModal()
	{
		return "templates/createFromTransactionModal";
	}

	@PostMapping(value = "/templates/fromTransaction")
	public String postNormal(Model model,
							 @RequestParam(value = "templateName") String templateName,
							 @ModelAttribute("NewTransaction") Transaction transaction, BindingResult bindingResult,
							 @RequestParam(value = "isPayment", required = false) boolean isPayment)
	{
		//TODO: handle BindingResult and errors

		transactionService.handleAmount(transaction, isPayment);
		transactionService.handleTags(transaction);

		final Template template = new Template(templateName, transaction);
		templateService.getRepository().save(template);

		return "redirect:/templates";
	}

	@GetMapping("/templates/{ID}/requestDelete")
	public String requestDeleteTemplate(Model model, @PathVariable("ID") Integer ID)
	{
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("templates", templateService.getRepository().findAllByOrderByTemplateNameAsc());
		model.addAttribute("currentTemplate", templateService.getRepository().getOne(ID));
		return "templates/templates";
	}

	@GetMapping("/templates/{ID}/delete")
	public String deleteTemplate(Model model, @PathVariable("ID") Integer ID)
	{
		templateService.getRepository().deleteById(ID);
		return "redirect:/templates";
	}
}