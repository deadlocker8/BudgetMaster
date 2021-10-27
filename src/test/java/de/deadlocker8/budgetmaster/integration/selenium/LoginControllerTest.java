package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import de.thecodelabs.utils.util.Localization;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

class LoginControllerTest extends SeleniumTestBase
{
	@Test
	void getLoginPage()
	{
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();

		WebElement input = driver.findElement(By.id("login-password"));
		assertThat(input).isNotNull();

		WebElement label = driver.findElement(By.cssSelector(".input-field label"));
		assertThat(label.getText()).isEqualTo(Localization.getString("login.password"));

		WebElement button = driver.findElement(By.tagName("button"));
		assertThat(IntegrationTestHelper.getTextNode(button)).isEqualTo(Localization.getString("login.button"));
	}

	@Test
	void wrongCredentials()
	{
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login("akhjfvbvahsdsa");

		WebElement label = driver.findElement(By.id("loginMessage"));
		assertThat(label.getText()).isEqualTo(Localization.getString("warning.wrong.password"));
	}

	@Test
	void successLogin()
	{
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		WebElement label = driver.findElement(By.id("logo-home"));
		String expected = helper.getUrl() + "/images/Logo_with_text_medium_res.png";
		assertThat(label.getAttribute("src")).isEqualTo(expected);
	}

	@Test
	void successLogin_cookieIsSet()
	{
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logo-home")));

		String dateString = new SimpleDateFormat("dd.MM.yy").format(new Date());
		Cookie expectedCookie = new Cookie("currentDate", dateString, "localhost", "/", null, false, false, "None");
		assertThat(driver.manage().getCookies()).contains(expectedCookie);
	}

	@Test
	void successLogin_cookieShortlyAfterMidnightInSystemTimezone()
	{
		// override system time to setup midnight scenario
		// DateTime.now() will return the time in UTC --> shortly before midnight
		DateTimeUtils.setCurrentMillisFixed(new DateTime(2021, 10, 24, 23, 10, 0).getMillis());

		// system time zone is Berlin --> at least +1 hour --> after midnight
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logo-home")));

		Cookie expectedCookie = new Cookie("currentDate", "25.10.21", "localhost", "/", null, false, false, "None");
		assertThat(driver.manage().getCookies()).contains(expectedCookie);
	}

	@Test
	void logout()
	{
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		WebElement buttonLogout = driver.findElement(By.xpath("//body/ul/li/a[contains(text(), 'Logout')]"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", buttonLogout);
		buttonLogout.click();

		WebElement label = driver.findElement(By.id("loginMessage"));
		assertThat(label.getText()).isEqualTo(Localization.getString("logout.success"));
	}
}