package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import de.deadlocker8.budgetmaster.integration.helpers.TransactionTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


class AccountTest extends SeleniumTestBase
{
	private static IntegrationTestHelper helper;

	@Override
	protected void importDatabaseOnce()
	{
		helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		String path = Account.class.getClassLoader().getResource("AccountDatabase.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path);
	}

	@AfterEach
	public void afterEach()
	{
		// delete account "zzzz" if existing

		driver.get(helper.getUrl() + "/accounts");

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		for(WebElement row : accountRows)
		{
			final List<WebElement> columns = row.findElements(By.tagName("td"));
			final String name = columns.get(2).getText();
			if(name.equals("zzzz"))
			{
				columns.get(3).findElements(By.tagName("a")).get(1).click();

				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".modal-content h4"), "Delete Account"));

				driver.findElements(By.cssSelector("#deleteModalContainerOnDemand .modal-footer a")).get(1).click();

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("notification-item")));

				return;
			}
		}
	}

	@Test
	void test_newAccount_cancel()
	{
		driver.get(helper.getUrl() + "/accounts");

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		final int numberOfAccountsBefore = accountRows.size();

		driver.findElement(By.id("button-new-account")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Account"));

		// click cancel button
		WebElement cancelButton = driver.findElement(By.id("button-cancel-save-account"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cancelButton);
		cancelButton.click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Accounts"));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/accounts");

		accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		assertThat(accountRows).hasSize(numberOfAccountsBefore);
	}

	@Test
	void test_newAccount()
	{
		driver.get(helper.getUrl() + "/accounts");
		driver.findElement(By.id("button-new-account")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Account"));

		String name = "zzzz";

		// fill form
		driver.findElement(By.id("account-name")).sendKeys(name);
		helper.selectAccountStateByName(AccountState.READ_ONLY);

		// submit form
		driver.findElement(By.id("button-save-account")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Accounts"));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/accounts");

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		assertThat(accountRows).hasSize(6);

		assertAccountColumns(accountRows.get(0).findElements(By.tagName("td")), true, true, AccountState.FULL_ACCESS, "Default Account");
		assertAccountColumns(accountRows.get(1).findElements(By.tagName("td")), true, false, AccountState.FULL_ACCESS, "DefaultAccount0815");
		assertAccountColumns(accountRows.get(2).findElements(By.tagName("td")), false, false, AccountState.HIDDEN, "hidden account");
		assertAccountColumns(accountRows.get(3).findElements(By.tagName("td")), false, false, AccountState.READ_ONLY, "read only account");
		assertAccountColumns(accountRows.get(4).findElements(By.tagName("td")), true, false, AccountState.FULL_ACCESS, "sfsdf");
		assertAccountColumns(accountRows.get(5).findElements(By.tagName("td")), false, false, AccountState.READ_ONLY, name);
	}

	@Test
	void test_newAccount_fallbackIconWithCustomFontColor()
	{
		driver.get(helper.getUrl() + "/accounts");
		driver.findElement(By.id("button-new-account")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Account"));

		final String name = "zzzz";
		final String color = "#FF0000";

		// fill form
		driver.findElement(By.id("account-name")).sendKeys(name);
		selectCustomFontColor(color);

		// submit form
		driver.findElement(By.id("button-save-account")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Accounts"));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/accounts");

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		assertThat(accountRows).hasSize(6);

		final WebElement icon = accountRows.get(5).findElements(By.tagName("td")).get(1);
		assertThat(icon).hasFieldOrPropertyWithValue("text", name.substring(0, 1).toUpperCase());
		assertThat(icon.findElement(By.tagName("span")).getCssValue("color")).isEqualTo("rgb(255, 0, 0)");
	}

	@Test
	void test_newAccount_builtinIconWithCustomFontColor()
	{
		driver.get(helper.getUrl() + "/accounts");
		driver.findElement(By.id("button-new-account")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Account"));

		final String name = "zzzz";
		final String color = "#FF0000";

		// fill form
		driver.findElement(By.id("account-name")).sendKeys(name);

		selectBuiltinIcon(".fas.fa-address-book");
		selectCustomFontColor(color);

		// submit form
		driver.findElement(By.id("button-save-account")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Accounts"));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/accounts");

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		assertThat(accountRows).hasSize(6);

		final WebElement icon = accountRows.get(5).findElements(By.tagName("td")).get(1);
		assertThat(icon.findElement(By.cssSelector("span i")).getAttribute("class")).isEqualTo("fas fa-address-book account-select-icon");
		assertThat(icon.findElement(By.tagName("span")).getCssValue("color")).isEqualTo("rgb(255, 0, 0)");
	}

	@Test
	void test_newAccount_imageIcon()
	{
		driver.get(helper.getUrl() + "/accounts");
		driver.findElement(By.id("button-new-account")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Account"));

		final String name = "zzzz";
		final String color = "#FF0000";

		// fill form
		driver.findElement(By.id("account-name")).sendKeys(name);

		selectImageIcon();
		selectCustomFontColor(color);

		// submit form
		driver.findElement(By.id("button-save-account")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Accounts"));

		// assert
		assertThat(driver.getCurrentUrl()).endsWith("/accounts");

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		assertThat(accountRows).hasSize(6);

		final WebElement icon = accountRows.get(5).findElements(By.tagName("td")).get(1);
		assertThat(icon.findElement(By.cssSelector("span img")).getAttribute("src")).startsWith(helper.getUrl() + "/media/getImageByIconID/");
	}

	private void selectCustomFontColor(String color)
	{
		final WebElement fontColorPicker = driver.findElement(By.cssSelector(".picker_editor input"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fontColorPicker);
		fontColorPicker.click();
		fontColorPicker.sendKeys(color);
	}

	private void selectBuiltinIcon(String iconClasses)
	{
		driver.findElement(By.id("item-icon-preview")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iconTabs")));

		driver.findElement(By.cssSelector(".builtin-icon-option-icon" + iconClasses)).click();

		driver.findElement(By.id("button-icon-confirm")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("iconTabs")));
	}

	private void selectImageIcon()
	{
		driver.findElement(By.id("item-icon-preview")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iconTabs")));

		driver.findElements(By.cssSelector("#iconTabs li")).get(1).click();

		driver.findElement(By.className("item-icon-option")).click();

		driver.findElement(By.id("button-icon-confirm")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("iconTabs")));
	}

	@Test
	void test_edit()
	{
		driver.get(helper.getUrl() + "/accounts/2/edit");

		assertThat(driver.findElement(By.id("account-name")).getAttribute("value")).isEqualTo("Default Account");
		assertThat(driver.findElement(By.cssSelector(".account-state-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo(AccountState.FULL_ACCESS.name());
	}

	@Test
	void test_readOnly_newTransaction_listOnlyReadableAccounts()
	{
		TransactionTestHelper.selectGlobalAccountByName(driver, "sfsdf");

		driver.get(helper.getUrl() + "/transactions");
		driver.findElement(By.id("button-new-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		final By locator = By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		driver.findElement(locator).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// assert
		List<WebElement> items = driver.findElements(By.cssSelector(".account-select-wrapper .custom-select-options .custom-select-item-name"));
		List<String> itemNames = items.stream()
				.map(webElement -> webElement.getAttribute("innerHTML").trim())
				.collect(Collectors.toList());
		assertThat(itemNames).containsExactlyInAnyOrder("Default Account", "DefaultAccount0815", "sfsdf");
	}

	@Test
	void test_readOnly_preventTransactionDeleteAndEdit()
	{
		// select "sfsdf"
		TransactionTestHelper.selectGlobalAccountByName(driver, "sfsdf");

		// open new transaction page
		driver.get(helper.getUrl() + "/transactions");
		driver.findElement(By.id("button-new-transaction")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		final By locator = By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'Transaction')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		driver.findElement(locator).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// fill form
		driver.findElement(By.id("transaction-name")).sendKeys("My transaction");
		driver.findElement(By.id("transaction-amount")).sendKeys("15.00");
		TransactionTestHelper.selectDayInTransactionDatePicker(driver, 20);
		TransactionTestHelper.selectCategoryByName(driver, "sdfdsf");

		// submit form
		driver.findElement(By.id("button-save-transaction")).click();

		// set account as readonly
		setAccountState(4, AccountState.READ_ONLY);

		driver.get(helper.getUrl() + "/transactions");

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Rest')]")));

		// assert
		List<WebElement> transactionsRows = driver.findElements(By.cssSelector(".transaction-container .hide-on-med-and-down.transaction-row-top"));
		assertThat(transactionsRows).hasSize(2);

		final WebElement row = transactionsRows.get(0);
		final List<WebElement> columns = row.findElements(By.className("col"));

		// check columns
		final List<WebElement> icons = columns.get(4).findElements(By.tagName("i"));
		assertThat(icons).hasSize(1);
		assertThat(icons.get(0).getText()).isEqualTo("content_copy");
	}

	@Test
	void test_readOnly_preventNewTransaction()
	{
		TransactionTestHelper.selectGlobalAccountByName(driver, "read only account");

		driver.get(helper.getUrl() + "/transactions");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalFilterTrigger")));

		assertThat(driver.findElements(By.id("button-new-transaction"))).isEmpty();

		// try to open new transaction page
		driver.get(helper.getUrl() + "/transactions/newTransaction/normal");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalFilterTrigger")));

		assertThat(driver.findElement(By.cssSelector(".notification.background-yellow")).isDisplayed()).isTrue();
	}

	public static void assertAccountColumns(List<WebElement> columns, boolean isDefaultIconVisible, boolean isDefaultIconSelected, AccountState expectedAccountState, String name)
	{
		// icons
		final List<WebElement> icons = columns.get(0).findElements(By.tagName("i"));
		int numberOfVisibleIcons = 0;

		if(isDefaultIconVisible)
		{
			final WebElement icon = icons.get(numberOfVisibleIcons);
			assertThat(icon.isDisplayed()).isTrue();
			if(isDefaultIconSelected)
			{
				assertThat(icon).hasFieldOrPropertyWithValue("text", "star");
			}
			else
			{
				assertThat(icon).hasFieldOrPropertyWithValue("text", "star_border");
			}

			numberOfVisibleIcons++;
		}

		final WebElement icon = icons.get(numberOfVisibleIcons);
		assertThat(icon.isDisplayed()).isTrue();
		switch(expectedAccountState)
		{
			case FULL_ACCESS:
				assertThat(icon.getAttribute("class")).contains("fa-edit");
				break;
			case READ_ONLY:
				assertThat(icon.getAttribute("class")).contains("fa-lock");
				break;
			case HIDDEN:
				assertThat(icon.getAttribute("class")).contains("fa-eye-slash");
				break;
		}

		numberOfVisibleIcons++;

		assertThat(icons).hasSize(numberOfVisibleIcons);

		// name
		assertThat(columns.get(2)).hasFieldOrPropertyWithValue("text", name);
	}

	private void setAccountState(int accountID, AccountState accountState)
	{
		driver.get(helper.getUrl() + "/accounts/" + accountID + "/edit");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("account-name")));

		helper.selectAccountStateByName(accountState);

		driver.findElement(By.id("button-save-account")).click();
	}
}