package de.deadlocker8.budgetmaster.templates;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.services.AccessAllEntities;
import de.deadlocker8.budgetmaster.services.Resettable;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionBase;
import de.deadlocker8.budgetmaster.services.AccessEntityByID;
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
import java.util.stream.Collectors;

@Service
public class TemplateService implements Resettable, AccessAllEntities<Template>, AccessEntityByID<Template>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);

	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	private final TemplateRepository templateRepository;
	private final AccountService accountService;
	private final CategoryService categoryService;
	private final ImageService imageService;
	private final IconService iconService;

	@Autowired
	public TemplateService(TemplateRepository templateRepository, AccountService accountService, CategoryService categoryService, SettingsService settingsService, ImageService imageService, IconService iconService)
	{
		this.templateRepository = templateRepository;
		this.accountService = accountService;
		this.categoryService = categoryService;
		this.imageService = imageService;
		this.iconService = iconService;

		createDefaults();
	}

	public TemplateRepository getRepository()
	{
		return templateRepository;
	}

	@Override
	public void deleteAll()
	{
		templateRepository.deleteAll();
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
			if(template.getIcon() != null && template.getIconReference() == null)
			{
				Integer imageID = template.getIcon().getID();
				Image image = imageService.getRepository().findById(imageID).orElseThrow();

				Icon iconReference = new Icon(image);
				iconService.getRepository().save(iconReference);

				template.setIconReference(iconReference);
				template.setIcon(null);

				templateRepository.save(template);
				LOGGER.debug(MessageFormat.format("Updated template {0}: Converted attribute \"icon\" to \"iconReference\" {1}", template.getName(), image.getFileName()));
			}
		}
	}

	public void createFromTransaction(String templateName, Transaction transaction, boolean includeCategory, boolean includeAccount)
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

		getRepository().save(template);
	}

	public void prepareTemplateForNewTransaction(TransactionBase template, boolean prepareAccount)
	{
		if(template.getCategory() == null)
		{
			template.setCategory(categoryService.findByType(CategoryType.NONE));
		}

		if(prepareAccount && template.getAccount() == null)
		{
			template.setAccount(accountService.getRepository().findByIsDefault(true));
		}

		final Account account = template.getAccount();
		if(account != null && account.getAccountState() != AccountState.FULL_ACCESS)
		{
			template.setAccount(accountService.getRepository().findByIsDefault(true));
		}

		final Account transferAccount = template.getTransferAccount();
		if(transferAccount != null && transferAccount.getAccountState() != AccountState.FULL_ACCESS)
		{
			template.setTransferAccount(accountService.getRepository().findByIsDefault(true));
		}
	}

	public void prepareModelNewOrEdit(Model model, boolean isEdit, TransactionBase item, List<Account> accounts)
	{
		model.addAttribute("isEdit", isEdit);
		model.addAttribute("categories", categoryService.getAllEntitiesAsc());
		model.addAttribute("accounts", accounts);
		model.addAttribute("template", item);
		model.addAttribute("suggestionsJSON", GSON.toJson(new ArrayList<String>()));
		model.addAttribute("fontawesomeIcons", FontAwesomeIcons.ICONS);
	}

	public List<String> getExistingTemplateNames()
	{
		return getRepository().findAll().stream()
				.map(Template::getTemplateName)
				.collect(Collectors.toList());
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

}
