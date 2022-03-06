package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MediaTest extends SeleniumTestBase
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

		String path = getClass().getClassLoader().getResource("OnlyAccountsWithDifferentImages.json").getFile().replace("/", File.separator);

		final Account account1 = new Account("account with jpg", AccountType.CUSTOM);
		final Account account2 = new Account("account with svg", AccountType.CUSTOM);
		final Account account3 = new Account("account with png", AccountType.CUSTOM);

		final List<Account> destinationAccounts = List.of(account1, account2, account3);

		helper.uploadDatabase(path, Arrays.asList("account with jpg", "account with svg", "account with png"), destinationAccounts);
	}

	@Test
	void test_imagesAreLoadedCorrectlyForAllSupportedFileExtensions()
	{
		driver.get(helper.getUrl() + "/accounts");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Accounts"));

		List<WebElement> accountRows = driver.findElements(By.cssSelector(".account-container tr"));
		assertThat(accountRows).hasSize(4);

		checkImageLoaded(accountRows.get(0), "JPG");
		checkImageLoaded(accountRows.get(1), "PNG");
		checkImageLoaded(accountRows.get(2), "SVG");
		// the last account ist the default account created on database creation and has no image
	}

	private void checkImageLoaded(WebElement accountRow, String imageType)
	{
		final List<WebElement> columns = accountRow.findElements(By.tagName("td"));
		final WebElement image = columns.get(1).findElement(By.className("account-select-icon"));

		Object result = ((JavascriptExecutor) driver).executeScript(
				"return arguments[0].complete && " +
						"typeof arguments[0].naturalWidth != \"undefined\" && " +
						"arguments[0].naturalWidth > 0", image);

		Boolean imageLoaded = false;
		if(result instanceof Boolean)
		{
			imageLoaded = (Boolean) result;
		}

		assertThat(imageLoaded).as(MessageFormat.format("Image of type {0} is not displayed", imageType))
				.isTrue();
	}
}