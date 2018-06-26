package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.authentication.User;
import de.deadlocker8.budgetmaster.authentication.UserRepository;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.database.DatabaseParser;
import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.repositories.SettingsRepository;
import de.deadlocker8.budgetmaster.services.DatabaseService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import de.deadlocker8.budgetmaster.utils.Strings;
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
import org.springframework.web.multipart.MultipartFile;
import tools.BASE58Type;
import tools.ConvertTo;
import tools.Localization;
import tools.RandomCreations;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


@Controller
public class SettingsController extends BaseController
{
	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HelpersService helpers;

	@Autowired
	private DatabaseService databaseService;

	@RequestMapping("/settings")
	public String settings(Model model)
	{
		model.addAttribute("settings", settingsRepository.findOne(0));
		return "settings";
	}

	@RequestMapping(value = "/settings/save", method = RequestMethod.POST)
	public String post(Model model, @ModelAttribute("Settings") Settings settings, BindingResult bindingResult,
					   @RequestParam(value = "password") String password,
					   @RequestParam(value = "languageType") String languageType)
	{
		if(password == null || password.equals(""))
		{
			bindingResult.addError(new FieldError("Settings", "password", password, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_EMPTY}, null, Strings.WARNING_SETTINGS_PASSWORD_EMPTY));
		}
		else if(password.length() < 3)
		{
			bindingResult.addError(new FieldError("Settings", "password", password, false, new String[]{Strings.WARNING_SETTINGS_PASSWORD_LENGTH}, null, Strings.WARNING_SETTINGS_PASSWORD_LENGTH));
		}

		settings.setLanguage(LanguageType.fromName(languageType));

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

			Localization.loadLanguage(settings.getLanguage().getLocale());
		}

		return "redirect:/settings";
	}

	@RequestMapping("/settings/database/requestExport")
	public void downloadFile(HttpServletResponse response)
	{
		LOGGER.debug("Exporting database...");
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
				LOGGER.debug("Exporting database DONE");
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	@RequestMapping("/settings/database/requestDelete")
	public String requestDeleteDatabase(Model model)
	{
		String verificationCode = ConvertTo.toBase58(RandomCreations.generateRandomMixedCaseString(4, true), true, BASE58Type.UPPER);
		model.addAttribute("deleteDatabase", true);
		model.addAttribute("verificationCode", verificationCode);
		return "settings";
	}

	@RequestMapping(value = "/settings/database/delete", method = RequestMethod.POST)
	public String deleteDatabase(Model model,
								 @RequestParam("verificationCode") String verificationCode,
								 @RequestParam("verificationUserInput") String verificationUserInput)
	{
		if(verificationUserInput.equals(verificationCode))
		{
			LOGGER.info("Deleting database...");
			databaseService.reset();
			LOGGER.info("Deleting database DONE.");
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
	public String upload(@RequestParam("file") MultipartFile file)
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

		}
		catch(Exception e)
		{
			//TODO
			e.printStackTrace();
		}

		//TODO redirect to account combination page
		return "redirect:/settings";
	}
}