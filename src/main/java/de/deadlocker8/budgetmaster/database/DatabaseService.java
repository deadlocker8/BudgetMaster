package de.deadlocker8.budgetmaster.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.tags.TagService;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.thecodelabs.utils.io.PathUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DatabaseService
{
	public static Gson GSON = new GsonBuilder()
			.excludeFieldsWithoutExposeAnnotation()
			.setPrettyPrinting()
			.registerTypeAdapter(DateTime.class, (JsonSerializer<DateTime>) (json, typeOfSrc, context) -> new JsonPrimitive(ISODateTimeFormat.date().print(json)))
			.create();

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseService.class);
	private AccountService accountService;
	private CategoryService categoryService;
	private TransactionService transactionService;
	private TagService tagService;
	private TemplateService templateService;
	private SettingsService settingsService;

	@Autowired
	public DatabaseService(AccountService accountService, CategoryService categoryService, TransactionService transactionService, TagService tagService, TemplateService templateService, SettingsService settingsService)
	{
		this.accountService = accountService;
		this.categoryService = categoryService;
		this.transactionService = transactionService;
		this.tagService = tagService;
		this.templateService = templateService;
		this.settingsService = settingsService;
	}

	public void reset()
	{
		resetTransactions();
		resetCategories();
		resetAccounts();
		resetTags();
	}

	private void resetAccounts()
	{
		LOGGER.info("Resetting accounts...");
		accountService.deleteAll();
		accountService.createDefaults();
		LOGGER.info("All accounts reset.");
	}

	private void resetCategories()
	{
		LOGGER.info("Resetting categories...");
		categoryService.deleteAll();
		categoryService.createDefaults();
		LOGGER.info("All categories reset.");
	}

	private void resetTransactions()
	{
		LOGGER.info("Resetting transactions...");
		transactionService.deleteAll();
		transactionService.createDefaults();
		LOGGER.info("All transactions reset.");
	}

	private void resetTags()
	{
		LOGGER.info("Resetting tags...");
		tagService.deleteAll();
		tagService.createDefaults();
		LOGGER.info("All tags reset.");
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
				e.printStackTrace();
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
			LOGGER.debug("Skipping backup rotation (existing backups: " + existingBackups.size() + ", files to keep: " + numberOfFilesToKeep + ")");
			return filesToDelete;
		}

		LOGGER.debug("Determining old backups (existing backups: " + existingBackups.size() + ", files to keep: " + numberOfFilesToKeep + ")");
		// reserve 1 file for the backup created afterwards
		final int allowedNumberOfFiles = existingBackups.size() - numberOfFilesToKeep + 1;
		for(int i = 0; i < allowedNumberOfFiles; i++)
		{
			final Path oldBackup = Paths.get(existingBackups.get(i));
			LOGGER.debug("Schedule old backup for deletion: " + oldBackup.toString());
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
					.collect(Collectors.toList());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	@Transactional
	public void backupDatabase(Path backupFolderPath)
	{
		LOGGER.info("Backup database...");
		PathUtils.createDirectoriesIfNotExists(backupFolderPath);

		rotatingBackup(backupFolderPath);

		final Database databaseForJsonSerialization = getDatabaseForJsonSerialization();
		final String fileName = getExportFileName(true);
		final String backupPath = backupFolderPath.resolve(fileName).toString();

		try(Writer writer = new FileWriter(backupPath))
		{
			LOGGER.info("Backup database to: {}", backupPath);
			DatabaseService.GSON.toJson(databaseForJsonSerialization, writer);
			LOGGER.info("Backup database DONE");
		}
		catch(IOException e)
		{
			LOGGER.error("Failed to backup database", e);
		}
	}

	public static String getExportFileName(boolean includeTime)
	{
		String formatString = "yyyy_MM_dd";
		if(includeTime)
		{
			formatString = "yyyy_MM_dd_HH_mm_ss";
		}

		return "BudgetMasterDatabase_" + DateTime.now().toString(formatString) + ".json";
	}

	public Database getDatabaseForJsonSerialization()
	{
		List<Category> categories = categoryService.getRepository().findAll();
		List<Account> accounts = accountService.getRepository().findAll();
		List<Transaction> transactions = transactionService.getRepository().findAll();
		List<Transaction> filteredTransactions = filterRepeatingTransactions(transactions);
		List<Template> templates = templateService.getRepository().findAll();
		LOGGER.debug("Reduced " + transactions.size() + " transactions to " + filteredTransactions.size());

		Database database = new Database(categories, accounts, filteredTransactions, templates);
		LOGGER.debug("Created database for JSON with " + database.getTransactions().size() + " transactions, " + database.getCategories().size() + " categories and " + database.getAccounts().size() + " accounts");
		return database;
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
