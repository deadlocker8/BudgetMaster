package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class FilterTest extends SeleniumTestBase
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

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path);
	}

	@Test
	void test_showFilterActiveButton()
	{
		driver.get(helper.getUrl() + "/transactions");

		assertThat(driver.findElement(By.id("modalFilterTrigger")).getAttribute("class")).doesNotContain("background-red");

		driver.findElement(By.id("modalFilterTrigger")).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalFilter")));

		driver.findElement(By.id("section-type")).click();
		final WebElement checkBox = driver.findElement(By.cssSelector("#section-type .text-default"));
		checkBox.click();

		driver.findElement(By.id("buttonApplyFilter")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("modalFilter")));

		assertThat(driver.findElement(By.id("modalFilterTrigger")).getAttribute("class")).contains("background-red");
	}
}