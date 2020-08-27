package de.deadlocker8.budgetmaster.templates;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.services.DateService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Controller
@RequestMapping(Mappings.TEMPLATES)
public class TemplateController extends BaseController
{
	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	private final TemplateService templateService;
	private final SettingsService settingsService;
	private final TransactionService transactionService;
	private final DateService dateService;
	private final AccountService accountService;

	@Autowired
	public TemplateController(TemplateService templateService, SettingsService settingsService, TransactionService transactionService, DateService dateService, AccountService accountService)
	{
		this.templateService = templateService;
		this.settingsService = settingsService;
		this.transactionService = transactionService;
		this.dateService = dateService;
		this.accountService = accountService;
	}

	@GetMapping
	public String showTemplates(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("templates", templateService.getRepository().findAllByOrderByTemplateNameAsc());
		return "templates/templates";
	}

	@GetMapping("/fromTransactionModal")
	public String fromTransactionModal(Model model)
	{
		model.addAttribute("existingTemplateNames", GSON.toJson(templateService.getExistingTemplateNames()));
		return "templates/createFromTransactionModal";
	}

	@PostMapping(value = "/fromTransaction")
	public String postFromTransaction(@RequestParam(value = "templateName") String templateName,
									  @ModelAttribute("NewTransaction") Transaction transaction,
									  @RequestParam(value = "includeCategory") Boolean includeCategory,
									  @RequestParam(value = "includeAccount") Boolean includeAccount)
	{
		if(transaction.getAmount() != null)
		{
			transactionService.handleAmount(transaction);
		}
		transactionService.handleTags(transaction);

		if(templateName == null || templateName.isEmpty())
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "templateName must not be empty");
		}

		templateService.createFromTransaction(templateName, transaction, includeCategory, includeAccount);
		return "redirect:/templates";
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteTemplate(Model model, @PathVariable("ID") Integer ID)
	{
		final Optional<Template> templateOptional = templateService.getRepository().findById(ID);
		if(templateOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("templates", templateService.getRepository().findAllByOrderByTemplateNameAsc());
		model.addAttribute("currentTemplate", templateOptional.get());
		return "templates/templates";
	}

	@GetMapping("/{ID}/delete")
	public String deleteTemplate(@PathVariable("ID") Integer ID)
	{
		templateService.getRepository().deleteById(ID);
		return "redirect:/templates";
	}

	@GetMapping("/{ID}/select")
	public String selectTemplate(Model model,
								 @CookieValue("currentDate") String cookieDate,
								 @PathVariable("ID") Integer ID)
	{
		final Optional<Template> templateOptional = templateService.getRepository().findById(ID);
		if(templateOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		final Template template = templateOptional.get();

		templateService.prepareTemplateForNewTransaction(template, true);

		if(template.getAmount() == null)
		{
			template.setIsExpenditure(true);
		}

		final DateTime date = dateService.getDateTimeFromCookie(cookieDate);
		transactionService.prepareModelNewOrEdit(model, false, date, null, template, accountService.getAllAccountsAsc());

		if(template.isTransfer())
		{
			return "transactions/newTransactionTransfer";
		}
		return "transactions/newTransactionNormal";
	}

	@GetMapping("/newTemplate")
	public String newTemplate(Model model)
	{
		final Template emptyTemplate = new Template();
		templateService.prepareTemplateForNewTransaction(emptyTemplate, false);
		templateService.prepareModelNewOrEdit(model, false, emptyTemplate, accountService.getAllAccountsAsc());
		return "templates/newTemplate";
	}

	@PostMapping(value = "/newTemplate")
	public String post(Model model,
					   @ModelAttribute("NewTemplate") Template template, BindingResult bindingResult,
					   @RequestParam(value = "includeAccount", required = false) boolean includeAccount,
					   @RequestParam(value = "includeTransferAccount", required = false) boolean includeTransferAccount)
	{
		template.setTemplateName(template.getTemplateName().trim());

		String previousTemplateName = null;
		boolean isEdit = template.getID() != null;
		if(isEdit)
		{
			final Optional<Template> existingTemplateOptional = templateService.getRepository().findById(template.getID());
			if(existingTemplateOptional.isPresent())
			{
				previousTemplateName = existingTemplateOptional.get().getTemplateName();
			}
		}

		TemplateValidator templateValidator = new TemplateValidator(previousTemplateName, templateService.getExistingTemplateNames());
		templateValidator.validate(template, bindingResult);

		if(template.isExpenditure() == null)
		{
			template.setIsExpenditure(false);
		}

		if(template.getAmount() != null)
		{
			transactionService.handleAmount(template);
		}
		transactionService.handleTags(template);

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			templateService.prepareModelNewOrEdit(model, template.getID() != null, template, accountService.getAllAccountsAsc());
			return "templates/newTemplate";
		}

		if(!includeAccount)
		{
			template.setAccount(null);
		}

		if(!includeTransferAccount)
		{
			template.setTransferAccount(null);
		}

		templateService.getRepository().save(template);
		return "redirect:/templates";
	}

	@GetMapping("/{ID}/edit")
	public String editTemplate(Model model, @PathVariable("ID") Integer ID)
	{
		Optional<Template> templateOptional = templateService.getRepository().findById(ID);
		if(templateOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		Template template = templateOptional.get();
		templateService.prepareTemplateForNewTransaction(template, false);
		templateService.prepareModelNewOrEdit(model, true, template, accountService.getAllAccountsAsc());

		return "templates/newTemplate";
	}
}