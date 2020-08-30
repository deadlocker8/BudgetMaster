package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTest;
import de.deadlocker8.budgetmaster.integration.helpers.TransactionTestHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SeleniumTest
public class NewTransactionRecurringTest
{
	private IntegrationTestHelper helper;
	private WebDriver driver;

	@LocalServerPort
	int port;

	@Rule
	public TestName name = new TestName();

	@Rule
	public TestWatcher testWatcher = new TestWatcher()
	{
		@Override
		protected void finished(Description description)
		{
			driver.quit();
		}

		@Override
		protected void failed(Throwable e, Description description)
		{
			IntegrationTestHelper.saveScreenshots(driver, name, NewTransactionRecurringTest.class);
		}
	};

	@Before
	public void prepare()
	{
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);
		driver = new FirefoxDriver(options);

		// prepare
		helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path, Arrays.asList("DefaultAccount0815", "sfsdf"), Arrays.asList("DefaultAccount0815", "Account2"));

		// open transactions page
		driver.get(helper.getUrl() + "/transactions");
		driver.findElement(By.id("button-new-transaction")).click();
	}

	@Test
	public void test_newTransaction_cancel()
	{
		// open new transaction page
		driver.findElement(By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Recurring')]")).click();

		// click cancel button
		driver.findElement(By.xpath("//a[contains(text(),'Cancel')]")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".headline-date")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSize(1);
	}

	@Test
	public void test_newTransaction_income()
	{
		// open new transaction page
		driver.findElement(By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Recurring')]")).click();

		String name = "My recurring transaction";
		String amount = "15.00";
		String description = "Lorem Ipsum dolor sit amet";
		String categoryName = "sdfdsf";
		String repeatingModifier = "1";
		String repeatingModifierType = "Days";

		// fill form
		driver.findElement(By.className("buttonIncome")).click();
		driver.findElement(By.id("transaction-name")).sendKeys(name);
		driver.findElement(By.id("transaction-amount")).sendKeys(amount);
		driver.findElement(By.id("transaction-description")).sendKeys(description);
		TransactionTestHelper.selectOptionFromDropdown(driver, By.id("categoryWrapper"), categoryName);

		// fill repeating options
		driver.findElement(By.id("transaction-repeating-modifier")).sendKeys(repeatingModifier);
		TransactionTestHelper.selectOptionFromDropdown(driver, By.cssSelector("#transaction-repeating-modifier-row"), repeatingModifierType);

		// fill date
		driver.findElement(By.id("transaction-datepicker")).click();
		List<WebElement> datePickerCells = driver.findElements(By.cssSelector(".datepicker-table td"));
		for(WebElement cell : datePickerCells)
		{
			if(cell.getText().equals("3"))
			{
				cell.click();
				driver.findElement(By.cssSelector(".datepicker-done")).click();
				break;
			}
		}

		// submit form
		WebElement submitButton = driver.findElement(By.id("button-save-transaction"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOf(submitButton));

		submitButton.click();

		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".headline-date")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSizeGreaterThan(2);

		final WebElement row = transactionsRows.get(transactionsRows.size()-2);
		final List<WebElement> columns = row.findElements(By.className("col"));
		assertThat(columns).hasSize(6);

		// check columns
		final String dateString = new SimpleDateFormat("03.MM.").format(new Date());
		TransactionTestHelper.assertTransactionColumns(columns, dateString, categoryName, "rgb(46, 124, 43)", true, false, name, description, amount);
	}

	@Test
	public void test_newTransaction_expenditure()
	{
		// open new transaction page
		driver.findElement(By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Recurring')]")).click();

		String name = "My recurring transaction";
		String amount = "15.00";
		String description = "Lorem Ipsum dolor sit amet";
		String categoryName = "sdfdsf";
		String repeatingModifier = "1";
		String repeatingModifierType = "Days";

		// fill form
		driver.findElement(By.className("buttonExpenditure")).click();
		driver.findElement(By.id("transaction-name")).sendKeys(name);
		driver.findElement(By.id("transaction-amount")).sendKeys(amount);
		driver.findElement(By.id("transaction-description")).sendKeys(description);
		TransactionTestHelper.selectOptionFromDropdown(driver, By.id("categoryWrapper"), categoryName);

		// fill repeating options
		driver.findElement(By.id("transaction-repeating-modifier")).sendKeys(repeatingModifier);
		TransactionTestHelper.selectOptionFromDropdown(driver, By.cssSelector("#transaction-repeating-modifier-row"), repeatingModifierType);

		// fill date
		driver.findElement(By.id("transaction-datepicker")).click();
		List<WebElement> datePickerCells = driver.findElements(By.cssSelector(".datepicker-table td"));
		for(WebElement cell : datePickerCells)
		{
			if(cell.getText().equals("3"))
			{
				cell.click();
				driver.findElement(By.cssSelector(".datepicker-done")).click();
				break;
			}
		}

		// submit form
		WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOf(submitButton));

		submitButton.click();

		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".headline-date")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSizeGreaterThan(2);

		final WebElement row = transactionsRows.get(transactionsRows.size()-2);
		final List<WebElement> columns = row.findElements(By.className("col"));
		assertThat(columns).hasSize(6);

		// check columns
		final String dateString = new SimpleDateFormat("03.MM.").format(new Date());
		TransactionTestHelper.assertTransactionColumns(columns, dateString, categoryName, "rgb(46, 124, 43)", true, false, name, description, "-" +  amount);
	}

	@Test
	public void test_edit()
	{
		driver.get(helper.getUrl() + "/transactions/6/edit");

		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("budgetmaster-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("beste");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("15.00");
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEqualTo("01.05.2019");
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEqualTo("Lorem Ipsum");
		assertThat(driver.findElement(By.id("transaction-category")).getAttribute("value")).isEqualTo("3");

		final List<WebElement> chips = driver.findElements(By.cssSelector("#transaction-chips .chip"));
		assertThat(chips).hasSize(1);
		assertThat(chips.get(0)).hasFieldOrPropertyWithValue("text", "123\nclose");

		assertThat(driver.findElement(By.id("transaction-account")).getAttribute("value")).isEqualTo("3");

		assertThat(driver.findElement(By.id("transaction-repeating-modifier")).getAttribute("value")).isEqualTo("1");
		assertThat(driver.findElement(By.id("transaction-repeating-modifier-type")).getAttribute("value")).isEqualTo("Days");

		assertThat(driver.findElement(By.id("repeating-end-after-x-times")).isSelected()).isTrue();
		assertThat(driver.findElement(By.id("transaction-repeating-end-after-x-times-input")).getAttribute("value")).isEqualTo("20");
	}
}