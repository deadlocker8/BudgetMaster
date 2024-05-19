package de.deadlocker8.budgetmaster.unit.database;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.charts.*;
import de.deadlocker8.budgetmaster.database.DatabaseParser;
import de.deadlocker8.budgetmaster.database.DatabaseService;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.JSONIdentifier;
import de.deadlocker8.budgetmaster.hints.HintService;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconRepository;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageFileExtension;
import de.deadlocker8.budgetmaster.images.ImageRepository;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagService;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupService;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.templates.TemplateService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.transactions.csvimport.CsvImportSettings;
import de.deadlocker8.budgetmaster.transactions.csvimport.CsvImportSettingsService;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeyword;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeywordRepository;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeywordService;
import de.thecodelabs.utils.util.Localization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class DatabaseExportTest
{
	@BeforeEach
	public void before()
	{
		Localization.setDelegate(new Localization.LocalizationDelegate()
		{
			@Override
			public Locale getLocale()
			{
				return Locale.ENGLISH;
			}

			@Override
			public String getBaseResource()
			{
				return "languages/base";
			}
		});
		Localization.load();
	}

	@Mock
	private AccountService accountService;

	@Mock
	private CategoryService categoryService;

	@Mock
	private TransactionService transactionService;

	@Mock
	private TagService tagService;

	@Mock
	private TemplateGroupService templateGroupService;

	@Mock
	private TemplateService templateService;

	@Mock
	private ChartService chartService;

	@Mock
	private SettingsService settingsService;

	@Mock
	private ImageService imageService;

	@Mock
	private IconService iconService;

	@Mock
	private HintService hintService;

	@Mock
	private TransactionNameKeywordService transactionNameKeywordService;

	@Mock
	private CsvImportSettingsService csvImportSettingsService;

	@InjectMocks
	private DatabaseService databaseService;

	@TempDir
	public Path tempFolder;

	@Test
	void test_specialFields() throws IOException
	{
		// categories
		Mockito.when(categoryService.getAllEntitiesAsc()).thenReturn(List.of());

		// accounts
		AccountRepository accountRepositoryMock = Mockito.mock(AccountRepository.class);
		Mockito.when(accountRepositoryMock.findAll()).thenReturn(List.of());
		Mockito.when(accountService.getRepository()).thenReturn(accountRepositoryMock);

		// transactions
		TransactionRepository transactionRepositoryMock = Mockito.mock(TransactionRepository.class);
		Mockito.when(transactionRepositoryMock.findAll()).thenReturn(List.of());
		Mockito.when(transactionService.getRepository()).thenReturn(transactionRepositoryMock);

		// template groups
		TemplateGroupRepository templateGroupRepositoryMock = Mockito.mock(TemplateGroupRepository.class);
		Mockito.when(templateGroupRepositoryMock.findAll()).thenReturn(List.of());
		Mockito.when(templateGroupService.getRepository()).thenReturn(templateGroupRepositoryMock);

		// templates
		TemplateRepository templateRepositoryMock = Mockito.mock(TemplateRepository.class);
		Mockito.when(templateRepositoryMock.findAll()).thenReturn(List.of());
		Mockito.when(templateService.getRepository()).thenReturn(templateRepositoryMock);

		// charts
		ChartRepository chartRepositoryMock = Mockito.mock(ChartRepository.class);
		Mockito.when(chartRepositoryMock.findAllByType(Mockito.any())).thenReturn(List.of());
		Mockito.when(chartService.getRepository()).thenReturn(chartRepositoryMock);

		// images
		ImageRepository imageRepositoryMock = Mockito.mock(ImageRepository.class);
		Mockito.when(imageRepositoryMock.findAll()).thenReturn(List.of());
		Mockito.when(imageService.getRepository()).thenReturn(imageRepositoryMock);

		// icons
		IconRepository iconRepositoryMock = Mockito.mock(IconRepository.class);
		Mockito.when(iconRepositoryMock.findAll()).thenReturn(List.of());
		Mockito.when(iconService.getRepository()).thenReturn(iconRepositoryMock);

		// transaction name keywords
		TransactionNameKeywordRepository transactionNameKeywordRepository = Mockito.mock(TransactionNameKeywordRepository.class);
		Mockito.when(transactionNameKeywordRepository.findAll()).thenReturn(List.of());
		Mockito.when(transactionNameKeywordService.getRepository()).thenReturn(transactionNameKeywordRepository);

		// csv import settings
		final CsvImportSettings csvImportSettings = CsvImportSettings.getDefault();
		Mockito.when(csvImportSettingsService.getCsvImportSettings()).thenReturn(csvImportSettings);

		// act
		Path exportPath = Files.createFile(tempFolder.resolve("exportTest.json"));
		databaseService.exportDatabase(exportPath);


		// assert
		String fileContent = Files.readString(exportPath, StandardCharsets.UTF_8);
		JsonObject root = JsonParser.parseString(fileContent).getAsJsonObject();

		assertThat(root.get("TYPE").getAsString()).isEqualTo(JSONIdentifier.BUDGETMASTER_DATABASE.toString());
		assertThat(root.get("VERSION").getAsInt()).isEqualTo(DatabaseParser.LATEST_VERSION);
	}

	@Test
	void test_exportDatabase() throws IOException
	{
		// categories
		Category categoryNone = new Category("NONE", "#000000", CategoryType.NONE);
		categoryNone.setID(1);
		Category categoryCustom = new Category("my First Category", "#FF0000", CategoryType.CUSTOM);
		categoryCustom.setID(2);
		Mockito.when(categoryService.getAllEntitiesAsc()).thenReturn(List.of(categoryNone, categoryCustom));

		// accounts
		Account account1 = new Account("Source_Account_1", null, AccountType.CUSTOM);
		account1.setID(2);
		Account account2 = new Account("Source_Account_2", null, AccountType.CUSTOM);
		account2.setID(3);

		AccountRepository accountRepositoryMock = Mockito.mock(AccountRepository.class);
		Mockito.when(accountRepositoryMock.findAll()).thenReturn(List.of(account1, account2));
		Mockito.when(accountService.getRepository()).thenReturn(accountRepositoryMock);

		// tags
		Tag tag1 = new Tag("Car");
		List<Tag> tags = new ArrayList<>();
		tags.add(tag1);

		// transactions
		Transaction transaction1 = new Transaction();
		transaction1.setAccount(account1);
		transaction1.setCategory(categoryNone);
		transaction1.setName("ShouldGoInAccount_1");
		transaction1.setAmount(200);
		transaction1.setIsExpenditure(false);
		transaction1.setDate(LocalDate.of(2018, 10, 3));
		transaction1.setTags(tags);

		Transaction transaction2 = new Transaction();
		transaction2.setAccount(account2);
		transaction2.setCategory(categoryCustom);
		transaction2.setName("ImPartOfAccount_2");
		transaction2.setAmount(-525);
		transaction2.setIsExpenditure(true);
		LocalDate transaction2Date = LocalDate.of(2018, 10, 3);
		transaction2.setDate(transaction2Date);
		transaction2.setTags(new ArrayList<>());
		RepeatingOption repeatingOption = new RepeatingOption(transaction2Date,
				new RepeatingModifierDays(3),
				new RepeatingEndAfterXTimes(3));
		transaction2.setRepeatingOption(repeatingOption);

		TransactionRepository transactionRepositoryMock = Mockito.mock(TransactionRepository.class);
		Mockito.when(transactionRepositoryMock.findAll()).thenReturn(List.of(transaction1, transaction2));
		Mockito.when(transactionService.getRepository()).thenReturn(transactionRepositoryMock);

		// template groups
		TemplateGroup templateGroupDefault = new TemplateGroup(1, "Default", TemplateGroupType.DEFAULT);
		TemplateGroup templateGroup = new TemplateGroup(2, "My Template Group", TemplateGroupType.CUSTOM);

		TemplateGroupRepository templateGroupRepositoryMock = Mockito.mock(TemplateGroupRepository.class);
		Mockito.when(templateGroupRepositoryMock.findAll()).thenReturn(List.of(templateGroupDefault, templateGroup));
		Mockito.when(templateGroupService.getRepository()).thenReturn(templateGroupRepositoryMock);

		// templates
		Template template1 = new Template();
		template1.setTemplateName("MyTemplate with utf8 üüü");
		template1.setAmount(1500);
		template1.setIsExpenditure(false);
		template1.setAccount(account1);
		template1.setName("Transaction from Template");
		List<Tag> tags2 = new ArrayList<>();
		tags2.add(tag1);
		template1.setTags(tags2);
		template1.setTemplateGroup(templateGroup);

		Template template2 = new Template();
		template2.setTemplateName("MyTemplate2");
		template2.setTransferAccount(account2);
		template2.setIsExpenditure(true);
		template2.setTags(new ArrayList<>());
		template2.setTemplateGroup(templateGroupDefault);

		TemplateRepository templateRepositoryMock = Mockito.mock(TemplateRepository.class);
		Mockito.when(templateRepositoryMock.findAll()).thenReturn(List.of(template1, template2));
		Mockito.when(templateService.getRepository()).thenReturn(templateRepositoryMock);

		// charts
		Chart chart = new Chart();
		chart.setID(9);
		chart.setName("The best chart");
		chart.setType(ChartType.CUSTOM);
		chart.setVersion(7);
		chart.setScript("/* This list will be dynamically filled with all the transactions between\r\n* the start and and date you select on the \"Show Chart\" page\r\n* and filtered according to your specified filter.\r\n* An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:\r\n* https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts\r\n*/\r\nvar transactionData \u003d [];\r\n\r\n// Prepare your chart settings here (mandatory)\r\nvar plotlyData \u003d [{\r\n    x: [],\r\n    y: [],\r\n    type: \u0027bar\u0027\r\n}];\r\n\r\n// Add your Plotly layout settings here (optional)\r\nvar plotlyLayout \u003d {};\r\n\r\n// Add your Plotly configuration settings here (optional)\r\nvar plotlyConfig \u003d {\r\n    showSendToCloud: false,\r\n    displaylogo: false,\r\n    showLink: false,\r\n    responsive: true\r\n};\r\n\r\n// Don\u0027t touch this line\r\nPlotly.newPlot(\"containerID\", plotlyData, plotlyLayout, plotlyConfig);\r\n");
		chart.setDisplayType(ChartDisplayType.CUSTOM);
		chart.setGroupType(ChartGroupType.NONE);

		ChartRepository chartRepositoryMock = Mockito.mock(ChartRepository.class);
		Mockito.when(chartRepositoryMock.findAllByType(Mockito.any())).thenReturn(List.of(chart));
		Mockito.when(chartService.getRepository()).thenReturn(chartRepositoryMock);

		// images
		final Image image = new Image(new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);
		image.setID(12);

		ImageRepository imageRepositoryMock = Mockito.mock(ImageRepository.class);
		Mockito.when(imageRepositoryMock.findAll()).thenReturn(List.of(image));
		Mockito.when(imageService.getRepository()).thenReturn(imageRepositoryMock);

		// icons
		final Icon iconImage = new Icon(image);
		iconImage.setID(38);

		final Icon iconBuiltin = new Icon("fas fa-icons");
		iconBuiltin.setID(39);

		IconRepository iconRepositoryMock = Mockito.mock(IconRepository.class);
		Mockito.when(iconRepositoryMock.findAll()).thenReturn(List.of(iconImage, iconBuiltin));
		Mockito.when(iconService.getRepository()).thenReturn(iconRepositoryMock);

		// transaction name keywords
		final TransactionNameKeyword keyword1 = new TransactionNameKeyword(1, "income");
		final TransactionNameKeyword keyword2 = new TransactionNameKeyword(2, "abc");

		TransactionNameKeywordRepository transactionNameKeywordRepository = Mockito.mock(TransactionNameKeywordRepository.class);
		Mockito.when(transactionNameKeywordRepository.findAll()).thenReturn(List.of(keyword1, keyword2));
		Mockito.when(transactionNameKeywordService.getRepository()).thenReturn(transactionNameKeywordRepository);

		// csv import settings
		final CsvImportSettings csvImportSettings = CsvImportSettings.getDefault();
		Mockito.when(csvImportSettingsService.getCsvImportSettings()).thenReturn(csvImportSettings);

		// act
		Path exportPath = Files.createFile(tempFolder.resolve("exportTest.json"));
		databaseService.exportDatabase(exportPath);

		// assert
		String fileContent = Files.readString(exportPath, StandardCharsets.UTF_8);
		DatabaseParser importer = new DatabaseParser(fileContent);
		InternalDatabase importedDatabase = importer.parseDatabaseFromJSON();

		assertThat(importedDatabase.getCategories()).containsExactly(categoryNone, categoryCustom);
		assertThat(importedDatabase.getAccounts()).containsExactly(account1, account2);
		assertThat(importedDatabase.getTransactions()).containsExactly(transaction1, transaction2);
		assertThat(importedDatabase.getTemplateGroups()).containsExactly(templateGroupDefault, templateGroup);
		assertThat(importedDatabase.getTemplates()).containsExactly(template1, template2);
		assertThat(importedDatabase.getCharts()).containsExactly(chart);
		assertThat(importedDatabase.getImages()).containsExactly(image);
		assertThat(importedDatabase.getIcons()).containsExactly(iconImage, iconBuiltin);
		assertThat(importedDatabase.getTransactionNameKeywords()).containsExactly(new TransactionNameKeyword("income"), new TransactionNameKeyword("abc"));
	}
}
