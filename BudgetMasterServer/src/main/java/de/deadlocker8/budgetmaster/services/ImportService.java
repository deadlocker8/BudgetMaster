package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.charts.ChartService;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.importer.*;
import de.deadlocker8.budgetmaster.icon.IconRepository;
import de.deadlocker8.budgetmaster.images.ImageRepository;
import de.deadlocker8.budgetmaster.repeating.RepeatingTransactionUpdater;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import de.deadlocker8.budgetmaster.utils.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportService.class);

	private final CategoryRepository categoryRepository;
	private final TransactionRepository transactionRepository;
	private final TemplateGroupRepository templateGroupRepository;
	private final TemplateRepository templateRepository;
	private final TagRepository tagRepository;
	private final ChartService chartService;
	private final ImageRepository imageRepository;
	private final RepeatingTransactionUpdater repeatingTransactionUpdater;
	private final AccountRepository accountRepository;
	private final IconRepository iconRepository;


	private InternalDatabase database;

	@Autowired
	public ImportService(CategoryRepository categoryRepository, TransactionRepository transactionRepository, TemplateGroupRepository templateGroupRepository, TemplateRepository templateRepository,
						 TagRepository tagRepository, ChartService chartService, ImageRepository imageRepository, RepeatingTransactionUpdater repeatingTransactionUpdater, AccountRepository accountRepository, IconRepository iconRepository)
	{
		this.categoryRepository = categoryRepository;
		this.transactionRepository = transactionRepository;
		this.templateGroupRepository = templateGroupRepository;
		this.templateRepository = templateRepository;
		this.tagRepository = tagRepository;
		this.chartService = chartService;
		this.imageRepository = imageRepository;
		this.repeatingTransactionUpdater = repeatingTransactionUpdater;
		this.accountRepository = accountRepository;
		this.iconRepository = iconRepository;
	}

	public List<ImportResultItem> importDatabase(InternalDatabase database, Boolean importTemplateGroups, Boolean importTemplates, Boolean importCharts)
	{
		this.database = database;

		final List<ImportResultItem> importResultItems = new ArrayList<>();

		LOGGER.debug("Importing database...");
		importResultItems.add(new ImageImporter(imageRepository).importItems(database.getImages()));
		new IconImporter(iconRepository).importItems(database.getIcons());
		importResultItems.add(new CategoryImporter(categoryRepository).importItems(database.getCategories()));
		importResultItems.add(new AccountImporter(accountRepository).importItems(database.getAccounts()));

		final TagImporter tagImporter = new TagImporter(tagRepository);
		importResultItems.add(new TransactionImporter(transactionRepository, tagImporter).importItems(database.getTransactions()));

		if(importTemplateGroups)
		{
			importResultItems.add(new TemplateGroupImporter(templateGroupRepository).importItems(database.getTemplateGroups()));
		}
		else
		{
			importResultItems.add(new ImportResultItem(EntityType.TEMPLATE_GROUP, 0, 0, List.of()));
		}

		if(importTemplates)
		{
			final TemplateGroup defaultTemplateGroup = templateGroupRepository.findFirstByType(TemplateGroupType.DEFAULT);
			importResultItems.add(new TemplateImporter(templateRepository, tagImporter, defaultTemplateGroup, !importTemplateGroups).importItems(database.getTemplates()));
		}
		else
		{
			importResultItems.add(new ImportResultItem(EntityType.TEMPLATE, 0, 0, List.of()));
		}

		if(importCharts)
		{
			importResultItems.add(new ChartImporter(chartService).importItems(database.getCharts()));
		}
		else
		{
			importResultItems.add(new ImportResultItem(EntityType.CHART, 0, 0, List.of()));
		}

		LOGGER.debug("Updating repeating transactions...");
		repeatingTransactionUpdater.updateRepeatingTransactions(DateHelper.getCurrentDate());

		LOGGER.debug("Importing database DONE");
		return importResultItems;
	}

	public InternalDatabase getDatabase()
	{
		return database;
	}

	public List<String> getCollectedErrorMessages(List<ImportResultItem> importResultItems)
	{
		return importResultItems.stream()
				.flatMap(importResultItem -> importResultItem.getCollectedErrorMessages().stream())
				.toList();
	}
}
