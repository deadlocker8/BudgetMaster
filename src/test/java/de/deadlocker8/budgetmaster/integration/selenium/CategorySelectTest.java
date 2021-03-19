package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTest;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SeleniumTest
public class CategorySelectTest
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
			IntegrationTestHelper.saveScreenshots(driver, name, CategorySelectTest.class);
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

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path, Arrays.asList("DefaultAccount0815", "sfsdf"), Arrays.asList("DefaultAccount0815", "Account2"));

		// open transactions page
		driver.get(helper.getUrl() + "/transactions");
		driver.findElement(By.id("button-new-transaction")).click();
	}

	@Test
	public void test_newTransaction_openWithEnter()
	{
		// open new transaction page
		driver.findElement(By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]")).click();

		// navigate to category select with tab traversal
		driver.findElement(By.tagName("body")).sendKeys(Keys.TAB);
		driver.findElement(By.tagName("body")).sendKeys(Keys.TAB);

		// open category select
		driver.findElement(By.tagName("body")).sendKeys(Keys.ENTER);
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.selected")));

		// assert
		List<WebElement> selectOptions = driver.findElements(By.cssSelector(".category-select-wrapper .custom-select-option"));
		assertThat(selectOptions).hasSize(3);
		assertThat(selectOptions.get(0).isDisplayed()).isTrue();
	}

	@Test
	public void test_newTransaction_goDownWithKey()
	{
		// open new transaction page
		driver.findElement(By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]")).click();

		// open category select
		driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-trigger")).click();
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.selected")));

		driver.findElement(By.tagName("body")).sendKeys(Keys.DOWN);

		// assert
		List<WebElement> selectOptions = driver.findElements(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered"));
		assertThat(selectOptions).hasSize(1);
		assertThat(selectOptions.get(0).findElement(By.cssSelector(".category-select-wrapper .custom-select-item-name")))
				.hasFieldOrPropertyWithValue("text", "sdfdsf");

		driver.findElement(By.tagName("body")).sendKeys(Keys.DOWN);

		// assert
		selectOptions = driver.findElements(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered"));
		assertThat(selectOptions).hasSize(1);
		assertThat(selectOptions.get(0).findElement(By.cssSelector(".category-select-wrapper .custom-select-item-name")))
				.hasFieldOrPropertyWithValue("text", "12sd");
	}

	@Test
	public void test_newTransaction_goUpWithKey()
	{
		// open new transaction page
		driver.findElement(By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]")).click();

		// open category select
		driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-trigger")).click();
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.selected")));

		driver.findElement(By.tagName("body")).sendKeys(Keys.UP);

		// assert
		List<WebElement> selectOptions = driver.findElements(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered"));
		assertThat(selectOptions).hasSize(1);
		assertThat(selectOptions.get(0).findElement(By.cssSelector(".category-select-wrapper .custom-select-item-name")))
				.hasFieldOrPropertyWithValue("text", "12sd");

		driver.findElement(By.tagName("body")).sendKeys(Keys.UP);

		// assert
		selectOptions = driver.findElements(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered"));
		assertThat(selectOptions).hasSize(1);
		assertThat(selectOptions.get(0).findElement(By.cssSelector(".category-select-wrapper .custom-select-item-name")))
				.hasFieldOrPropertyWithValue("text", "sdfdsf");
	}

	@Test
	public void test_newTransaction_confirmWithEnter()
	{
		// open new transaction page
		driver.findElement(By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]")).click();

		// open category select
		driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-trigger")).click();

		driver.findElement(By.tagName("body")).sendKeys(Keys.UP);
		driver.findElement(By.tagName("body")).sendKeys(Keys.ENTER);

		// assert
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(By.cssSelector(".category-select-wrapper .custom-select"), "class", "open")));

		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-item-name")))
				.hasFieldOrPropertyWithValue("text", "12sd");
	}

	@Test
	public void test_newTransaction_jumpToCategoryByFirstLetter()
	{
		// open new transaction page
		driver.findElement(By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]")).click();

		// open category select
		driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-trigger")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.selected")));

		driver.findElement(By.tagName("body")).sendKeys("s");

		// assert
		List<WebElement> selectOptions = driver.findElements(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered"));
		assertThat(selectOptions).hasSize(1);
		assertThat(selectOptions.get(0).findElement(By.className("custom-select-item-name")))
				.hasFieldOrPropertyWithValue("text", "sdfdsf");
	}
}