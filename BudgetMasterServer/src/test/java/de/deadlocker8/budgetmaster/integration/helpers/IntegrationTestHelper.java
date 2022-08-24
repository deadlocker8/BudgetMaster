package de.deadlocker8.budgetmaster.integration.helpers;

import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.thecodelabs.utils.util.Localization;
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
import java.time.Duration;
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
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalWhatsNew")));

			WebElement buttonCloseReminder = driver.findElement(By.cssSelector("#modalWhatsNew #buttonCloseWhatsNew"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", buttonCloseReminder);
			buttonCloseReminder.click();
		}
		catch(NoSuchElementException | TimeoutException ignored)
		{
		}
	}

	public void hideMigrationDialog()
	{
		try
		{
			WebElement buttonCloseReminder = driver.findElement(By.cssSelector("#modalMigration #buttonCloseMigration"));
			buttonCloseReminder.click();
		}
		catch(NoSuchElementException ignored)
		{
		}
	}

	public void uploadDatabase(String path)
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

		// confirm import
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonImport")));
		final WebElement buttonImport = driver.findElement(By.id("buttonImport"));
		buttonImport.sendKeys("");
		buttonImport.click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("import-entity-name")));

		// close result page
		driver.findElement(By.id("button-finish-import")).click();
		assertThat(IntegrationTestHelper.getTextNode(driver.findElement(By.className("headline"))))
				.isEqualTo(Localization.getString("menu.settings"));

		start();
	}

	public void selectAccountStateByName(AccountState accountState)
	{
		final WebElement accountStateSelect = driver.findElement(By.cssSelector(".account-state-select-wrapper .custom-select"));
		accountStateSelect.click();
		driver.findElements(By.cssSelector(".account-state-select-wrapper .custom-select-item-name")).stream()
				.filter(webElement -> webElement.getText().equals(Localization.getString(accountState.getLocalizationKey())))
				.findFirst().orElseThrow().click();
	}

	public static void saveScreenshots(WebDriver webDriver, String methodName, String className)
	{
		File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);

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
