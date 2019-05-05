package de.deadlocker8.budgetmaster.integration;

import de.deadlocker8.budgetmaster.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SeleniumTest
@Transactional
public class ImportTest
{
	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private WebDriver driver;

	@LocalServerPort
	int port;

	@Test
	public void requestImport()
	{
		IntegrationTestHelper helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login();
		helper.hideBackupReminder();

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		List<String> sourceAccounts = Arrays.asList("DefaultAccount0815", "sfsdf");
		List<String> destinationAccounts = Arrays.asList("DefaultAccount0815", "Account2");
		helper.uploadDatabase(path, sourceAccounts, destinationAccounts);
	}
}