package de.deadlocker8.budgetmaster.migration;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Controller
@RequestMapping(Mappings.MIGRATION)
public class MigrationController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String ERROR = "error";
		public static final String MIGRATION_SETTINGS = "migrationSettings";
	}

	private static class ReturnValues
	{
		public static final String MIGRATION_SETTINGS = "migration";
	}

	private final SettingsService settingsService;

	@Autowired
	public MigrationController(SettingsService settingsService)
	{
		this.settingsService = settingsService;
	}

	@GetMapping("/cancel")
	public String cancel(HttpServletRequest request)
	{
		settingsService.updateMigrationDeclined(true);
		return "redirect:" + request.getHeader("Referer");
	}

	@GetMapping
	public String migrate(Model model)
	{
		model.addAttribute(ModelAttributes.MIGRATION_SETTINGS, new MigrationSettings(null, null, null, null, null));
		return ReturnValues.MIGRATION_SETTINGS;
	}

	@PostMapping
	public String post(Model model,
					   @ModelAttribute("MigrationSettings") @Valid MigrationSettings migrationSettings, BindingResult bindingResult,
					   @RequestParam(value = "verificationPassword") String verificationPassword)
	{
		// TODO validate verification password
//		final Optional<FieldError> passwordErrorOptional = settingsService.validatePassword(password, passwordConfirmation);
//		passwordErrorOptional.ifPresent(bindingResult::addError);


		final MigrationSettingsValidator migrationSettingsValidator = new MigrationSettingsValidator();
		migrationSettingsValidator.validate(migrationSettings, bindingResult);

		if(bindingResult.hasErrors())
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);
			model.addAttribute(ModelAttributes.MIGRATION_SETTINGS, migrationSettings);
			return ReturnValues.MIGRATION_SETTINGS;
		}

		// TODO
		return ReturnValues.MIGRATION_SETTINGS;
	}
}