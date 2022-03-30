package de.deadlocker8.budgetmaster.unit.database;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.charts.ChartRepository;
import de.deadlocker8.budgetmaster.charts.ChartService;
import de.deadlocker8.budgetmaster.charts.ChartType;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconRepository;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.repeating.RepeatingTransactionUpdater;
import de.deadlocker8.budgetmaster.services.ImportService;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class ImportServiceTest
{
	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private TagRepository tagRepository;

	@Mock
	private TemplateGroupRepository templateGroupRepository;

	@Mock
	private TemplateRepository templateRepository;

	@Mock
	private ChartService chartService;

	@Mock
	private ImageService imageService;

	@Mock
	private RepeatingTransactionUpdater repeatingTransactionUpdater;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private IconService iconService;

	@InjectMocks
	private ImportService importService;

	@Test
	void test_importFullDatabase()
	{
		// source accounts
		Account sourceAccount1 = new Account("Source_Account_1", AccountType.CUSTOM);
		sourceAccount1.setID(2);
		Account sourceAccount2 = new Account("Source_Account_2", AccountType.CUSTOM);
		sourceAccount2.setID(3);

		List<Account> accounts = new ArrayList<>();
		accounts.add(sourceAccount1);
		accounts.add(sourceAccount2);

		// destination accounts
		Account destAccount1 = new Account("Destination_Account_1", AccountType.CUSTOM);
		destAccount1.setID(5);
		Account destAccount2 = new Account("Destination_Account_2", AccountType.CUSTOM);
		destAccount2.setID(2);

		// tags
		Tag tag1 = new Tag("Car");
		List<Tag> tags = new ArrayList<>();
		tags.add(tag1);

		// categories
		Category category1 = new Category("Category1", "#ff0000", CategoryType.CUSTOM);
		Category category2 = new Category("Category2", "#00ffff", CategoryType.CUSTOM);
		List<Category> categories = List.of(category1, category2);

		// transactions
		List<Transaction> transactions = new ArrayList<>();
		Transaction transaction1 = new Transaction();
		transaction1.setAccount(sourceAccount1);
		transaction1.setName("ShouldGoInAccount_1");
		transaction1.setAmount(200);
		transaction1.setDate(LocalDate.of(2018, 10, 3));
		transaction1.setTags(tags);
		transaction1.setCategory(category1);
		transactions.add(transaction1);

		Transaction transaction2 = new Transaction();
		transaction2.setAccount(sourceAccount1);
		transaction2.setName("ShouldGoInAccount_1_Too");
		transaction2.setAmount(100);
		transaction2.setDate(LocalDate.of(2018, 10, 3));
		transaction2.setTags(tags);
		transaction2.setCategory(category1);
		transactions.add(transaction2);

		Transaction transaction3 = new Transaction();
		transaction3.setAccount(sourceAccount2);
		transaction3.setName("ImPartOfAccount_2");
		transaction3.setAmount(-525);
		transaction3.setDate(LocalDate.of(2018, 10, 3));
		transaction3.setTags(new ArrayList<>());
		transaction3.setCategory(category2);
		transactions.add(transaction3);

		// template group
		TemplateGroup templateGroup = new TemplateGroup(1, "My Template Group", TemplateGroupType.CUSTOM);

		// templates
		Template template1 = new Template();
		template1.setTemplateName("MyTemplate");
		template1.setAmount(1500);
		template1.setAccount(sourceAccount1);
		template1.setName("Transaction from Template");
		template1.setCategory(category1);
		List<Tag> tags2 = new ArrayList<>();
		tags2.add(tag1);
		template1.setTags(tags2);

		Icon icon1 = new Icon("fas fa-icons");
		icon1.setID(12);
		template1.setIconReference(icon1);

		Template template2 = new Template();
		template2.setTemplateName("MyTemplate2");
		template2.setTransferAccount(sourceAccount2);
		template2.setCategory(category1);
		template2.setTags(new ArrayList<>());

		Template template3 = new Template();
		template3.setTemplateName("MyTemplate3");
		template3.setTransferAccount(sourceAccount1);
		template3.setTags(new ArrayList<>());

		List<Template> templates = new ArrayList<>();
		templates.add(template1);
		templates.add(template2);
		templates.add(template3);

		// charts
		Chart chart = new Chart();
		chart.setID(9);
		chart.setName("The best chart");
		chart.setType(ChartType.CUSTOM);
		chart.setVersion(7);
		chart.setScript("/* This list will be dynamically filled with all the transactions between\r\n* the start and and date you select on the \"Show Chart\" page\r\n* and filtered according to your specified filter.\r\n* An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:\r\n* https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts\r\n*/\r\nvar transactionData \u003d [];\r\n\r\n// Prepare your chart settings here (mandatory)\r\nvar plotlyData \u003d [{\r\n    x: [],\r\n    y: [],\r\n    type: \u0027bar\u0027\r\n}];\r\n\r\n// Add your Plotly layout settings here (optional)\r\nvar plotlyLayout \u003d {};\r\n\r\n// Add your Plotly configuration settings here (optional)\r\nvar plotlyConfig \u003d {\r\n    showSendToCloud: false,\r\n    displaylogo: false,\r\n    showLink: false,\r\n    responsive: true\r\n};\r\n\r\n// Don\u0027t touch this line\r\nPlotly.newPlot(\"containerID\", plotlyData, plotlyLayout, plotlyConfig);\r\n");

		// database
		InternalDatabase database = new InternalDatabase(categories, accounts, transactions, List.of(templateGroup), templates, List.of(chart), List.of(), List.of(icon1));

		// account matches
		AccountMatch match1 = new AccountMatch(sourceAccount1);
		match1.setAccountDestination(destAccount1);

		AccountMatch match2 = new AccountMatch(sourceAccount2);
		match2.setAccountDestination(destAccount2);

		List<AccountMatch> matches = new ArrayList<>();
		matches.add(match1);
		matches.add(match2);

		AccountMatchList accountMatchList = new AccountMatchList(matches);

		// expected
		Category expectedCategory1 = new Category("Category1", "#ff0000", CategoryType.CUSTOM);
		expectedCategory1.setID(1);
		Category expectedCategory2 = new Category("Category2", "#00ffff", CategoryType.CUSTOM);
		expectedCategory2.setID(2);

		Transaction expectedTransaction1 = new Transaction();
		expectedTransaction1.setAccount(destAccount1);
		expectedTransaction1.setName("ShouldGoInAccount_1");
		expectedTransaction1.setAmount(200);
		expectedTransaction1.setDate(LocalDate.of(2018, 10, 3));
		expectedTransaction1.setTags(tags);

		Transaction expectedTransaction2 = new Transaction();
		expectedTransaction2.setAccount(destAccount1);
		expectedTransaction2.setName("ShouldGoInAccount_1_Too");
		expectedTransaction2.setAmount(100);
		expectedTransaction2.setDate(LocalDate.of(2018, 10, 3));
		expectedTransaction2.setTags(tags);

		Transaction expectedTransaction3 = new Transaction();
		expectedTransaction3.setAccount(destAccount2);
		expectedTransaction3.setName("ImPartOfAccount_2");
		expectedTransaction3.setAmount(-525);
		expectedTransaction3.setDate(LocalDate.of(2018, 10, 3));
		expectedTransaction3.setTags(new ArrayList<>());

		Template expectedTemplate1 = new Template();
		expectedTemplate1.setTemplateName("MyTemplate");
		expectedTemplate1.setAmount(1500);
		expectedTemplate1.setAccount(destAccount1);
		expectedTemplate1.setName("Transaction from Template");
		List<Tag> expectedTemplateTags = new ArrayList<>();
		expectedTemplateTags.add(tag1);
		expectedTemplate1.setTags(expectedTemplateTags);

		Icon expectedIcon = new Icon("fas fa-icons");
		expectedIcon.setID(28);
		expectedTemplate1.setIconReference(expectedIcon);

		Template expectedTemplate2 = new Template();
		expectedTemplate2.setTemplateName("MyTemplate2");
		expectedTemplate2.setTransferAccount(destAccount2);
		expectedTemplate2.setTags(new ArrayList<>());

		Template expectedTemplate3 = new Template();
		expectedTemplate3.setTemplateName("MyTemplate3");
		expectedTemplate3.setTransferAccount(destAccount1);
		expectedTemplate3.setTags(new ArrayList<>());

		TemplateGroup expectedTemplateGroup = new TemplateGroup(5, "My Template Group", TemplateGroupType.CUSTOM);

		// act
		Mockito.when(categoryRepository.save(category1)).thenReturn(expectedCategory1);
		Mockito.when(categoryRepository.save(category2)).thenReturn(expectedCategory2);

		Mockito.when(tagRepository.save(Mockito.any(Tag.class))).thenReturn(tag1);

		Mockito.when(chartService.getHighestUsedID()).thenReturn(8);
		final ChartRepository chartRepositoryMock = Mockito.mock(ChartRepository.class);
		Mockito.when(chartService.getRepository()).thenReturn(chartRepositoryMock);

		Mockito.when(accountRepository.findById(5)).thenReturn(Optional.of(destAccount1));
		Mockito.when(accountRepository.findById(2)).thenReturn(Optional.of(destAccount2));

		IconRepository iconRepositoryMock = Mockito.mock(IconRepository.class);
		Mockito.when(iconService.getRepository()).thenReturn(iconRepositoryMock);
		Mockito.when(iconRepositoryMock.save(Mockito.any())).thenReturn(expectedIcon);

		Mockito.when(templateGroupRepository.save(Mockito.any())).thenReturn(expectedTemplateGroup);

		importService.importDatabase(database, accountMatchList, true, true, true);
		InternalDatabase databaseResult = importService.getDatabase();

		// assert
		assertThat(databaseResult.getCategories())
				.hasSize(2)
				.contains(expectedCategory1, expectedCategory2);
		assertThat(databaseResult.getTransactions())
				.hasSize(3)
				.contains(expectedTransaction1, expectedTransaction2, expectedTransaction3);
		assertThat(databaseResult.getTemplateGroups())
				.hasSize(1)
				.contains(templateGroup);
		assertThat(databaseResult.getTemplates())
				.hasSize(3)
				.contains(expectedTemplate1, expectedTemplate2, expectedTemplate3);
		assertThat(databaseResult.getCharts())
				.hasSize(1)
				.contains(chart);
		assertThat(importService.getCollectedErrorMessages()).isEmpty();
	}

	@Test
	void test_skipTemplates()
	{
		Template template = new Template();
		template.setTemplateName("myTemplate");
		template.setAmount(200);
		template.setName("Test");
		template.setTags(new ArrayList<>());

		// database
		InternalDatabase database = new InternalDatabase(List.of(), List.of(), List.of(), List.of(), List.of(template), List.of(), List.of(), List.of());

		// act
		importService.importDatabase(database, new AccountMatchList(List.of()), true, false, true);

		// assert
		Mockito.verify(templateRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	void test_skipTemplateGroups()
	{
		TemplateGroup templateGroup = new TemplateGroup(1, "My Template Group", TemplateGroupType.CUSTOM);

		Template templateWithGroup = new Template();
		templateWithGroup.setTemplateName("myTemplate");
		templateWithGroup.setTags(new ArrayList<>());
		templateWithGroup.setTemplateGroup(templateGroup);

		TemplateGroup templateGroupDefault = new TemplateGroup(1, "Default", TemplateGroupType.DEFAULT);
		Mockito.when(templateGroupRepository.findFirstByType(TemplateGroupType.DEFAULT)).thenReturn(templateGroupDefault);

		// database
		InternalDatabase database = new InternalDatabase(List.of(), List.of(), List.of(), List.of(templateGroup), List.of(templateWithGroup), List.of(), List.of(), List.of());

		// act
		importService.importDatabase(database, new AccountMatchList(List.of()), false, true, true);

		// assert
		Mockito.verify(templateGroupRepository, Mockito.never()).save(Mockito.any());

		Template expectedTemplate = new Template();
		expectedTemplate.setTemplateName("myTemplate");
		expectedTemplate.setTags(new ArrayList<>());
		expectedTemplate.setTemplateGroup(templateGroupDefault);

		Mockito.verify(templateRepository, Mockito.atLeast(1)).save(expectedTemplate);
	}

	@Test
	void test_skipCharts()
	{
		Chart chart = new Chart();
		chart.setID(9);
		chart.setName("The best chart");
		chart.setType(ChartType.CUSTOM);
		chart.setVersion(7);

		// database
		InternalDatabase database = new InternalDatabase(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(chart), List.of(), List.of());

		Mockito.when(chartService.getHighestUsedID()).thenReturn(8);
		final ChartRepository chartRepositoryMock = Mockito.mock(ChartRepository.class);
		Mockito.when(chartService.getRepository()).thenReturn(chartRepositoryMock);

		// act
		importService.importDatabase(database, new AccountMatchList(List.of()), true, true, false);

		// assert
		Mockito.verify(chartRepositoryMock, Mockito.never()).save(Mockito.any());
	}
}