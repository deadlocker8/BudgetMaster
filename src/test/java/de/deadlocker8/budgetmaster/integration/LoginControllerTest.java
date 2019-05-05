package de.deadlocker8.budgetmaster.integration;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.authentication.UserService;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SeleniumTest
public class LoginControllerTest
{
	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private WebDriver driver;

	@LocalServerPort
	int port;

	@Test
	public void getLoginPage()
	{
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();

		WebElement input = driver.findElement(By.id("login-password"));
		assertNotNull(input);

		WebElement label = driver.findElement(By.cssSelector(".input-field label"));
		assertEquals(Localization.getString("login.password"), label.getText());

		WebElement button = driver.findElement(By.tagName("button"));
		assertEquals(Localization.getString("login.button"), IntegrationTestHelper.getTextNode(button));
	}

	@Test
	public void wrongCredentials()
	{
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login("akhjfvbvahsdsa");

		WebElement label = driver.findElement(By.id("loginMessage"));
		assertEquals(Localization.getString("warning.wrong.password"), label.getText());
	}

	@Test
	public void successLogin()
	{
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);

		WebElement label = driver.findElement(By.id("logo-home"));
		String expected = helper.getUrl() + "/images/Logo_with_text_medium_res.png";
		assertEquals(expected, label.getAttribute("src"));
	}

	@Test
	public void logout()
	{
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);

		WebElement buttonLogout = driver.findElement(By.xpath("//body/ul/li/a[contains(text(), 'Logout')]"));
		buttonLogout.click();

		WebElement label = driver.findElement(By.id("loginMessage"));
		assertEquals(Localization.getString("logout.success"), label.getText());
	}
}