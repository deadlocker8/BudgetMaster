package de.deadlocker8.budgetmaster.templates;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.services.AccessAllEntities;
import de.deadlocker8.budgetmaster.services.AccessEntityByID;
import de.deadlocker8.budgetmaster.services.Resettable;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupService;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionBase;
import de.deadlocker8.budgetmaster.utils.FontAwesomeIcons;
import org.padler.natorder.NaturalOrderComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TemplateService implements Resettable, AccessAllEntities<Template>, AccessEntityByID<Template>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);

	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	private final TemplateRepository templateRepository;
	private final CategoryService categoryService;
	private final TemplateGroupService templateGroupService;

	@Autowired
	public TemplateService(TemplateRepository templateRepository, CategoryService categoryService, TemplateGroupService templateGroupService)
	{
		this.templateRepository = templateRepository;
		this.categoryService = categoryService;
		this.templateGroupService = templateGroupService;

		createDefaults();
	}

	public TemplateRepository getRepository()
	{
		return templateRepository;
	}

	@Override
	public void deleteAll()
	{
		LOGGER.info("Resetting templates...");
		templateRepository.deleteAll();
		LOGGER.info("All templates reset.");
	}

	@Override
	public void createDefaults()
	{
		updateMissingAttributes();
	}

	private void updateMissingAttributes()
	{
		for(Template template : templateRepository.findAll())
		{
			handleNullValuesForTemplateGroup(template);
			templateRepository.save(template);
		}
	}

	private void handleNullValuesForTemplateGroup(Template template)
	{
		if(template.getTemplateGroup() == null)
		{
			template.setTemplateGroup(templateGroupService.getRepository().findFirstByType(TemplateGroupType.DEFAULT));
			LOGGER.debug(MessageFormat.format("Updated template {0}: Set missing attribute \"templateGroup\" to {1}", template.getName(), template.getTemplateGroup().getName()));
		}
	}

	public Template createFromTransaction(String templateName, Transaction transaction, boolean includeCategory, boolean includeAccount)
	{
		final Template template = new Template(templateName, transaction);
		if(!includeCategory)
		{
			template.setCategory(null);
		}

		if(!includeAccount)
		{
			template.setAccount(null);
		}

		template.setTemplateGroup(templateGroupService.getDefaultGroup());

		return getRepository().save(template);
	}

	public void prepareTemplateForNewTransaction(TransactionBase template, boolean prepareAccount, Account selectedOrFallbackAccount)
	{
		if(template.getCategory() == null)
		{
			template.setCategory(categoryService.findByType(CategoryType.NONE));
		}

		if(prepareAccount && template.getAccount() == null)
		{
			template.setAccount(selectedOrFallbackAccount);
		}

		final Account account = template.getAccount();
		if(account != null && account.getAccountState() != AccountState.FULL_ACCESS)
		{
			template.setAccount(selectedOrFallbackAccount);
		}

		final Account transferAccount = template.getTransferAccount();
		if(transferAccount != null && transferAccount.getAccountState() != AccountState.FULL_ACCESS)
		{
			template.setTransferAccount(selectedOrFallbackAccount);
		}
	}

	public void prepareModelNewOrEdit(Model model, boolean isEdit, TransactionBase item, List<Account> accounts)
	{
		model.addAttribute(TemplateModelAttributes.IS_EDIT, isEdit);
		model.addAttribute(TemplateModelAttributes.CATEGORIES, categoryService.getAllEntitiesAsc());
		model.addAttribute(TemplateModelAttributes.ACCOUNTS, accounts);
		model.addAttribute(TemplateModelAttributes.TEMPLATE, item);
		model.addAttribute(TemplateModelAttributes.SUGGESTIONS_JSON, GSON.toJson(new ArrayList<String>()));
		model.addAttribute(TemplateModelAttributes.FONTAWESOME_ICONS, FontAwesomeIcons.ICONS);
	}

	public List<String> getExistingTemplateNames()
	{
		return getRepository().findAll().stream()
				.map(Template::getTemplateName)
				.toList();
	}

	@Override
	public List<Template> getAllEntitiesAsc()
	{
		final List<Template> templates = templateRepository.findAllByOrderByTemplateNameAsc();
		templates.sort((t1, t2) -> new NaturalOrderComparator().compare(t1.getTemplateName(), t2.getTemplateName()));
		return templates;
	}

	@Override
	public Optional<Template> findById(Integer ID)
	{
		return templateRepository.findById(ID);
	}

	public void unsetTemplatesWithAccount(Account account)
	{
		for(Template referringTemplate : templateRepository.findAllByAccount(account))
		{
			referringTemplate.setAccount(null);
			templateRepository.save(referringTemplate);
		}

		for(Template referringTemplate : templateRepository.findAllByTransferAccount(account))
		{
			referringTemplate.setTransferAccount(null);
			templateRepository.save(referringTemplate);
		}
	}
}
