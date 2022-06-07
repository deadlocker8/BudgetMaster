package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;


class AccountSelectTest extends SeleniumTestBase
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

		String path = Account.class.getClassLoader().getResource("AccountDatabase.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path);
	}

	@Test
	void test_openGlobalAccountSelectWithHotKey()
	{
		driver.get(helper.getUrl());

		driver.findElement(By.tagName("body")).sendKeys("a");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#modalGlobalAccountSelect h4")));

		assertThat(driver.findElements(By.className("global-account-select-option")))
				.hasSize(5);
	}

	@Test
	void test_selectAccountWithNumberHotKey()
	{
		driver.get(helper.getUrl());

		assertThat(driver.findElement(By.cssSelector("#globalAccountSelect .global-account-select-name")).getText())
				.isEqualTo("Default Account First");

		final WebElement globalAccountSelect = driver.findElement(By.id("globalAccountSelect"));
		globalAccountSelect.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#modalGlobalAccountSelect h4")));

		driver.findElement(By.id("modalGlobalAccountSelect")).sendKeys("3");

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#modalGlobalAccountSelect h4")));

		assertThat(driver.findElement(By.cssSelector("#globalAccountSelect .global-account-select-name")).getText())
				.isEqualTo("read only account");
	}
}