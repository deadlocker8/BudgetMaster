package de.deadlocker8.budgetmaster.integration;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTest;
import de.thecodelabs.utils.util.Localization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SeleniumTest
public class SearchTest
{
	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private WebDriver driver;

	@LocalServerPort
	int port;

	@Test
	public void searchFromNavbar()
	{
		// prepare
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		List<String> sourceAccounts = Arrays.asList("DefaultAccount0815", "sfsdf");
		List<String> destinationAccounts = Arrays.asList("DefaultAccount0815", "Account2");
		helper.uploadDatabase(path, sourceAccounts, destinationAccounts);

		// search
		WebElement inputSearch = driver.findElement(By.id("search"));
		inputSearch.sendKeys("e");
		driver.findElement(By.id("buttonSearch")).click();

		// validate
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
		// prepare
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		List<String> sourceAccounts = Arrays.asList("DefaultAccount0815", "sfsdf");
		List<String> destinationAccounts = Arrays.asList("DefaultAccount0815", "Account2");
		helper.uploadDatabase(path, sourceAccounts, destinationAccounts);

		// search
		WebElement inputSearch = driver.findElement(By.id("search"));
		inputSearch.sendKeys("e");
		driver.findElement(By.id("buttonSearch")).click();

		WebElement headline = driver.findElement(By.className("headline"));
		String expected = Localization.getString("menu.search.results", 24);
		assertEquals(expected, headline.getText());

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

	//TODO: test highlight, browser back, checkboxes
}