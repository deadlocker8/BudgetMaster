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
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.DestinationRepeatingOption;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.DestinationRepeatingOptionRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.end.*;
import de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.modifier.*;
import de.deadlocker8.budgetmaster.databasemigrator.destination.report.DestinationReportColumn;
import de.deadlocker8.budgetmaster.databasemigrator.destination.report.DestinationReportColumnRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.report.DestinationReportSettings;
import de.deadlocker8.budgetmaster.databasemigrator.destination.report.DestinationReportSettingsRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.settings.DestinationSettings;
import de.deadlocker8.budgetmaster.databasemigrator.destination.settings.DestinationSettingsRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.tag.*;
import de.deadlocker8.budgetmaster.databasemigrator.destination.template.DestinationTemplate;
import de.deadlocker8.budgetmaster.databasemigrator.destination.template.DestinationTemplateRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.templateGroup.DestinationTemplateGroup;
import de.deadlocker8.budgetmaster.databasemigrator.destination.templateGroup.DestinationTemplateGroupRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.transaction.DestinationTransaction;
import de.deadlocker8.budgetmaster.databasemigrator.destination.transaction.DestinationTransactionRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.user.DestinationUser;
import de.deadlocker8.budgetmaster.databasemigrator.destination.user.DestinationUserRepository;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericChunkListener;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericJobListener;
import de.deadlocker8.budgetmaster.databasemigrator.listener.GenericStepListener;
import de.deadlocker8.budgetmaster.databasemigrator.steps.GenericDoNothingProcessor;
import de.deadlocker8.budgetmaster.databasemigrator.steps.GenericWriter;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.*;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.RepeatingOptionReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.end.RepeatingEndAfterXTimesReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.end.RepeatingEndDateReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.end.RepeatingEndNeverReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.end.RepeatingEndReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.modifier.RepeatingModifierDaysReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.modifier.RepeatingModifierMonthsReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.modifier.RepeatingModifierReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.repeating.modifier.RepeatingModifierYearsReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.report.ReportColumnReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.report.ReportSettingsReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.tag.TagReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.tag.TemplateTagReader;
import de.deadlocker8.budgetmaster.databasemigrator.steps.reader.tag.TransactionTagReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfiguration.class);

	final JobBuilderFactory jobBuilderFactory;
	final StepBuilderFactory stepBuilderFactory;

	final EntityManager entityManager;

	final DataSource primaryDataSource;

	final DestinationImageRepository destinationImageRepository;
	final DestinationIconRepository destinationIconRepository;
	final DestinationCategoryRepository destinationCategoryRepository;
	final DestinationAccountRepository destinationAccountRepository;
	final DestinationChartRepository destinationChartRepository;
	final DestinationHintRepository destinationHintRepository;

	final DestinationRepeatingEndRepository destinationRepeatingEndRepository;
	final DestinationRepeatingEndAfterXTimesRepository destinationRepeatingEndAfterXTimesRepository;
	final DestinationRepeatingEndDateRepository destinationRepeatingEndDateRepository;
	final DestinationRepeatingEndNeverRepository destinationRepeatingEndNeverRepository;

	final DestinationRepeatingModifierRepository destinationRepeatingModifierRepository;
	final DestinationRepeatingModifierDaysRepository destinationRepeatingModifierDaysRepository;
	final DestinationRepeatingModifierMonthsRepository destinationRepeatingModifierMonthsRepository;
	final DestinationRepeatingModifierYearsRepository destinationRepeatingModifierYearsRepository;

	final DestinationRepeatingOptionRepository destinationRepeatingOptionRepository;

	final DestinationReportColumnRepository destinationReportColumnRepository;
	final DestinationReportSettingsRepository destinationReportSettingsRepository;

	final DestinationSettingsRepository destinationSettingsRepository;

	final DestinationTagRepository destinationTagRepository;
	final DestinationTemplateTagRepository destinationTemplateTagRepository;
	final DestinationTransactionTagRepository destinationTransactionTagRepository;

	final DestinationUserRepository destinationUserRepository;
	final DestinationTransactionRepository destinationTransactionRepository;

	final DestinationTemplateRepository destinationTemplateRepository;
	final DestinationTemplateGroupRepository destinationTemplateGroupRepository;

	@SuppressWarnings("squid:S107")
	public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManager entityManager, DataSource primaryDataSource, DestinationImageRepository destinationImageRepository, DestinationIconRepository destinationIconRepository, DestinationCategoryRepository destinationCategoryRepository, DestinationAccountRepository destinationAccountRepository, DestinationChartRepository destinationChartRepository, DestinationHintRepository destinationHintRepository, DestinationRepeatingEndRepository destinationRepeatingEndRepository, DestinationRepeatingEndAfterXTimesRepository destinationRepeatingEndAfterXTimesRepository, DestinationRepeatingEndDateRepository destinationRepeatingEndDateRepository, DestinationRepeatingEndNeverRepository destinationRepeatingEndNeverRepository, DestinationRepeatingModifierRepository destinationRepeatingModifierRepository, DestinationRepeatingModifierDaysRepository destinationRepeatingModifierDaysRepository, DestinationRepeatingModifierMonthsRepository destinationRepeatingModifierMonthsRepository, DestinationRepeatingModifierYearsRepository destinationRepeatingModifierYearsRepository, DestinationRepeatingOptionRepository destinationRepeatingOptionRepository, DestinationReportColumnRepository destinationReportColumnRepository, DestinationReportSettingsRepository destinationReportSettingsRepository, DestinationSettingsRepository destinationSettingsRepository, DestinationTagRepository destinationTagRepository, DestinationTemplateTagRepository destinationTemplateTagRepository, DestinationTransactionTagRepository destinationTransactionTagRepository, DestinationUserRepository destinationUserRepository, DestinationTransactionRepository destinationTransactionRepository, DestinationTemplateRepository destinationTemplateRepository, DestinationTemplateGroupRepository destinationTemplateGroupRepository)
	{
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.entityManager = entityManager;
		this.primaryDataSource = primaryDataSource;

		this.destinationImageRepository = destinationImageRepository;
		this.destinationIconRepository = destinationIconRepository;
		this.destinationCategoryRepository = destinationCategoryRepository;
		this.destinationAccountRepository = destinationAccountRepository;
		this.destinationChartRepository = destinationChartRepository;
		this.destinationHintRepository = destinationHintRepository;

		this.destinationRepeatingEndRepository = destinationRepeatingEndRepository;
		this.destinationRepeatingEndAfterXTimesRepository = destinationRepeatingEndAfterXTimesRepository;
		this.destinationRepeatingEndDateRepository = destinationRepeatingEndDateRepository;
		this.destinationRepeatingEndNeverRepository = destinationRepeatingEndNeverRepository;

		this.destinationRepeatingModifierRepository = destinationRepeatingModifierRepository;
		this.destinationRepeatingModifierDaysRepository = destinationRepeatingModifierDaysRepository;
		this.destinationRepeatingModifierMonthsRepository = destinationRepeatingModifierMonthsRepository;
		this.destinationRepeatingModifierYearsRepository = destinationRepeatingModifierYearsRepository;

		this.destinationRepeatingOptionRepository = destinationRepeatingOptionRepository;

		this.destinationReportColumnRepository = destinationReportColumnRepository;
		this.destinationReportSettingsRepository = destinationReportSettingsRepository;

		this.destinationSettingsRepository = destinationSettingsRepository;

		this.destinationTagRepository = destinationTagRepository;
		this.destinationTemplateTagRepository = destinationTemplateTagRepository;
		this.destinationTransactionTagRepository = destinationTransactionTagRepository;

		this.destinationUserRepository = destinationUserRepository;
		this.destinationTransactionRepository = destinationTransactionRepository;

		this.destinationTemplateRepository = destinationTemplateRepository;
		this.destinationTemplateGroupRepository = destinationTemplateGroupRepository;
	}

	public void cleanDatabase()
	{
		LOGGER.debug(">>> Cleanup database...");

		// deletion order is important!

		LOGGER.debug("Cleaning tags...");
		destinationTemplateTagRepository.deleteAll();
		destinationTransactionTagRepository.deleteAll();
		destinationTagRepository.deleteAll();

		LOGGER.debug("Cleaning transactions...");
		destinationTransactionRepository.deleteAll();

		LOGGER.debug("Cleaning templates...");
		destinationTemplateRepository.deleteAll();
		LOGGER.debug("Cleaning template groups.");
		destinationTemplateGroupRepository.deleteAll();

		LOGGER.debug("Cleaning categories...");
		destinationCategoryRepository.deleteAll();
		LOGGER.debug("Cleaning users...");
		destinationUserRepository.deleteAll();
		LOGGER.debug("Cleaning accounts...");
		destinationAccountRepository.deleteAll();

		LOGGER.debug("Cleaning report settings...");
		destinationReportColumnRepository.deleteAll();
		destinationReportSettingsRepository.deleteAll();

		LOGGER.debug("Cleaning charts...");
		destinationChartRepository.deleteAll();
		LOGGER.debug("Cleaning hints...");
		destinationHintRepository.deleteAll();

		LOGGER.debug("Cleaning icons...");
		destinationIconRepository.deleteAll();
		LOGGER.debug("Cleaning images...");
		destinationImageRepository.deleteAll();

		LOGGER.debug("Cleaning repeating options...");
		destinationRepeatingOptionRepository.deleteAll();
		destinationRepeatingEndAfterXTimesRepository.deleteAll();
		destinationRepeatingEndDateRepository.deleteAll();
		destinationRepeatingEndNeverRepository.deleteAll();
		destinationRepeatingEndRepository.deleteAll();
		destinationRepeatingModifierDaysRepository.deleteAll();
		destinationRepeatingModifierMonthsRepository.deleteAll();
		destinationRepeatingModifierYearsRepository.deleteAll();
		destinationRepeatingModifierRepository.deleteAll();

		LOGGER.debug("Cleaning settings...");
		destinationSettingsRepository.deleteAll();

		LOGGER.debug(">>> Cleanup database DONE");
	}

	@Bean(name = "migrateJob")
	public Job createMigrateJob()
	{
		return jobBuilderFactory.get("Migrate from h2 to postgresql")
				.incrementer(new RunIdIncrementer())
				.start(createStepForImageMigration())
				.next(createStepForIconMigration())

				.next(createStepForCategoryMigration())
				.next(createStepForAccountMigration())

				.next(createStepForChartMigration())
				.next(createStepForHintMigration())

				// repeating end options
				.next(createStepForRepeatingEndMigration())
				.next(createStepForRepeatingEndAfterXTimesMigration())
				.next(createStepForRepeatingEndDateMigration())
				.next(createStepForRepeatingEndNeverMigration())

				// repeating modifiers
				.next(createStepForRepeatingModifierMigration())
				.next(createStepForRepeatingModifierDaysMigration())
				.next(createStepForRepeatingModifierMonthsMigration())
				.next(createStepForRepeatingModifierYearsMigration())

				.next(createStepForRepeatingOptionMigration())

				.next(createStepForReportSettingsMigration())
				.next(createStepForReportColumnMigration())

				.next(createStepForSettingsMigration())
				.next(createStepForUserMigration())

				.next(createStepForTransactionMigration())

				.next(createStepForTemplateGroupMigration())
				.next(createStepForTemplateMigration())

				.next(createStepForTagMigration())
				.next(createStepForTemplateTagMigration())
				.next(createStepForTransactionTagMigration())

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
				.listener(new GenericStepListener(TableNames.IMAGE, entityManager, destinationImageRepository))
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
				.listener(new GenericStepListener(TableNames.ICON, entityManager, destinationIconRepository))
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
				.listener(new GenericStepListener(TableNames.CATEGORY, entityManager, destinationCategoryRepository))
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
				.listener(new GenericStepListener(TableNames.ACCOUNT, entityManager, destinationAccountRepository))
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
				.listener(new GenericStepListener(TableNames.CHART, entityManager, destinationChartRepository))
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
				.listener(new GenericStepListener(TableNames.HINT, entityManager, destinationHintRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForRepeatingEndMigration()
	{
		return stepBuilderFactory.get(StepNames.REPEATING_ENDS)
				.<DestinationRepeatingEnd, DestinationRepeatingEnd>chunk(1)
				.reader(new RepeatingEndReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationRepeatingEndRepository))
				.listener(new GenericChunkListener(TableNames.REPEATING_END))
				.listener(new GenericStepListener(TableNames.REPEATING_END, entityManager, destinationRepeatingEndRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForRepeatingEndAfterXTimesMigration()
	{
		return stepBuilderFactory.get(StepNames.REPEATING_END_AFTER_X_TIMES)
				.<DestinationRepeatingEndAfterXTimes, DestinationRepeatingEndAfterXTimes>chunk(1)
				.reader(new RepeatingEndAfterXTimesReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationRepeatingEndAfterXTimesRepository))
				.listener(new GenericChunkListener(TableNames.REPEATING_END_AFTER_X_TIMES))
				.listener(new GenericStepListener(TableNames.REPEATING_END_AFTER_X_TIMES, entityManager, destinationRepeatingEndAfterXTimesRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForRepeatingEndDateMigration()
	{
		return stepBuilderFactory.get(StepNames.REPEATING_END_DATE)
				.<DestinationRepeatingEndDate, DestinationRepeatingEndDate>chunk(1)
				.reader(new RepeatingEndDateReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationRepeatingEndDateRepository))
				.listener(new GenericChunkListener(TableNames.REPEATING_END_DATE))
				.listener(new GenericStepListener(TableNames.REPEATING_END_DATE, entityManager, destinationRepeatingEndDateRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForRepeatingEndNeverMigration()
	{
		return stepBuilderFactory.get(StepNames.REPEATING_END_NEVER)
				.<DestinationRepeatingEndNever, DestinationRepeatingEndNever>chunk(1)
				.reader(new RepeatingEndNeverReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationRepeatingEndNeverRepository))
				.listener(new GenericChunkListener(TableNames.REPEATING_END_NEVER))
				.listener(new GenericStepListener(TableNames.REPEATING_END_NEVER, entityManager, destinationRepeatingEndNeverRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForRepeatingModifierMigration()
	{
		return stepBuilderFactory.get(StepNames.REPEATING_MODIFIERS)
				.<DestinationRepeatingModifier, DestinationRepeatingModifier>chunk(1)
				.reader(new RepeatingModifierReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationRepeatingModifierRepository))
				.listener(new GenericChunkListener(TableNames.REPEATING_MODIFIER))
				.listener(new GenericStepListener(TableNames.REPEATING_MODIFIER, entityManager, destinationRepeatingModifierRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForRepeatingModifierDaysMigration()
	{
		return stepBuilderFactory.get(StepNames.REPEATING_MODIFIER_DAYS)
				.<DestinationRepeatingModifierDays, DestinationRepeatingModifierDays>chunk(1)
				.reader(new RepeatingModifierDaysReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationRepeatingModifierDaysRepository))
				.listener(new GenericChunkListener(TableNames.REPEATING_MODIFIER_DAYS))
				.listener(new GenericStepListener(TableNames.REPEATING_MODIFIER_DAYS, entityManager, destinationRepeatingModifierDaysRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForRepeatingModifierMonthsMigration()
	{
		return stepBuilderFactory.get(StepNames.REPEATING_MODIFIER_MONTHS)
				.<DestinationRepeatingModifierMonths, DestinationRepeatingModifierMonths>chunk(1)
				.reader(new RepeatingModifierMonthsReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationRepeatingModifierMonthsRepository))
				.listener(new GenericChunkListener(TableNames.REPEATING_MODIFIER_MONTHS))
				.listener(new GenericStepListener(TableNames.REPEATING_MODIFIER_MONTHS, entityManager, destinationRepeatingModifierMonthsRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForRepeatingModifierYearsMigration()
	{
		return stepBuilderFactory.get(StepNames.REPEATING_MODIFIER_YEARS)
				.<DestinationRepeatingModifierYears, DestinationRepeatingModifierYears>chunk(1)
				.reader(new RepeatingModifierYearsReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationRepeatingModifierYearsRepository))
				.listener(new GenericChunkListener(TableNames.REPEATING_MODIFIER_YEARS))
				.listener(new GenericStepListener(TableNames.REPEATING_MODIFIER_YEARS, entityManager, destinationRepeatingModifierYearsRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForRepeatingOptionMigration()
	{
		return stepBuilderFactory.get(StepNames.REPEATING_OPTIONS)
				.<DestinationRepeatingOption, DestinationRepeatingOption>chunk(1)
				.reader(new RepeatingOptionReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationRepeatingOptionRepository))
				.listener(new GenericChunkListener(TableNames.REPEATING_OPTION))
				.listener(new GenericStepListener(TableNames.REPEATING_OPTION, entityManager, destinationRepeatingOptionRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForReportColumnMigration()
	{
		return stepBuilderFactory.get(StepNames.REPORT_COLUMNS)
				.<DestinationReportColumn, DestinationReportColumn>chunk(1)
				.reader(new ReportColumnReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationReportColumnRepository))
				.listener(new GenericChunkListener(TableNames.REPORT_COLUMN))
				.listener(new GenericStepListener(TableNames.REPORT_COLUMN, entityManager, destinationReportColumnRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForReportSettingsMigration()
	{
		return stepBuilderFactory.get(StepNames.REPORT_SETTINGS)
				.<DestinationReportSettings, DestinationReportSettings>chunk(1)
				.reader(new ReportSettingsReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationReportSettingsRepository))
				.listener(new GenericChunkListener(TableNames.REPORT_SETTINGS))
				.listener(new GenericStepListener(TableNames.REPORT_SETTINGS, entityManager, destinationReportSettingsRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForSettingsMigration()
	{
		return stepBuilderFactory.get(StepNames.SETTINGS)
				.<DestinationSettings, DestinationSettings>chunk(1)
				.reader(new SettingsReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationSettingsRepository))
				.listener(new GenericChunkListener(TableNames.SETTINGS))
				.listener(new GenericStepListener(TableNames.SETTINGS, entityManager, destinationSettingsRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForTagMigration()
	{
		return stepBuilderFactory.get(StepNames.TAGS)
				.<DestinationTag, DestinationTag>chunk(1)
				.reader(new TagReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationTagRepository))
				.listener(new GenericChunkListener(TableNames.TAG))
				.listener(new GenericStepListener(TableNames.TAG, entityManager, destinationTagRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForTemplateTagMigration()
	{
		return stepBuilderFactory.get(StepNames.TEMPLATE_TAGS)
				.<DestinationTemplateTag, DestinationTemplateTag>chunk(1)
				.reader(new TemplateTagReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationTemplateTagRepository))
				.listener(new GenericChunkListener(TableNames.TEMPLATE_TAGS))
				.listener(new GenericStepListener(TableNames.TEMPLATE_TAGS, entityManager, destinationTemplateTagRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForTransactionTagMigration()
	{
		return stepBuilderFactory.get(StepNames.TRANSACTION_TAGS)
				.<DestinationTransactionTag, DestinationTransactionTag>chunk(1)
				.reader(new TransactionTagReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationTransactionTagRepository))
				.listener(new GenericChunkListener(TableNames.TRANSACTION_TAGS))
				.listener(new GenericStepListener(TableNames.TRANSACTION_TAGS, entityManager, destinationTransactionTagRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForUserMigration()
	{
		return stepBuilderFactory.get(StepNames.USER)
				.<DestinationUser, DestinationUser>chunk(1)
				.reader(new UserReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationUserRepository))
				.listener(new GenericChunkListener(TableNames.USER_SOURCE))
				.listener(new GenericStepListener(TableNames.USER_SOURCE, entityManager, destinationUserRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForTransactionMigration()
	{
		return stepBuilderFactory.get(StepNames.TRANSACTIONS)
				.<DestinationTransaction, DestinationTransaction>chunk(1)
				.reader(new TransactionReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationTransactionRepository))
				.listener(new GenericChunkListener(TableNames.TRANSACTION))
				.listener(new GenericStepListener(TableNames.TRANSACTION, entityManager, destinationTransactionRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForTemplateMigration()
	{
		return stepBuilderFactory.get(StepNames.TEMPLATES)
				.<DestinationTemplate, DestinationTemplate>chunk(1)
				.reader(new TemplateReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationTemplateRepository))
				.listener(new GenericChunkListener(TableNames.TEMPLATE))
				.listener(new GenericStepListener(TableNames.TEMPLATE, entityManager, destinationTemplateRepository))
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	public Step createStepForTemplateGroupMigration()
	{
		return stepBuilderFactory.get(StepNames.TEMPLATE_GROUPS)
				.<DestinationTemplateGroup, DestinationTemplateGroup>chunk(1)
				.reader(new TemplateGroupReader(primaryDataSource))
				.processor(new GenericDoNothingProcessor<>())
				.writer(new GenericWriter<>(destinationTemplateGroupRepository))
				.listener(new GenericChunkListener(TableNames.TEMPLATE_GROUP))
				.listener(new GenericStepListener(TableNames.TEMPLATE_GROUP, entityManager, destinationTemplateGroupRepository))
				.allowStartIfComplete(true)
				.build();
	}
}
