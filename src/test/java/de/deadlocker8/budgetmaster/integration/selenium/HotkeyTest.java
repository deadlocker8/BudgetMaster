package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTest;
import de.deadlocker8.budgetmaster.integration.helpers.TransactionTestHelper;
import de.thecodelabs.utils.util.OS;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SeleniumTest
public class HotkeyTest
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
			IntegrationTestHelper.saveScreenshots(driver, name, HotkeyTest.class);
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
	public void hotkey_newTransaction_normal()
	{
		driver.findElement(By.tagName("body")).sendKeys("n");

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form[name='NewTransaction']")));

		assertThat(driver.getCurrentUrl()).endsWith("/newTransaction/normal");
	}

	@Test
	public void hotkey_newTransaction_recurring()
	{
		driver.findElement(By.tagName("body")).sendKeys("r");

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form[name='NewTransaction']")));

		assertThat(driver.getCurrentUrl()).endsWith("/newTransaction/repeating");
	}

	@Test
	public void hotkey_newTransaction_transfer()
	{
		driver.findElement(By.tagName("body")).sendKeys("t");

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form[name='NewTransaction']")));

		assertThat(driver.getCurrentUrl()).endsWith("/newTransaction/transfer");
	}

	@Test
	public void hotkey_newTransaction_transactionFromTemplate()
	{
		driver.findElement(By.tagName("body")).sendKeys("v");

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchTemplate")));

		assertThat(driver.getCurrentUrl()).endsWith("/templates");
	}

	@Test
	public void hotkey_filter()
	{
		driver.findElement(By.tagName("body")).sendKeys("f");

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".headline-date")));

		assertThat(driver.getCurrentUrl()).endsWith("/transactions#modalFilter");
		assertThat(driver.findElement(By.id("modalFilter")).isDisplayed()).isTrue();
	}

	@Test
	public void hotkey_search()
	{
		driver.findElement(By.tagName("body")).sendKeys("s");

		assertThat(driver.findElement(By.id("search"))).isEqualTo(driver.switchTo().activeElement());
	}

	@Test
	public void hotkey_saveTransaction()
	{
		assumeTrue(OS.isWindows());

		// open transactions page
		driver.get(helper.getUrl() + "/transactions/newTransaction/normal");

		// fill mandatory inputs
		driver.findElement(By.id("transaction-name")).sendKeys("My Transaction");
		driver.findElement(By.id("transaction-amount")).sendKeys("15.00");
		TransactionTestHelper.selectOptionFromDropdown(driver, By.id("categoryWrapper"), "sdfdsf");

		WebElement categoryWrapper = driver.findElement(By.id("categoryWrapper"));
		Action seriesOfActions = new Actions(driver)
				.keyDown(categoryWrapper, Keys.CONTROL)
				.sendKeys(categoryWrapper, Keys.ENTER)
				.keyUp(categoryWrapper, Keys.CONTROL)
				.build();
		seriesOfActions.perform();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".headline-date")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSize(2);
	}
}