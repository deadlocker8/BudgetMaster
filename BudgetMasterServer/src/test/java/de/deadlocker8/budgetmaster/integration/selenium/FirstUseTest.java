package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class FirstUseTest extends SeleniumTestBase
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
	void test_firstUserBanner()
	{
		helper.start();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hint-1")));
		assertThat(driver.findElement(By.id("hint-1")).isDisplayed()).isTrue();
	}

	@Test
	void test_firstUserBanner_dismiss()
	{
		helper.start();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hint-1")));

		driver.findElements(By.className("hint-clear")).get(0).click();

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("hint-1")));
		assertThat(driver.findElement(By.id("hint-1")).isDisplayed()).isFalse();
	}

	@Test
	void test_firstUserBanner_click()
	{
		helper.start();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hint-1")));

		driver.findElements(By.className("notification")).get(0).click();

		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "First use guide"));

		assertThat(driver.getCurrentUrl()).endsWith("/firstUse");
	}
}