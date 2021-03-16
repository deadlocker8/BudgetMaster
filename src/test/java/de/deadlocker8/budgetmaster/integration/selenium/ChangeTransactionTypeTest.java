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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SeleniumTest
public class ChangeTransactionTypeTest
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
			IntegrationTestHelper.saveScreenshots(driver, name, ChangeTransactionTypeTest.class);
		}
	};

	private void openTransferTypeModal(int transactionID)
	{
		driver.get(helper.getUrl() + "/transactions/" + transactionID + "/edit");

		Actions builder = new Actions(driver);
		WebElement element = driver.findElement(By.id("transaction-actions-button"));
		builder.moveToElement(element).build().perform();

		By changeTypeButtonSelector = By.xpath("//a[contains(@data-action-type, 'changeType')][1]");
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(changeTypeButtonSelector));

		WebElement buttonChangeType = driver.findElement(changeTypeButtonSelector);
		assertThat(buttonChangeType.isDisplayed()).isTrue();

		buttonChangeType.click();
		assertThat(driver.findElement(By.id("modalChangeTransactionType")).isDisplayed()).isTrue();
	}

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

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path, Arrays.asList("DefaultAccount0815", "sfsdf"), Arrays.asList("DefaultAccount0815", "Account2"));
	}

	@Test
	public void test_availableOptions_normal()
	{
		openTransferTypeModal(2);

		final List<WebElement> typeOptions = driver.findElements(By.cssSelector("#newTypeSelect option"));
		assertThat(typeOptions).hasSize(2);
		assertThat(typeOptions.get(0).getAttribute("text")).isEqualTo("Recurring");
		assertThat(typeOptions.get(1).getAttribute("text")).isEqualTo("Transfer");
	}

	@Test
	public void test_availableOptions_recurring()
	{
		openTransferTypeModal(6);

		final List<WebElement> typeOptions = driver.findElements(By.cssSelector("#newTypeSelect option"));
		assertThat(typeOptions).hasSize(2);
		assertThat(typeOptions.get(0).getAttribute("text")).isEqualTo("Transaction");
		assertThat(typeOptions.get(1).getAttribute("text")).isEqualTo("Transfer");
	}

	@Test
	public void test_availableOptions_transfer()
	{
		openTransferTypeModal(3);

		final List<WebElement> typeOptions = driver.findElements(By.cssSelector("#newTypeSelect option"));
		assertThat(typeOptions).hasSize(2);
		assertThat(typeOptions.get(0).getAttribute("text")).isEqualTo("Transaction");
		assertThat(typeOptions.get(1).getAttribute("text")).isEqualTo("Recurring");
	}

	@Test
	public void test_normal_to_transfer()
	{
		openTransferTypeModal(2);
		TransactionTestHelper.selectOptionFromDropdown(driver, By.cssSelector("#modalChangeTransactionType .select-wrapper"), "Transfer");
		driver.findElement(By.id("buttonChangeTransactionType")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBe(By.cssSelector(".headline"), "Edit Transfer"));

		assertThatThrownBy(()->driver.findElement(By.className("buttonExpenditure"))).isInstanceOf(NoSuchElementException.class);

		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("Test");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("15.00");
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEqualTo("01.05.2019");
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEqualTo("Lorem Ipsum");
		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("4");

		final List<WebElement> chips = driver.findElements(By.cssSelector("#transaction-chips .chip"));
		assertThat(chips).hasSize(1);
		assertThat(chips.get(0)).hasFieldOrPropertyWithValue("text", "123\nclose");

		assertThat(driver.findElement(By.cssSelector(".account-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("3");
		assertThat(driver.findElement(By.id("transaction-transfer-account")).getAttribute("value")).isEqualTo("2");
	}

	@Test
	public void test_recurring_to_normal()
	{
		openTransferTypeModal(6);
		TransactionTestHelper.selectOptionFromDropdown(driver, By.cssSelector("#modalChangeTransactionType .select-wrapper"), "Transaction");
		driver.findElement(By.id("buttonChangeTransactionType")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBe(By.cssSelector(".headline"), "Edit Transaction"));

		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("background-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("beste");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("15.00");
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEqualTo("01.05.2019");
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEqualTo("Lorem Ipsum");
		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("3");

		final List<WebElement> chips = driver.findElements(By.cssSelector("#transaction-chips .chip"));
		assertThat(chips).hasSize(1);
		assertThat(chips.get(0)).hasFieldOrPropertyWithValue("text", "123\nclose");

		assertThat(driver.findElement(By.cssSelector(".account-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("3");
	}

	@Test
	public void test_transfer_to_recurring()
	{
		openTransferTypeModal(3);
		TransactionTestHelper.selectOptionFromDropdown(driver, By.cssSelector("#modalChangeTransactionType .select-wrapper"), "Recurring");
		driver.findElement(By.id("buttonChangeTransactionType")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBe(By.cssSelector(".headline"), "Edit Recurring Transaction"));

		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("background-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("Transfer dings");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("3.00");
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEqualTo("01.05.2019");
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEmpty();
		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("1");

		final List<WebElement> chips = driver.findElements(By.cssSelector("#transaction-chips .chip"));
		assertThat(chips).hasSize(1);
		assertThat(chips.get(0)).hasFieldOrPropertyWithValue("text", "123\nclose");

		assertThat(driver.findElement(By.cssSelector(".account-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("3");

		assertThat(driver.findElement(By.id("transaction-repeating-modifier")).getAttribute("value")).isEmpty();
		assertThat(driver.findElement(By.id("transaction-repeating-modifier-type")).getAttribute("value")).isEqualTo("Months");

		assertThat(driver.findElement(By.id("repeating-end-never")).isSelected()).isTrue();
	}
}