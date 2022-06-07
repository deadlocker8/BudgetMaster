package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import de.deadlocker8.budgetmaster.integration.helpers.TransactionTestHelper;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TagTest extends SeleniumTestBase
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
		helper.hideMigrationDialog();

		String path = getClass().getClassLoader().getResource("TagWithSingleQuoteTest.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path);
	}

	@Override
	protected void runBeforeEachTest()
	{
		driver.get(helper.getUrl() + "/transactions");

		TransactionTestHelper.selectGlobalAccountByName(driver, "DefaultAccount");
	}

	@Test
	void test_newTransaction_tagWithSingleQuote()
	{
		driver.get(helper.getUrl() + "/transactions/1/edit");

		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("Transaction with tag");

		final List<WebElement> chips = driver.findElements(By.cssSelector("#transaction-chips .chip"));
		assertThat(chips).hasSize(1);
		assertThat(chips.get(0)).hasFieldOrPropertyWithValue("text", "Tag with single ' quote\nclose");
	}
}