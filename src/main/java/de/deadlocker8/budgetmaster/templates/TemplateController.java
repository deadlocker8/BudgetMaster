package de.deadlocker8.budgetmaster.templates;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.services.DateService;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.Localization;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Controller
@RequestMapping(Mappings.TEMPLATES)
public class TemplateController extends BaseController
{
	private static class ReturnValues
	{
		public static final String ALL_ENTITIES = "templates/templates";
		public static final String REDIRECT_ALL_ENTITIES = "redirect:/templates";
		public static final String NEW_ENTITY = "templates/newTemplate";
		public static final String DELETE_ENTITY = "templates/deleteTemplateModal";
		public static final String CREATE_FROM_TRANSACTION = "templates/createFromTransactionModal";
		public static final String NEW_TRANSACTION_NORMAL = "transactions/newTransactionNormal";
		public static final String NEW_TRANSACTION_TRANSFER = "transactions/newTransactionTransfer";
	}

	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	private final TemplateService templateService;
	private final TemplateGroupService templateGroupService;
	private final TransactionService transactionService;
	private final DateService dateService;
	private final AccountService accountService;
	private final IconService iconService;

	@Autowired
	public TemplateController(TemplateService templateService, TemplateGroupService templateGroupService, TransactionService transactionService, DateService dateService, AccountService accountService, IconService iconService)
	{
		this.templateService = templateService;
		this.templateGroupService = templateGroupService;
		this.transactionService = transactionService;
		this.dateService = dateService;
		this.accountService = accountService;
		this.iconService = iconService;
	}

	@GetMapping
	public String showTemplates(Model model)
	{
		model.addAttribute(TemplateModelAttributes.TEMPLATES_BY_GROUP, templateGroupService.getTemplatesByGroupedByTemplateGroup());
		return ReturnValues.ALL_ENTITIES;
	}

	@GetMapping("/fromTransactionModal")
	public String fromTransactionModal(Model model)
	{
		model.addAttribute(TemplateModelAttributes.EXISTING_TEMPLATE_NAMES, GSON.toJson(templateService.getExistingTemplateNames()));
		return ReturnValues.CREATE_FROM_TRANSACTION;
	}

	@PostMapping(value = "/fromTransaction")
	public String postFromTransaction(WebRequest request,
									  @RequestParam(value = "templateName") String templateName,
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

		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.template.add.success", templateName), NotificationType.SUCCESS));

		return ReturnValues.REDIRECT_ALL_ENTITIES;
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteTemplate(Model model, @PathVariable("ID") Integer ID)
	{
		final Optional<Template> templateOptional = templateService.findById(ID);
		if(templateOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		model.addAttribute(TemplateModelAttributes.TEMPLATES_BY_GROUP, templateGroupService.getTemplatesByGroupedByTemplateGroup());
		model.addAttribute(TemplateModelAttributes.ENTITY_TO_DELETE, templateOptional.get());
		return ReturnValues.DELETE_ENTITY;
	}

	@GetMapping("/{ID}/delete")
	public String deleteTemplate(WebRequest request, @PathVariable("ID") Integer ID)
	{
		final Template templateToDelete = templateService.getRepository().getById(ID);
		templateService.getRepository().deleteById(ID);

		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.template.delete.success", templateToDelete.getTemplateName()), NotificationType.SUCCESS));

		return ReturnValues.REDIRECT_ALL_ENTITIES;
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
		final Transaction newTransaction = new Transaction();
		newTransaction.setName(template.getName());
		newTransaction.setAmount(template.getAmount());
		newTransaction.setCategory(template.getCategory());
		newTransaction.setDescription(template.getDescription());
		newTransaction.setAccount(template.getAccount());
		newTransaction.setTransferAccount(template.getTransferAccount());
		newTransaction.setTags(template.getTags());
		newTransaction.setIsExpenditure(template.isExpenditure());

		templateService.prepareTemplateForNewTransaction(newTransaction, true);

		if(newTransaction.getAmount() == null && newTransaction.isExpenditure() == null)
		{
			newTransaction.setIsExpenditure(true);
		}

		final DateTime date = dateService.getDateTimeFromCookie(cookieDate);
		transactionService.prepareModelNewOrEdit(model, false, date, false, newTransaction, accountService.getAllActivatedAccountsAsc());

		if(newTransaction.isTransfer())
		{
			return ReturnValues.NEW_TRANSACTION_TRANSFER;
		}
		return ReturnValues.NEW_TRANSACTION_NORMAL;
	}

	@GetMapping("/newTemplate")
	public String newTemplate(Model model)
	{
		final Template emptyTemplate = new Template();
		templateService.prepareTemplateForNewTransaction(emptyTemplate, false);
		templateService.prepareModelNewOrEdit(model, false, emptyTemplate, accountService.getAllActivatedAccountsAsc());
		return ReturnValues.NEW_ENTITY;
	}

	@PostMapping(value = "/newTemplate")
	public String post(WebRequest request,
					   Model model,
					   @ModelAttribute("NewTemplate") Template template, BindingResult bindingResult,
					   @RequestParam(value = "includeAccount", required = false) boolean includeAccount,
					   @RequestParam(value = "includeTransferAccount", required = false) boolean includeTransferAccount,
					   @RequestParam(value = "iconImageID", required = false) Integer iconImageID,
					   @RequestParam(value = "builtinIconIdentifier", required = false) String builtinIconIdentifier,
					   @RequestParam(value = "fontColor", required = false) String fontColor)
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

		template.updateIcon(iconService, iconImageID, builtinIconIdentifier, fontColor, templateService);

		if(bindingResult.hasErrors())
		{
			model.addAttribute(TemplateModelAttributes.ERROR, bindingResult);
			templateService.prepareModelNewOrEdit(model, template.getID() != null, template, accountService.getAllActivatedAccountsAsc());
			return ReturnValues.NEW_ENTITY;
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
		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.template.save.success", template.getName()), NotificationType.SUCCESS));
		return ReturnValues.REDIRECT_ALL_ENTITIES;
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
		templateService.prepareModelNewOrEdit(model, true, template, accountService.getAllActivatedAccountsAsc());

		return ReturnValues.NEW_ENTITY;
	}
}