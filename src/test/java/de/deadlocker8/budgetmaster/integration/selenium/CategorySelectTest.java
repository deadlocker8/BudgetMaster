package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CategorySelectTest extends SeleniumTestBase
{
	private IntegrationTestHelper helper;

	@BeforeAll
	public void beforeAll()
	{
		helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);

		final Account account1 = new Account("DefaultAccount0815", AccountType.CUSTOM);
		final Account account2 = new Account("Account2", AccountType.CUSTOM);

		helper.uploadDatabase(path, Arrays.asList("DefaultAccount0815", "sfsdf"), List.of(account1, account2));
	}

	@BeforeEach
	public void beforeEach()
	{
		// open transactions page
		driver.get(helper.getUrl() + "/transactions");
		driver.findElement(By.id("button-new-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		final By locator = By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		driver.findElement(locator).click();

		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));
	}

	@Test
	void test_newTransaction_openWithEnter()
	{
		// navigate to category select with tab traversal
		driver.findElement(By.tagName("body")).sendKeys(Keys.TAB);
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for=transaction-amount].active")));

		driver.findElement(By.tagName("body")).sendKeys(Keys.TAB);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("label[for=transaction-amount].active")));

		// open category select
		driver.findElement(By.tagName("body")).sendKeys(Keys.ENTER);
		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.selected")));

		// assert
		List<WebElement> selectOptions = driver.findElements(By.cssSelector(".category-select-wrapper .custom-select-option"));
		assertThat(selectOptions).hasSize(3);
		assertThat(selectOptions.get(0).isDisplayed()).isTrue();
	}

	@Test
	void test_newTransaction_goDownWithKey()
	{
		// open category select
		driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-trigger")).click();
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.selected")));

		driver.findElement(By.tagName("body")).sendKeys(Keys.DOWN);
		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered[data-value=\"3\"]")));

		// assert
		List<WebElement> selectOptions = driver.findElements(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered"));
		assertThat(selectOptions).hasSize(1);
		assertThat(selectOptions.get(0).findElement(By.cssSelector(".category-select-wrapper .custom-select-item-name")))
				.hasFieldOrPropertyWithValue("text", "sdfdsf");

		driver.findElement(By.tagName("body")).sendKeys(Keys.DOWN);
		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered[data-value=\"4\"]")));

		// assert
		selectOptions = driver.findElements(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered"));
		assertThat(selectOptions).hasSize(1);
		assertThat(selectOptions.get(0).findElement(By.cssSelector(".category-select-wrapper .custom-select-item-name")))
				.hasFieldOrPropertyWithValue("text", "12sd");
	}

	@Test
	void test_newTransaction_goUpWithKey()
	{
		// open category select
		driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-trigger")).click();
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.selected")));

		driver.findElement(By.tagName("body")).sendKeys(Keys.UP);
		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered[data-value=\"4\"]")));

		// assert
		List<WebElement> selectOptions = driver.findElements(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered"));
		assertThat(selectOptions).hasSize(1);
		assertThat(selectOptions.get(0).findElement(By.cssSelector(".category-select-wrapper .custom-select-item-name")))
				.hasFieldOrPropertyWithValue("text", "12sd");

		driver.findElement(By.tagName("body")).sendKeys(Keys.UP);
		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered[data-value=\"3\"]")));

		// assert
		selectOptions = driver.findElements(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered"));
		assertThat(selectOptions).hasSize(1);
		assertThat(selectOptions.get(0).findElement(By.cssSelector(".category-select-wrapper .custom-select-item-name")))
				.hasFieldOrPropertyWithValue("text", "sdfdsf");
	}

	@Test
	void test_newTransaction_confirmWithEnter()
	{
		// open category select
		driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-trigger")).click();

		driver.findElement(By.tagName("body")).sendKeys(Keys.UP);
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered[data-value=\"4\"]")));

		driver.findElement(By.tagName("body")).sendKeys(Keys.ENTER);

		// assert
		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(By.cssSelector(".category-select-wrapper .custom-select"), "class", "open")));

		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-item-name")))
				.hasFieldOrPropertyWithValue("text", "12sd");
	}

	@Test
	void test_newTransaction_jumpToCategoryByFirstLetter()
	{
		// open category select
		driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-trigger")).click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".category-select-wrapper .custom-select-option.selected")));

		driver.findElement(By.tagName("body")).sendKeys("s");

		// assert
		List<WebElement> selectOptions = driver.findElements(By.cssSelector(".category-select-wrapper .custom-select-option.custom-select-option-hovered"));
		assertThat(selectOptions).hasSize(1);
		assertThat(selectOptions.get(0).findElement(By.className("custom-select-item-name")))
				.hasFieldOrPropertyWithValue("text", "sdfdsf");
	}
}