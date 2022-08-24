package de.deadlocker8.budgetmaster.unit.settings.containers;

import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.settings.containers.UpdateSettingsContainer;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@LocalizedTest
class UpdateSettingsContainerTest
{
	@Mock
	private SettingsService settingsService;

	@Test
	void test_validate_valid()
	{
		final UpdateSettingsContainer container = new UpdateSettingsContainer(false);

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		assertThat(errors.getAllErrors())
				.isEmpty();
	}

	@Test
	void test_fixBooleans()
	{
		final UpdateSettingsContainer container = new UpdateSettingsContainer(null);

		container.fixBooleans();

		assertThat(container)
				.hasFieldOrPropertyWithValue("autoUpdateCheckEnabled", false);
	}

	@Test
	void test_updateSettings()
	{
		final Settings defaultSettings = Settings.getDefault();

		Mockito.when(settingsService.getSettings()).thenReturn(defaultSettings);

		final UpdateSettingsContainer container = new UpdateSettingsContainer(false);
		final Settings updatedSettings = container.updateSettings(settingsService);

		assertThat(updatedSettings)
				.hasFieldOrPropertyWithValue("autoUpdateCheckEnabled", false);
	}
}
