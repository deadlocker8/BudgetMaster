package de.deadlocker8.budgetmaster.templates;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.services.AccessAllEntities;
import de.deadlocker8.budgetmaster.services.Resettable;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionBase;
import org.padler.natorder.NaturalOrderComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TemplateService implements Resettable, AccessAllEntities<Template>
{
	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	private final TemplateRepository templateRepository;
	private final AccountService accountService;
	private final CategoryService categoryService;


	@Autowired
	public TemplateService(TemplateRepository templateRepository, AccountService accountService, CategoryService categoryService, SettingsService settingsService)
	{
		this.templateRepository = templateRepository;
		this.accountService = accountService;
		this.categoryService = categoryService;
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
			final Account selectedAccount = accountService.getRepository().findByIsSelected(true);
			template.setAccount(selectedAccount);
		}

		final Account account = template.getAccount();
		if(account != null && account.getAccountState() != AccountState.FULL_ACCESS)
		{
			template.setAccount(accountService.getRepository().findByIsDefault(true));
		}
	}

	public void prepareModelNewOrEdit(Model model, boolean isEdit, TransactionBase item, List<Account> accounts)
	{
		model.addAttribute("isEdit", isEdit);
		model.addAttribute("categories", categoryService.getAllEntitiesAsc());
		model.addAttribute("accounts", accounts);
		model.addAttribute("template", item);
		model.addAttribute("suggestionsJSON", GSON.toJson(new ArrayList<String>()));
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
		templates.sort((t1, t2) -> new NaturalOrderComparator().compare(t1.getName(), t2.getName()));
		return templates;
	}
}
