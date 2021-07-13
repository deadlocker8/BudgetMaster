package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTest;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestWatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SeleniumTestWatcher.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SeleniumTest
class FirstUseTest extends SeleniumTestBase
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
	}

	@Test
	void test_firstUserBanner()
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hint-1")));
		assertThat(driver.findElement(By.id("hint-1")).isDisplayed()).isTrue();
	}

	@Test
	void test_firstUserBanner_dismiss()
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hint-1")));

		driver.findElements(By.className("hint-clear")).get(0).click();

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("hint-1")));
		assertThat(driver.findElement(By.id("hint-1")).isDisplayed()).isFalse();
	}

	@Test
	void test_firstUserBanner_click()
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hint-1")));

		driver.findElements(By.className("notification")).get(0).click();

		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "First use guide"));

		assertThat(driver.getCurrentUrl()).endsWith("/firstUse");
	}
}