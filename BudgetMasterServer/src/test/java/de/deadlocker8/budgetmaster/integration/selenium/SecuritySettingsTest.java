package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;


class SecuritySettingsTest extends SeleniumTestBase
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

		driver.findElement(By.id("securitySettingsContainerHeader")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("securitySettingsContainer")));

		driver.findElement(By.id("settings-password")).sendKeys("abc");

		// assert
		assertThat(driver.findElement(By.cssSelector("#securitySettingsContainerHeader .collapsible-header-button")).getText())
				.contains("unsaved");
	}

	@Test
	void test_save()
	{
		driver.get(helper.getUrl() + "/settings");

		driver.findElement(By.id("securitySettingsContainerHeader")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("securitySettingsContainer")));

		driver.findElement(By.id("settings-password")).sendKeys("abc");
		driver.findElement(By.id("settings-password-confirmation")).sendKeys("abc");

		driver.findElement(By.cssSelector("#securitySettingsContainer button")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast")));

		// assert
		assertThat(driver.findElement(By.className("toast")).getText())
				.contains("Password saved");

		assertThat(driver.findElement(By.cssSelector("#securitySettingsContainerHeader .collapsible-header-button")).isDisplayed())
				.isFalse();
	}

	@Test
	void test_save_error()
	{
		driver.get(helper.getUrl() + "/settings");

		driver.findElement(By.id("securitySettingsContainerHeader")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("securitySettingsContainer")));

		driver.findElement(By.id("settings-password")).sendKeys("");

		driver.findElement(By.cssSelector("#securitySettingsContainer button")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast")));

		// assert
		assertThat(driver.findElement(By.className("toast")).getText())
				.contains("Error saving password");

		assertThat(driver.findElement(By.cssSelector("#securitySettingsContainerHeader .collapsible-header-button")).isDisplayed())
				.isFalse();
	}
}