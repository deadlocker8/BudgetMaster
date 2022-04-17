package de.deadlocker8.budgetmaster.integration;

import de.deadlocker8.budgetmaster.BudgetMasterServerMain;
import de.deadlocker8.budgetmaster.TestConstants;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.utils.eventlistener.EnsureAllIconizableHaveAnIconInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BudgetMasterServerMain.class)
@ActiveProfiles("test")
@Transactional
@Testcontainers
class EnsureAllIconizableHaveAnIconInstanceTest
{
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

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SettingsService settingsService;

	@Autowired
	private EnsureAllIconizableHaveAnIconInstance ensureAllIconizableHaveAnIconInstance;


	@BeforeEach
	void beforeEach()
	{
		final Account accountWithoutIconReference = new Account("My Account", AccountType.CUSTOM);
		accountRepository.save(accountWithoutIconReference);

		final Category categoryWithoutIconReference = new Category("Car", "#ffcc00", CategoryType.CUSTOM);
		categoryRepository.save(categoryWithoutIconReference);

		final Template templateWithoutIconReference = new Template();
		templateWithoutIconReference.setName("My Template");
		templateRepository.save(templateWithoutIconReference);

		// force older version to trigger update of items without icon references
		final Settings settings = settingsService.getSettings();
		settings.setInstalledVersionCode(EnsureAllIconizableHaveAnIconInstance.ACTIVATION_VERSION_CODE);
		settingsService.updateSettings(settings);
		ensureAllIconizableHaveAnIconInstance.onApplicationEvent(null);
	}

	@Test
	void test_Accounts()
	{
		final List<Account> accounts = accountRepository.findAll();

		assertThat(accounts).hasSize(3);
		assertThat(accounts.stream()
				.allMatch(account -> account.getIconReference() != null))
				.isTrue();
	}

	@Test
	void test_Templates()
	{
		final List<Template> templates = templateRepository.findAll();

		assertThat(templates).hasSize(1);
		assertThat(templates.stream()
				.allMatch(template -> template.getIconReference() != null))
				.isTrue();
	}

	@Test
	void test_Categories()
	{
		final List<Category> categories = categoryRepository.findAll();

		assertThat(categories).hasSize(3);
		assertThat(categories.stream()
				.allMatch(category -> category.getIconReference() != null))
				.isTrue();
	}
}
