package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategory;
import de.deadlocker8.budgetmaster.databasemigrator.source.category.SourceCategory;
import de.deadlocker8.budgetmaster.databasemigrator.steps.category.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration
{
	final JobBuilderFactory jobBuilderFactory;
	final StepBuilderFactory stepBuilderFactory;
	final JobExplorer jobExplorer;

	final CategoryReader categoryReader;
	final CategoryWriter categoryWriter;
	final CategoryProcessor categoryProcessor;

	public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, JobExplorer jobExplorer, CategoryReader categoryReader, CategoryWriter categoryWriter, CategoryProcessor categoryProcessor)
	{
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.jobExplorer = jobExplorer;
		this.categoryReader = categoryReader;
		this.categoryWriter = categoryWriter;
		this.categoryProcessor = categoryProcessor;
	}

	@Bean
	public Job createJob()
	{
		return jobBuilderFactory.get("Migrate from h2 to postgresql")
				.incrementer(new RunIdIncrementer())
				.flow(createStepForCategoryMigration())
				.end()
				.build();
	}

	@Bean
	public Step createStepForCategoryMigration()
	{
		return stepBuilderFactory.get("Migrate categories")
				.<SourceCategory, DestinationCategory>chunk(1)
				.reader(categoryReader)
				.processor(categoryProcessor)
				.writer(categoryWriter)
				.listener(new CategoryChunkListener())
				.listener(new CategoryStepListener())
				.build();
	}
}
