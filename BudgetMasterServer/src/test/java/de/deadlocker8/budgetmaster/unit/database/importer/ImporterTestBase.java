package de.deadlocker8.budgetmaster.unit.database.importer;

import de.deadlocker8.budgetmaster.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.EntityManager;
import java.text.MessageFormat;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Testcontainers
abstract class ImporterTestBase
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

	@BeforeEach
	void beforeEach()
	{
		for(String tableName : getTableNamesToResetSequence())
		{
			entityManager.createNativeQuery(MessageFormat.format("ALTER SEQUENCE {0}_id_seq RESTART WITH 1", tableName))
					.executeUpdate();
		}
	}

	abstract List<String> getTableNamesToResetSequence();

	@Autowired
	private EntityManager entityManager;
}