package de.deadlocker8.budgetmaster.databasemigrator;

import org.junit.jupiter.api.AfterEach;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@SpringBatchTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ContextConfiguration(classes = {BatchConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EnableJpaRepositories(
		entityManagerFactoryRef = "secondaryEntityManagerFactory",
		transactionManagerRef = "secondaryTransactionManager",
		basePackages = {"de.deadlocker8.budgetmaster.databasemigrator.destination"}
)
@Import(MigratorTestBase.DestinationTestDatabaseConfiguration.class)
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

	@TestConfiguration
	static class DestinationTestDatabaseConfiguration
	{
		@Primary
		@Bean(name = "primaryEntityManagerFactory")
		public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(EntityManagerFactoryBuilder builder,
																				  @Qualifier("primaryDataSource") DataSource primaryDataSource)
		{
			return builder
					.dataSource(primaryDataSource)
					.packages("de.deadlocker8.budgetmaster.databasemigrator.source")
					.build();
		}

		@Bean(name = "primaryTransactionManager")
		public PlatformTransactionManager primaryTransactionManager(
				@Qualifier("primaryEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory)
		{
			return new JpaTransactionManager(primaryEntityManagerFactory);
		}

		@Bean(name = "secondaryDataSource")
		public DataSource secondaryDataSource()
		{
			return DataSourceBuilder.create().username(postgresDB.getUsername()).password(postgresDB.getPassword())
					.url(postgresDB.getJdbcUrl()).driverClassName(postgresDB.getDriverClassName()).build();
		}

		@Bean(name = "secondaryEntityManagerFactory")
		public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(EntityManagerFactoryBuilder builder,
																					@Qualifier("secondaryDataSource") DataSource secondaryDataSource)
		{
			return builder
					.dataSource(secondaryDataSource)
					.packages("de.deadlocker8.budgetmaster.databasemigrator.destination")
					.build();
		}

		@Bean(name = "secondaryTransactionManager")
		public PlatformTransactionManager secondaryTransactionManager(
				@Qualifier("secondaryEntityManagerFactory") EntityManagerFactory secondaryEntityManagerFactory)
		{
			return new JpaTransactionManager(secondaryEntityManagerFactory);
		}
	}
}
