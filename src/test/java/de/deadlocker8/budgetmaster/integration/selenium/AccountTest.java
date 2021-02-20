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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SeleniumTest
public class AccountTest
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
			IntegrationTestHelper.saveScreenshots(driver, name, AccountTest.class);
		}
	};

	@Before
	public void prepare()
	{
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(false);
		driver = new FirefoxDriver(options);

		// prepare
		helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path, Arrays.asList("DefaultAccount0815", "sfsdf"), Arrays.asList("DefaultAccount0815", "Account2"));
	}

	@Test
	public void test_newAccount_cancel()
	{
		driver.get(helper.getUrl() + "/accounts");
		driver.findElement(By.id("button-new-account")).click();

		// click cancel button
		WebElement cancelButton = driver.findElement(By.id("button-cancel-save-account"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cancelButton);
		cancelButton.click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Accounts"));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/accounts");

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		assertThat(accountRows).hasSize(3);
	}

	@Test
	public void test_newAccount()
	{
		driver.get(helper.getUrl() + "/accounts");
		driver.findElement(By.id("button-new-account")).click();

		String name = "My new account";

		// fill form
		driver.findElement(By.id("account-name")).sendKeys(name);

		// submit form
		driver.findElement(By.id("button-save-account")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Accounts"));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/accounts");

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		assertThat(accountRows).hasSize(4);

		assertAccountColumns(accountRows.get(0).findElements(By.tagName("td")), true, false, true, false, "Account2");
		assertAccountColumns(accountRows.get(1).findElements(By.tagName("td")), true, true, false, false, "Default Account");
		assertAccountColumns(accountRows.get(2).findElements(By.tagName("td")), true, false, true, false, "DefaultAccount0815");
		assertAccountColumns(accountRows.get(3).findElements(By.tagName("td")), true, false, true, false, name);
	}

	@Test
	public void test_edit()
	{
		driver.get(helper.getUrl() + "/accounts/2/edit");

		assertThat(driver.findElement(By.id("account-name")).getAttribute("value")).isEqualTo("Default Account");
	}

	@Test
	public void test_setAsDefault()
	{
		driver.get(helper.getUrl() + "/accounts");

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		final List<WebElement> columns = accountRows.get(0).findElements(By.tagName("td"));
		final List<WebElement> icons = columns.get(0).findElements(By.tagName("i"));

		icons.get(0).click();

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/accounts");

		accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		assertThat(accountRows).hasSize(3);

		assertAccountColumns(accountRows.get(0).findElements(By.tagName("td")), true, true, false, false, "Account2");
		assertAccountColumns(accountRows.get(1).findElements(By.tagName("td")), true, false, true, false, "Default Account");
		assertAccountColumns(accountRows.get(2).findElements(By.tagName("td")), true, false, true, false, "DefaultAccount0815");
	}

	@Test
	public void test_setReadOnly()
	{
		setAsReadOnly();

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/accounts");

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		assertThat(accountRows).hasSize(3);

		assertAccountColumns(accountRows.get(0).findElements(By.tagName("td")), false, false, true, true, "Account2");
		assertAccountColumns(accountRows.get(1).findElements(By.tagName("td")), true, true, false, false, "Default Account");
		assertAccountColumns(accountRows.get(2).findElements(By.tagName("td")), true, false, true, false, "DefaultAccount0815");
	}

	private void setAsReadOnly()
	{
		driver.get(helper.getUrl() + "/accounts");

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		final List<WebElement> columns = accountRows.get(0).findElements(By.tagName("td"));
		final List<WebElement> icons = columns.get(0).findElements(By.tagName("i"));

		icons.get(1).click();
	}

	@Test
	public void test_readOnly_newTransaction_listOnlyReadableAccounts()
	{
		setAsReadOnly();

		driver.get(helper.getUrl() + "/transactions");
		driver.findElement(By.id("button-new-transaction")).click();
		driver.findElement(By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]")).click();

		// assert
		WebElement select = driver.findElement(By.id("accountWrapper"));
		select.findElement(By.className("select-dropdown")).click();

		List<WebElement> items = select.findElements(By.xpath(".//ul/li/span"));
		List<String> itemNames = items.stream()
				.map(WebElement::getText)
				.collect(Collectors.toList());
		assertThat(itemNames).containsExactly("Default Account", "DefaultAccount0815");
	}

	@Test
	public void test_readOnly_preventTransactionDeleteAndEdit()
	{
		// select "Account2"
		TransactionTestHelper.selectOptionFromDropdown(driver, By.id("selectWrapper"), "Account2");

		// open new transaction page
		driver.get(helper.getUrl() + "/transactions");
		driver.findElement(By.id("button-new-transaction")).click();
		driver.findElement(By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]")).click();

		// fill form
		driver.findElement(By.id("transaction-name")).sendKeys("My transaction");
		driver.findElement(By.id("transaction-amount")).sendKeys("15.00");
		TransactionTestHelper.selectOptionFromDropdown(driver, By.id("categoryWrapper"), "sdfdsf");

		// submit form
		driver.findElement(By.id("button-save-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".headline-date")));

		// set used account as readonly
		setAsReadOnly();

		driver.get(helper.getUrl() + "/transactions");

		// assert
		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSize(2);

		final WebElement row = transactionsRows.get(0);
		final List<WebElement> columns = row.findElements(By.className("col"));

		// check columns
		final List<WebElement> icons = columns.get(5).findElements(By.tagName("i"));
		assertThat(icons).isEmpty();
	}

	public static void assertAccountColumns(List<WebElement> columns, boolean isDefaultIconVisible, boolean isDefaultIconSelected, boolean isReadOnlyIconVisible, boolean isReadOnlyIconSelected, String name)
	{
		// icons
		final List<WebElement> icons = columns.get(0).findElements(By.tagName("i"));
		int numberOfVisibleIcons = 0;

		if(isDefaultIconVisible)
		{
			final WebElement icon = icons.get(numberOfVisibleIcons);
			assertThat(icon.isDisplayed()).isTrue();
			if(isDefaultIconSelected)
			{
				assertThat(icon).hasFieldOrPropertyWithValue("text", "star");
			}
			else
			{
				assertThat(icon).hasFieldOrPropertyWithValue("text", "star_border");
			}

			numberOfVisibleIcons++;
		}

		if(isReadOnlyIconVisible)
		{
			final WebElement icon = icons.get(numberOfVisibleIcons);
			assertThat(icon.isDisplayed()).isTrue();
			if(isReadOnlyIconSelected)
			{
				assertThat(icon.getAttribute("class")).contains("fa-lock");
			}
			else
			{
				assertThat(icon.getAttribute("class")).contains("fa-lock-open");
			}

			numberOfVisibleIcons++;
		}

		assertThat(icons).hasSize(numberOfVisibleIcons);

		// name
		assertThat(columns.get(1)).hasFieldOrPropertyWithValue("text", name);
	}
}