package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import de.deadlocker8.budgetmaster.integration.helpers.TransactionTestHelper;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NewTransactionNormalTest extends SeleniumTestBase
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

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path);
	}

	@Override
	protected void runBeforeEachTest()
	{
		driver.get(helper.getUrl() + "/transactions");
	}

	private void openNewTransactionPage()
	{
		driver.findElement(By.id("button-new-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		final By locator = By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		driver.findElement(locator).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));
	}

	@Test
	void test_newTransaction_cancel()
	{
		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		final int numberOfTransactionsBefore = transactionsRows.size();

		openNewTransactionPage();

		// click cancel button
		WebElement cancelButton = driver.findElement(By.id("button-cancel-save-transaction"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cancelButton);
		cancelButton.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Rest')]")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");

		transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSize(numberOfTransactionsBefore);
	}

	@Test
	void test_newTransaction_income()
	{
		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		final int numberOfTransactionsBefore = transactionsRows.size();

		openNewTransactionPage();

		final String name = "My normal transaction";
		final String amount = "15.00";
		final String description = "Lorem Ipsum dolor sit amet";
		final String categoryName = "sdfdsf";
		final int day = 20;

		// fill form
		driver.findElement(By.className("buttonIncome")).click();
		driver.findElement(By.id("transaction-name")).sendKeys(name);
		driver.findElement(By.id("transaction-amount")).sendKeys(amount);
		TransactionTestHelper.selectDayInTransactionDatePicker(driver, day);
		driver.findElement(By.id("transaction-description")).sendKeys(description);
		TransactionTestHelper.selectCategoryByName(driver, categoryName);

		// submit form
		driver.findElement(By.id("button-save-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Rest')]")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");

		transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSize(numberOfTransactionsBefore + 1);

		final List<WebElement> transactionDateGroups = driver.findElements(By.className("transaction-date-group"));

		final WebElement dateGroup = transactionDateGroups.get(0);
		assertThat(dateGroup.findElement(By.className("transaction-date"))).hasFieldOrPropertyWithValue("text", TransactionTestHelper.getDateString(day));
		final List<WebElement> transactionsInGroup = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));

		final WebElement transactionRow = transactionsInGroup.get(0);
		final List<WebElement> columns = transactionRow.findElements(By.className("col"));
		assertThat(columns).hasSize(5);

		// check columns
		TransactionTestHelper.assertTransactionColumns(columns, categoryName, "rgb(46, 124, 43)", false, false, name, description, amount);
	}

	@Test
	void test_newTransaction_expenditure()
	{
		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		final int numberOfTransactionsBefore = transactionsRows.size();

		openNewTransactionPage();

		final String name = "My normal transaction";
		final String amount = "15.00";
		final String description = "Lorem Ipsum dolor sit amet";
		final String categoryName = "sdfdsf";
		final int day = 20;

		// fill form
		driver.findElement(By.className("buttonExpenditure")).click();
		driver.findElement(By.id("transaction-name")).sendKeys(name);
		driver.findElement(By.id("transaction-amount")).sendKeys(amount);
		TransactionTestHelper.selectDayInTransactionDatePicker(driver, day);
		driver.findElement(By.id("transaction-description")).sendKeys(description);
		TransactionTestHelper.selectCategoryByName(driver, categoryName);

		// submit form
		driver.findElement(By.id("button-save-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Rest')]")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");

		transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSize(numberOfTransactionsBefore + 1);


		final List<WebElement> transactionDateGroups = driver.findElements(By.className("transaction-date-group"));

		final WebElement dateGroup = transactionDateGroups.get(0);
		assertThat(dateGroup.findElement(By.className("transaction-date"))).hasFieldOrPropertyWithValue("text", TransactionTestHelper.getDateString(day));
		final List<WebElement> transactionsInGroup = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));

		final WebElement transactionRow = transactionsInGroup.get(0);
		final List<WebElement> columns = transactionRow.findElements(By.className("col"));
		assertThat(columns).hasSize(5);

		// check columns
		TransactionTestHelper.assertTransactionColumns(columns, categoryName, "rgb(46, 124, 43)", false, false, name, description, "-" + amount);
	}

	@Test
	void test_edit()
	{
		driver.get(helper.getUrl() + "/transactions/2/edit");

		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("background-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("Test");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("15.00");
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEqualTo("01.05.2019");
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEqualTo("Lorem Ipsum");
		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("4");

		final List<WebElement> chips = driver.findElements(By.cssSelector("#transaction-chips .chip"));
		assertThat(chips).hasSize(1);
		assertThat(chips.get(0)).hasFieldOrPropertyWithValue("text", "123\nclose");

		assertThat(driver.findElement(By.cssSelector(".account-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("3");
	}

	@Test
	void test_saveAndContinue()
	{
		openNewTransactionPage();

		final String name = "Save and continue transaction";
		final String amount = "15.00";
		final int day = 20;

		// fill form
		driver.findElement(By.className("buttonExpenditure")).click();
		driver.findElement(By.id("transaction-name")).sendKeys(name);
		driver.findElement(By.id("transaction-amount")).sendKeys(amount);
		TransactionTestHelper.selectDayInTransactionDatePicker(driver, day);

		// submit form
		driver.findElement(By.id("button-save-transaction-and-continue")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/newTransaction/normal");

		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("background-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEmpty();
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEmpty();
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEmpty();
	}

	@Test
	void test_newTransaction_keywordInName_clickOnButtonToCancelSaving()
	{
		openNewTransactionPage();

		final String name = "special income transaction";
		final String amount = "15.00";
		final String categoryName = "sdfdsf";
		final int day = 20;

		// fill form
		driver.findElement(By.className("buttonExpenditure")).click();
		driver.findElement(By.id("transaction-name")).sendKeys(name);
		driver.findElement(By.id("transaction-amount")).sendKeys(amount);
		TransactionTestHelper.selectDayInTransactionDatePicker(driver, day);
		TransactionTestHelper.selectCategoryByName(driver, categoryName);

		// submit form
		driver.findElement(By.id("button-save-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalTransactionNameKeywordWarning")));

		// assert
		assertThat(driver.findElement(By.id("keyword")).getText()).isEqualTo("income");

		driver.findElement(By.id("keyword-warning-button-cancel")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("modalTransactionNameKeywordWarning")));

		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo(name);
	}

	@Test
	void test_newTransaction_keywordInName_clickOnButtonToIgnoreWarning()
	{
		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		final int numberOfTransactionsBefore = transactionsRows.size();

		openNewTransactionPage();

		final String name = "special income transaction";
		final String amount = "15.00";
		final String categoryName = "sdfdsf";
		final int day = 20;

		// fill form
		driver.findElement(By.className("buttonExpenditure")).click();
		driver.findElement(By.id("transaction-name")).sendKeys(name);
		driver.findElement(By.id("transaction-amount")).sendKeys(amount);
		TransactionTestHelper.selectDayInTransactionDatePicker(driver, day);
		TransactionTestHelper.selectCategoryByName(driver, categoryName);

		// submit form
		driver.findElement(By.id("button-save-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalTransactionNameKeywordWarning")));

		driver.findElement(By.id("keyword-warning-button-ignore")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("modalTransactionNameKeywordWarning")));

		// assert
		transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSize(numberOfTransactionsBefore + 1);

		final List<WebElement> transactionDateGroups = driver.findElements(By.className("transaction-date-group"));

		final WebElement dateGroup = transactionDateGroups.get(0);
		assertThat(dateGroup.findElement(By.className("transaction-date"))).hasFieldOrPropertyWithValue("text", TransactionTestHelper.getDateString(day));
		final List<WebElement> transactionsInGroup = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));

		final WebElement transactionRow = transactionsInGroup.get(0);
		final List<WebElement> columns = transactionRow.findElements(By.className("col"));
		assertThat(columns).hasSize(5);

		// check columns
		TransactionTestHelper.assertTransactionColumns(columns, categoryName, "rgb(46, 124, 43)", false, false, name, null, "-" + amount);
	}

	@Test
	void test_newTransaction_keywordInName_clickOnButtonToIgnoreWarning_saveAndContinueClickedBefore()
	{
		openNewTransactionPage();

		final String name = "special income transaction";
		final String amount = "15.00";
		final String categoryName = "sdfdsf";
		final int day = 20;

		// fill form
		driver.findElement(By.className("buttonExpenditure")).click();
		driver.findElement(By.id("transaction-name")).sendKeys(name);
		driver.findElement(By.id("transaction-amount")).sendKeys(amount);
		TransactionTestHelper.selectDayInTransactionDatePicker(driver, day);
		TransactionTestHelper.selectCategoryByName(driver, categoryName);

		// submit form
		driver.findElement(By.id("button-save-transaction-and-continue")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalTransactionNameKeywordWarning")));

		driver.findElement(By.id("keyword-warning-button-ignore")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("modalTransactionNameKeywordWarning")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/newTransaction/normal");

		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("background-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEmpty();
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEmpty();
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEmpty();
	}

	@Test
	void test_newTransaction_keywordInName_alreadyMarkedAsIncome_dontShowWarning()
	{
		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		final int numberOfTransactionsBefore = transactionsRows.size();

		openNewTransactionPage();

		final String name = "special income transaction";
		final String amount = "15.00";
		final String categoryName = "sdfdsf";
		final int day = 20;

		// fill form
		driver.findElement(By.className("buttonIncome")).click();
		driver.findElement(By.id("transaction-name")).sendKeys(name);
		driver.findElement(By.id("transaction-amount")).sendKeys(amount);
		TransactionTestHelper.selectDayInTransactionDatePicker(driver, 20);
		TransactionTestHelper.selectCategoryByName(driver, categoryName);

		// submit form
		driver.findElement(By.id("button-save-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Rest')]")));

		assertThat(driver.getCurrentUrl()).endsWith("/transactions");

		transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSize(numberOfTransactionsBefore + 1);

		final List<WebElement> transactionDateGroups = driver.findElements(By.className("transaction-date-group"));

		final WebElement dateGroup = transactionDateGroups.get(0);
		assertThat(dateGroup.findElement(By.className("transaction-date"))).hasFieldOrPropertyWithValue("text", TransactionTestHelper.getDateString(day));
		final List<WebElement> transactionsInGroup = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));

		final WebElement transactionRow = transactionsInGroup.get(0);
		final List<WebElement> columns = transactionRow.findElements(By.className("col"));
		assertThat(columns).hasSize(5);

		// check columns
		TransactionTestHelper.assertTransactionColumns(columns, categoryName, "rgb(46, 124, 43)", false, false, name, null, amount);
	}
}