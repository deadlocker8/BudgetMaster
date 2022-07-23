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


class BackupSettingsTest extends SeleniumTestBase
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

		driver.findElement(By.id("backupSettingsContainerHeader")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("backupSettingsContainer")));

		driver.findElement(By.cssSelector("#backupSettingsContainer .lever")).click();

		// assert
		assertThat(driver.findElement(By.cssSelector("#backupSettingsContainerHeader .collapsible-header-button")).getText())
				.contains("unsaved");
	}

	@Test
	void test_save()
	{
		driver.get(helper.getUrl() + "/settings");

		driver.findElement(By.id("backupSettingsContainerHeader")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("backupSettingsContainer")));

		driver.findElement(By.cssSelector("#backupSettingsContainer .lever")).click();

		driver.findElements(By.cssSelector("#backupSettingsContainer button")).get(1).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast")));

		// assert
		assertThat(driver.findElement(By.className("toast")).getText())
				.contains("Backup settings saved");

		assertThat(driver.findElement(By.cssSelector("#backupSettingsContainerHeader .collapsible-header-button")).isDisplayed())
				.isFalse();
	}

	@Test
	void test_save_error()
	{
		driver.get(helper.getUrl() + "/settings");

		driver.findElement(By.id("backupSettingsContainerHeader")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("backupSettingsContainer")));

		driver.findElements(By.cssSelector("#backupSettingsContainer .lever")).get(1).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("settings-backup-auto-days")));

		driver.findElement(By.id("settings-backup-auto-days")).sendKeys("x");

		driver.findElements(By.cssSelector("#backupSettingsContainer button")).get(1).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.id("settings-backup-auto-days"), "data-tooltip", "greater than 0"));

		assertThat(driver.findElement(By.cssSelector("#backupSettingsContainerHeader .collapsible-header-button")).isDisplayed())
				.isTrue();
	}
}