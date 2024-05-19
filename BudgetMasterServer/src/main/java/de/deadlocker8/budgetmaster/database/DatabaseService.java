package de.deadlocker8.budgetmaster.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.charts.ChartService;
import de.deadlocker8.budgetmaster.charts.ChartType;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.database.model.v11.BackupDatabase_v11;
import de.deadlocker8.budgetmaster.hints.HintService;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.services.Resettable;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.tags.TagService;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupService;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.transactions.csvimport.CsvImportSettings;
import de.deadlocker8.budgetmaster.transactions.csvimport.CsvImportSettingsService;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeyword;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeywordService;
import de.deadlocker8.budgetmaster.utils.DateHelper;
import de.thecodelabs.utils.io.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DatabaseService
{
	public static final Gson GSON = new GsonBuilder().create();
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseService.class);

	private static final String BACKUP_DATE_FORMAT = "yyyy_MM_dd_HH_mm_ss";

	private final AccountService accountService;
	private final CategoryService categoryService;
	private final TransactionService transactionService;
	private final TagService tagService;
	private final TemplateService templateService;
	private final TemplateGroupService templateGroupService;
	private final ChartService chartService;
	private final SettingsService settingsService;
	private final ImageService imageService;
	private final IconService iconService;
	private final HintService hintService;
	private final TransactionNameKeywordService transactionNameKeywordService;
	private final CsvImportSettingsService csvImportSettingsService;

	private final List<Resettable> services;

	@Autowired
	public DatabaseService(AccountService accountService, CategoryService categoryService, TransactionService transactionService, TagService tagService, TemplateService templateService, TemplateGroupService templateGroupService, ChartService chartService, SettingsService settingsService, ImageService imageService, IconService iconService, HintService hintService, TransactionNameKeywordService transactionNameKeywordService, CsvImportSettingsService csvImportSettingsService)
	{
		this.accountService = accountService;
		this.categoryService = categoryService;
		this.transactionService = transactionService;
		this.tagService = tagService;
		this.templateService = templateService;
		this.templateGroupService = templateGroupService;
		this.chartService = chartService;
		this.settingsService = settingsService;
		this.imageService = imageService;
		this.iconService = iconService;
		this.hintService = hintService;
		this.transactionNameKeywordService = transactionNameKeywordService;
		this.csvImportSettingsService = csvImportSettingsService;
		this.services = List.of(transactionService, templateService, templateGroupService, categoryService, accountService, tagService, chartService, iconService, imageService, tagService, hintService, transactionNameKeywordService);
	}

	public void reset()
	{
		for(Resettable service : services)
		{
			service.deleteAll();
		}

		// create defaults after deletion to avoid deletion of newly created defaults
		createDefaults();
	}

	public void createDefaults()
	{
		for(Resettable service : services)
		{
			service.createDefaults();
		}
	}

	public void rotatingBackup(Path backupFolderPath)
	{
		final List<Path> filesToDelete = determineFilesToDelete(backupFolderPath);

		for(Path path : filesToDelete)
		{
			try
			{
				Files.deleteIfExists(path);
			}
			catch(IOException e)
			{
				LOGGER.error("Can not rotate backup", e);
			}
		}
	}

	public List<Path> determineFilesToDelete(Path backupFolderPath)
	{
		final ArrayList<Path> filesToDelete = new ArrayList<>();

		final Integer numberOfFilesToKeep = settingsService.getSettings().getAutoBackupFilesToKeep();
		if(numberOfFilesToKeep == 0)
		{
			LOGGER.debug("Skipping backup rotation since number of files to keep is set to unlimited");
			return filesToDelete;
		}

		final List<String> existingBackups = getExistingBackups(backupFolderPath);
		if(existingBackups.size() < numberOfFilesToKeep)
		{
			LOGGER.debug(MessageFormat.format("Skipping backup rotation (existing backups: {0}, files to keep: {1})", existingBackups.size(), numberOfFilesToKeep));
			return filesToDelete;
		}

		LOGGER.debug(MessageFormat.format("Determining old backups (existing backups: {0}, files to keep: {1})", existingBackups.size(), numberOfFilesToKeep));
		// reserve 1 file for the backup created afterwards
		final int allowedNumberOfFiles = existingBackups.size() - numberOfFilesToKeep + 1;
		for(int i = 0; i < allowedNumberOfFiles; i++)
		{
			final Path oldBackup = Paths.get(existingBackups.get(i));
			LOGGER.debug(MessageFormat.format("Schedule old backup for deletion: {0}", oldBackup));
			filesToDelete.add(oldBackup);
		}

		return filesToDelete;
	}

	public List<String> getExistingBackups(Path backupFolderPath)
	{
		try(Stream<Path> walk = Files.walk(backupFolderPath))
		{
			return walk.filter(Files::isRegularFile)
					.map(Path::toString)
					.filter(path -> path.endsWith(".json"))
					.sorted()
					.toList();
		}
		catch(IOException e)
		{
			LOGGER.error("Could not determine existing backups", e);
		}

		return new ArrayList<>();
	}

	@Transactional
	public void backupDatabase(Path backupFolderPath)
	{
		LOGGER.info("Backup database...");
		PathUtils.createDirectoriesIfNotExists(backupFolderPath);

		rotatingBackup(backupFolderPath);

		final String fileName = getExportFileName();
		final Path backupPath = backupFolderPath.resolve(fileName);

		exportDatabase(backupPath);
	}

	@Transactional
	public void exportDatabase(Path backupPath)
	{
		final BackupDatabase database = getDatabaseForJsonSerialization();

		try(Writer writer = new FileWriter(backupPath.toString(), StandardCharsets.UTF_8))
		{
			LOGGER.info("Backup database to: {}", backupPath);
			DatabaseService.GSON.toJson(database, writer);
			LOGGER.info("Backup database DONE");
		}
		catch(IOException e)
		{
			LOGGER.error("Failed to backup database", e);
		}
	}

	public static String getExportFileName()
	{
		return "BudgetMasterDatabase_" + DateHelper.getCurrentDateTime().format(DateTimeFormatter.ofPattern(BACKUP_DATE_FORMAT)) + ".json";
	}

	public BackupDatabase getDatabaseForJsonSerialization()
	{
		final List<Category> categories = categoryService.getAllEntitiesAsc();
		final List<Account> accounts = accountService.getRepository().findAll();
		final List<Transaction> transactions = transactionService.getRepository().findAll();
		final List<Transaction> filteredTransactions = filterRepeatingTransactions(transactions);
		final List<TemplateGroup> templateGroups = templateGroupService.getRepository().findAll();
		final List<Template> templates = templateService.getRepository().findAll();
		final List<Chart> charts = chartService.getRepository().findAllByType(ChartType.CUSTOM);
		final List<Image> images = imageService.getRepository().findAll();
		final List<Icon> icons = iconService.getRepository().findAll();
		final List<TransactionNameKeyword> transactionNameKeywords = transactionNameKeywordService.getRepository().findAll();
		final CsvImportSettings csvImportSettings = csvImportSettingsService.getCsvImportSettings();

		LOGGER.debug(MessageFormat.format("Reduced {0} transactions to {1}", transactions.size(), filteredTransactions.size()));

		InternalDatabase database = new InternalDatabase(categories, accounts, filteredTransactions, templateGroups, templates, charts, images, icons, transactionNameKeywords, List.of(csvImportSettings));
		LOGGER.debug(MessageFormat.format("Created database for JSON with {0} transactions, {1} categories, {2} accounts, {3} templates groups, {4} templates, {5} charts {6} images {7} icons and {8} transaction name keywords", database.getTransactions().size(), database.getCategories().size(), database.getAccounts().size(), database.getTemplateGroups().size(), database.getTemplates().size(), database.getCharts().size(), database.getImages().size(), database.getIcons().size(), database.getTransactionNameKeywords().size()));

		BackupDatabase_v11 databaseInExternalForm = BackupDatabase_v11.createFromInternalEntities(database);
		LOGGER.debug("Converted database to external form");
		return databaseInExternalForm;
	}

	private List<Transaction> filterRepeatingTransactions(List<Transaction> transactions)
	{
		List<Transaction> filteredTransactions = new ArrayList<>();

		for(Transaction transaction : transactions)
		{
			if(transaction.isRepeating())
			{
				if(isRepeatingOptionInList(transaction.getRepeatingOption(), filteredTransactions))
				{
					continue;
				}
			}

			filteredTransactions.add(transaction);
		}

		return filteredTransactions;
	}

	private boolean isRepeatingOptionInList(RepeatingOption repeatingOption, List<Transaction> transactions)
	{
		for(Transaction transaction : transactions)
		{
			if(transaction.isRepeating())
			{
				if(transaction.getRepeatingOption().equals(repeatingOption))
				{
					return true;
				}
			}
		}
		return false;
	}
}
