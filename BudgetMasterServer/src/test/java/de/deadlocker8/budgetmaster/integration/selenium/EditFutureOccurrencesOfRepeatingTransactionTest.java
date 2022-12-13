package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import de.deadlocker8.budgetmaster.integration.helpers.TransactionTestHelper;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EditFutureOccurrencesOfRepeatingTransactionTest extends SeleniumTestBase
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

		String path = getClass().getClassLoader().getResource("EditFutureOccurrencesOfRepeatingTransactionTest.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path);
	}

	@Override
	protected void runBeforeEachTest()
	{
		driver.get(helper.getUrl() + "/transactions");

		TransactionTestHelper.selectGlobalAccountByName(driver, "Default Account First");
	}

	@Test
	void test_editFutureOccurrencesOfRepeatingTransaction()
	{
		TransactionTestHelper.gotoSpecificYearAndMonth(driver, 2022, "October");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		final WebElement row = transactionsRows.get(transactionsRows.size() - 4);
		List<WebElement> columns = row.findElements(By.className("col"));
		columns.get(4).findElement(By.className("edit-transaction-button-link")).click();

		final WebElement buttonEditFutureOccurrences = row.findElement(By.className("button-edit-future-occurrences"));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOf(buttonEditFutureOccurrences));
		buttonEditFutureOccurrences.click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		final WebElement amountInput = driver.findElement(By.id("transaction-amount"));
		amountInput.clear();
		amountInput.sendKeys("-20");
		driver.findElement(By.id("button-save-transaction")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Last month balance')]")));

		// assert
		final List<WebElement> transactionDateGroups = driver.findElements(By.className("transaction-date-group"));

		assertTransaction(transactionDateGroups, transactionDateGroups.size() - 4, 1, "-20.00 €", 22);
		assertTransaction(transactionDateGroups, transactionDateGroups.size() - 3, 1, "-20.00 €", 15);
		assertTransaction(transactionDateGroups, transactionDateGroups.size() - 2, 1, "-10.00 €", 8);
		assertTransaction(transactionDateGroups, transactionDateGroups.size() - 1, 2, "-10.00 €", 1);
	}

	private void assertTransaction(List<WebElement> transactionDateGroups, int groupIndex, int expectedNumberOfTransactions, String expectedAmount, int day)
	{
		final WebElement dateGroup = transactionDateGroups.get(groupIndex);
		assertThat(dateGroup.findElement(By.className("transaction-date"))).hasFieldOrPropertyWithValue("text", String.format("%02d. %s", day, "OCTOBER 2022"));
		final List<WebElement> transactionsInGroup = dateGroup.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsInGroup).hasSize(expectedNumberOfTransactions);
		final WebElement transactionRow = transactionsInGroup.get(0);
		final List<WebElement> transactionColumns = transactionRow.findElements(By.className("col"));
		assertThat(transactionColumns.get(3).getText()).contains(expectedAmount);
	}
}