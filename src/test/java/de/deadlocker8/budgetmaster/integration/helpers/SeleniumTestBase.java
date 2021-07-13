package de.deadlocker8.budgetmaster.integration.helpers;

import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.web.server.LocalServerPort;

public class SeleniumTestBase
{
	protected WebDriver driver;

	@LocalServerPort
	protected int port;

	@BeforeEach
	public final void init()
	{
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(false);
		options.addPreference("devtools.console.stdout.content", true);
		driver = new FirefoxDriver(options);
		driver.manage().window().maximize();
	}

	public WebDriver getDriver()
	{
		return driver;
	}
}
