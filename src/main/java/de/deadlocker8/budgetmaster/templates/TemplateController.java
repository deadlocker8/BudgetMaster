package de.deadlocker8.budgetmaster.templates;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.services.DateService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
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

	@GetMapping("/templates")
	public String showTemplates(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("templates", templateService.getRepository().findAllByOrderByTemplateNameAsc());
		return "templates/templates";
	}

	@GetMapping("/templates/select")
	public String select(Model model)
	{
		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("templates", templateService.getRepository().findAllByOrderByTemplateNameAsc());
		return "templates/selectTemplate";
	}

	@GetMapping("/templates/fromTransactionModal")
	public String fromTransactionModal(Model model)
	{
		model.addAttribute("existingTemplateNames", GSON.toJson(templateService.getExistingTemplateNames()));
		return "templates/createFromTransactionModal";
	}

	@PostMapping(value = "/templates/fromTransaction")
	public String postFromTransaction(@RequestParam(value = "templateName") String templateName,
									  @ModelAttribute("NewTransaction") Transaction transaction,
									  @RequestParam(value = "isPayment", required = false) boolean isPayment,
									  @RequestParam(value = "includeCategory") Boolean includeCategory,
									  @RequestParam(value = "includeAccount") Boolean includeAccount)
	{
		if(transaction.getAmount() != null)
		{
			transactionService.handleAmount(transaction, isPayment);
		}
		transactionService.handleTags(transaction);

		if(templateName == null || templateName.isEmpty())
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "templateName must not be empty");
		}

		templateService.createFromTransaction(templateName, transaction, includeCategory, includeAccount);
		return "redirect:/templates";
	}

	@GetMapping("/templates/{ID}/requestDelete")
	public String requestDeleteTemplate(Model model, @PathVariable("ID") Integer ID)
	{
		final Optional<Template> templateOptional = templateService.getRepository().findById(ID);
		if(!templateOptional.isPresent())
		{
			throw new ResourceNotFoundException();
		}

		model.addAttribute("settings", settingsService.getSettings());
		model.addAttribute("templates", templateService.getRepository().findAllByOrderByTemplateNameAsc());
		model.addAttribute("currentTemplate", templateOptional.get());
		return "templates/templates";
	}

	@GetMapping("/templates/{ID}/delete")
	public String deleteTemplate(@PathVariable("ID") Integer ID)
	{
		templateService.getRepository().deleteById(ID);
		return "redirect:/templates";
	}

	@GetMapping("/templates/{ID}/select")
	public String selectTemplate(Model model,
								 @CookieValue("currentDate") String cookieDate,
								 @PathVariable("ID") Integer ID)
	{
		final Optional<Template> templateOptional = templateService.getRepository().findById(ID);
		if(!templateOptional.isPresent())
		{
			throw new ResourceNotFoundException();
		}

		final Template template = templateOptional.get();

		template.setID(null);

		templateService.prepareTemplateForNewTransaction(template, true);

		boolean isPayment = true;
		if(template.getAmount() != null)
		{
			isPayment = template.getAmount() <= 0;
		}

		final DateTime date = dateService.getDateTimeFromCookie(cookieDate);
		transactionService.prepareModelNewOrEdit(model, false, date, template, isPayment, accountService.getAllAccountsAsc());

		if(template.isTransfer())
		{
			return "transactions/newTransactionTransfer";
		}
		return "transactions/newTransactionNormal";
	}

	@GetMapping("/templates/newTemplate")
	public String newTemplate(Model model)
	{
		final Template emptyTemplate = new Template();
		templateService.prepareTemplateForNewTransaction(emptyTemplate, false);
		templateService.prepareModelNewOrEdit(model, false, emptyTemplate, true, accountService.getAllAccountsAsc());
		return "templates/newTemplate";
	}

	@PostMapping(value = "/templates/newTemplate")
	public String post(Model model,
					   @ModelAttribute("NewTemplate") Template template, BindingResult bindingResult,
					   @RequestParam(value = "isPayment", required = false) boolean isPayment,
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

		if(template.getAmount() != null)
		{
			transactionService.handleAmount(template, isPayment);
		}
		transactionService.handleTags(template);

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			templateService.prepareModelNewOrEdit(model, template.getID() != null, template, isPayment, accountService.getAllAccountsAsc());
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

	@GetMapping("/templates/{ID}/edit")
	public String editTemplate(Model model, @PathVariable("ID") Integer ID)
	{
		Optional<Template> templateOptional = templateService.getRepository().findById(ID);
		if(!templateOptional.isPresent())
		{
			throw new ResourceNotFoundException();
		}

		Template template = templateOptional.get();
		templateService.prepareTemplateForNewTransaction(template, false);
		templateService.prepareModelNewOrEdit(model, true, template, template.isPayment(), accountService.getAllAccountsAsc());

		return "templates/newTemplate";
	}
}