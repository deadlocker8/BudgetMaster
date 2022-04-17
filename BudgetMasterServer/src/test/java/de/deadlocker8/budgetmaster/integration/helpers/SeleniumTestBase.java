package de.deadlocker8.budgetmaster.integration.helpers;

import de.deadlocker8.budgetmaster.BudgetMasterServerMain;
import de.deadlocker8.budgetmaster.TestConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
@SpringBootTest(classes = BudgetMasterServerMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@SeleniumTest
@ActiveProfiles("test")
public abstract class SeleniumTestBase
{
	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	protected WebDriver driver;

	@LocalServerPort
	protected int port;

	private static boolean isDatabaseAlreadyImported = false;

	@Container
	static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>(TestConstants.POSTGRES_VERSION)
			.withDatabaseName("budgetmaster-tests-db")
			.withUsername("budgetmaster")
			.withPassword("BudgetMaster");


	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry)
	{
		registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
		registry.add("spring.datasource.username", postgresDB::getUsername);
		registry.add("spring.datasource.password", postgresDB::getPassword);
	}

	@BeforeAll
	static void beforeAll()
	{
		isDatabaseAlreadyImported = false;
	}

	@BeforeEach
	public void beforeEach()
	{
		if(isDatabaseAlreadyImported)
		{
			runBeforeEachTest();
			return;
		}

		importDatabaseOnce();

		isDatabaseAlreadyImported = true;

		runBeforeEachTest();
	}

	protected abstract void importDatabaseOnce();

	protected void runBeforeEachTest()
	{
	}
}
