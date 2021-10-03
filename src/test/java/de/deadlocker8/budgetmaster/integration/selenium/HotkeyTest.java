package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.*;
import de.thecodelabs.utils.util.OS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(SeleniumTestWatcher.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SeleniumTest
class HotkeyTest extends SeleniumTestBase
{
	private IntegrationTestHelper helper;

	@BeforeEach
	public void prepare()
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

	@Test
	void hotkey_newTransaction_normal()
	{
		driver.findElement(By.tagName("body")).sendKeys("n");

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form[name='NewTransaction']")));

		assertThat(driver.getCurrentUrl()).endsWith("/newTransaction/normal");
	}

	@Test
	void hotkey_newTransaction_transfer()
	{
		driver.findElement(By.tagName("body")).sendKeys("t");

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form[name='NewTransaction']")));

		assertThat(driver.getCurrentUrl()).endsWith("/newTransaction/transfer");
	}

	@Test
	void hotkey_newTransaction_transactionFromTemplate()
	{
		driver.findElement(By.tagName("body")).sendKeys("v");

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchTemplate")));

		assertThat(driver.getCurrentUrl()).endsWith("/templates");
	}

	@Test
	void hotkey_filter()
	{
		driver.findElement(By.tagName("body")).sendKeys("f");

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".headline-date")));

		assertThat(driver.getCurrentUrl()).endsWith("/transactions#modalFilter");
		assertThat(driver.findElement(By.id("modalFilter")).isDisplayed()).isTrue();
	}

	@Test
	void hotkey_search()
	{
		driver.findElement(By.tagName("body")).sendKeys("s");

		assertThat(driver.findElement(By.id("search"))).isEqualTo(driver.switchTo().activeElement());
	}

	@Test
	void hotkey_saveTransaction()
	{
		assumeTrue(OS.isWindows());

		// open transactions page
		driver.get(helper.getUrl() + "/transactions/newTransaction/normal");

		// fill mandatory inputs
		driver.findElement(By.id("transaction-name")).sendKeys("My Transaction");
		driver.findElement(By.id("transaction-amount")).sendKeys("15.00");
		TransactionTestHelper.selectCategoryByName(driver, "sdfdsf");

		WebElement categoryWrapper = driver.findElement(By.className("custom-select"));
		Action seriesOfActions = new Actions(driver)
				.keyDown(categoryWrapper, Keys.CONTROL)
				.sendKeys(categoryWrapper, Keys.ENTER)
				.keyUp(categoryWrapper, Keys.CONTROL)
				.build();
		seriesOfActions.perform();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".headline-date")));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/transactions");

		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSize(2);
	}
}