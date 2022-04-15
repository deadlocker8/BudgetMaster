package de.deadlocker8.budgetmaster.settings;

import com.google.gson.JsonObject;
import de.deadlocker8.budgetmaster.Build;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.backup.*;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.database.DatabaseParser;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.database.model.BackupDatabase;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import de.deadlocker8.budgetmaster.services.ImportService;
import de.deadlocker8.budgetmaster.update.BudgetMasterUpdateService;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.RandomUtils;
import de.thecodelabs.versionizer.UpdateItem;
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
		public static final String AVAILABLE_ACCOUNTS = "availableAccounts";
		public static final String ACCOUNT_MATCH_LIST = "accountMatchList";
		public static final String IMPORT_RESULT_ITEMS = "importResultItems";
		public static final String ERROR_MESSAGES = "errorMessages";
		public static final String PERFORM_UPDATE = "performUpdate";
		public static final String UPDATE_STRING = "updateString";
		public static final String SETTINGS = "settings";
		public static final String SEARCH_RESULTS_PER_PAGE = "searchResultsPerPageOptions";
		public static final String AUTO_BACKUP_TIME = "autoBackupTimes";
		public static final String AUTO_BACKUP_STATUS = "autoBackupStatus";
		public static final String NEXT_BACKUP_TIME = "nextBackupTime";
	}

	private static class ReturnValues
	{
		public static final String ALL_ENTITIES = "settings/settings";
		public static final String REDIRECT_ALL_ENTITIES = "redirect:/settings";
		public static final String REDIRECT_DELETE_DATABASE = "redirect:/settings/database/requestDelete";
		public static final String REDIRECT_REQUEST_IMPORT = "redirect:/settings/database/requestImport";
		public static final String REDIRECT_IMPORT_DATABASE_STEP_1 = "redirect:/settings/database/import/step1";
		public static final String IMPORT_DATABASE_STEP_1 = "settings/importStepOne";
		public static final String REDIRECT_IMPORT_DATABASE_STEP_2 = "redirect:/settings/database/import/step2";
		public static final String IMPORT_DATABASE_STEP_2 = "settings/importStepTwo";
		public static final String IMPORT_DATABASE_RESULT = "settings/importResult";
		public static final String REDIRECT_NEW_ACCOUNT = "redirect:/accounts/newAccount";
	}

	private static class RequestAttributeNames
	{
		public static final String DATABASE = "database";
		public static final String IMPORT_TEMPLATE_GROUPS = "importTemplatesGroups";
		public static final String IMPORT_TEMPLATES = "importTemplates";
		public static final String IMPORT_CHARTS = "importCharts";
		public static final String ACCOUNT_MATCH_LIST = "accountMatchList";
	}

	private static final String PASSWORD_PLACEHOLDER = "•••••";
	private final SettingsService settingsService;
	private final DatabaseService databaseService;
	private final AccountService accountService;
	private final CategoryService categoryService;
	private final ImportService importService;
	private final BudgetMasterUpdateService budgetMasterUpdateService;
	private final BackupService backupService;

	private final List<Integer> SEARCH_RESULTS_PER_PAGE_OPTIONS = Arrays.asList(10, 20, 25, 30, 50, 100);

	@Autowired
	public SettingsController(SettingsService settingsService, DatabaseService databaseService, AccountService accountService, CategoryService categoryService, ImportService importService, BudgetMasterUpdateService budgetMasterUpdateService, BackupService backupService)
	{
		this.settingsService = settingsService;
		this.databaseService = databaseService;
		this.accountService = accountService;
		this.categoryService = categoryService;
		this.importService = importService;
		this.budgetMasterUpdateService = budgetMasterUpdateService;
		this.backupService = backupService;
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

	@PostMapping(value = "/save")
	public String post(WebRequest request, Model model,
					   @ModelAttribute("Settings") Settings settings, BindingResult bindingResult,
					   @RequestParam(value = "password") String password,
					   @RequestParam(value = "passwordConfirmation") String passwordConfirmation,
					   @RequestParam(value = "languageType") String languageType,
					   @RequestParam(value = "autoBackupStrategyType", required = false) String autoBackupStrategyType,
					   @RequestParam(value = "runBackup", required = false) Boolean runBackup)
	{
		settings.setLanguage(LanguageType.fromName(languageType));
		if(autoBackupStrategyType == null)
		{
			settings.setAutoBackupStrategy(AutoBackupStrategy.NONE);
		}
		else
		{
			settings.setAutoBackupStrategy(AutoBackupStrategy.fromName(autoBackupStrategyType));
		}

		Optional<FieldError> passwordErrorOptional = settingsService.validatePassword(password, passwordConfirmation);
		passwordErrorOptional.ifPresent(bindingResult::addError);

		SettingsValidator settingsValidator = new SettingsValidator();
		settingsValidator.validate(settings, bindingResult);

		fillMissingFieldsWithDefaults(settings);

		if(bindingResult.hasErrors())
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);
			prepareBasicModel(model, settings);
			return ReturnValues.ALL_ENTITIES;
		}

		if(!password.equals(PASSWORD_PLACEHOLDER))
		{
			settingsService.savePassword(password);
		}

		updateSettings(settings);

		runBackup(request, runBackup);

		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.settings.saved"), NotificationType.SUCCESS));
		return ReturnValues.REDIRECT_ALL_ENTITIES;
	}

	private void runBackup(WebRequest request, Boolean runBackup)
	{
		if(runBackup == null)
		{
			return;
		}

		if(runBackup)
		{
			backupService.runNow();

			BackupStatus backupStatus = backupService.getBackupStatus();
			if(backupStatus == BackupStatus.OK)
			{
				WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.settings.backup.run.success"), NotificationType.SUCCESS));
			}
			else
			{
				WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.settings.backup.run.error"), NotificationType.ERROR));
			}
		}
	}

	private void fillMissingFieldsWithDefaults(Settings settings)
	{
		if(settings.getBackupReminderActivated() == null)
		{
			settings.setBackupReminderActivated(false);
		}

		if(settings.getAutoBackupStrategy() == null)
		{
			settings.setAutoBackupStrategy(AutoBackupStrategy.NONE);
		}

		if(settings.getAutoBackupGitToken().equals(PASSWORD_PLACEHOLDER))
		{
			settings.setAutoBackupGitToken(settingsService.getSettings().getAutoBackupGitToken());
		}

		if(settings.getAutoBackupStrategy() == AutoBackupStrategy.NONE)
		{
			final Settings defaultSettings = Settings.getDefault();
			settings.setAutoBackupDays(defaultSettings.getAutoBackupDays());
			settings.setAutoBackupTime(defaultSettings.getAutoBackupTime());
			settings.setAutoBackupFilesToKeep(defaultSettings.getAutoBackupFilesToKeep());
			settings.setAutoBackupGitUserName(defaultSettings.getAutoBackupGitUserName());
			settings.setAutoBackupGitToken(defaultSettings.getAutoBackupGitToken());
		}

		if(settings.getShowCategoriesAsCircles() == null)
		{
			settings.setShowCategoriesAsCircles(false);
		}
	}

	public void updateSettings(Settings settings)
	{
		final Settings previousSettings = settingsService.getSettings();
		settingsService.updateSettings(settings);

		backupService.stopBackupCron();
		if(settings.getAutoBackupStrategy() != AutoBackupStrategy.NONE)
		{
			final Optional<BackupTask> previousBackupTaskOptional = settings.getAutoBackupStrategy().getBackupTask(databaseService, settingsService);
			previousBackupTaskOptional.ifPresent(runnable -> runnable.cleanup(previousSettings, settings));

			final Optional<BackupTask> backupTaskOptional = settings.getAutoBackupStrategy().getBackupTask(databaseService, settingsService);
			final String cron = backupService.computeCron(settings.getAutoBackupTime(), settings.getAutoBackupDays());
			backupTaskOptional.ifPresent(runnable -> backupService.startBackupCron(cron, runnable));
		}

		Localization.load();
		categoryService.localizeDefaultCategories();
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

	@PostMapping("/database/import/step2")
	public String importStepTwoPost(WebRequest request, Model model,
									@RequestParam(value = "TEMPLATE", required = false) boolean importTemplates,
									@RequestParam(value = "TEMPLATE_GROUP", required = false) boolean importTemplatesGroups,
									@RequestParam(value = "CHART", required = false) boolean importCharts)
	{
		request.setAttribute(RequestAttributeNames.IMPORT_TEMPLATES, importTemplates, RequestAttributes.SCOPE_SESSION);
		request.setAttribute(RequestAttributeNames.IMPORT_TEMPLATE_GROUPS, importTemplatesGroups, RequestAttributes.SCOPE_SESSION);
		request.setAttribute(RequestAttributeNames.IMPORT_CHARTS, importCharts, RequestAttributes.SCOPE_SESSION);

		model.addAttribute(ModelAttributes.DATABASE, request.getAttribute(RequestAttributeNames.DATABASE, RequestAttributes.SCOPE_SESSION));
		model.addAttribute(ModelAttributes.AVAILABLE_ACCOUNTS, accountService.getAllEntitiesAsc());
		return ReturnValues.REDIRECT_IMPORT_DATABASE_STEP_2;
	}

	@GetMapping("/database/import/step2")
	public String importStepTwo(WebRequest request, Model model)
	{
		Object accountMatches = request.getAttribute(RequestAttributeNames.ACCOUNT_MATCH_LIST, RequestAttributes.SCOPE_SESSION);
		if(accountMatches != null)
		{
			final AccountMatchList accountMatchList = (AccountMatchList) accountMatches;
			model.addAttribute(ModelAttributes.ACCOUNT_MATCH_LIST, accountMatchList);
			request.removeAttribute(RequestAttributeNames.ACCOUNT_MATCH_LIST, RequestAttributes.SCOPE_SESSION);
		}

		model.addAttribute(ModelAttributes.DATABASE, request.getAttribute(RequestAttributeNames.DATABASE, RequestAttributes.SCOPE_SESSION));
		model.addAttribute(ModelAttributes.AVAILABLE_ACCOUNTS, accountService.getAllEntitiesAsc());
		return ReturnValues.IMPORT_DATABASE_STEP_2;
	}

	@PostMapping("/database/import/step2/createAccount")
	public String importCreateAccount(WebRequest request, @ModelAttribute("Import") AccountMatchList accountMatchList)
	{
		request.setAttribute(RequestAttributeNames.ACCOUNT_MATCH_LIST, accountMatchList, RequestAttributes.SCOPE_SESSION);
		return ReturnValues.REDIRECT_NEW_ACCOUNT;
	}

	@PostMapping("/database/import/step3")
	public String importDatabase(WebRequest request, @ModelAttribute("Import") AccountMatchList accountMatchList, Model model)
	{
		final InternalDatabase database = (InternalDatabase) request.getAttribute(RequestAttributeNames.DATABASE, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.DATABASE, RequestAttributes.SCOPE_SESSION);

		final Boolean importTemplates = (Boolean) request.getAttribute(RequestAttributeNames.IMPORT_TEMPLATES, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.IMPORT_TEMPLATES, RequestAttributes.SCOPE_SESSION);

		final Boolean importTemplateGroups = (Boolean) request.getAttribute(RequestAttributeNames.IMPORT_TEMPLATE_GROUPS, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.IMPORT_TEMPLATE_GROUPS, RequestAttributes.SCOPE_SESSION);

		final Boolean importCharts = (Boolean) request.getAttribute(RequestAttributeNames.IMPORT_CHARTS, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(RequestAttributeNames.IMPORT_CHARTS, RequestAttributes.SCOPE_SESSION);

		prepareBasicModel(model, settingsService.getSettings());

		final List<ImportResultItem> importResultItems = importService.importDatabase(database, accountMatchList, importTemplateGroups, importTemplates, importCharts);
		model.addAttribute(ModelAttributes.IMPORT_RESULT_ITEMS, importResultItems);
		model.addAttribute(ModelAttributes.ERROR_MESSAGES, importService.getCollectedErrorMessages(importResultItems));

		return ReturnValues.IMPORT_DATABASE_RESULT;
	}

	@GetMapping("/updateSearch")
	public String updateSearch(WebRequest request)
	{
		budgetMasterUpdateService.getUpdateService().fetchCurrentVersion();

		if(budgetMasterUpdateService.isUpdateAvailable())
		{
			WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.settings.update.available", budgetMasterUpdateService.getAvailableVersionString()), NotificationType.INFO));
		}
		return ReturnValues.REDIRECT_ALL_ENTITIES;
	}

	@GetMapping("/update")
	public String update(Model model)
	{
		model.addAttribute(ModelAttributes.PERFORM_UPDATE, true);
		model.addAttribute(ModelAttributes.UPDATE_STRING, Localization.getString("info.text.update", Build.getInstance().getVersionName(), budgetMasterUpdateService.getAvailableVersionString()));
		prepareBasicModel(model, settingsService.getSettings());
		return ReturnValues.ALL_ENTITIES;
	}

	@GetMapping("/performUpdate")
	public String performUpdate()
	{
		if(budgetMasterUpdateService.isRunningFromSource())
		{
			LOGGER.debug("Running from source code: Skipping update check");
			return ReturnValues.REDIRECT_ALL_ENTITIES;
		}

		UpdateItem.Entry entry = new UpdateItem.Entry(budgetMasterUpdateService.getAvailableVersion());
		try
		{
			budgetMasterUpdateService.getUpdateService().runVersionizerInstance(entry);
		}
		catch(IOException e)
		{
			LOGGER.error("Could not update BudgetMaster version", e);
		}

		LOGGER.info(MessageFormat.format("Stopping BudgetMaster for update to version {0}", budgetMasterUpdateService.getAvailableVersionString()));
		System.exit(0);

		return "";
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
		model.addAttribute(ModelAttributes.AUTO_BACKUP_TIME, AutoBackupTime.values());

		final Optional<LocalDateTime> nextBackupTimeOptional = backupService.getNextRun();
		nextBackupTimeOptional.ifPresent(date -> model.addAttribute(ModelAttributes.NEXT_BACKUP_TIME, date));
		model.addAttribute(ModelAttributes.AUTO_BACKUP_STATUS, backupService.getBackupStatus());
	}
}