package de.deadlocker8.budgetmaster.integration.helpers;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SeleniumTestExecutionListener extends AbstractTestExecutionListener
{
	private WebDriver webDriver;

	@Override
	public int getOrder()
	{
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public void prepareTestInstance(TestContext testContext)
	{
		if(!System.getProperties().containsKey("testProfile"))
		{
			throw new RuntimeException("Test profile not activated. Skipping tests. (Set -DtestProfile=true in your VM arguments)");
		}

		if(webDriver != null)
		{
			return;
		}
		ApplicationContext context = testContext.getApplicationContext();
		if(context instanceof ConfigurableApplicationContext)
		{

			FirefoxOptions options = new FirefoxOptions();
			options.setHeadless(true);
			webDriver = new FirefoxDriver(options);

			ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
			ConfigurableListableBeanFactory bf = configurableApplicationContext.getBeanFactory();
			bf.registerResolvableDependency(WebDriver.class, webDriver);
		}
	}

	@Override
	public void beforeTestMethod(TestContext testContext)
	{
	}

	@Override
	public void afterTestClass(TestContext testContext)
	{
		if(webDriver != null)
		{
			webDriver.quit();
		}
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception
	{
		if(testContext.getTestException() == null)
		{
			return;
		}
		File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);

		String testName = testContext.getTestClass().getSimpleName();
		String methodName = testContext.getTestMethod().getName();
		final Path destination = Paths.get("screenshots", testName + "_" + methodName + "_" + screenshot.getName());

		if(Files.notExists(destination.getParent()))
		{
			Files.createDirectories(destination.getParent());
		}

		Files.copy(screenshot.toPath(), destination);
	}
}
