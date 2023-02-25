package de.deadlocker8.budgetmaster.settings;

import com.google.gson.JsonObject;
import de.deadlocker8.budgetmaster.Build;
import de.deadlocker8.budgetmaster.backup.*;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.database.DatabaseParser;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.hints.HintService;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import de.deadlocker8.budgetmaster.services.ImportService;
import de.deadlocker8.budgetmaster.settings.containers.*;
import de.deadlocker8.budgetmaster.settings.demo.DemoDataCreator;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeyword;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeywordRepository;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeywordService;
import de.deadlocker8.budgetmaster.update.BudgetMasterUpdateService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.RandomUtils;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping(Mappings.SETTINGS)
public class SettingsController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String ERROR = "error";
		public static final String DATABASE = "database";
		public static final String DELETE_DATABASE = "deleteDatabase";
		public static final String VERIFICATION_CODE = "verificationCode";
		public static final String IMPORT_DATABASE = "importDatabase";
		public static final String ERROR_IMPORT_DATABASE = "errorImportDatabase";
		public static final String IMPORT_RESULT_ITEMS = "importResultItems";
		public static final String ERROR_MESSAGES = "errorMessages";
		public static final String PERFORM_UPDATE = "performUpdate";
		public static final String UPDATE_STRING = "updateString";
		public static final String AVAILABLE_VERSION = "availableVersion";
		public static final String SETTINGS = "settings";
		public static final String SEARCH_RESULTS_PER_PAGE = "searchResultsPerPageOptions";
		public static final String AUTO_BACKUP_TIME = "autoBackupTimes";
		public static final String AUTO_BACKUP_STATUS = "autoBackupStatus";
		public static final String NEXT_BACKUP_TIME = "nextBackupTime";
		public static final String TOAST_CONTENT = "toastContent";
		public static final String TRANSACTION_NAME_KEYWORDS = "transactionNameKeywords";
	}

	private static class ReturnValues
	{
		public static final String ALL_ENTITIES = "settings/settings";
		public static final String REDIRECT_ALL_ENTITIES = "redirect:/settings";
		public static final String REDIRECT_DELETE_DATABASE = "redirect:/settings/database/requestDelete";
		public static final String REDIRECT_REQUEST_IMPORT = "redirect:/settings/database/requestImport";
		public static final String REDIRECT_IMPORT_DATABASE_STEP_1 = "redirect:/settings/database/import/step1";
		public static final String IMPORT_DATABASE_STEP_1 = "settings/importStepOne";
		public static final String IMPORT_DATABASE_RESULT = "settings/importResult";
		public static final String CONTAINER_MISC = "settings/containers/settingsMisc";
	}

	private static class RequestAttributeNames
	{
		public static final String DATABASE = "database";
		public static final String IMPORT_TEMPLATE_GROUPS = "importTemplatesGroups";
		public static final String IMPORT_TEMPLATES = "importTemplates";
		public static final String IMPORT_CHARTS = "importCharts";
	}

	public static final String PASSWORD_PLACEHOLDER = "•••••";
	private final SettingsService settingsService;
	private final DatabaseService databaseService;
	private final CategoryService categoryService;
	private final ImportService importService;
	private final BudgetMasterUpdateService budgetMasterUpdateService;
	private final BackupService backupService;
	private final HintService hintService;
	private final TransactionNameKeywordService keywordService;
	private final DemoDataCreator demoDataCreator;

	private final List<Integer> SEARCH_RESULTS_PER_PAGE_OPTIONS = Arrays.asList(10, 20, 25, 30, 50, 100);

	@Autowired
	public SettingsController(SettingsService settingsService, DatabaseService databaseService, CategoryService categoryService, ImportService importService, BudgetMasterUpdateService budgetMasterUpdateService, BackupService backupService, HintService hintService, TransactionNameKeywordService keywordService, DemoDataCreator demoDataCreator)
	{
		this.settingsService = settingsService;
		this.databaseService = databaseService;
		this.categoryService = categoryService;
		this.importService = importService;
		this.budgetMasterUpdateService = budgetMasterUpdateService;
		this.backupService = backupService;
		this.hintService = hintService;
		this.keywordService = keywordService;
		this.demoDataCreator = demoDataCreator;
	}

	@GetMapping
	public String settings(WebRequest request, Model model)
	{
		prepareBasicModel(model, settingsService.getSettings());
		request.removeAttribute(RequestAttributeNames.DATABASE, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.IMPORT_TEMPLATE_GROUPS, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.IMPORT_TEMPLATES, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.IMPORT_CHARTS, RequestAttributes.SCOPE_SESSION);

		return ReturnValues.ALL_ENTITIES;
	}

	@PostMapping(value = "/save/security")
	public String saveContainerSecurity(Model model,
										@ModelAttribute("SecuritySettingsContainer") SecuritySettingsContainer securitySettingsContainer,
										BindingResult bindingResult)
	{
		return saveContainer(model, securitySettingsContainer, bindingResult).templatePath();
	}

	@PostMapping(value = "/save/personalization")
	public String saveContainerPersonalization(Model model,
											   @ModelAttribute("PersonalizationSettingsContainer") PersonalizationSettingsContainer personalizationSettingsContainer,
											   BindingResult bindingResult)
	{
		final SettingsContainerResult result = saveContainer(model, personalizationSettingsContainer, bindingResult);

		if(result.isSuccess())
		{
			// reload localization
			Localization.load();
			categoryService.localizeDefaultCategories();
		}

		return result.templatePath();
	}

	@PostMapping(value = "/save/transactions")
	public String saveContainerTransactions(Model model,
											@ModelAttribute("TransactionsSettingsContainer") TransactionsSettingsContainer transactionsSettingsContainer,
											BindingResult bindingResult)
	{
		final TransactionNameKeywordRepository keywordRepository = keywordService.getRepository();
		keywordRepository.deleteAll();

		final List<TransactionNameKeyword> keywords = transactionsSettingsContainer.getKeywords();
		if(keywords != null)
		{
			for(TransactionNameKeyword keyword : keywords)
			{
				keyword.setID(null);
				keywordRepository.save(keyword);
			}
		}

		return saveContainer(model, transactionsSettingsContainer, bindingResult).templatePath();
	}

	@PostMapping(value = "/save/backup")
	public String saveContainerBackup(Model model,
									  @ModelAttribute("BackupSettingsContainer") BackupSettingsContainer backupSettingsContainer,
									  @RequestParam(value = "runBackup", required = false) Boolean runBackup,
									  BindingResult bindingResult)
	{
		final SettingsContainerResult result = saveContainer(model, backupSettingsContainer, bindingResult);

		if(result.isSuccess())
		{
			updateBackupTask(result.previousSettings(), settingsService.getSettings());

			// run backup now if requested
			JsonObject toastContent = runBackupIfRequested(runBackup);
			model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
			prepareBasicModel(model, settingsService.getSettings());
		}

		return result.templatePath();
	}

	@PostMapping(value = "/save/update")
	public String saveContainerUpdate(Model model,
									  @ModelAttribute("UpdateSettingsContainer") UpdateSettingsContainer updateSettingsContainer,
									  BindingResult bindingResult)
	{
		return saveContainer(model, updateSettingsContainer, bindingResult).templatePath();
	}

	@PostMapping(value = "/save/misc")
	public String saveContainerMisc(Model model)
	{
		hintService.resetAll();

		final JsonObject toastContent = getToastContent("notification.settings.hints.reset", NotificationType.SUCCESS);
		model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
		return ReturnValues.CONTAINER_MISC;
	}

	private SettingsContainerResult saveContainer(Model model, SettingsContainer settingsContainer, BindingResult bindingResult)
	{
		// fix and validate
		settingsContainer.fixBooleans();
		settingsContainer.validate(bindingResult);

		// update settings
		final Settings previousSettings = settingsService.getSettings();
		Settings updatedSettings = settingsContainer.updateSettings(settingsService);

		// cancel on error
		if(bindingResult.hasErrors())
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);

			final JsonObject toastContent = getToastContent(settingsContainer.getErrorLocalizationKey(), NotificationType.ERROR);
			model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
			prepareBasicModel(model, settingsService.getSettings());
			return new SettingsContainerResult(false, settingsContainer.getTemplatePath(), previousSettings);
		}

		// persist changes
		settingsContainer.persistChanges(settingsService, previousSettings, updatedSettings);

		// return success message
		final JsonObject toastContent = getToastContent(settingsContainer.getSuccessLocalizationKey(), NotificationType.SUCCESS);
		model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
		prepareBasicModel(model, settingsService.getSettings());
		return new SettingsContainerResult(true, settingsContainer.getTemplatePath(), previousSettings);
	}

	public static JsonObject getToastContent(String localizationKey, NotificationType notificationType)
	{
		final JsonObject toastContent = new JsonObject();
		toastContent.addProperty("localizedMessage", Localization.getString(localizationKey));
		toastContent.addProperty("classes", getToastClasses(notificationType));
		return toastContent;
	}

	private static String getToastClasses(NotificationType notificationType)
	{
		return MessageFormat.format("{0} {1}", notificationType.getBackgroundColor(), notificationType.getTextColor());
	}

	private JsonObject runBackupIfRequested(Boolean runBackup)
	{
		if(runBackup == null || !runBackup)
		{
			return getToastContent("notification.settings.backup.saved", NotificationType.SUCCESS);
		}

		backupService.runNow();

		BackupStatus backupStatus = backupService.getBackupStatus();
		if(backupStatus == BackupStatus.OK)
		{
			return getToastContent("notification.settings.backup.run.success", NotificationType.SUCCESS);
		}
		else
		{
			return getToastContent("notification.settings.backup.run.error", NotificationType.ERROR);
		}
	}

	public void updateBackupTask(Settings previousSettings, Settings settings)
	{
		backupService.stopBackupCron();
		if(settings.getAutoBackupStrategy() != AutoBackupStrategy.NONE)
		{
			final Optional<BackupTask> previousBackupTaskOptional = settings.getAutoBackupStrategy().getBackupTask(databaseService, settingsService);
			previousBackupTaskOptional.ifPresent(runnable -> runnable.cleanup(previousSettings, settings));

			final Optional<BackupTask> backupTaskOptional = settings.getAutoBackupStrategy().getBackupTask(databaseService, settingsService);
			final String cron = backupService.computeCron(settings.getAutoBackupTime(), settings.getAutoBackupDays());
			backupTaskOptional.ifPresent(runnable -> backupService.startBackupCron(cron, runnable));
		}
	}


	@GetMapping("/database/requestExport")
	public void downloadFile(HttpServletResponse response)
	{
		LOGGER.debug("Exporting database...");

		final BackupDatabase databaseForJsonSerialization = databaseService.getDatabaseForJsonSerialization();
		String data = DatabaseService.GSON.toJson(databaseForJsonSerialization);
		byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

		String fileName = DatabaseService.getExportFileName();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		response.setContentType("application/json; charset=UTF-8");
		response.setContentLength(dataBytes.length);
		response.setCharacterEncoding("UTF-8");

		try(ServletOutputStream out = response.getOutputStream())
		{
			out.write(dataBytes);
			out.flush();
			LOGGER.debug("Exporting database DONE");
		}
		catch(IOException e)
		{
			LOGGER.error("Could not export database", e);
		}
	}

	@GetMapping("/database/requestDelete")
	public String requestDeleteDatabase(Model model)
	{
		String verificationCode = RandomUtils.generateRandomString(RandomUtils.RandomType.BASE_58, 4, RandomUtils.RandomStringPolicy.UPPER, RandomUtils.RandomStringPolicy.DIGIT);
		model.addAttribute(ModelAttributes.DELETE_DATABASE, true);
		model.addAttribute(ModelAttributes.VERIFICATION_CODE, verificationCode);
		prepareBasicModel(model, settingsService.getSettings());
		return ReturnValues.ALL_ENTITIES;
	}

	@PostMapping(value = "/database/delete")
	public String deleteDatabase(WebRequest request, @RequestParam("verificationCode") String verificationCode,
								 @RequestParam("verificationUserInput") String verificationUserInput)
	{
		if(verificationUserInput.equals(verificationCode))
		{
			LOGGER.info("Deleting database...");
			databaseService.reset();
			LOGGER.info("Deleting database DONE.");

			WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.settings.database.delete.success"), NotificationType.SUCCESS));

			return ReturnValues.REDIRECT_ALL_ENTITIES;
		}
		else
		{
			return ReturnValues.REDIRECT_DELETE_DATABASE;
		}
	}

	@GetMapping("/database/requestImport")
	public String requestImportDatabase(Model model)
	{
		model.addAttribute(ModelAttributes.IMPORT_DATABASE, true);
		prepareBasicModel(model, settingsService.getSettings());
		return ReturnValues.ALL_ENTITIES;
	}

	@PostMapping("/database/upload")
	public String upload(WebRequest request, Model model, @RequestParam("file") MultipartFile file)
	{
		if(file.isEmpty())
		{
			return ReturnValues.REDIRECT_REQUEST_IMPORT;
		}

		try
		{
			String jsonString = new String(file.getBytes(), StandardCharsets.UTF_8);
			DatabaseParser importer = new DatabaseParser(jsonString);
			InternalDatabase database = importer.parseDatabaseFromJSON();

			request.setAttribute(RequestAttributeNames.DATABASE, database, RequestAttributes.SCOPE_SESSION);
			return ReturnValues.REDIRECT_IMPORT_DATABASE_STEP_1;
		}
		catch(Exception e)
		{
			LOGGER.error("Database upload failed", e);

			model.addAttribute(ModelAttributes.ERROR_IMPORT_DATABASE, e.getMessage());
			prepareBasicModel(model, settingsService.getSettings());
			return ReturnValues.ALL_ENTITIES;
		}
	}

	@GetMapping("/database/import/step1")
	public String importStepOne(WebRequest request, Model model)
	{
		model.addAttribute(ModelAttributes.DATABASE, request.getAttribute(RequestAttributeNames.DATABASE, RequestAttributes.SCOPE_SESSION));
		return ReturnValues.IMPORT_DATABASE_STEP_1;
	}

	@PostMapping("/database/import/step1")
	public String importStepOnePost(WebRequest request, Model model,
									@RequestParam(value = "TEMPLATE", required = false) boolean importTemplates,
									@RequestParam(value = "TEMPLATE_GROUP", required = false) boolean importTemplateGroups,
									@RequestParam(value = "CHART", required = false) boolean importCharts)
	{
		final InternalDatabase database = (InternalDatabase) request.getAttribute(RequestAttributeNames.DATABASE, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.DATABASE, RequestAttributes.SCOPE_SESSION);

		prepareBasicModel(model, settingsService.getSettings());

		final List<ImportResultItem> importResultItems = importService.importDatabase(database, importTemplateGroups, importTemplates, importCharts);
		model.addAttribute(ModelAttributes.IMPORT_RESULT_ITEMS, importResultItems);
		model.addAttribute(ModelAttributes.ERROR_MESSAGES, importService.getCollectedErrorMessages(importResultItems));

		return ReturnValues.IMPORT_DATABASE_RESULT;
	}

	@GetMapping("/updateSearch")
	@ResponseBody
	public String updateSearch()
	{
		budgetMasterUpdateService.getUpdateService().fetchCurrentVersion();

		if(budgetMasterUpdateService.isUpdateAvailable())
		{
			final JsonObject toastContent = new JsonObject();
			toastContent.addProperty("localizedMessage", Localization.getString("notification.settings.update.available", budgetMasterUpdateService.getAvailableVersionString()));
			toastContent.addProperty("classes", getToastClasses(NotificationType.INFO));
			return toastContent.toString();
		}

		final JsonObject toastContent = getToastContent("notification.settings.update.not.available", NotificationType.INFO);
		return toastContent.toString();
	}

	@GetMapping("/update")
	public String update(Model model)
	{
		model.addAttribute(ModelAttributes.PERFORM_UPDATE, true);
		model.addAttribute(ModelAttributes.UPDATE_STRING, Localization.getString("info.text.update", Build.getInstance().getVersionName(), budgetMasterUpdateService.getAvailableVersionString()));
		model.addAttribute(ModelAttributes.AVAILABLE_VERSION, budgetMasterUpdateService.getAvailableVersionString());
		prepareBasicModel(model, settingsService.getSettings());
		return ReturnValues.ALL_ENTITIES;
	}

	@PostMapping("/git/test")
	@ResponseBody
	public String testGit(@RequestParam(value = "autoBackupGitUrl") String autoBackupGitUrl,
						  @RequestParam(value = "autoBackupGitUserName") String autoBackupGitUserName,
						  @RequestParam(value = "autoBackupGitToken") String autoBackupGitToken)
	{
		if(autoBackupGitToken.equals(PASSWORD_PLACEHOLDER))
		{
			autoBackupGitToken = settingsService.getSettings().getAutoBackupGitToken();
		}

		final CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(autoBackupGitUserName, autoBackupGitToken);
		final Optional<String> checkConnectionOptional = GitHelper.checkConnection(autoBackupGitUrl, credentialsProvider);
		final boolean isValidConnection = checkConnectionOptional.isEmpty();
		String errorText = "";
		if(checkConnectionOptional.isPresent())
		{
			errorText = checkConnectionOptional.get();
		}


		String localizedMessage = Localization.getString("settings.backup.auto.git.test.fail", errorText);
		if(isValidConnection)
		{
			localizedMessage = Localization.getString("settings.backup.auto.git.test.success");
		}

		final JsonObject data = new JsonObject();
		data.addProperty("isValidConnection", isValidConnection);
		data.addProperty("localizedMessage", localizedMessage);

		return data.toString();
	}


	@GetMapping("/database/createDemoData")
	public String createDemoData(Model model)
	{
		demoDataCreator.createDemoData();

		prepareBasicModel(model, settingsService.getSettings());
		return ReturnValues.ALL_ENTITIES;
	}

	private void prepareBasicModel(Model model, Settings settings)
	{
		model.addAttribute(ModelAttributes.SETTINGS, settings);
		model.addAttribute(ModelAttributes.SEARCH_RESULTS_PER_PAGE, SEARCH_RESULTS_PER_PAGE_OPTIONS);
		model.addAttribute(ModelAttributes.AUTO_BACKUP_TIME, AutoBackupTime.values());
		model.addAttribute(ModelAttributes.TRANSACTION_NAME_KEYWORDS, keywordService.getRepository().findAll());

		final Optional<LocalDateTime> nextBackupTimeOptional = backupService.getNextRun();
		nextBackupTimeOptional.ifPresent(date -> model.addAttribute(ModelAttributes.NEXT_BACKUP_TIME, date));
		model.addAttribute(ModelAttributes.AUTO_BACKUP_STATUS, backupService.getBackupStatus());
	}
}