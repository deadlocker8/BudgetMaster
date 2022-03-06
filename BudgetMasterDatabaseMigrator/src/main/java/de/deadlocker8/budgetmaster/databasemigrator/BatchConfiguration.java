package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategory;
import de.deadlocker8.budgetmaster.databasemigrator.destination.image.DestinationImage;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericChunkListener;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericStepListener;
import de.deadlocker8.budgetmaster.databasemigrator.source.category.SourceCategory;
import de.deadlocker8.budgetmaster.databasemigrator.source.image.SourceImage;
import de.deadlocker8.budgetmaster.databasemigrator.steps.category.CategoryProcessor;
import de.deadlocker8.budgetmaster.databasemigrator.steps.category.CategoryReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.category.CategoryWriter;
import de.deadlocker8.budgetmaster.databasemigrator.steps.image.ImageProcessor;
import de.deadlocker8.budgetmaster.databasemigrator.steps.image.ImageReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.image.ImageWriter;
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

	final ImageReader imageReader;
	final ImageWriter imageWriter;
	final ImageProcessor imageProcessor;

	final CategoryReader categoryReader;
	final CategoryWriter categoryWriter;
	final CategoryProcessor categoryProcessor;

	public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, JobExplorer jobExplorer, ImageReader imageReader, ImageWriter imageWriter, ImageProcessor imageProcessor, CategoryReader categoryReader, CategoryWriter categoryWriter, CategoryProcessor categoryProcessor)
	{
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.jobExplorer = jobExplorer;
		this.imageReader = imageReader;
		this.imageWriter = imageWriter;
		this.imageProcessor = imageProcessor;
		this.categoryReader = categoryReader;
		this.categoryWriter = categoryWriter;
		this.categoryProcessor = categoryProcessor;
	}

	@Bean
	public Job createJob()
	{
		return jobBuilderFactory.get("Migrate from h2 to postgresql")
				.incrementer(new RunIdIncrementer())
				.start(createStepForImageMigration())
				.next(createStepForCategoryMigration())
				.build();
	}

	@Bean
	public Step createStepForImageMigration()
	{
		return stepBuilderFactory.get("Migrate images")
				.<SourceImage, DestinationImage>chunk(1)
				.reader(imageReader)
				.processor(imageProcessor)
				.writer(imageWriter)
				.listener(new GenericChunkListener("image"))
				.listener(new GenericStepListener("images"))
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
				.listener(new GenericChunkListener("category"))
				.listener(new GenericStepListener("categories"))
				.build();
	}
}
