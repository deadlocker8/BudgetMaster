package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTest;
import de.deadlocker8.budgetmaster.integration.helpers.TransactionTestHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SeleniumTest
public class NewTransactionFromTemplateTest
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
			IntegrationTestHelper.saveScreenshots(driver, name, NewTransactionFromTemplateTest.class);
		}
	};

	@Before
	public void prepare()
	{
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);
		driver = new FirefoxDriver(options);

		// prepare
		helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path, Arrays.asList("DefaultAccount0815", "sfsdf"), Arrays.asList("DefaultAccount0815", "Account2"));

		// open transactions page
		driver.get(helper.getUrl() + "/transactions");
		driver.findElement(By.id("button-new-transaction")).click();
	}

	@Test
	public void test_newTransactionFromTemplate_FullTemplate()
	{
		driver.findElement(By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'From template')]")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		driver.findElements(By.cssSelector(".template-item .btn-flat no-padding text-color"));
		driver.findElement(By.xpath("//li[contains(@class, 'template-item')]//a[contains(@href, '/templates/2/select')]")).click();

		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// assert
		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("budgetmaster-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("NameFromTemplate");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("15.00");
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEqualTo("DescriptionFromTemplate");
		assertThat(driver.findElement(By.id("transaction-category")).getAttribute("value")).isEqualTo("1");

		final List<WebElement> chips = driver.findElements(By.cssSelector("#transaction-chips .chip"));
		assertThat(chips).hasSize(1);
		assertThat(chips.get(0)).hasFieldOrPropertyWithValue("text", "TagFromTemplate\nclose");

		assertThat(driver.findElement(By.id("transaction-account")).getAttribute("value")).isEqualTo("3");
	}

	@Test
	public void test_newTransactionFromTemplate_OnlyIncome()
	{
		driver.findElement(By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'From template')]")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		driver.findElements(By.cssSelector(".template-item .btn-flat no-padding text-color"));
		driver.findElement(By.xpath("//li[contains(@class, 'template-item')]//a[contains(@href, '/templates/1/select')]")).click();

		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// assert
		assertThat(driver.findElement(By.className("buttonIncome")).getAttribute("class")).contains("budgetmaster-green");
	}
}