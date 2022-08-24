package de.deadlocker8.budgetmaster.integration.selenium.settings;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;


class PersonalizationSettingsTest extends SeleniumTestBase
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
		helper.hideMigrationDialog();
	}

	@Test
	void test_unsavedChangesWarningIsShown()
	{
		driver.get(helper.getUrl() + "/settings");

		driver.findElement(By.id("personalizationSettingsContainerHeader")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("personalizationSettingsContainer")));

		driver.findElement(By.id("settings-currency")).sendKeys("abc");

		// assert
		assertThat(driver.findElement(By.cssSelector("#personalizationSettingsContainerHeader .collapsible-header-button")).getText())
				.contains("unsaved");
	}

	@Test
	void test_save()
	{
		driver.get(helper.getUrl() + "/settings");

		driver.findElement(By.id("personalizationSettingsContainerHeader")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("personalizationSettingsContainer")));

		driver.findElement(By.id("settings-currency")).sendKeys("abc");

		driver.findElement(By.cssSelector("#personalizationSettingsContainer button")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast")));

		// assert
		assertThat(driver.findElement(By.className("toast")).getText())
				.contains("Personalization settings saved");

		assertThat(driver.findElement(By.cssSelector("#personalizationSettingsContainerHeader .collapsible-header-button")).isDisplayed())
				.isFalse();
	}

	@Test
	void test_manualPageReloadAfterSave()
	{
		driver.get(helper.getUrl() + "/settings");

		driver.findElement(By.id("personalizationSettingsContainerHeader")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("personalizationSettingsContainer")));

		driver.findElement(By.id("settings-currency")).sendKeys("abc");

		driver.findElement(By.cssSelector("#personalizationSettingsContainer button")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast")));

		// assert
		assertThat(driver.findElement(By.className("toast")).getText())
				.contains("Personalization settings saved");

		final WebElement reloadNotification = driver.findElement(By.cssSelector("#personalizationSettingsContainer span.notification-item"));

		assertThat(reloadNotification.isDisplayed()).isTrue();

		reloadNotification.findElement(By.tagName("a")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("personalizationSettingsContainer")));
	}
}