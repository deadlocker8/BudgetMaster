package de.deadlocker8.budgetmaster.integration.selenium.settings;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;


class UpdatesSettingsTest extends SeleniumTestBase
{
	private static IntegrationTestHelper helper;

	@Override
	protected void importDatabaseOnce()
	{
		helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();
	}

	@Test
	void test_unsavedChangesWarningIsShown()
	{
		driver.get(helper.getUrl() + "/settings");

		driver.findElement(By.id("updateSettingsContainerHeader")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("updateSettingsContainer")));

		driver.findElement(By.cssSelector("#updateSettingsContainer .lever")).click();

		// assert
		assertThat(driver.findElement(By.cssSelector("#updateSettingsContainerHeader .collapsible-header-button")).getText())
				.contains("unsaved");
	}

	@Test
	void test_save()
	{
		driver.get(helper.getUrl() + "/settings");

		driver.findElement(By.id("updateSettingsContainerHeader")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("updateSettingsContainer")));

		driver.findElement(By.cssSelector("#updateSettingsContainer .lever")).click();

		driver.findElement(By.cssSelector("#updateSettingsContainer button")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast")));

		// assert
		assertThat(driver.findElement(By.className("toast")).getText())
				.contains("Update settings saved");

		assertThat(driver.findElement(By.cssSelector("#updateSettingsContainerHeader .collapsible-header-button")).isDisplayed())
				.isFalse();
	}
}