package de.deadlocker8.budgetmaster.settings;

import com.google.gson.JsonObject;
import de.deadlocker8.budgetmaster.Build;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.authentication.User;
import de.deadlocker8.budgetmaster.authentication.UserRepository;
import de.deadlocker8.budgetmaster.backup.*;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.database.DatabaseParser;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.services.ImportEntityType;
import de.deadlocker8.budgetmaster.services.ImportService;
import de.deadlocker8.budgetmaster.services.UpdateCheckService;
import de.deadlocker8.budgetmaster.update.BudgetMasterUpdateService;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.RandomUtils;
import de.thecodelabs.versionizer.UpdateItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping(Mappings.SETTINGS)
public class SettingsController extends BaseController
{
	private static final String PASSWORD_PLACEHOLDER = "•••••";
	private final SettingsService settingsService;
	private final UserRepository userRepository;
	private final DatabaseService databaseService;
	private final AccountService accountService;
	private final CategoryService categoryService;
	private final ImportService importService;
	private final BudgetMasterUpdateService budgetMasterUpdateService;
	private final UpdateCheckService updateCheckService;
	private final BackupService scheduleTaskService;

	private final List<Integer> SEARCH_RESULTS_PER_PAGE_OPTIONS = Arrays.asList(10, 20, 25, 30, 50, 100);

	@Autowired
	public SettingsController(SettingsService settingsService, UserRepository userRepository, DatabaseService databaseService, AccountService accountService, CategoryService categoryService, ImportService importService, BudgetMasterUpdateService budgetMasterUpdateService, UpdateCheckService updateCheckService, BackupService scheduleTaskService)
	{
		this.settingsService = settingsService;
		this.userRepository = userRepository;
		this.databaseService = databaseService;
		this.accountService = accountService;
		this.categoryService = categoryService;
		this.importService = importService;
		this.budgetMasterUpdateService = budgetMasterUpdateService;
		this.updateCheckService = updateCheckService;
		this.scheduleTaskService = scheduleTaskService;
	}

	@GetMapping
	public String settings(WebRequest request, Model model)
	{
		prepareBasicModel(model, settingsService.getSettings());
		request.removeAttribute("database", WebRequest.SCOPE_SESSION);
		return "settings/settings";
	}

	@PostMapping(value = "/save")
	public String post(WebRequest request, Model model,
					   @ModelAttribute("Settings") Settings settings, BindingResult bindingResult,
					   @RequestParam(value = "password") String password,
					   @RequestParam(value = "passwordConfirmation") String passwordConfirmation,
					   @RequestParam(value = "languageType") String languageType,
					   @RequestParam(value = "autoBackupStrategyType") String autoBackupStrategyType)
	{
		settings.setLanguage(LanguageType.fromName(languageType));
		settings.setAutoBackupStrategy(AutoBackupStrategy.fromName(autoBackupStrategyType));

		Optional<FieldError> passwordErrorOptional = validatePassword(password, passwordConfirmation);
		if(passwordErrorOptional.isPresent())
		{
			bindingResult.addError(passwordErrorOptional.get());
		}

		SettingsValidator settingsValidator = new SettingsValidator();
		settingsValidator.validate(settings, bindingResult);

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

		final String cron = scheduleTaskService.computeCron(settings.getAutoBackupTime(), settings.getAutoBackupDays());
		scheduleTaskService.stopBackupCron();
		if(settings.getAutoBackupStrategy() == AutoBackupStrategy.NONE)
		{
			final Settings defaultSettings = Settings.getDefault();
			settings.setAutoBackupDays(defaultSettings.getAutoBackupDays());
			settings.setAutoBackupTime(defaultSettings.getAutoBackupTime());
			settings.setAutoBackupFilesToKeep(defaultSettings.getAutoBackupFilesToKeep());
			settings.setAutoBackupGitUserName(defaultSettings.getAutoBackupGitUserName());
			settings.setAutoBackupGitToken(defaultSettings.getAutoBackupGitToken());
		}
		else
		{
			final Optional<BackupTask> previousBackupTaskOptional = settings.getAutoBackupStrategy().getBackupTask(databaseService, settingsService);
			previousBackupTaskOptional.ifPresent(runnable -> runnable.cleanup(settingsService.getSettings(), settings));

			final Optional<BackupTask> backupTaskOptional = settings.getAutoBackupStrategy().getBackupTask(databaseService, settingsService);
			backupTaskOptional.ifPresent(runnable -> scheduleTaskService.startBackupCron(cron, runnable));
		}


		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			prepareBasicModel(model, settings);
			return "settings/settings";
		}

