package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

class ImportTest extends SeleniumTestBase
{
	@Test
	void requestImport()
	{
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		List<String> sourceAccounts = Arrays.asList("DefaultAccount0815", "sfsdf");
		final Account account1 = new Account("DefaultAccount0815", AccountType.CUSTOM);
		final Account account2 = new Account("Account2", AccountType.CUSTOM);
		helper.uploadDatabase(path, sourceAccounts, List.of(account1, account2));
	}
}