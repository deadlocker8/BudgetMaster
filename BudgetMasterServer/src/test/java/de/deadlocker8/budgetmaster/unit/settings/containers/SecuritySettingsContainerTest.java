package de.deadlocker8.budgetmaster.unit.settings.containers;

import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsController;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.settings.containers.PersonalizationSettingsContainer;
import de.deadlocker8.budgetmaster.settings.containers.SecuritySettingsContainer;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@LocalizedTest
class SecuritySettingsContainerTest
{
	@Mock
	private SettingsService settingsService;

	@Test
	void test_validate_valid()
	{
		final SecuritySettingsContainer container = new SecuritySettingsContainer("abc0815", "abc0815");

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		assertThat(errors.getAllErrors())
				.isEmpty();
	}

	@Test
	void test_validate_passwordIsNull()
	{
		final SecuritySettingsContainer container = new SecuritySettingsContainer(null, "");

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);

		final ObjectError error = finalErrors.get(0);
		assertThat(error)
				.hasFieldOrPropertyWithValue("field", "password");
		assertThat(error.getCode())
				.isEqualTo(Strings.WARNING_SETTINGS_PASSWORD_EMPTY);
	}

	@Test
	void test_validate_passwordIsEmpty()
	{
		final SecuritySettingsContainer container = new SecuritySettingsContainer("", "");

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);

		final ObjectError error = finalErrors.get(0);
		assertThat(error)
				.hasFieldOrPropertyWithValue("field", "password");
		assertThat(error.getCode())
				.isEqualTo(Strings.WARNING_SETTINGS_PASSWORD_EMPTY);
	}

	@Test
	void test_validate_passwordTooShort()
	{
		final SecuritySettingsContainer container = new SecuritySettingsContainer("01", "01");

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);

		final ObjectError error = finalErrors.get(0);
		assertThat(error)
				.hasFieldOrPropertyWithValue("field", "password");
		assertThat(error.getCode())
				.isEqualTo(Strings.WARNING_SETTINGS_PASSWORD_LENGTH);
	}

	@Test
	void test_validate_passwordIsPlaceholder()
	{
		final SecuritySettingsContainer container = new SecuritySettingsContainer(SettingsController.PASSWORD_PLACEHOLDER, SettingsController.PASSWORD_PLACEHOLDER);

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);

		final ObjectError error = finalErrors.get(0);
		assertThat(error)
				.hasFieldOrPropertyWithValue("field", "password");
		assertThat(error.getCode())
				.isEqualTo(Strings.WARNING_SETTINGS_PASSWORD_EQUALS_EXISTING);
	}

	@Test
	void test_validate_passwordConfirmationIsNull()
	{
		final SecuritySettingsContainer container = new SecuritySettingsContainer("abc0815", null);

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);

		final ObjectError error = finalErrors.get(0);
		assertThat(error)
				.hasFieldOrPropertyWithValue("field", "passwordConfirmation");
		assertThat(error.getCode())
				.isEqualTo(Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_EMPTY);
	}

	@Test
	void test_validate_passwordConfirmationIsEmpty()
	{
		final SecuritySettingsContainer container = new SecuritySettingsContainer("abc0815", "");

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);

		final ObjectError error = finalErrors.get(0);
		assertThat(error)
				.hasFieldOrPropertyWithValue("field", "passwordConfirmation");
		assertThat(error.getCode())
				.isEqualTo(Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_EMPTY);
	}

	@Test
	void test_validate_passwordConfirmationDoesNotMatch()
	{
		final SecuritySettingsContainer container = new SecuritySettingsContainer("abc0815", "xyz");

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		final List<ObjectError> finalErrors = errors.getAllErrors();
		assertThat(finalErrors)
				.hasSize(1);

		final ObjectError error = finalErrors.get(0);
		assertThat(error)
				.hasFieldOrPropertyWithValue("field", "passwordConfirmation");
		assertThat(error.getCode())
				.isEqualTo(Strings.WARNING_SETTINGS_PASSWORD_CONFIRMATION_WRONG);
	}

	@Test
	void test_updateSettings()
	{
		final Settings defaultSettings = Settings.getDefault();

		Mockito.when(settingsService.getSettings()).thenReturn(defaultSettings);

		final PersonalizationSettingsContainer container = new PersonalizationSettingsContainer(LanguageType.ENGLISH.getName(), "€", true, true, 10);
		final Settings updatedSettings = container.updateSettings(settingsService);

		assertThat(updatedSettings).isEqualTo(defaultSettings);
	}
}
