package de.deadlocker8.budgetmaster.unit.database;

import de.deadlocker8.budgetmaster.BudgetMasterServerMain;
import de.deadlocker8.budgetmaster.TestConstants;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.charts.*;
import de.deadlocker8.budgetmaster.database.DatabaseParser;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageFileExtension;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.RepeatingTransactionUpdater;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
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
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BudgetMasterServerMain.class)
@ActiveProfiles("test")
@Transactional
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ImportServiceTest
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
		final List<String> tableNames = List.of("template");
		for(String tableName : tableNames)
		{
			entityManager.createNativeQuery(MessageFormat.format("ALTER SEQUENCE {0}_id_seq RESTART WITH 1", tableName))
					.executeUpdate();
		}
	}

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	@SpyBean
	private TemplateGroupRepository templateGroupRepository;

	@Autowired
	@SpyBean
	private TemplateRepository templateRepository;

	@Autowired
	@SpyBean
	private ChartService chartService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private RepeatingTransactionUpdater repeatingTransactionUpdater;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private IconService iconService;

	@Autowired
	private ImportService importService;

	@Test
	void test_importFullDatabase() throws URISyntaxException, IOException
	{
		final Path jsonPath = Paths.get(getClass().getClassLoader().getResource("ImportServiceTest.json").toURI());
		final String fileContent = Files.readString(jsonPath, StandardCharsets.UTF_8);
		final DatabaseParser parser = new DatabaseParser(fileContent);
		final InternalDatabase importedDatabase = parser.parseDatabaseFromJSON();

		// source accounts
		final Account sourceAccount1 = new Account("Default Account", AccountType.CUSTOM);
		sourceAccount1.setID(2);
		final Account sourceAccount2 = new Account("Read-only account", AccountType.CUSTOM);
		sourceAccount2.setID(6);
		final Account sourceAccount3 = new Account("Second Account", AccountType.CUSTOM);
		sourceAccount3.setID(8);

		Icon iconDestAccount2 = new Icon("fas fa-ambulance");
		iconDestAccount2 = iconService.getRepository().save(iconDestAccount2);

		Icon iconDestAccount3 = new Icon("fas fa-apple");
		iconDestAccount3 = iconService.getRepository().save(iconDestAccount3);

		// destination accounts
		final Account destAccount1 = accountRepository.findByIsDefault(true);
		Account destAccount2 = new Account("Destination_Account_1", AccountType.CUSTOM);
		destAccount2.setAccountState(AccountState.FULL_ACCESS);
		destAccount2.setIconReference(iconDestAccount2);
		destAccount2 = accountRepository.save(destAccount2);
		Account destAccount3 = new Account("Destination_Account_2", AccountType.CUSTOM);
		destAccount3.setAccountState(AccountState.FULL_ACCESS);
		destAccount3.setIconReference(iconDestAccount3);
		destAccount3 = accountRepository.save(destAccount3);

		// account matches
		final AccountMatch match1 = new AccountMatch(sourceAccount1);
		match1.setAccountDestination(destAccount1);

		final AccountMatch match2 = new AccountMatch(sourceAccount2);
		match2.setAccountDestination(destAccount2);

		final AccountMatch match3 = new AccountMatch(sourceAccount3);
		match3.setAccountDestination(destAccount3);

		final List<AccountMatch> matches = List.of(match1, match2, match3);
		final AccountMatchList accountMatchList = new AccountMatchList(matches);

		// act
		final List<ImportResultItem> importResultItems = importService.importDatabase(importedDatabase, accountMatchList, true, true, true);
		final InternalDatabase databaseResult = importService.getDatabase();

		// assert images
		final Image image = new Image(new Byte[0], "BudgetMaster.svg", ImageFileExtension.SVG);
		image.setID(1);
		assertThat(imageService.getRepository().findAll())
				.hasSize(1)
				.containsExactly(image);

		// assert icons
		// icons created for default accounts, categories, ...
		final Icon iconAllAccounts = createIcon(1, "fas fa-landmark", null, null);
		final Icon iconAccountDefault = createIcon(2, null, null, null);
		final Icon iconCategoryNone = createIcon(4, null, null, null);
		final Icon iconCategoryRest = createIcon(5, null, null, null);

		// imported icons
		final Icon iconAccountReadOnly = createIcon(11, "fas fa-ban", "#2eb952ff", null);
		final Icon iconAccountDefaultNew = createIcon(12, null, null, image);
		final Icon iconAccountSecond = createIcon(13, null, "#2e79b9ff", null);
		final Icon iconCategoryCar = createIcon(14, "fas fa-ambulance", null, null);
		final Icon iconCategoryRent = createIcon(15, null, null, image);
		final Icon iconTemplateFull = createIcon(16, "fas fa-battery-three-quarters", "#e34f4fff", null);
		final Icon iconTemplateUngrouped = createIcon(17, null, "#212121ff", null);
		final Icon iconTemplateRandom = createIcon(18, "fas fa-award", "#212121ff", null);
		final Icon iconTemplateWithTags = createIcon(19, null, null, image);
		assertThat(iconService.getRepository().findAll())
				.hasSize(16)
				.containsExactlyInAnyOrder(
						iconAllAccounts,
						iconAccountDefault,
						iconCategoryNone,
						iconCategoryRest,
						createIcon(8, "fas fa-landmark", null, null),
						createIcon(9, null, null, null),
						createIcon(10, null, null, null),
						iconAccountReadOnly,
						iconAccountDefaultNew,
						iconAccountSecond,
						iconCategoryCar,
						iconCategoryRent,
						iconTemplateFull,
						iconTemplateUngrouped,
						iconTemplateRandom,
						iconTemplateWithTags);

		// assert categories
		final Category categoryNone = createCategory(1, "No Category", "#FFFFFF", CategoryType.NONE, iconCategoryNone);
		final Category categoryRest = createCategory(2, "Rest", "#FFFF00", CategoryType.REST, iconCategoryRest);
		final Category categoryCar = createCategory(3, "Car", "#007afa", CategoryType.CUSTOM, iconCategoryCar);
		final Category categoryRent = createCategory(4, "Rent", "#eeeeee", CategoryType.CUSTOM, iconCategoryRent);
		assertThat(categoryRepository.findAll())
				.hasSize(4)
				.containsExactlyInAnyOrder(
						categoryNone,
						categoryRest,
						categoryCar,
						categoryRent);

		// assert accounts
		final Account accountPlaceholder = createAccount(1, "Placeholder", AccountType.ALL, AccountState.FULL_ACCESS, iconAllAccounts, false, false);
		final Account accountDefault = createAccount(2, "Default Account", AccountType.CUSTOM, AccountState.FULL_ACCESS, iconAccountDefaultNew, true, true);
		final Account accountReadOnly = createAccount(3, "Destination_Account_1", AccountType.CUSTOM, AccountState.FULL_ACCESS, iconAccountReadOnly, false, false);
		final Account accountSecond = createAccount(4, "Destination_Account_2", AccountType.CUSTOM, AccountState.FULL_ACCESS, iconAccountSecond, false, false);
		assertThat(accountRepository.findAll())
				.hasSize(4)
				.contains(accountPlaceholder,
						accountReadOnly,
						accountSecond);
		assertThat(accountRepository.getById(accountDefault.getID()))
				.hasFieldOrPropertyWithValue("name", accountDefault.getName())
				.hasFieldOrPropertyWithValue("type", accountDefault.getType())
				.hasFieldOrPropertyWithValue("accountState", accountDefault.getAccountState())
				.hasFieldOrPropertyWithValue("isSelected", accountDefault.isSelected())
				.hasFieldOrPropertyWithValue("isDefault", accountDefault.isDefault())
				.hasFieldOrPropertyWithValue("iconReference", accountDefault.getIconReference());

		// assert template groups
		final TemplateGroup templateGroupDefault = new TemplateGroup(1, "Not grouped", TemplateGroupType.DEFAULT);
		final TemplateGroup templateGroup1 = new TemplateGroup(2, "Enter the group!", TemplateGroupType.CUSTOM);
		final TemplateGroup templateGroup2 = new TemplateGroup(3, "Group 2.0", TemplateGroupType.CUSTOM);
		assertThat(templateGroupRepository.findAll())
				.hasSize(3)
				.containsExactlyInAnyOrder(templateGroupDefault, templateGroup1, templateGroup2);


		// assert charts
		final Chart chart = new Chart("Custom chart", "/* This list will be dynamically filled with all the transactions between\r\n* the start and and date you select on the \"Show Chart\" page\r\n* and filtered according to your specified filter.\r\n* An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:\r\n* https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts\r\n*/\r\nvar transactionData \u003d [];\r\n\r\n// Prepare your chart settings here (mandatory)\r\nvar plotlyData \u003d [{\r\n    x: [],\r\n    y: [],\r\n    type: \u0027bar\u0027\r\n}];\r\n\r\n// Add your Plotly layout settings here (optional)\r\nvar plotlyLayout \u003d {};\r\n\r\n// Add your Plotly configuration settings here (optional)\r\nvar plotlyConfig \u003d {\r\n    showSendToCloud: false,\r\n    displaylogo: false,\r\n    showLink: false,\r\n    responsive: true\r\n};\r\n\r\nconsole.log(\"dummy\");\r\n\r\n// Don\u0027t touch this line\r\nPlotly.newPlot(\"containerID\", plotlyData, plotlyLayout, plotlyConfig);", ChartType.CUSTOM, 0, ChartDisplayType.CUSTOM, ChartGroupType.NONE, null);
		chart.setID(13);
		assertThat(chartService.getRepository().findAll())
				.hasSize(13)
				.contains(chart);

		// assert templates
		final Template templateFull = new Template();
		templateFull.setID(1);
		templateFull.setTemplateName("Full template");
		templateFull.setAmount(-1200);
		templateFull.setIsExpenditure(true);
		templateFull.setAccount(accountDefault);
		templateFull.setCategory(categoryCar);
		templateFull.setName("My awesome transaction");
		templateFull.setDescription("Lorem Ipsum");
		templateFull.setIconReference(iconTemplateFull);
		final Tag tag0815 = new Tag("0815");
		tag0815.setID(1);
		final Tag tag12 = new Tag("12");
		tag12.setID(2);
		final Tag tag13 = new Tag("13");
		tag13.setID(3);
		templateFull.setTags(List.of(tag0815, tag12));
		templateFull.setTransferAccount(accountSecond);
		templateFull.setTemplateGroup(templateGroup2);

		final Template templateWithTags = new Template();
		templateWithTags.setID(2);
		templateWithTags.setIsExpenditure(true);
		templateWithTags.setTemplateName("Template with tags");
		templateWithTags.setCategory(categoryNone);
		templateWithTags.setName("");
		templateWithTags.setDescription("");
		templateWithTags.setIconReference(iconTemplateWithTags);
		templateWithTags.setTags(List.of(tag12, tag13));
		templateWithTags.setTemplateGroup(templateGroup2);

		final Template templateUngrouped = new Template();
		templateUngrouped.setID(3);
		templateUngrouped.setIsExpenditure(true);
		templateUngrouped.setTemplateName("Ungrouped template");
		templateUngrouped.setCategory(categoryNone);
		templateUngrouped.setName("");
		templateUngrouped.setDescription("");
		templateUngrouped.setIconReference(iconTemplateUngrouped);
		templateUngrouped.setTags(List.of());
		templateUngrouped.setTemplateGroup(templateGroupDefault);

		final Template templateRandom = new Template();
		templateRandom.setID(4);
		templateRandom.setAmount(2000);
		templateRandom.setIsExpenditure(false);
		templateRandom.setTemplateName("Random template");
		templateRandom.setCategory(categoryNone);
		templateRandom.setName("");
		templateRandom.setDescription("");
		templateRandom.setIconReference(iconTemplateRandom);
		templateRandom.setTags(List.of());
		templateRandom.setTemplateGroup(templateGroup1);

		assertThat(templateRepository.findAll())
				.hasSize(4)
				.containsExactlyInAnyOrder(templateFull, templateWithTags, templateUngrouped, templateRandom);

		// assert transactions
		final Transaction transactionNormal = new Transaction();
		transactionNormal.setID(1);
		transactionNormal.setAmount(-1100);
		transactionNormal.setIsExpenditure(true);
		transactionNormal.setAccount(accountDefault);
		transactionNormal.setCategory(categoryCar);
		transactionNormal.setName("normal transaction");
		transactionNormal.setDescription("Lorem Ipsum dolor");
		transactionNormal.setTags(List.of(tag0815));
		transactionNormal.setDate(LocalDate.of(2022, 3, 30));
		transactionNormal.setRepeatingOption(null);
		transactionNormal.setTransferAccount(null);

		final Transaction transactionRepeating = new Transaction();
		transactionRepeating.setID(2);
		transactionRepeating.setAmount(-100);
		transactionRepeating.setIsExpenditure(true);
		transactionRepeating.setAccount(accountDefault);
		transactionRepeating.setCategory(categoryNone);
		transactionRepeating.setName("Repeating transaction");
		transactionRepeating.setDescription("");
		transactionRepeating.setTags(List.of());
		final LocalDate transactionRepeatingDate = LocalDate.of(2022, 3, 9);
		transactionRepeating.setDate(transactionRepeatingDate);
		final RepeatingModifierDays repeatingTransactionModifier = new RepeatingModifierDays(1);
		repeatingTransactionModifier.setID(1);
		final RepeatingEndAfterXTimes repeatingTransactionEndOption = new RepeatingEndAfterXTimes(3);
		repeatingTransactionEndOption.setID(1);
		final RepeatingOption transactionRepeatingOption = new RepeatingOption(transactionRepeatingDate, repeatingTransactionModifier, repeatingTransactionEndOption);
		transactionRepeatingOption.setID(1);
		transactionRepeating.setRepeatingOption(transactionRepeatingOption);
		transactionRepeating.setTransferAccount(null);

		final Transaction transactionTransfer = new Transaction();
		transactionTransfer.setID(3);
		transactionTransfer.setAmount(-1600);
		transactionTransfer.setIsExpenditure(true);
		transactionTransfer.setAccount(accountDefault);
		transactionTransfer.setCategory(categoryRent);
		transactionTransfer.setName("Transfer");
		transactionTransfer.setDescription("");
		transactionTransfer.setTags(List.of(tag12));
		transactionTransfer.setDate(LocalDate.of(2022, 3, 30));
		transactionTransfer.setRepeatingOption(null);
		transactionTransfer.setTransferAccount(accountSecond);

		final Transaction transactionRepeatingTransfer = new Transaction();
		transactionRepeatingTransfer.setID(4);
		transactionRepeatingTransfer.setAmount(-200);
		transactionRepeatingTransfer.setIsExpenditure(true);
		transactionRepeatingTransfer.setAccount(accountDefault);
		transactionRepeatingTransfer.setCategory(categoryCar);
		transactionRepeatingTransfer.setName("Repeating transfer");
		transactionRepeatingTransfer.setDescription("");
		transactionRepeatingTransfer.setTags(List.of());
		final LocalDate transactionRepeatingTransferDate = LocalDate.of(2022, 3, 11);
		transactionRepeatingTransfer.setDate(transactionRepeatingTransferDate);
		final RepeatingModifierDays repeatingTransferModifier = new RepeatingModifierDays(1);
		repeatingTransferModifier.setID(2);
		final RepeatingEndAfterXTimes repeatingTransferEndOption = new RepeatingEndAfterXTimes(2);
		repeatingTransferEndOption.setID(2);
		final RepeatingOption transactionRepeatingTransferOption = new RepeatingOption(transactionRepeatingTransferDate, repeatingTransferModifier, repeatingTransferEndOption);
		transactionRepeatingTransferOption.setID(2);
		transactionRepeatingTransfer.setRepeatingOption(transactionRepeatingTransferOption);
		transactionRepeatingTransfer.setTransferAccount(accountSecond);

		final Transaction transactionIncomeWithTags = new Transaction();
		transactionIncomeWithTags.setID(5);
		transactionIncomeWithTags.setAmount(2036);
		transactionIncomeWithTags.setIsExpenditure(false);
		transactionIncomeWithTags.setAccount(accountDefault);
		transactionIncomeWithTags.setCategory(categoryNone);
		transactionIncomeWithTags.setName("income with tags");
		transactionIncomeWithTags.setDescription("");
		transactionIncomeWithTags.setTags(List.of(tag12, tag13));
		transactionIncomeWithTags.setDate(LocalDate.of(2022, 3, 29));
		transactionIncomeWithTags.setRepeatingOption(null);
		transactionIncomeWithTags.setTransferAccount(null);

		assertThat(databaseResult.getTransactions())
				.hasSize(5)
				.contains(transactionNormal,
						transactionRepeating,
						transactionTransfer,
						transactionRepeatingTransfer,
						transactionIncomeWithTags);

		assertThat(importService.getCollectedErrorMessages(importResultItems)).isEmpty();
	}

	private Icon createIcon(int ID, String builtinIdentifier, String fontColor, Image image)
	{
		Icon icon;
		if(image == null)
		{
			icon = new Icon(builtinIdentifier, fontColor);
		}
		else
		{
			icon = new Icon(image);
		}

		icon.setID(ID);
		return icon;
	}

	private Category createCategory(int ID, String name, String color, CategoryType type, Icon icon)
	{
		final Category category = new Category(name, color, type, icon);
		category.setID(ID);
		return category;
	}

	private Account createAccount(int ID, String name, AccountType type, AccountState state, Icon icon, boolean isSelected, boolean isDefault)
	{
		final Account account = new Account(name, type, icon);
		account.setID(ID);
		account.setAccountState(state);
		account.setSelected(isSelected);
		account.setDefault(isDefault);
		return account;
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
		final TemplateGroup defaultGroup = new TemplateGroup();
		defaultGroup.setID(1);
		defaultGroup.setName(Localization.getString(Strings.TEMPLATE_GROUP_DEFAULT));
		defaultGroup.setType(TemplateGroupType.DEFAULT);

		Template expectedTemplate = new Template();
		expectedTemplate.setID(1);
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