package de.deadlocker8.budgetmaster.integration.helpers;

import de.thecodelabs.utils.util.Localization;
import org.junit.rules.TestName;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
			WebElement buttonCloseReminder = driver.findElement(By.cssSelector("#modalWhatsNew #buttonCloseWhatsNew"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", buttonCloseReminder);
			buttonCloseReminder.click();
		}
		catch(NoSuchElementException ignored)
		{
		}
	}

	public void uploadDatabase(String path, List<String> sourceAccounts, List<String> destinationAccounts)
	{
		if(path.startsWith("\\"))
		{
			path = path.substring(1);
		}

		driver.get(url + "/settings/database/requestImport");

		// upload database
		WebElement input = driver.findElement(By.id("inputDatabaseImport"));
		input.sendKeys(path);

		// confirm upload
		driver.findElement(By.id("button-confirm-database-import")).click();

		// create new accounts
		for(String account : destinationAccounts)
		{
			createAccountOnImport(account);
		}

		// account matching
		matchAccounts(sourceAccounts, destinationAccounts);

		// confirm import
		driver.findElement(By.id("buttonImport")).click();

		assertEquals(Localization.getString("menu.settings"), IntegrationTestHelper.getTextNode(driver.findElement(By.className("headline"))));

		// open transactions page in order to update repeating transactions
		driver.get(url + "/transactions");
		start();
	}

	private void createAccountOnImport(String accountName)
	{
		driver.findElement(By.className("button-new-account")).click();
		WebElement inputAccountName = driver.findElement(By.id("account-name"));
		inputAccountName.sendKeys(accountName);
		driver.findElement(By.tagName("button")).click();
	}

	private void matchAccounts(List<String> sourceAccounts, List<String> destinationAccounts)
	{
		WebElement headlineImport = driver.findElement(By.className("headline"));
		assertEquals(Localization.getString("info.title.database.import.dialog"), IntegrationTestHelper.getTextNode(headlineImport));

		List<WebElement> tableRows = driver.findElements(By.cssSelector(".container form table tr"));
		assertEquals(destinationAccounts.size(), tableRows.size());

		for(int i = 0; i < destinationAccounts.size(); i++)
		{
			String account = destinationAccounts.get(i);

			WebElement row = tableRows.get(i);
			WebElement sourceAccount = row.findElement(By.className("account-source"));
			assertEquals(sourceAccounts.get(i), IntegrationTestHelper.getTextNode(sourceAccount));

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
