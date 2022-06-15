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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
		public static final String CONTAINER_SECURITY = "settings/containers/settingsSecurity";
		public static final String CONTAINER_PERSONALIZATION = "settings/containers/settingsPersonalization";
		public static final String CONTAINER_TRANSACTIONS = "settings/containers/settingsTransactions";
		public static final String CONTAINER_BACKUP = "settings/containers/settingsBackup";
		public static final String CONTAINER_UPDATE = "settings/containers/settingsUpdate";
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

	private final List<Integer> SEARCH_RESULTS_PER_PAGE_OPTIONS = Arrays.asList(10, 20, 25, 30, 50, 100);

	@Autowired
	public SettingsController(SettingsService settingsService, DatabaseService databaseService, CategoryService categoryService, ImportService importService, BudgetMasterUpdateService budgetMasterUpdateService, BackupService backupService, HintService hintService)
	{
		this.settingsService = settingsService;
		this.databaseService = databaseService;
		this.categoryService = categoryService;
		this.importService = importService;
		this.budgetMasterUpdateService = budgetMasterUpdateService;
		this.backupService = backupService;
		this.hintService = hintService;
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
		securitySettingsContainer.fixBooleans();
		securitySettingsContainer.validate(bindingResult);

		if(bindingResult.hasErrors())
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);

			final JsonObject toastContent = getToastContent("notification.settings.security.error", NotificationType.ERROR);
			model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
			return ReturnValues.CONTAINER_SECURITY;
		}

		final String password = securitySettingsContainer.password();
		if(password.equals(PASSWORD_PLACEHOLDER))
		{
			final JsonObject toastContent = getToastContent("notification.settings.security.warning", NotificationType.WARNING);
			model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
			return ReturnValues.CONTAINER_SECURITY;
		}

		settingsService.savePassword(password);

		final JsonObject toastContent = getToastContent("notification.settings.security.saved", NotificationType.SUCCESS);
		model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
		return ReturnValues.CONTAINER_SECURITY;
	}

	@PostMapping(value = "/save/personalization")
	public String saveContainerPersonalization(Model model,
											   @ModelAttribute("PersonalizationSettingsContainer") PersonalizationSettingsContainer personalizationSettingsContainer,
											   BindingResult bindingResult)
	{
		personalizationSettingsContainer.fixBooleans();
		personalizationSettingsContainer.validate(bindingResult);

		final Settings settings = settingsService.getSettings();

		if(bindingResult.hasErrors())
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);

			final JsonObject toastContent = getToastContent("notification.settings.personalization.error", NotificationType.ERROR);
			model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
			model.addAttribute(ModelAttributes.SETTINGS, settings);
			model.addAttribute(ModelAttributes.SEARCH_RESULTS_PER_PAGE, SEARCH_RESULTS_PER_PAGE_OPTIONS);
			return ReturnValues.CONTAINER_PERSONALIZATION;
		}

		// update settings
		settings.setLanguage(personalizationSettingsContainer.getLanguageType());
		settings.setCurrency(personalizationSettingsContainer.currency());
		settings.setUseDarkTheme(personalizationSettingsContainer.useDarkTheme());
		settings.setShowCategoriesAsCircles(personalizationSettingsContainer.showCategoriesAsCircles());
		settings.setSearchItemsPerPage(personalizationSettingsContainer.searchItemsPerPage());
		settingsService.updateSettings(settings);

		// reload localization
		Localization.load();
		categoryService.localizeDefaultCategories();

		final JsonObject toastContent = getToastContent("notification.settings.personalization.saved", NotificationType.SUCCESS);
		model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
		model.addAttribute(ModelAttributes.SETTINGS, settings);
		model.addAttribute(ModelAttributes.SEARCH_RESULTS_PER_PAGE, SEARCH_RESULTS_PER_PAGE_OPTIONS);
		return ReturnValues.CONTAINER_PERSONALIZATION;
	}

	@PostMapping(value = "/save/transactions")
	public String saveContainerTransactions(Model model,
											@ModelAttribute("TransactionsSettingsContainer") TransactionsSettingsContainer transactionsSettingsContainer,
											BindingResult bindingResult)
	{
		transactionsSettingsContainer.fixBooleans();
		transactionsSettingsContainer.validate(bindingResult);

		final Settings settings = settingsService.getSettings();

		if(bindingResult.hasErrors())
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);

			final JsonObject toastContent = getToastContent("notification.settings.transactions.error", NotificationType.ERROR);
			model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
			model.addAttribute(ModelAttributes.SETTINGS, settings);
			return ReturnValues.CONTAINER_TRANSACTIONS;
		}

		// update settings
		settings.setRestActivated(transactionsSettingsContainer.getRestActivated());
		settingsService.updateSettings(settings);

		final JsonObject toastContent = getToastContent("notification.settings.transactions.saved", NotificationType.SUCCESS);
		model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
		model.addAttribute(ModelAttributes.SETTINGS, settings);
		return ReturnValues.CONTAINER_TRANSACTIONS;
	}

	@PostMapping(value = "/save/backup")
	public String saveContainerBackup( Model model,
									  @ModelAttribute("BackupSettingsContainer") BackupSettingsContainer backupSettingsContainer,
									  @RequestParam(value = "runBackup", required = false) Boolean runBackup,
									  BindingResult bindingResult)
	{
		backupSettingsContainer.fixBooleans();
		backupSettingsContainer.validate(bindingResult);

		final Settings settings = settingsService.getSettings();
		backupSettingsContainer.fillMissingFieldsWithDefaults(settings);

		final Settings previousSettings = settingsService.getSettings();

		// update settings here to hand them over to ftl to allow validation to show in case of binding errors
		settings.setBackupReminderActivated(backupSettingsContainer.getBackupReminderActivated());
		settings.setAutoBackupStrategy(backupSettingsContainer.getAutoBackupStrategy());
		settings.setAutoBackupDays(backupSettingsContainer.getAutoBackupDays());
		settings.setAutoBackupTime(backupSettingsContainer.getAutoBackupTime());
		settings.setAutoBackupFilesToKeep(backupSettingsContainer.getAutoBackupFilesToKeep());
		settings.setAutoBackupGitUrl(backupSettingsContainer.getAutoBackupGitUrl());
		settings.setAutoBackupGitBranchName(backupSettingsContainer.getAutoBackupGitBranchName());
		settings.setAutoBackupGitUserName(backupSettingsContainer.getAutoBackupGitUserName());
		settings.setAutoBackupGitToken(backupSettingsContainer.getAutoBackupGitToken());

		if(bindingResult.hasErrors())
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);

			final JsonObject toastContent = getToastContent("notification.settings.backup.error", NotificationType.ERROR);
			model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
			prepareModelBackup(model, settings);
			return ReturnValues.CONTAINER_BACKUP;
		}

		settingsService.updateSettings(settings);

		updateBackupTask(previousSettings, settings);

		// run backup now if requested
		JsonObject toastContent = runBackupIfRequested(runBackup);

		model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
		prepareModelBackup(model, settings);
		return ReturnValues.CONTAINER_BACKUP;
	}

	private void prepareModelBackup(Model model, Settings settings)
	{
		model.addAttribute(ModelAttributes.SETTINGS, settings);
		model.addAttribute(ModelAttributes.AUTO_BACKUP_TIME, AutoBackupTime.values());

		final Optional<LocalDateTime> nextBackupTimeOptional = backupService.getNextRun();
		nextBackupTimeOptional.ifPresent(date -> model.addAttribute(ModelAttributes.NEXT_BACKUP_TIME, date));
		model.addAttribute(ModelAttributes.AUTO_BACKUP_STATUS, backupService.getBackupStatus());
	}

	@PostMapping(value = "/save/update")
	public String saveContainerUpdate(Model model,
									  @ModelAttribute("UpdateSettingsContainer") UpdateSettingsContainer updateSettingsContainer,
									  BindingResult bindingResult)
	{
		updateSettingsContainer.fixBooleans();
		updateSettingsContainer.validate(bindingResult);

		final Settings settings = settingsService.getSettings();

		if(bindingResult.hasErrors())
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);

			final JsonObject toastContent = getToastContent("notification.settings.update.error", NotificationType.ERROR);
			model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
			model.addAttribute(ModelAttributes.SETTINGS, settings);
			return ReturnValues.CONTAINER_UPDATE;
		}

		// update settings
		settings.setAutoUpdateCheckEnabled(updateSettingsContainer.getAutoUpdateCheckEnabled());
		settingsService.updateSettings(settings);

		final JsonObject toastContent = getToastContent("notification.settings.update.saved", NotificationType.SUCCESS);
		model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
		model.addAttribute(ModelAttributes.SETTINGS, settings);
		return ReturnValues.CONTAINER_UPDATE;
	}

	@PostMapping(value = "/save/misc")
	public String saveContainerMisc(Model model)
	{
		hintService.resetAll();

		final JsonObject toastContent = getToastContent("notification.settings.hints.reset", NotificationType.SUCCESS);
		model.addAttribute(ModelAttributes.TOAST_CONTENT, toastContent);
		return ReturnValues.CONTAINER_MISC;
	}

	private JsonObject getToastContent(String localizationKey, NotificationType notificationType)
	{
		final JsonObject toastContent = new JsonObject();
		toastContent.addProperty("localizedMessage", Localization.getString(localizationKey));
		toastContent.addProperty("classes", getToastClasses(notificationType));
		return toastContent;
	}

	private String getToastClasses(NotificationType notificationType)
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

	private void prepareBasicModel(Model model, Settings settings)
	{
		model.addAttribute(ModelAttributes.SETTINGS, settings);
		model.addAttribute(ModelAttributes.SEARCH_RESULTS_PER_PAGE, SEARCH_RESULTS_PER_PAGE_OPTIONS);
		prepareModelBackup(model, settings);
	}
}