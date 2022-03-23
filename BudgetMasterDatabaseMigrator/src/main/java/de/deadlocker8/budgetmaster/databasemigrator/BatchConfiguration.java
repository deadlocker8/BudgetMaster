package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.StepNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import de.deadlocker8.budgetmaster.databasemigrator.destination.account.DestinationAccount;
import de.deadlocker8.budgetmaster.databasemigrator.destination.account.DestinationAccountRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategory;
import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategoryRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.chart.DestinationChart;
import de.deadlocker8.budgetmaster.databasemigrator.destination.chart.DestinationChartRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.hint.DestinationHint;
import de.deadlocker8.budgetmaster.databasemigrator.destination.hint.DestinationHintRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.icon.DestinationIcon;
import de.deadlocker8.budgetmaster.databasemigrator.destination.icon.DestinationIconRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.image.DestinationImage;
import de.deadlocker8.budgetmaster.databasemigrator.destination.image.DestinationImageRepository;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericChunkListener;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericJobListener;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericStepListener;
import de.deadlocker8.budgetmaster.databasemigrator.steps.GenericDoNothingProcessor;
import de.deadlocker8.budgetmaster.databasemigrator.steps.GenericWriter;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.*;
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
	final DestinationChartRepository destinationChartRepository;
	final DestinationHintRepository destinationHintRepository;

	public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource primaryDataSource, DestinationImageRepository destinationImageRepository, DestinationIconRepository destinationIconRepository, DestinationCategoryRepository destinationCategoryRepository, DestinationAccountRepository destinationAccountRepository, DestinationChartRepository destinationChartRepository, DestinationHintRepository destinationHintRepository)
	{
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.primaryDataSource = primaryDataSource;

		this.destinationImageRepository = destinationImageRepository;
		this.destinationIconRepository = destinationIconRepository;
		this.destinationCategoryRepository = destinationCategoryRepository;
		this.destinationAccountRepository = destinationAccountRepository;
		this.destinationChartRepository = destinationChartRepository;
		this.destinationHintRepository = destinationHintRepository;
	}

	@Bean(name = "migrateJob")
	public Job createJob()
	{
		return jobBuilderFactory.get("Migrate from h2 to postgresql")
				.incrementer(new RunIdIncrementer())
				.start(createStepForImageMigration())
				.next(createStepForIconMigration())
				.next(createStepForCategoryMigration())
				.next(createStepForAccountMigration())
				.next(createStepForChartMigration())
				.next(createStepForHintMigration())
				.listener(new GenericJobListener())
				.build();
	}

	@Bean
	public Step createStepForImageMigration()
	{
		return stepBuilderFactory.get(StepNames.IMAGES)
				.<DestinationImage, DestinationImage>chunk(1)
				.reader(new ImageReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationImageRepository))
				.listener(new GenericChunkListener(TableNames.IMAGE))
				.listener(new GenericStepListener(TableNames.IMAGE))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForIconMigration()
	{
		return stepBuilderFactory.get(StepNames.ICONS)
				.<DestinationIcon, DestinationIcon>chunk(1)
				.reader(new IconReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationIconRepository))
				.listener(new GenericChunkListener(TableNames.ICON))
				.listener(new GenericStepListener(TableNames.ICON))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForCategoryMigration()
	{
		return stepBuilderFactory.get(StepNames.CATEGORIES)
				.<DestinationCategory, DestinationCategory>chunk(1)
				.reader(new CategoryReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationCategoryRepository))
				.listener(new GenericChunkListener(TableNames.CATEGORY))
				.listener(new GenericStepListener(TableNames.CATEGORY))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForAccountMigration()
	{
		return stepBuilderFactory.get(StepNames.ACCOUNTS)
				.<DestinationAccount, DestinationAccount>chunk(1)
				.reader(new AccountReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationAccountRepository))
				.listener(new GenericChunkListener(TableNames.ACCOUNT))
				.listener(new GenericStepListener(TableNames.ACCOUNT))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForChartMigration()
	{
		return stepBuilderFactory.get(StepNames.CHARTS)
				.<DestinationChart, DestinationChart>chunk(1)
				.reader(new ChartReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationChartRepository))
				.listener(new GenericChunkListener(TableNames.CHART))
				.listener(new GenericStepListener(TableNames.CHART))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForHintMigration()
	{
		return stepBuilderFactory.get(StepNames.HINTS)
				.<DestinationHint, DestinationHint>chunk(1)
				.reader(new HintReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationHintRepository))
				.listener(new GenericChunkListener(TableNames.HINT))
				.listener(new GenericStepListener(TableNames.HINT))
				.allowStartIfComplete(true)
				.build();
	}
}
