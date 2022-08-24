package de.deadlocker8.budgetmaster.unit.settings.containers;

import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.settings.containers.TransactionsSettingsContainer;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@LocalizedTest
class TransactionSettingsContainerTest
{
	@Mock
	private SettingsService settingsService;

	@Test
	void test_validate_valid()
	{
		final TransactionsSettingsContainer container = new TransactionsSettingsContainer(true, List.of());

		final Errors errors = new BeanPropertyBindingResult(container, "container");
		container.validate(errors);

		assertThat(errors.getAllErrors())
				.isEmpty();
	}

	@Test
	void test_fixBooleans()
	{
		final TransactionsSettingsContainer container = new TransactionsSettingsContainer(null, List.of());

		container.fixBooleans();

		assertThat(container)
				.hasFieldOrPropertyWithValue("restActivated", false);
	}

	@Test
	void test_updateSettings()
	{
		final Settings defaultSettings = Settings.getDefault();

		Mockito.when(settingsService.getSettings()).thenReturn(defaultSettings);

		final TransactionsSettingsContainer container = new TransactionsSettingsContainer(false, List.of());
		final Settings updatedSettings = container.updateSettings(settingsService);

		assertThat(updatedSettings)
				.hasFieldOrPropertyWithValue("restActivated", false);
	}
}
