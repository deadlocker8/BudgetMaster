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
class WhatsNewTest extends SeleniumTestBase
{
	private IntegrationTestHelper helper;

	@BeforeEach
	public void prepare()
	{
		helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
	}

	@Test
	void test_whats_new_dialog()
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalWhatsNew")));
		assertThat(driver.findElement(By.id("modalWhatsNew")).isDisplayed()).isTrue();
	}
}