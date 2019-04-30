package de.deadlocker8.budgetmaster.integration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class LoginControllerTest
{
	private WebDriver driver;
	private String baseUrl = "https://localhost:";

	@LocalServerPort
	int port;

	@Before
	public void before()
	{
		WebDriverManager.firefoxdriver().setup();
		driver = new FirefoxDriver();
		baseUrl += port;
	}

	@Test
	public void getSearchPage()
	{
		driver.get(baseUrl);
		WebElement input = driver.findElement(By.id("login-password"));
		assertNotNull(input);

		WebElement label = driver.findElement(By.cssSelector(".input-field label"));
		assertEquals("Password", label.getText());

		WebElement button = driver.findElement(By.tagName("button"));
		assertEquals("Login", IntegrationTestHelper.getTextNode(button));
	}

	@After
	public void closeBrowser()
	{
		driver.close();
	}
}