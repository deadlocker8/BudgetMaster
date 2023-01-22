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

class NewTransactionFromExistingOneTest extends SeleniumTestBase
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

		String path = getClass().getClassLoader().getResource("NewTransactionFromExistingOneTest.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path);
	}

	@Override
	protected void runBeforeEachTest()
	{
		driver.get(helper.getUrl() + "/transactions");

		TransactionTestHelper.selectGlobalAccountByName(driver, "Default Account First");
	}

	@Test
	void test_newTransactionFromExisting_normal()
	{
		TransactionTestHelper.gotoSpecificYearAndMonth(driver, 2021, "October");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		List<WebElement> columns = transactionsRows.get(2).findElements(By.className("col"));
		columns.get(4).findElement(By.className("button-new-from-existing")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// assert
		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("background-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("Full normal");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("12.00");
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEmpty();
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEqualTo("lorem ipsum");
		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("3");

		final List<WebElement> chips = driver.findElements(By.cssSelector("#transaction-chips .chip"));
		assertThat(chips).hasSize(1);
		assertThat(chips.get(0)).hasFieldOrPropertyWithValue("text", "123\nclose");

		assertThat(driver.findElement(By.cssSelector(".account-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("3");

		TransactionTestHelper.selectDayInTransactionDatePicker(driver, 20);

		// submit form
		driver.findElement(By.id("button-save-transaction")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Last month balance')]")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");
	}

	@Test
	void test_newTransactionFromExisting_transfer()
	{
		TransactionTestHelper.gotoSpecificYearAndMonth(driver, 2021, "September");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		List<WebElement> columns = transactionsRows.get(1).findElements(By.className("col"));
		columns.get(4).findElement(By.className("button-new-from-existing")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transfer"));

		// assert
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("Full transfer");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("15.00");
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEmpty();
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEqualTo("dolor sit amet");
		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("3");

		final List<WebElement> chips = driver.findElements(By.cssSelector("#transaction-chips .chip"));
		assertThat(chips).hasSize(1);
		assertThat(chips.get(0)).hasFieldOrPropertyWithValue("text", "456\nclose");

		assertThat(driver.findElement(By.cssSelector(".account-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("3");
		assertThat(driver.findElement(By.cssSelector(".transfer-account-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("4");

		TransactionTestHelper.selectDayInTransactionDatePicker(driver, 20);

		// submit form
		driver.findElement(By.id("button-save-transaction")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Last month balance')]")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");
	}

	@Test
	void test_newTransactionFromExisting_recurring()
	{
		TransactionTestHelper.gotoSpecificYearAndMonth(driver, 2021, "June");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		List<WebElement> columns = transactionsRows.get(1).findElements(By.className("col"));
		columns.get(4).findElement(By.className("button-new-from-existing")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// assert
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("normal recurring");
		assertThat(driver.findElement(By.id("transaction-repeating-modifier")).getAttribute("value")).isEqualTo("1");
		assertThat(driver.findElement(By.id("transaction-repeating-modifier-type")).getAttribute("value")).isEqualTo("Months");

		assertThat(driver.findElement(By.id("repeating-end-after-x-times")).isSelected()).isTrue();
		assertThat(driver.findElement(By.id("transaction-repeating-end-after-x-times-input")).getAttribute("value")).isEqualTo("1");

		TransactionTestHelper.selectDayInTransactionDatePicker(driver, 20);

		// submit form
		driver.findElement(By.id("button-save-transaction")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Last month balance')]")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");
	}

	@Test
	void test_newTransactionFromExisting_transactionFromReadonlyAccount()
	{
		TransactionTestHelper.selectGlobalAccountByName(driver, "Readonly Account");

		TransactionTestHelper.gotoSpecificYearAndMonth(driver, 2021, "October");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		List<WebElement> columns = transactionsRows.get(1).findElements(By.className("col"));
		columns.get(4).findElement(By.className("button-new-from-existing")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// assert
		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("background-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("Transaction in readonly account");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("12.00");

		// should fall back to default account as the readonly account will not allow new transactions
		assertThat(driver.findElement(By.cssSelector(".account-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("2");

		TransactionTestHelper.selectDayInTransactionDatePicker(driver, 20);

		// submit form
		driver.findElement(By.id("button-save-transaction")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Last month balance')]")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");
	}
}