		if(!password.equals(PASSWORD_PLACEHOLDER))
		{
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String encryptedPassword = bCryptPasswordEncoder.encode(password);
			User user = userRepository.findByName("Default");
			user.setPassword(encryptedPassword);
			userRepository.save(user);
		}

		settingsService.updateSettings(settings);

		Localization.load();
		categoryService.localizeDefaultCategories();

		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.settings.saved"), NotificationType.SUCCESS));

		return "redirect:/settings";
	}

	private Optional<FieldError> validatePassword(String password, String passwordConfirmation)
	{
		if(password == null || password.equals(""))
		{
			return Optional.of(new FieldError("Settings", "password", password, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_EMPTY}, null, Strings.WARNING_SETTINGS_PASSWORD_EMPTY));
		}
		else if(password.length() < 3)
		{
			return Optional.of(new FieldError("Settings", "password", password, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_LENGTH}, null, Strings.WARNING_SETTINGS_PASSWORD_LENGTH));
		}

		if(passwordConfirmation == null || passwordConfirmation.equals(""))
		{
			return Optional.of(new FieldError("Settings", "passwordConfirmation", passwordConfirmation, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_EMPTY}, null, Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_EMPTY));
		}

		if(!password.equals(passwordConfirmation))
		{
			return Optional.of(new FieldError("Settings", "passwordConfirmation", passwordConfirmation, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_WRONG}, null, Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_WRONG));
		}

		return Optional.empty();
	}

	@GetMapping("/database/requestExport")
	public void downloadFile(HttpServletResponse response)
	{
		LOGGER.debug("Exporting database...");

		final Database databaseForJsonSerialization = databaseService.getDatabaseForJsonSerialization();
		String data = DatabaseService.GSON.toJson(databaseForJsonSerialization);
		byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

		String fileName = DatabaseService.getExportFileName(false);
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
			e.printStackTrace();
		}
	}

	@GetMapping("/database/requestDelete")
	public String requestDeleteDatabase(Model model)
	{
		String verificationCode = RandomUtils.generateRandomString(RandomUtils.RandomType.BASE_58, 4, RandomUtils.RandomStringPolicy.UPPER, RandomUtils.RandomStringPolicy.DIGIT);
		model.addAttribute("deleteDatabase", true);
		model.addAttribute("verificationCode", verificationCode);
		prepareBasicModel(model, settingsService.getSettings());
		return "settings/settings";
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

			return "redirect:/settings";
		}
		else
		{
			return "redirect:/settings/database/requestDelete";
		}
	}

	@GetMapping("/database/requestImport")
	public String requestImportDatabase(Model model)
	{
		model.addAttribute("importDatabase", true);
		prepareBasicModel(model, settingsService.getSettings());
		return "settings/settings";
	}

	@RequestMapping("/database/upload")
	public String upload(WebRequest request, Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
	{
		if(file.isEmpty())
		{
			return "redirect:/settings/database/requestImport";
		}

		try
		{
			String jsonString = new String(file.getBytes(), StandardCharsets.UTF_8);
			DatabaseParser importer = new DatabaseParser(jsonString, categoryService.findByType(CategoryType.NONE));
			Database database = importer.parseDatabaseFromJSON();

			request.setAttribute("database", database, WebRequest.SCOPE_SESSION);
			return "redirect:/settings/database/accountMatcher";
		}
		catch(Exception e)
		{
			e.printStackTrace();

			model.addAttribute("errorImportDatabase", e.getMessage());
			prepareBasicModel(model, settingsService.getSettings());
			return "settings/settings";
		}
	}

	@GetMapping("/database/accountMatcher")
	public String openAccountMatcher(WebRequest request, Model model)
	{
		model.addAttribute("database", request.getAttribute("database", WebRequest.SCOPE_SESSION));
		model.addAttribute("availableAccounts", accountService.getAllActivatedAccountsAsc());
		model.addAttribute("settings", settingsService.getSettings());
		return "settings/import";
	}

	@PostMapping("/database/import")
	public String importDatabase(WebRequest request, @ModelAttribute("Import") AccountMatchList accountMatchList, Model model)
	{
		final Map<ImportEntityType, Integer> numberOfImportedEntitiesByType = importService.importDatabase((Database) request.getAttribute("database", WebRequest.SCOPE_SESSION), accountMatchList);
		request.removeAttribute("database", RequestAttributes.SCOPE_SESSION);
		prepareBasicModel(model, settingsService.getSettings());

		final String message = Localization.getString("notification.settings.database.import.success",
				numberOfImportedEntitiesByType.get(ImportEntityType.ACCOUNT),
				numberOfImportedEntitiesByType.get(ImportEntityType.TRANSACTION),
				numberOfImportedEntitiesByType.get(ImportEntityType.CATEGORY),
				numberOfImportedEntitiesByType.get(ImportEntityType.TEMPLATE),
				numberOfImportedEntitiesByType.get(ImportEntityType.CHART));
		WebRequestUtils.putNotification(request, new Notification(message, NotificationType.SUCCESS));

		return "settings/settings";
	}

	@GetMapping("/updateSearch")
	public String updateSearch(WebRequest request)
	{
		budgetMasterUpdateService.getUpdateService().fetchCurrentVersion();

		if(updateCheckService.isUpdateAvailable())
		{
			WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.settings.update.available", updateCheckService.getAvailableVersionString()), NotificationType.INFO));
		}
		return "redirect:/settings";
	}

	@GetMapping("/update")
	public String update(Model model)
	{
		model.addAttribute("performUpdate", true);
		model.addAttribute("updateString", Localization.getString("info.text.update", Build.getInstance().getVersionName(), budgetMasterUpdateService.getAvailableVersionString()));
		prepareBasicModel(model, settingsService.getSettings());
		return "settings/settings";
	}

	@GetMapping("/performUpdate")
	public String performUpdate()
	{
		if(budgetMasterUpdateService.isRunningFromSource())
		{
			LOGGER.debug("Running from source code: Skipping update check");
			return "redirect:/settings";
		}

		UpdateItem.Entry entry = new UpdateItem.Entry(budgetMasterUpdateService.getAvailableVersion());
		try
		{
			budgetMasterUpdateService.getUpdateService().runVersionizerInstance(entry);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		LOGGER.info(MessageFormat.format("Stopping BudgetMaster for update to version {0}", budgetMasterUpdateService.getAvailableVersionString()));
		System.exit(0);

		return "";
	}

	@RequestMapping("/hideFirstUseBanner")
	public String hideFirstUseBanner()
	{
		settingsService.disableFirstUseBanner();
		return "redirect:/";
	}

	@PostMapping("/git/test")
	public String testGit(Model model,
						  @RequestParam(value = "autoBackupGitUrl") String autoBackupGitUrl,
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

		model.addAttribute("data", data.toString());
		return "helpers/sendData";
	}

	private void prepareBasicModel(Model model, Settings settings)
	{
		model.addAttribute("settings", settings);
		model.addAttribute("searchResultsPerPageOptions", SEARCH_RESULTS_PER_PAGE_OPTIONS);
		model.addAttribute("autoBackupTimes", AutoBackupTime.values());

		final Optional<DateTime> nextBackupTimeOptional = scheduleTaskService.getNextRun();
		nextBackupTimeOptional.ifPresent(date -> model.addAttribute("nextBackupTime", date));
		model.addAttribute("autoBackupStatus", scheduleTaskService.getBackupStatus());
	}
}