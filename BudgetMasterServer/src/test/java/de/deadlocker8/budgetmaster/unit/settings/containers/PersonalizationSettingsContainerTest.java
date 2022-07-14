package de.deadlocker8.budgetmaster.unit.settings.containers;

import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.settings.containers.PersonalizationSettingsContainer;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import de.deadlocker8.budgetmaster.utils.LanguageType;
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
class PersonalizationSettingsContainerTest
{
	@Mock
	private SettingsService settingsService;

	@Test
	void test_validate_valid()
	{
		final PersonalizationSettingsContainer container = new PersonalizationSettingsContainer(LanguageType.ENGLISH.getName(), "€", true, false, 10);

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		assertThat(errors.getAllErrors())
				.isEmpty();
	}

	@Test
	void test_fixBooleans()
	{
		final PersonalizationSettingsContainer container = new PersonalizationSettingsContainer(LanguageType.ENGLISH.getName(), "€", null, null, 10);

		container.fixBooleans();

		assertThat(container)
				.hasFieldOrPropertyWithValue("useDarkTheme", false)
				.hasFieldOrPropertyWithValue("showCategoriesAsCircles", false);
	}


	@Test
	void test_updateSettings()
	{
		final Settings defaultSettings = Settings.getDefault();

		Mockito.when(settingsService.getSettings()).thenReturn(defaultSettings);

		final PersonalizationSettingsContainer container = new PersonalizationSettingsContainer(LanguageType.ENGLISH.getName(), "€", true, true, 10);
		final Settings updatedSettings = container.updateSettings(settingsService);

		assertThat(updatedSettings)
				.hasFieldOrPropertyWithValue("language", LanguageType.ENGLISH)
				.hasFieldOrPropertyWithValue("currency", "€")
				.hasFieldOrPropertyWithValue("useDarkTheme", true)
				.hasFieldOrPropertyWithValue("showCategoriesAsCircles", true)
				.hasFieldOrPropertyWithValue("searchItemsPerPage", 10);
	}
}
