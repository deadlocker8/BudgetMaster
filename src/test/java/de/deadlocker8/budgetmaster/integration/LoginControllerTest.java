package de.deadlocker8.budgetmaster.integration;

import de.deadlocker8.budgetmaster.Main;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SeleniumTest
public class LoginControllerTest
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
	public void getSearchPage()
	{
		driver.get(url);
		WebElement input = driver.findElement(By.id("login-password"));
		assertNotNull(input);

		WebElement label = driver.findElement(By.cssSelector(".input-field label"));
		assertEquals(Localization.getString("login.password"), label.getText());

		WebElement button = driver.findElement(By.tagName("button"));
		assertEquals(Localization.getString("login.button"), IntegrationTestHelper.getTextNode(button));
	}
}