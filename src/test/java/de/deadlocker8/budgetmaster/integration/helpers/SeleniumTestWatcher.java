package de.deadlocker8.budgetmaster.integration.helpers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.WebDriver;

public class SeleniumTestWatcher implements TestWatcher
{
	@Override
	public void testSuccessful(ExtensionContext context)
	{
		final WebDriver driver = getDriver(context);
		driver.quit();
	}

	@Override
	public void testAborted(ExtensionContext context, Throwable cause)
	{
		final WebDriver driver = getDriver(context);
		driver.quit();
	}

	@Override
	public void testFailed(ExtensionContext context, Throwable cause)
	{
		final WebDriver driver = getDriver(context);
		IntegrationTestHelper.saveScreenshots(driver, context.getRequiredTestMethod().getName(), context.getRequiredTestClass().getSimpleName());
		driver.quit();
	}

	private WebDriver getDriver(ExtensionContext context)
	{
		final SeleniumTestBase testInstance = (SeleniumTestBase) context.getRequiredTestInstance();
		return testInstance.getDriver();
	}
}
