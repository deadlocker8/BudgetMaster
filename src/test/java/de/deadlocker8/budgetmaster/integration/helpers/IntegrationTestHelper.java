package de.deadlocker8.budgetmaster.integration.helpers;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.thecodelabs.utils.util.Localization;
import org.junit.rules.TestName;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTestHelper
{
	public static String getTextNode(WebElement e)
	{
		String text = e.getText().trim();
		List<WebElement> children = e.findElements(By.xpath("./*"));
		for(WebElement child : children)
		{
			text = text.replaceFirst(child.getText(), "").trim();
		}
		return text;
	}

	private static final String BASE_URL = "http://localhost:";
	private WebDriver driver;
	private String url;

	public IntegrationTestHelper(WebDriver driver, int port)
	{
		this.driver = driver;
		this.url = BASE_URL + port;
	}

	public String getUrl()
	{
		return url;
	}

	public void start()
	{
		driver.get(url);
	}

	public void login(String password)
	{
		WebElement inputPassword = driver.findElement(By.id("login-password"));
		inputPassword.sendKeys(password);
		WebElement buttonLogin = driver.findElement(By.tagName("button"));
		buttonLogin.click();
	}

	public void hideBackupReminder()
	{
		try
		{
			WebElement buttonCloseReminder = driver.findElement(By.cssSelector("#modalBackupReminder #buttonCloseReminder"));
			buttonCloseReminder.click();
		}
		catch(NoSuchElementException ignored)
		{
		}
	}

	public void hideWhatsNewDialog()
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, 2);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalWhatsNew")));

			WebElement buttonCloseReminder = driver.findElement(By.cssSelector("#modalWhatsNew #buttonCloseWhatsNew"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", buttonCloseReminder);
			buttonCloseReminder.click();
		}
		catch(NoSuchElementException | TimeoutException ignored)
		{
		}
	}

	public void uploadDatabase(String path, List<String> sourceAccounts, List<Account> destinationAccounts)
	{
		if(path.startsWith("\\"))
		{
			path = path.substring(1);
		}

		try
		{
			path = URLDecoder.decode(path, StandardCharsets.UTF_8.toString());
		}
		catch(UnsupportedEncodingException ex)
		{
			throw new RuntimeException(ex.getCause());
		}

		driver.get(url + "/settings/database/requestImport");

		// upload database
		WebElement input = driver.findElement(By.id("inputDatabaseImport"));
		input.sendKeys(path);

		// confirm upload
		driver.findElement(By.id("button-confirm-database-import")).click();

		// confirm import step 1
		driver.findElement(By.id("buttonImport")).click();
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("button-new-account")));

		// create new accounts
		for(Account account : destinationAccounts)
		{
			createAccountOnImport(account.getName(), account.getAccountState());
		}

		// account matching
		matchAccounts(sourceAccounts, destinationAccounts);

		// confirm import
		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonImport")));
		final WebElement buttonImport = driver.findElement(By.id("buttonImport"));
		buttonImport.sendKeys("");
		buttonImport.click();

		wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("import-entity-name")));

		// close result page
		driver.findElement(By.id("button-finish-import")).click();
		assertThat(IntegrationTestHelper.getTextNode(driver.findElement(By.className("headline"))))
				.isEqualTo(Localization.getString("menu.settings"));

		start();
	}

	private void createAccountOnImport(String accountName, AccountState accountState)
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("button-new-account")));
		driver.findElement(By.className("button-new-account")).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("account-name")));
		WebElement inputAccountName = driver.findElement(By.id("account-name"));
		inputAccountName.sendKeys(accountName);

		selectAccountStateByName(accountState);

		driver.findElement(By.id("button-save-account")).click();
	}

	public void selectAccountStateByName(AccountState accountState)
	{
		final WebElement accountStateSelect = driver.findElement(By.cssSelector(".account-state-select-wrapper .custom-select"));
		accountStateSelect.click();
		driver.findElements(By.cssSelector(".account-state-select-wrapper .custom-select-item-name")).stream()
				.filter(webElement -> webElement.getText().equals(Localization.getString(accountState.getLocalizationKey())))
				.findFirst().orElseThrow().click();
	}

	private void matchAccounts(List<String> sourceAccounts, List<Account> destinationAccounts)
	{
		WebElement headlineImport = driver.findElement(By.className("headline"));
		assertThat(IntegrationTestHelper.getTextNode(headlineImport))
				.isEqualTo(Localization.getString("info.title.database.import.dialog"));

		List<WebElement> tableRows = driver.findElements(By.cssSelector(".container form table tr"));
		assertThat(tableRows.size()).isEqualTo(destinationAccounts.size());

		for(int i = 0; i < destinationAccounts.size(); i++)
		{
			String account = destinationAccounts.get(i).getName();

			WebElement row = tableRows.get(i);
			WebElement sourceAccount = row.findElement(By.className("account-source"));
			assertThat(IntegrationTestHelper.getTextNode(sourceAccount)).isEqualTo(sourceAccounts.get(i));

			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("select-dropdown")));

			row.findElement(By.className("select-dropdown")).click();
			WebElement accountToSelect = row.findElement(By.xpath("//form/table/tbody/tr[" + (i + 1) + "]/td[5]/div/div/ul/li/span[text()='" + account + "']"));
			accountToSelect.click();
		}
	}

	public static void saveScreenshots(WebDriver webDriver, TestName testName, Class testClass)
	{
		File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);

		String className = testClass.getSimpleName();
		String methodName = testName.getMethodName();
		final Path destination = Paths.get("screenshots", className + "_" + methodName + "_" + screenshot.getName());

		try
		{
			if(Files.notExists(destination.getParent()))
			{
				Files.createDirectories(destination.getParent());
			}

			Files.copy(screenshot.toPath(), destination);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
