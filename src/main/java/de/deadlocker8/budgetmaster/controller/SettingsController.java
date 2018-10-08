package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.authentication.User;
import de.deadlocker8.budgetmaster.authentication.UserRepository;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.database.DatabaseParser;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.repositories.AccountRepository;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.services.DatabaseService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.services.ImportService;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.tobias.logger.Logger;
import de.tobias.utils.util.Localization;
import de.tobias.utils.util.RandomUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


@Controller
public class SettingsController
{
	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HelpersService helpers;

	@Autowired
	private DatabaseService databaseService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ImportService importService;

	@RequestMapping("/settings")
	public String settings(WebRequest request, Model model)
	{
		model.addAttribute("settings", settingsRepository.findOne(0));
		request.removeAttribute("database", WebRequest.SCOPE_SESSION);
		return "settings";
	}

	@RequestMapping(value = "/settings/save", method = RequestMethod.POST)
	public String post(Model model, @ModelAttribute("Settings") Settings settings, BindingResult bindingResult,
					   @RequestParam(value = "password") String password,
					   @RequestParam(value = "passwordConfirmation") String passwordConfirmation,
					   @RequestParam(value = "languageType") String languageType)
	{
		settings.setLanguage(LanguageType.fromName(languageType));

		FieldError error = validatePassword(password, passwordConfirmation);
		if(error != null)
		{
			bindingResult.addError(error);
		}

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("settings", settings);
			return "settings";
		}
		else
		{
			if(!password.equals("•••••"))
			{
				BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
				String encryptedPassword = bCryptPasswordEncoder.encode(password);
				User user = userRepository.findByName("Default");
				user.setPassword(encryptedPassword);
				userRepository.save(user);
			}

			settingsRepository.delete(0);
			settingsRepository.save(settings);

			Localization.load();
		}

		return "redirect:/settings";
	}

	private FieldError validatePassword(String password, String passwordConfirmation)
	{
		if(password == null || password.equals(""))
		{
			return new FieldError("Settings", "password", password, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_EMPTY}, null, Strings.WARNING_SETTINGS_PASSWORD_EMPTY);
		}
		else if(password.length() < 3)
		{
			return new FieldError("Settings", "password", password, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_LENGTH}, null, Strings.WARNING_SETTINGS_PASSWORD_LENGTH);
		}

		if(passwordConfirmation == null || passwordConfirmation.equals(""))
		{
			return new FieldError("Settings", "passwordConfirmation", passwordConfirmation, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_EMPTY}, null, Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_EMPTY);
		}

		if(!password.equals(passwordConfirmation))
		{
			return new FieldError("Settings", "passwordConfirmation", passwordConfirmation, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_WRONG}, null, Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_WRONG);
		}

		return null;
	}

	@RequestMapping("/settings/database/requestExport")
	public void downloadFile(HttpServletResponse response)
	{
		Logger.debug("Exporting database...");
		String data = databaseService.getDatabaseAsJSON();
		try
		{
			byte[] dataBytes = data.getBytes("UTF8");
			String fileName = "BudgetMasterDatabase_" + DateTime.now().toString("yyyy_MM_dd") + ".json";
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			response.setContentType("application/json; charset=UTF-8");
			response.setContentLength(dataBytes.length);
			response.setCharacterEncoding("UTF-8");

			try(ServletOutputStream out = response.getOutputStream())
			{
				out.write(dataBytes);
				out.flush();
				Logger.debug("Exporting database DONE");
			}
			catch(IOException e)
			{
				Logger.error(e);
			}
		}
		catch(UnsupportedEncodingException e)
		{
			Logger.error(e);
		}
	}

	@RequestMapping("/settings/database/requestDelete")
	public String requestDeleteDatabase(Model model)
	{
		String verificationCode = RandomUtils.generateRandomString(RandomUtils.RandomType.BASE_58, 4, RandomUtils.RandomStringPolicy.UPPER, RandomUtils.RandomStringPolicy.DIGIT);
		model.addAttribute("deleteDatabase", true);
		model.addAttribute("verificationCode", verificationCode);
		return "settings";
	}

	@RequestMapping(value = "/settings/database/delete", method = RequestMethod.POST)
	public String deleteDatabase(@RequestParam("verificationCode") String verificationCode,
								  @RequestParam("verificationUserInput") String verificationUserInput)
	{
		if(verificationUserInput.equals(verificationCode))
		{
			Logger.info("Deleting database...");
			databaseService.reset();
			Logger.info("Deleting database DONE.");
		}
		else
		{
			return "redirect:/settings/database/requestDelete";
		}

		return "settings";
	}

	@RequestMapping("/settings/database/requestImport")
	public String requestImportDatabase(Model model)
	{
		model.addAttribute("importDatabase", true);
		return "settings";
	}

	@RequestMapping("/settings/database/upload")
	public String upload(WebRequest request, Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
	{
		if(file.isEmpty())
		{
			return "redirect:/settings/database/requestImport";
		}

		try
		{
			String jsonString = new String(file.getBytes());
			DatabaseParser importer = new DatabaseParser(jsonString);
			Database database = importer.parseDatabaseFromJSON();

			request.setAttribute("database", database, WebRequest.SCOPE_SESSION);
			return "redirect:/settings/database/accountMatcher";
		}
		catch(Exception e)
		{
			Logger.error(e);

			model.addAttribute("errorImportDatabase", e.getMessage());
			return "settings";
		}
	}

	@RequestMapping("/settings/database/accountMatcher")
	public String openAccountMatcher(WebRequest request, Model model)
	{
		model.addAttribute("database", request.getAttribute("database", WebRequest.SCOPE_SESSION));
		model.addAttribute("availableAccounts", accountRepository.findAllByOrderByNameAsc());
		return "import";
	}

	@RequestMapping("/settings/database/import")
	public String importDatabase(WebRequest request, @ModelAttribute("Import") AccountMatchList accountMatchList)
	{
		importService.importDatabase((Database)request.getAttribute("database", WebRequest.SCOPE_SESSION), accountMatchList);
		request.removeAttribute("database", RequestAttributes.SCOPE_SESSION);
		return "settings";
	}
}