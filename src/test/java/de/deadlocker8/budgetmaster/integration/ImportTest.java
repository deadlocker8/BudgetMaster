package de.deadlocker8.budgetmaster.integration;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.thecodelabs.utils.util.Localization;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SeleniumTest
@Transactional
public class ImportTest
{
	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private WebDriver driver;
	private final static String BASE_URL = "https://localhost:";
	private String url;

	@LocalServerPort
	int port;

	@Before
	public void before()
	{
		url = BASE_URL + port;
	}

	@Test
	public void requestImport() throws InterruptedException
	{
		driver.get(url);

		// login
		WebElement inputPassword = driver.findElement(By.id("login-password"));
		inputPassword.sendKeys(UserService.DEFAULT_PASSWORD);
		WebElement buttonLogin = driver.findElement(By.tagName("button"));
		buttonLogin.click();

		// hide backup reminder if present
		try
		{
			WebElement buttonCloseReminder = driver.findElement(By.cssSelector("#modalBackupReminder #buttonCloseReminder"));
			buttonCloseReminder.click();
		}
		catch(NoSuchElementException ignored)
		{
		}

		driver.get(url + "/settings/database/requestImport");

		// upload database
		WebElement input = driver.findElement(By.id("inputDatabaseImport"));
		assertNotNull(input);

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		if(path.startsWith("\\"))
		{
			path = path.substring(1);
		}
		input.sendKeys(path);

		WebElement buttonUpload = driver.findElement(By.id("button-confirm-database-import"));
		buttonUpload.click();

		// create new account
		driver.findElement(By.className("button-new-account")).click();
		WebElement inputAccountName = driver.findElement(By.id("account-name"));
		inputAccountName.sendKeys("DefaultAccount0815");
		driver.findElement(By.tagName("button")).click();

		// create second account
		driver.findElement(By.className("button-new-account")).click();
		inputAccountName = driver.findElement(By.id("account-name"));
		inputAccountName.sendKeys("Account2");
		driver.findElement(By.tagName("button")).click();

		// account matching
		WebElement headlineImport = driver.findElement(By.className("headline"));
		assertEquals(Localization.getString("info.title.database.import.dialog"), IntegrationTestHelper.getTextNode(headlineImport));

		List<WebElement> tableRows = driver.findElements(By.cssSelector(".container form table tr"));
		assertEquals(2, tableRows.size());

		WebElement row1 = tableRows.get(0);
		WebElement sourceAccount1 = row1.findElement(By.className("account-source"));
		assertEquals("DefaultAccount0815", IntegrationTestHelper.getTextNode(sourceAccount1));

		// match first account
		row1.findElement(By.className("select-dropdown")).click();
		WebElement accountToSelect = row1.findElement(By.xpath("//form/table/tbody/tr[1]/td[5]/div/div/ul/li/span[text()='DefaultAccount0815']"));
		accountToSelect.click();

		// match second account
		tableRows.get(1).findElement(By.className("select-dropdown")).click();
		accountToSelect = row1.findElement(By.xpath("//form/table/tbody/tr[2]/td[5]/div/div/ul/li/span[text()='Account2']"));
		accountToSelect.click();

		// confirm import
		driver.findElement(By.id("buttonImport")).click();

		assertEquals(Localization.getString("menu.settings"), IntegrationTestHelper.getTextNode(driver.findElement(By.className("headline"))));
	}
}