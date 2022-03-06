package de.deadlocker8.budgetmaster.databasemigrator;

import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategory;
import de.deadlocker8.budgetmaster.databasemigrator.destination.category.DestinationCategoryRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.image.DestinationImage;
import de.deadlocker8.budgetmaster.databasemigrator.destination.image.DestinationImageRepository;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericChunkListener;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericJobListener;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericStepListener;
import de.deadlocker8.budgetmaster.databasemigrator.source.category.SourceCategory;
import de.deadlocker8.budgetmaster.databasemigrator.source.image.SourceImage;
import de.deadlocker8.budgetmaster.databasemigrator.steps.GenericWriter;
import de.deadlocker8.budgetmaster.databasemigrator.steps.category.CategoryProcessor;
import de.deadlocker8.budgetmaster.databasemigrator.steps.category.CategoryReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.image.ImageProcessor;
import de.deadlocker8.budgetmaster.databasemigrator.steps.image.ImageReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration
{
	final JobBuilderFactory jobBuilderFactory;
	final StepBuilderFactory stepBuilderFactory;

	final ImageReader imageReader;
	final ImageProcessor imageProcessor;
	final DestinationImageRepository destinationImageRepository;

	final CategoryReader categoryReader;
	final CategoryProcessor categoryProcessor;
	final DestinationCategoryRepository destinationCategoryRepository;

	public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, ImageReader imageReader, ImageProcessor imageProcessor, DestinationImageRepository destinationImageRepository, CategoryReader categoryReader, CategoryProcessor categoryProcessor, DestinationCategoryRepository destinationCategoryRepository)
	{
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

		this.imageReader = imageReader;
		this.imageProcessor = imageProcessor;
		this.destinationImageRepository = destinationImageRepository;

		this.categoryReader = categoryReader;
		this.destinationCategoryRepository = destinationCategoryRepository;
		this.categoryProcessor = categoryProcessor;
	}

	@Bean
	public Job createJob()
	{
		return jobBuilderFactory.get("Migrate from h2 to postgresql")
				.incrementer(new RunIdIncrementer())
				.start(createStepForImageMigration())
				.next(createStepForCategoryMigration())
				.listener(new GenericJobListener())
				.build();
	}

	@Bean
	public Step createStepForImageMigration()
	{
		return stepBuilderFactory.get("Migrate images")
				.<SourceImage, DestinationImage>chunk(1)
				.reader(imageReader)
				.processor(imageProcessor)
				.writer(new GenericWriter<>(destinationImageRepository))
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
				.writer(new GenericWriter<>(destinationCategoryRepository))
				.listener(new GenericChunkListener("category"))
				.listener(new GenericStepListener("categories"))
				.build();
	}
}
