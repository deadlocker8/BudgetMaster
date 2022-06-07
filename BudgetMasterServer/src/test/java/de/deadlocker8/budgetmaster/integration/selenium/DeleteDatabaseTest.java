package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import de.deadlocker8.budgetmaster.integration.helpers.TransactionTestHelper;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteDatabaseTest extends SeleniumTestBase
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

		String path = getClass().getClassLoader().getResource("DatabaseDeleteTest.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path);
	}

	@Test
	void test_deleteDatabase()
	{
		driver.get(helper.getUrl() + "/settings/database/requestDelete");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("#modalConfirmDelete h4"), "Delete Database"));

		driver.findElement(By.id("verificationCode")).click();
		final WebElement verificationInput = driver.findElement(By.id("verification"));
		final Action seriesOfActions = new Actions(driver)
				.keyDown(verificationInput, Keys.CONTROL)
				.sendKeys(verificationInput, "v")
				.keyUp(verificationInput, Keys.CONTROL)
				.build();
		seriesOfActions.perform();

		driver.findElement(By.id("button-confirm-database-delete")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Settings"));

		// assert
		driver.get(helper.getUrl() + "/transactions");

		// assert only transaction "rest" is showing
		TransactionTestHelper.selectGlobalAccountByName(driver, "Default Account First");
		TransactionTestHelper.gotoSpecificYearAndMonth(driver, 2022, "March");
		final List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSize(1);
		final WebElement row = transactionsRows.get(0);
		final List<WebElement> columns = row.findElements(By.className("col"));
		TransactionTestHelper.assertTransactionColumns(columns, "01.03.", "Rest", "rgb(255, 255, 0)", false, false, "Rest", null, "0.00");
	}
}