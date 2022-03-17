package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.account.DestinationAccount;
import de.deadlocker8.budgetmaster.databasemigrator.destination.account.DestinationAccountRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategory;
import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategoryRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.icon.DestinationIcon;
import de.deadlocker8.budgetmaster.databasemigrator.destination.icon.DestinationIconRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.image.DestinationImage;
import de.deadlocker8.budgetmaster.databasemigrator.destination.image.DestinationImageRepository;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericChunkListener;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericJobListener;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericStepListener;
import de.deadlocker8.budgetmaster.databasemigrator.steps.GenericDoNothingProcessor;
import de.deadlocker8.budgetmaster.databasemigrator.steps.GenericWriter;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.AccountReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.CategoryReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.IconReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.ImageReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration
{
	final JobBuilderFactory jobBuilderFactory;
	final StepBuilderFactory stepBuilderFactory;

	final DataSource primaryDataSource;

	final DestinationImageRepository destinationImageRepository;
	final DestinationIconRepository destinationIconRepository;
	final DestinationCategoryRepository destinationCategoryRepository;
	final DestinationAccountRepository destinationAccountRepository;

	public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource primaryDataSource, DestinationImageRepository destinationImageRepository, DestinationIconRepository destinationIconRepository, DestinationCategoryRepository destinationCategoryRepository, DestinationAccountRepository destinationAccountRepository)
	{
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.primaryDataSource = primaryDataSource;

		this.destinationImageRepository = destinationImageRepository;
		this.destinationIconRepository = destinationIconRepository;
		this.destinationCategoryRepository = destinationCategoryRepository;
		this.destinationAccountRepository = destinationAccountRepository;
	}

	@Bean(name="migrateJob")
	public Job createJob()
	{
		return jobBuilderFactory.get("Migrate from h2 to postgresql")
				.incrementer(new RunIdIncrementer())
				.start(createStepForImageMigration())
				.next(createStepForIconMigration())
				.next(createStepForCategoryMigration())
				.next(createStepFoAccountMigration())
				.listener(new GenericJobListener())
				.build();
	}

	@Bean
	public Step createStepForImageMigration()
	{
		return stepBuilderFactory.get("Migrate images")
				.<DestinationImage, DestinationImage>chunk(1)
				.reader(new ImageReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationImageRepository))
				.listener(new GenericChunkListener("image"))
				.listener(new GenericStepListener("images"))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForIconMigration()
	{
		return stepBuilderFactory.get("Migrate icons")
				.<DestinationIcon, DestinationIcon>chunk(1)
				.reader(new IconReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationIconRepository))
				.listener(new GenericChunkListener("icon"))
				.listener(new GenericStepListener("icons"))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForCategoryMigration()
	{
		return stepBuilderFactory.get("Migrate categories")
				.<DestinationCategory, DestinationCategory>chunk(1)
				.reader(new CategoryReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationCategoryRepository))
				.listener(new GenericChunkListener("category"))
				.listener(new GenericStepListener("categories"))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepFoAccountMigration()
	{
		return stepBuilderFactory.get("Migrate accounts")
				.<DestinationAccount, DestinationAccount>chunk(1)
				.reader(new AccountReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationAccountRepository))
				.listener(new GenericChunkListener("account"))
				.listener(new GenericStepListener("accounts"))
				.allowStartIfComplete(true)
				.build();
	}
}
