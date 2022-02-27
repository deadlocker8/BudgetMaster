package de.deadlocker8.budgetmaster.integration.helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;


public class SeleniumTestExecutionListener extends AbstractTestExecutionListener
{
	private WebDriver driver;

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

		if(driver != null)
		{
			return;
		}

		// allow driver to be Autowired
		final ApplicationContext context = testContext.getApplicationContext();
		if(context instanceof final ConfigurableApplicationContext configurableApplicationContext)
		{
			FirefoxOptions options = new FirefoxOptions();
			options.setHeadless(false);
			options.addPreference("devtools.console.stdout.content", true);
			driver = new FirefoxDriver(options);
			driver.manage().window().maximize();

			ConfigurableListableBeanFactory factory = configurableApplicationContext.getBeanFactory();
			factory.registerResolvableDependency(WebDriver.class, driver);
		}
	}

	@Override
	public void afterTestClass(TestContext testContext)
	{
		if(driver != null)
		{
			driver.quit();
		}
	}

	@Override
	public void afterTestMethod(TestContext testContext)
	{
		// skipped by e.g. assumeTrue()
		final boolean isAbortedByAssumption = testContext.getTestException() instanceof TestAbortedException;

		final boolean isSuccess = testContext.getTestException() == null;
		if(isSuccess || isAbortedByAssumption)
		{
			return;
		}

		IntegrationTestHelper.saveScreenshots(driver, testContext.getTestMethod().getName(), testContext.getTestClass().getSimpleName());
	}
}
