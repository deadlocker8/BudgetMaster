package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import de.deadlocker8.budgetmaster.integration.helpers.TransactionTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class NewTransactionRecurringTest extends SeleniumTestBase
{
	private static final String TRANSACTION_NAME = "My recurring transaction";

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

	@Override
	protected void runBeforeEachTest()
	{
		openNewTransactionPage("Transaction");
	}

	private void openNewTransactionPage(String type)
	{
		driver.get(helper.getUrl() + "/transactions");

		driver.findElement(By.id("button-new-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		final By locator = By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'" + type + "')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		driver.findElement(locator).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New " + type));
	}

	@AfterEach
	public void afterEach()
	{
		// delete added transaction

		driver.get(helper.getUrl() + "/transactions");

		final List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		for(WebElement row : transactionsRows)
		{
			final List<WebElement> columns = row.findElements(By.className("col"));
			final String name = columns.get(2).findElement(By.className("transaction-text")).getText();
			if(name.equals(TRANSACTION_NAME))
			{
				columns.get(4).findElements(By.tagName("a")).get(1).click();

				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("#modalConfirmDelete .modal-content h4"), "Delete Entry"));

				driver.findElements(By.cssSelector("#deleteModalContainerOnDemand .modal-footer a")).get(1).click();

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("notification-item")));

				return;
			}
		}
	}

	@Test
	void test_newTransaction_income()
	{
		final String amount = "25.00";
		final String description = "Lorem Ipsum dolor sit amet";
		final String categoryName = "sdfdsf";
		final int day = 3;
		final String repeatingModifier = "1";
		final String repeatingModifierType = "Days";

		// fill form
		driver.findElement(By.className("buttonIncome")).click();
		driver.findElement(By.id("transaction-name")).sendKeys(TRANSACTION_NAME);
		driver.findElement(By.id("transaction-amount")).sendKeys(amount);
		driver.findElement(By.id("transaction-description")).sendKeys(description);
		TransactionTestHelper.selectDayInTransactionDatePicker(driver, day);
		TransactionTestHelper.selectCategoryByName(driver, categoryName);

		driver.findElement(By.id("button-transaction-add-repeating-option")).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("button-transaction-remove-repeating-option"))));

		// fill repeating options
		driver.findElement(By.id("transaction-repeating-modifier")).sendKeys(repeatingModifier);
		TransactionTestHelper.selectOptionFromDropdown(driver, By.cssSelector("#transaction-repeating-modifier-row"), repeatingModifierType);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(By.cssSelector(".modal-overlay"))));

		// submit form
		WebElement submitButton = driver.findElement(By.id("button-save-transaction"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);

		((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Rest')]")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSizeGreaterThan(2);

		final List<WebElement> transactionDateGroups = driver.findElements(By.className("transaction-date-group"));

		final WebElement dateGroup = transactionDateGroups.get(transactionDateGroups.size() - 2);
		assertThat(dateGroup.findElement(By.className("transaction-date"))).hasFieldOrPropertyWithValue("text", TransactionTestHelper.getDateString(day));
		final List<WebElement> transactionsInGroup = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));

		final WebElement transactionRow = transactionsInGroup.get(0);
		final List<WebElement> columns = transactionRow.findElements(By.className("col"));
		assertThat(columns).hasSize(5);

		// check columns
		TransactionTestHelper.assertTransactionColumns(columns, categoryName, "rgb(46, 124, 43)", true, false, TRANSACTION_NAME, description, amount);
	}

	@Test
	void test_newTransaction_expenditure()
	{
		final String amount = "15.00";
		final String description = "Lorem Ipsum dolor sit amet";
		final String categoryName = "sdfdsf";
		final int day = 3;
		final String repeatingModifier = "1";
		final String repeatingModifierType = "Days";

		// fill form
		driver.findElement(By.className("buttonExpenditure")).click();
		driver.findElement(By.id("transaction-name")).sendKeys(TRANSACTION_NAME);
		driver.findElement(By.id("transaction-amount")).sendKeys(amount);
		driver.findElement(By.id("transaction-description")).sendKeys(description);
		TransactionTestHelper.selectDayInTransactionDatePicker(driver, day);
		TransactionTestHelper.selectCategoryByName(driver, categoryName);

		driver.findElement(By.id("button-transaction-add-repeating-option")).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("button-transaction-remove-repeating-option"))));

		// fill repeating options
		driver.findElement(By.id("transaction-repeating-modifier")).sendKeys(repeatingModifier);
		TransactionTestHelper.selectOptionFromDropdown(driver, By.cssSelector("#transaction-repeating-modifier-row"), repeatingModifierType);

		// fill date
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(By.cssSelector(".modal-overlay"))));

		// submit form
		WebElement submitButton = driver.findElement(By.id("button-save-transaction"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.elementToBeClickable(submitButton));

		((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Rest')]")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSizeGreaterThan(2);

		final List<WebElement> transactionDateGroups = driver.findElements(By.className("transaction-date-group"));

		final WebElement dateGroup = transactionDateGroups.get(transactionDateGroups.size() - 2);
		assertThat(dateGroup.findElement(By.className("transaction-date"))).hasFieldOrPropertyWithValue("text", TransactionTestHelper.getDateString(day));
		final List<WebElement> transactionsInGroup = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));

		final WebElement transactionRow = transactionsInGroup.get(0);
		final List<WebElement> columns = transactionRow.findElements(By.className("col"));
		assertThat(columns).hasSize(5);

		// check columns
		TransactionTestHelper.assertTransactionColumns(columns, categoryName, "rgb(46, 124, 43)", true, false, TRANSACTION_NAME, description, "-" + amount);
	}

	@Test
	void test_newTransaction_transfer()
	{
		final String type = "Transfer";
		openNewTransactionPage(type);

		final String amount = "30.00";
		final String description = "sit amet";
		final String categoryName = "sdfdsf";
		final int day = 3;
		final String repeatingModifier = "1";
		final String repeatingModifierType = "Days";
		final String transferAccountName = "sfsdf";

		// fill form
		driver.findElement(By.id("transaction-name")).sendKeys(TRANSACTION_NAME);
		driver.findElement(By.id("transaction-amount")).sendKeys(amount);
		driver.findElement(By.id("transaction-description")).sendKeys(description);
		TransactionTestHelper.selectDayInTransactionDatePicker(driver, day);
		TransactionTestHelper.selectCategoryByName(driver, categoryName);
		TransactionTestHelper.selectTransferAccountByName(driver, transferAccountName);

		driver.findElement(By.id("button-transaction-add-repeating-option")).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("button-transaction-remove-repeating-option"))));

		// fill repeating options
		driver.findElement(By.id("transaction-repeating-modifier")).sendKeys(repeatingModifier);
		TransactionTestHelper.selectOptionFromDropdown(driver, By.cssSelector("#transaction-repeating-modifier-row"), repeatingModifierType);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(By.cssSelector(".modal-overlay"))));

		// submit form
		WebElement submitButton = driver.findElement(By.id("button-save-transaction"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);

		((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Rest')]")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSizeGreaterThan(2);

		final List<WebElement> transactionDateGroups = driver.findElements(By.className("transaction-date-group"));

		final WebElement dateGroup = transactionDateGroups.get(transactionDateGroups.size() - 2);
		assertThat(dateGroup.findElement(By.className("transaction-date"))).hasFieldOrPropertyWithValue("text", TransactionTestHelper.getDateString(day));
		final List<WebElement> transactionsInGroup = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));

		final WebElement transactionRow = transactionsInGroup.get(0);
		final List<WebElement> columns = transactionRow.findElements(By.className("col"));
		assertThat(columns).hasSize(5);

		// check columns
		TransactionTestHelper.assertTransactionColumns(columns, categoryName, "rgb(46, 124, 43)", true, true, TRANSACTION_NAME, description, amount);

		// open transaction in edit view again
		columns.get(4).findElement(By.cssSelector("a")).click();
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Edit " + type));

		assertThat(driver.findElement(By.cssSelector(".account-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("2");
		assertThat(driver.findElement(By.cssSelector(".transfer-account-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("4");
	}

	@Test
	void test_edit()
	{
		driver.get(helper.getUrl() + "/transactions/6/edit");

		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("background-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("beste");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("15.00");
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEqualTo("01.05.2019");
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEqualTo("Lorem Ipsum");
		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("3");

		final List<WebElement> chips = driver.findElements(By.cssSelector("#transaction-chips .chip"));
		assertThat(chips).hasSize(1);
		assertThat(chips.get(0)).hasFieldOrPropertyWithValue("text", "123\nclose");

		assertThat(driver.findElement(By.cssSelector(".account-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("3");

		assertThat(driver.findElement(By.id("transaction-repeating-modifier")).getAttribute("value")).isEqualTo("1");
		assertThat(driver.findElement(By.id("transaction-repeating-modifier-type")).getAttribute("value")).isEqualTo("Days");

		assertThat(driver.findElement(By.id("repeating-end-after-x-times")).isSelected()).isTrue();
		assertThat(driver.findElement(By.id("transaction-repeating-end-after-x-times-input")).getAttribute("value")).isEqualTo("20");

		assertThat(driver.findElement(By.id("button-transaction-remove-repeating-option")).isDisplayed()).isFalse();
	}

	@Test
	void test_new_removeRepeatingOption()
	{
		driver.findElement(By.id("button-transaction-add-repeating-option")).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("button-transaction-remove-repeating-option"))));

		assertThat(driver.findElement(By.id("button-transaction-remove-repeating-option")).isDisplayed()).isTrue();

		driver.findElement(By.id("button-transaction-remove-repeating-option")).click();
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("button-transaction-add-repeating-option"))));

		assertThat(driver.findElement(By.id("button-transaction-remove-repeating-option")).isDisplayed()).isFalse();
	}
}