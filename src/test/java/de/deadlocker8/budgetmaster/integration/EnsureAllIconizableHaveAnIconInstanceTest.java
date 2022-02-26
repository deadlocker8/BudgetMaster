package de.deadlocker8.budgetmaster.integration;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTest;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Main.class)
@Import(EnsureAllIconizableHaveAnIconInstanceTest.TestDatabaseConfiguration.class)
@ActiveProfiles("test")
@Transactional
class EnsureAllIconizableHaveAnIconInstanceTest
{
	@TestConfiguration
	static class TestDatabaseConfiguration
	{
		@Value("classpath:missing_icon_instances.mv.db")
		private Resource databaseResource;

		@Bean
		@Primary
		public DataSource dataSource() throws IOException
		{
			final String folderName = databaseResource.getFile().getAbsolutePath().replace(".mv.db", "");
			String jdbcString = "jdbc:h2:/" + folderName + ";DB_CLOSE_ON_EXIT=TRUE";
			return DataSourceBuilder.create().username("sa").password("").url(jdbcString).driverClassName("org.h2.Driver").build();
		}
	}

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	void test_Accounts()
	{
		final List<Account> accounts = accountRepository.findAll();

		assertThat(accounts).hasSize(4);
		assertThat(accounts.stream()
				.allMatch(account -> account.getIconReference() != null))
				.isTrue();
	}

	@Test
	void test_Templates()
	{
		final List<Template> templates = templateRepository.findAll();

		assertThat(templates).hasSize(2);
		assertThat(templates.stream()
				.allMatch(template -> template.getIconReference() != null))
				.isTrue();
	}

	@Test
	void test_Categories()
	{
		final List<Category> categories = categoryRepository.findAll();

		assertThat(categories).hasSize(7);
		assertThat(categories.stream()
				.allMatch(category -> category.getIconReference() != null))
				.isTrue();
	}
}
