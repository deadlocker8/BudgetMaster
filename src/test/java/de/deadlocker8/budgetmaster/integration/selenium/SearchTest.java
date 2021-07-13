package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTest;
import de.thecodelabs.utils.util.Localization;
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
public class SearchTest
{
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
			IntegrationTestHelper.saveScreenshots(driver, name, SearchTest.class);
		}
	};

	@Before
	public void prepare()
	{
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(false);
		options.addPreference("devtools.console.stdout.content", true);
		driver = new FirefoxDriver(options);
		driver.manage().window().maximize();

		// prepare
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		final Account account1 = new Account("DefaultAccount0815", AccountType.CUSTOM);
		final Account account2 = new Account("Account2", AccountType.CUSTOM);

		helper.uploadDatabase(path, Arrays.asList("DefaultAccount0815", "sfsdf"), List.of(account1, account2));
		// search
		WebElement inputSearch = driver.findElement(By.id("search"));
		inputSearch.sendKeys("e");
		driver.findElement(By.id("buttonSearch")).click();
	}

	@Test
	public void searchFromNavbar()
	{
		// headline
		WebElement headline = driver.findElement(By.className("headline"));
		String expected = Localization.getString("menu.search.results", 24);
		assertThat(headline.getText()).isEqualTo(expected);

		// checkboxes
		assertThat(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchName\"]")).isSelected()).isTrue();
		assertThat(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchDescription\"]")).isSelected()).isTrue();
		assertThat(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchCategory\"]")).isSelected()).isTrue();
		assertThat(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchTags\"]")).isSelected()).isTrue();

		// results
		List<WebElement> results = driver.findElements(By.cssSelector(".search-container .card-panel"));
		assertThat(results.size()).isEqualTo(10);
	}

	@Test
	public void pagination()
	{
		// === PAGE 1 ===
		List<WebElement> pages = driver.findElements(By.cssSelector(".pagination li"));
		assertThat(pages.size()).isEqualTo(5);

		assertThat(pages.get(0).getAttribute("class")).contains("disabled");
		assertThat(pages.get(1).findElement(By.className("page-link")).getText()).isEqualTo("1");
		assertThat(pages.get(1).getAttribute("class")).contains("active");
		assertThat(pages.get(2).findElement(By.className("page-link")).getText()).isEqualTo("2");
		assertThat(pages.get(3).findElement(By.className("page-link")).getText()).isEqualTo("3");
		assertThat(pages.get(4).getAttribute("class")).doesNotContain("disabled");

		// validate results
		List<WebElement> results = driver.findElements(By.cssSelector(".search-container .card-panel"));
		assertThat(results.size()).isEqualTo(10);

		// === PAGE 1 ===
		pages.get(3).click();

		pages = driver.findElements(By.cssSelector(".pagination li"));
		assertThat(pages.size()).isEqualTo(5);

		// previous button should be enabled
		assertThat(pages.get(0).getAttribute("class")).doesNotContain("disabled");

		assertThat(pages.get(1).findElement(By.className("page-link")).getText()).isEqualTo("1");
		assertThat(pages.get(2).findElement(By.className("page-link")).getText()).isEqualTo("2");
		assertThat(pages.get(3).findElement(By.className("page-link")).getText()).isEqualTo("3");
		assertThat(pages.get(3).getAttribute("class")).contains("active");

		// next button should be disabled
		assertThat(pages.get(4).getAttribute("class")).contains("disabled");

		// validate
		results = driver.findElements(By.cssSelector(".search-container .card-panel"));
		assertThat(results.size()).isEqualTo(4);
	}

	@Test
	public void checkboxes()
	{
		// deselect some checkboxes (use JavascriptExecutor here as the checkbox is covered by a span)
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchName\"]")));
		executor.executeScript("arguments[0].click();", driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchDescription\"]")));

		// search
		driver.findElement(By.cssSelector(".main-card #searchForm button[type=\"submit\"]")).click();

		// validate
		assertThat(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchName\"]")).isSelected()).isFalse();
		assertThat(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchDescription\"]")).isSelected()).isFalse();
		assertThat(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchCategory\"]")).isSelected()).isTrue();
		assertThat(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchTags\"]")).isSelected()).isTrue();

		// results
		List<WebElement> results = driver.findElements(By.cssSelector(".search-container .card-panel"));
		assertThat(results.size()).isEqualTo(2);
	}

	@Test
	public void highlight()
	{
		driver.findElement(By.cssSelector(".main-card .search-result .hide-on-med-and-down .buttonHighlight")).click();

		assertThat(driver.findElement(By.cssSelector(".headline-date")).getText()).isEqualTo("May 2019");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows.size()).isEqualTo(25);
		assertThat(transactionsRows.get(0).getAttribute("class")).contains("background-blue-light");
		for(int i = 1; i < transactionsRows.size(); i++)
		{
			assertThat(transactionsRows.get(i).getAttribute("class")).doesNotContain("background-blue-light");
		}
	}
}