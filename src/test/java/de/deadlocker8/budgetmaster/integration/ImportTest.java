package de.deadlocker8.budgetmaster.integration;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.thecodelabs.utils.util.Localization;
import org.junit.Before;
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
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SeleniumTest
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
		driver.get(url + "/settings/database/requestImport");

		// login
		WebElement inputPassword = driver.findElement(By.id("login-password"));
		inputPassword.sendKeys(UserService.DEFAULT_PASSWORD);
		WebElement buttonLogin = driver.findElement(By.tagName("button"));
		buttonLogin.click();

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

		// account matching
		WebElement headlineImport = driver.findElement(By.className("headline"));
		assertEquals(Localization.getString("info.title.database.import.dialog"), IntegrationTestHelper.getTextNode(headlineImport));

		List<WebElement> tableRows = driver.findElements(By.cssSelector(".container form table tr"));
		assertEquals(2, tableRows.size());

		WebElement row1 = tableRows.get(0);
		WebElement sourceAccount1 = row1.findElement(By.className("account-source"));
		assertEquals("DefaultAccount0815", IntegrationTestHelper.getTextNode(sourceAccount1));

		List<WebElement> destinationAccounts = row1.findElements(By.cssSelector(".account-destination option"));
		assertEquals(1, destinationAccounts.size());
		System.out.println(destinationAccounts.get(0).getText());
//		assertEquals("Default Account", IntegrationTestHelper.getTextNode(destinationAccounts.get(0)));
	}
}