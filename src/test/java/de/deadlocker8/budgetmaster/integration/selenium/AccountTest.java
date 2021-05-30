package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
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
		options.addPreference("devtools.console.stdout.content", true);
		driver = new FirefoxDriver(options);

		// prepare
		helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		String path = getClass().getClassLoader().getResource("AccountDatabase.json").getFile().replace("/", File.separator);

		final Account account1 = new Account("DefaultAccount0815", AccountType.CUSTOM);
		final Account account2 = new Account("sfsdf", AccountType.CUSTOM);
		final Account account3 = new Account("read only account", AccountType.CUSTOM);
		account3.setAccountState(AccountState.READ_ONLY);
		final Account account4 = new Account("hidden account", AccountType.CUSTOM);
		account4.setAccountState(AccountState.HIDDEN);

		final List<Account> destinationAccounts = List.of(account1, account2, account3, account4);

		helper.uploadDatabase(path, Arrays.asList("DefaultAccount0815", "sfsdf", "read only account", "hidden account"), destinationAccounts);
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
		assertThat(accountRows).hasSize(5);
	}

	@Test
	public void test_newAccount()
	{
		driver.get(helper.getUrl() + "/accounts");
		driver.findElement(By.id("button-new-account")).click();

		String name = "My new account";

		// fill form
		driver.findElement(By.id("account-name")).sendKeys(name);
		helper.selectAccountStateByName(AccountState.READ_ONLY);

		// submit form
		driver.findElement(By.id("button-save-account")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Accounts"));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/accounts");

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		assertThat(accountRows).hasSize(6);

		assertAccountColumns(accountRows.get(0).findElements(By.tagName("td")), true, true, AccountState.FULL_ACCESS, "Default Account");
		assertAccountColumns(accountRows.get(1).findElements(By.tagName("td")), true, false, AccountState.FULL_ACCESS, "DefaultAccount0815");
		assertAccountColumns(accountRows.get(2).findElements(By.tagName("td")), false, false, AccountState.HIDDEN, "hidden account");
		assertAccountColumns(accountRows.get(3).findElements(By.tagName("td")), false, false, AccountState.READ_ONLY, name);
		assertAccountColumns(accountRows.get(4).findElements(By.tagName("td")), false, false, AccountState.READ_ONLY, "read only account");
		assertAccountColumns(accountRows.get(5).findElements(By.tagName("td")), true, false, AccountState.FULL_ACCESS, "sfsdf");
	}

	@Test
	public void test_edit()
	{
		driver.get(helper.getUrl() + "/accounts/2/edit");

		assertThat(driver.findElement(By.id("account-name")).getAttribute("value")).isEqualTo("Default Account");
		assertThat(driver.findElement(By.cssSelector(".account-state-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo(AccountState.FULL_ACCESS.name());
	}

	@Test
	public void test_readOnly_newTransaction_listOnlyReadableAccounts()
	{
		driver.get(helper.getUrl() + "/transactions");
		driver.findElement(By.id("button-new-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		final By locator = By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		driver.findElement(locator).click();

		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// assert
		List<WebElement> items = driver.findElements(By.cssSelector(".account-select-wrapper .custom-select-options .custom-select-item-name"));
		List<String> itemNames = items.stream()
				.map(webElement -> webElement.getAttribute("innerHTML").trim())
				.collect(Collectors.toList());
		assertThat(itemNames).containsExactlyInAnyOrder("Default Account", "DefaultAccount0815", "sfsdf");
	}

	@Test
	public void test_readOnly_preventTransactionDeleteAndEdit()
	{
		// select "sfsdf"
		TransactionTestHelper.selectGlobalAccountByName(driver, "sfsdf");

		// open new transaction page
		driver.get(helper.getUrl() + "/transactions");
		driver.findElement(By.id("button-new-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		final By locator = By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		driver.findElement(locator).click();

		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// fill form
		driver.findElement(By.id("transaction-name")).sendKeys("My transaction");
		driver.findElement(By.id("transaction-amount")).sendKeys("15.00");
		TransactionTestHelper.selectCategoryByName(driver, "sdfdsf");

		// submit form
		driver.findElement(By.id("button-save-transaction")).click();

		// set account as readonly
		setAccountState(4, AccountState.READ_ONLY);

		driver.get(helper.getUrl() + "/transactions");

		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".headline-date")));

		// assert
		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSize(2);

		final WebElement row = transactionsRows.get(0);
		final List<WebElement> columns = row.findElements(By.className("col"));

		// check columns
		final List<WebElement> icons = columns.get(5).findElements(By.tagName("i"));
		assertThat(icons).isEmpty();
	}

	@Test
	public void test_readOnly_preventNewTransaction()
	{
		TransactionTestHelper.selectGlobalAccountByName(driver, "read only account");

		driver.get(helper.getUrl() + "/transactions");
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalFilterTrigger")));

		assertThat(driver.findElements(By.id("button-new-transaction"))).isEmpty();

		// try to open new transaction page
		driver.get(helper.getUrl() + "/transactions/newTransaction/normal");
		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalFilterTrigger")));

		assertThat(driver.findElement(By.cssSelector(".notification.background-yellow")).isDisplayed()).isTrue();
	}

	public static void assertAccountColumns(List<WebElement> columns, boolean isDefaultIconVisible, boolean isDefaultIconSelected, AccountState expectedAccountState, String name)
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

		final WebElement icon = icons.get(numberOfVisibleIcons);
		assertThat(icon.isDisplayed()).isTrue();
		switch(expectedAccountState)
		{
			case FULL_ACCESS:
				assertThat(icon.getAttribute("class")).contains("fa-edit");
				break;
			case READ_ONLY:
				assertThat(icon.getAttribute("class")).contains("fa-lock");
				break;
			case HIDDEN:
				assertThat(icon.getAttribute("class")).contains("fa-eye-slash");
				break;
		}

		numberOfVisibleIcons++;

		assertThat(icons).hasSize(numberOfVisibleIcons);

		// name
		assertThat(columns.get(2)).hasFieldOrPropertyWithValue("text", name);
	}

	private void setAccountState(int accountID, AccountState accountState)
	{
		driver.get(helper.getUrl() + "/accounts/" + accountID + "/edit");

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("account-name")));

		helper.selectAccountStateByName(accountState);

		driver.findElement(By.id("button-save-account")).click();
	}
}