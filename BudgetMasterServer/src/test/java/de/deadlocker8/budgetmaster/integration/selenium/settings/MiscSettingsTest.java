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


class MiscSettingsTest extends SeleniumTestBase
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
	void test_save()
	{
		driver.get(helper.getUrl() + "/settings");

		driver.findElement(By.id("miscSettingsContainerHeader")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("miscSettingsContainer")));

		driver.findElement(By.cssSelector("#miscSettingsContainer button")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast")));

		// assert
		assertThat(driver.findElement(By.className("toast")).getText())
				.contains("All hints reset");

		assertThat(driver.findElement(By.cssSelector("#miscSettingsContainerHeader .collapsible-header-button")).isDisplayed())
				.isFalse();
	}
}