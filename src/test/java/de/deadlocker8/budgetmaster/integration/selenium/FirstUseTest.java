package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SeleniumTest
public class FirstUseTest
{
	private IntegrationTestHelper helper;
	private WebDriver driver;

	@LocalServerPort
	int port;

	@Rule
	public TestName name = new TestName();

	@Rule
	public TestWatcher testWatcher = new TestWatcher()
	{
		@Override
		protected void finished(Description description)
		{
			driver.quit();
		}

		@Override
		protected void failed(Throwable e, Description description)
		{
			IntegrationTestHelper.saveScreenshots(driver, name, FirstUseTest.class);
		}
	};

	@Before
	public void prepare()
	{
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(false);
		driver = new FirefoxDriver(options);

		// prepare
		helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();
	}

	@Test
	public void test_firstUserBanner()
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstUseBanner")));
		assertThat(driver.findElement(By.id("firstUseBanner")).isDisplayed()).isTrue();
	}

	@Test
	public void test_firstUserBanner_dismiss()
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstUseBanner")));

		driver.findElements(By.className("notification-clear")).get(0).click();

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("firstUseBanner")));
		assertThat(driver.findElements(By.id("firstUseBanner"))).isEmpty();
	}

	@Test
	public void test_firstUserBanner_click()
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstUseBanner")));

		driver.findElements(By.className("notification")).get(0).click();

		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "First use guide"));

		assertThat(driver.getCurrentUrl()).endsWith("/firstUse");
	}
}