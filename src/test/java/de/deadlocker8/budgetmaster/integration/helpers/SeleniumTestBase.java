package de.deadlocker8.budgetmaster.integration.helpers;

import de.deadlocker8.budgetmaster.Main;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SeleniumTestWatcher.class)
@DirtiesContext
@SeleniumTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeleniumTestBase
{
	protected WebDriver driver;

	@LocalServerPort
	protected int port;

	@Order(1)
	@BeforeAll
	public void init()
	{
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(false);
		options.addPreference("devtools.console.stdout.content", true);
		driver = new FirefoxDriver(options);
		driver.manage().window().maximize();
	}

	@AfterAll
	public void afterAll() {
		driver.quit();
	}

	public WebDriver getDriver()
	{
		return driver;
	}
}
