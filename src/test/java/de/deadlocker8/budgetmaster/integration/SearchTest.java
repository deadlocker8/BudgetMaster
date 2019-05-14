package de.deadlocker8.budgetmaster.integration;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTest;
import de.thecodelabs.utils.util.Localization;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SeleniumTest
public class SearchTest
{
	private WebDriver driver;

	@LocalServerPort
	int port;

	@Before
	public void prepare()
	{
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);
		driver = new FirefoxDriver(options);

		// prepare
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path, Arrays.asList("DefaultAccount0815", "sfsdf"), Arrays.asList("DefaultAccount0815", "Account2"));

		// search
		WebElement inputSearch = driver.findElement(By.id("search"));
		inputSearch.sendKeys("e");
		driver.findElement(By.id("buttonSearch")).click();
	}

	@After
	public void adftet() {
		driver.quit();
	}

	@Test
	public void searchFromNavbar()
	{
		// headline
		WebElement headline = driver.findElement(By.className("headline"));
		String expected = Localization.getString("menu.search.results", 24);
		assertEquals(expected, headline.getText());

		// checkboxes
		assertTrue(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchName\"]")).isSelected());
		assertTrue(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchDescription\"]")).isSelected());
		assertTrue(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchCategory\"]")).isSelected());
		assertTrue(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchTags\"]")).isSelected());

		// results
		List<WebElement> results = driver.findElements(By.cssSelector(".search-container .card-panel"));
		assertEquals(10, results.size());
	}

	@Test
	public void pagination()
	{
		// === PAGE 1 ===
		List<WebElement> pages = driver.findElements(By.cssSelector(".pagination li"));
		assertEquals(5, pages.size());

		assertTrue(pages.get(0).getAttribute("class").contains("disabled"));
		assertEquals("1", pages.get(1).findElement(By.className("page-link")).getText());
		assertTrue(pages.get(1).getAttribute("class").contains("active"));
		assertEquals("2", pages.get(2).findElement(By.className("page-link")).getText());
		assertEquals("3", pages.get(3).findElement(By.className("page-link")).getText());
		assertTrue(!pages.get(4).getAttribute("class").contains("disabled"));

		// validate results
		List<WebElement> results = driver.findElements(By.cssSelector(".search-container .card-panel"));
		assertEquals(10, results.size());

		// === PAGE 1 ===
		pages.get(3).click();

		pages = driver.findElements(By.cssSelector(".pagination li"));
		assertEquals(5, pages.size());

		// previous button should be enabled
		assertTrue(!pages.get(0).getAttribute("class").contains("disabled"));

		assertEquals("1", pages.get(1).findElement(By.className("page-link")).getText());
		assertEquals("2", pages.get(2).findElement(By.className("page-link")).getText());
		assertEquals("3", pages.get(3).findElement(By.className("page-link")).getText());
		assertTrue(pages.get(3).getAttribute("class").contains("active"));

		// next button should be disabled
		assertTrue(pages.get(4).getAttribute("class").contains("disabled"));

		// validate
		results = driver.findElements(By.cssSelector(".search-container .card-panel"));
		assertEquals(4, results.size());
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
		assertFalse(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchName\"]")).isSelected());
		assertFalse(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchDescription\"]")).isSelected());
		assertTrue(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchCategory\"]")).isSelected());
		assertTrue(driver.findElement(By.cssSelector(".main-card #searchForm input[name=\"searchTags\"]")).isSelected());

		// results
		List<WebElement> results = driver.findElements(By.cssSelector(".search-container .card-panel"));
		assertEquals(2, results.size());
	}

	@Test
	public void highlight()
	{
		driver.findElement(By.cssSelector(".main-card .search-result .hide-on-med-and-down .buttonHighlight")).click();

		assertEquals("May 2019", driver.findElement(By.cssSelector(".headline-date")).getText());

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertEquals(25, transactionsRows.size());
		assertTrue(transactionsRows.get(0).getAttribute("class").contains("budgetmaster-blue-light"));
		for(int i = 1; i < transactionsRows.size(); i++)
		{
			assertFalse(transactionsRows.get(i).getAttribute("class").contains("budgetmaster-blue-light"));
		}
	}
}