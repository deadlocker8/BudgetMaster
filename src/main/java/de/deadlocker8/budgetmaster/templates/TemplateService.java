package de.deadlocker8.budgetmaster.templates;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.services.Resetable;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateService implements Resetable
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private final TemplateRepository templateRepository;
	private final AccountService accountService;
	private final CategoryService categoryService;

	@Autowired
	public TemplateService(TemplateRepository templateRepository, AccountService accountService, CategoryService categoryService)
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

	public void prepareTemplateForNewTransaction(Template template)
	{
		if(template.getCategory() == null)
		{
			template.setCategory(categoryService.getRepository().findByType(CategoryType.NONE));
		}

		if(template.getAmount() == null)
		{
			final Account selectedAccount = accountService.getRepository().findByIsSelected(true);
			template.setAccount(selectedAccount);
		}
	}
}
