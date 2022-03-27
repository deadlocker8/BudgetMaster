package de.deadlocker8.budgetmaster.utils.eventlistener;

import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.icon.Iconizable;
import de.deadlocker8.budgetmaster.services.AccessEntityByID;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.templates.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

/**
 * Version 35 requires all accounts, templates and categories (implementors of the Iconizable interface) to have
 * a corresponding icon instance in the database.
 * <p>
 * This class creates icon instances if missing.
 * <p>
 * Run for databases with version 35 (v2.9.0) or older
 */


@Component
public class EnsureAllIconizableHaveAnIconInstance
{
	private static final Logger LOGGER = LoggerFactory.getLogger(EnsureAllIconizableHaveAnIconInstance.class);

	public static final int ACTIVATION_VERSION_CODE = 35;

	private final AccountService accountService;
	private final TemplateService templateService;
	private final CategoryService categoryService;
	private final IconService iconService;

	private final SettingsService settingsService;

	@Autowired
	public EnsureAllIconizableHaveAnIconInstance(AccountService accountService, TemplateService templateService, CategoryService categoryService, IconService iconService, SettingsService settingsService)
	{
		this.accountService = accountService;
		this.templateService = templateService;
		this.categoryService = categoryService;
		this.iconService = iconService;
		this.settingsService = settingsService;
	}

	@EventListener
	@Transactional
	@Order(1)
	public void onApplicationEvent(ApplicationStartedEvent event)
	{
		if(settingsService.getSettings().getInstalledVersionCode() <= ACTIVATION_VERSION_CODE)
		{
			addMissingIconInstances(accountService.getAllEntitiesAsc(), accountService, "accounts");
			addMissingIconInstances(templateService.getAllEntitiesAsc(), templateService, "templates");
			addMissingIconInstances(categoryService.getAllEntitiesAsc(), categoryService, "categories");
		}
	}

	private <T extends Iconizable> void addMissingIconInstances(List<T> items, AccessEntityByID<T> itemService, String type)
	{
		long fixedCount = 0;

		for(Iconizable item : items)
		{
			if(item.getIconReference() != null)
			{
				continue;
			}

			item.updateIcon(iconService, null, null, null, itemService);

			fixedCount++;
		}

		if(fixedCount > 0)
		{
			LOGGER.debug(MessageFormat.format("Fixed {0} {1} (Created missing icon instance)", fixedCount, type));
		}
	}
}
