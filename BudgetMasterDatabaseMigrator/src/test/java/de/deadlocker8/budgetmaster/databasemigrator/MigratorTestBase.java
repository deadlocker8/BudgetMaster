package de.deadlocker8.budgetmaster.databasemigrator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.io.IOException;


@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(classes = {BatchConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class MigratorTestBase
{
	protected static final JobParameters DEFAULT_JOB_PARAMETERS = new JobParametersBuilder().toJobParameters();

	@Autowired
	protected JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	protected JobRepositoryTestUtils jobRepositoryTestUtils;

	@AfterEach
	public void afterEach()
	{
		jobRepositoryTestUtils.removeJobExecutions();
	}

	@Container
	static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>("postgres:14.2")
			.withDatabaseName("budgetmaster-tests-db")
			.withUsername("budgetmaster")
			.withPassword("BudgetMaster");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry)
	{
		registry.add("spring.seconddatasource.url", postgresDB::getJdbcUrl);
		registry.add("spring.seconddatasource.username", postgresDB::getUsername);
		registry.add("spring.seconddatasource.password", postgresDB::getPassword);
	}
}
