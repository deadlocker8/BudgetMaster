package de.deadlocker8.budgetmaster.unit.database;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.charts.ChartRepository;
import de.deadlocker8.budgetmaster.charts.ChartService;
import de.deadlocker8.budgetmaster.charts.ChartType;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageRepository;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.services.ImportService;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionBase;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class DatabaseImportTest
{
	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private TagRepository tagRepository;

	@Mock
	private TemplateRepository templateRepository;

	@Mock
	private ChartService chartService;

	@Mock
	private ImageService imageService;

	@InjectMocks
	private ImportService importService;

	@Test
	public void test_updateCategoriesForTransactions()
	{
		Category category1 = new Category("Category1", "#ff0000", CategoryType.CUSTOM);
		category1.setID(3);

		Category category2 = new Category("Category2", "#ff0000", CategoryType.CUSTOM);
		category2.setID(4);

		List<TransactionBase> transactionList = new ArrayList<>();
		Transaction transaction1 = new Transaction();
		transaction1.setName("Test");
		transaction1.setAmount(200);
		transaction1.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction1.setCategory(category1);
		transactionList.add(transaction1);

		Transaction transaction2 = new Transaction();
		transaction2.setName("Test_2");
		transaction2.setAmount(-525);
		transaction2.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction2.setCategory(category2);
		transactionList.add(transaction2);

		List<TransactionBase> updatedTransactions = importService.updateCategoriesForItems(transactionList, 3, 5);
		assertThat(updatedTransactions).hasSize(1);
		assertThat(updatedTransactions.get(0).getCategory()).hasFieldOrPropertyWithValue("ID", 5);
	}

	@Test
	public void test_updateCategoriesForTemplates()
	{
		Category category1 = new Category("Category1", "#ff0000", CategoryType.CUSTOM);
		category1.setID(3);

		Category category2 = new Category("Category2", "#ff0000", CategoryType.CUSTOM);
		category2.setID(4);

		Template template1 = new Template();
		template1.setTemplateName("MyTemplate");
		template1.setCategory(category1);
		template1.setAmount(200);
		template1.setName("Test");
		template1.setTags(new ArrayList<>());

		Template template2 = new Template();
		template2.setTemplateName("MyTemplate_2");
		template2.setCategory(category2);
		template2.setAmount(-525);
		template2.setName("Test_2");
		template2.setTags(new ArrayList<>());

		List<TransactionBase> templateList = new ArrayList<>();
		templateList.add(template1);
		templateList.add(template2);

		List<TransactionBase> updatedTemplates = importService.updateCategoriesForItems(templateList, 3, 5);
		assertThat(updatedTemplates).hasSize(1);
		assertThat(updatedTemplates.get(0).getCategory()).hasFieldOrPropertyWithValue("ID", 5);
	}

	@Test
	public void test_removeAlreadyUpdatedTransactions()
	{
		Category category1 = new Category("Category1", "#ff0000", CategoryType.CUSTOM);
		category1.setID(3);

		Category category2 = new Category("Category2", "#ff0000", CategoryType.CUSTOM);
		category2.setID(4);

		List<Transaction> transactionList = new ArrayList<>();
		Transaction transaction1 = new Transaction();
		transaction1.setName("Test");
		transaction1.setAmount(200);
		transaction1.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction1.setCategory(category1);
		transaction1.setAccount(new Account("Account", AccountType.CUSTOM));
		transactionList.add(transaction1);

		Transaction transaction2 = new Transaction();
		transaction2.setName("Test_2");
		transaction2.setAmount(-525);
		transaction2.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction2.setCategory(category2);
		transaction2.setAccount(new Account("Account", AccountType.CUSTOM));
		transactionList.add(transaction2);

		List<Transaction> alreadyUpdatedTransactions = new ArrayList<>();
		transaction1.setCategory(category2);
		alreadyUpdatedTransactions.add(transaction1);

		transactionList.removeAll(alreadyUpdatedTransactions);
		assertThat(transactionList)
				.hasSize(1)
				.contains(transaction2);
	}

	@Test
	public void test_updateAccountsForTransactions()
	{
		Account account1 = new Account("Account_1", AccountType.CUSTOM);
		account1.setID(2);
		Account account2 = new Account("Account_2", AccountType.CUSTOM);
		account2.setID(3);

		Account destinationAccount = new Account("DestinationAccount_1", AccountType.CUSTOM);
		destinationAccount.setID(5);

		List<TransactionBase> transactionList = new ArrayList<>();
		Transaction transaction1 = new Transaction();
		transaction1.setAccount(account1);
		transaction1.setName("ShouldGoInAccount_1");
		transaction1.setAmount(200);
		transaction1.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transactionList.add(transaction1);

		Transaction transaction2 = new Transaction();
		transaction2.setAccount(account2);
		transaction2.setName("ImPartOfAccount_2");
		transaction2.setAmount(-525);
		transaction2.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transactionList.add(transaction2);

		List<TransactionBase> updatedTransactions = importService.updateAccountsForItems(transactionList, account1.getID(), destinationAccount);
		assertThat(updatedTransactions).hasSize(1);
		assertThat(updatedTransactions.get(0).getAccount().getID()).isEqualTo(5);
	}

	@Test
	public void test_updateTransferAccountsForTransactions()
	{
		Account account1 = new Account("Account_1", AccountType.CUSTOM);
		account1.setID(2);
		Account transferAccount = new Account("TransferAccount", AccountType.CUSTOM);
		transferAccount.setID(3);

		Account destinationAccount = new Account("DestinationAccount_1", AccountType.CUSTOM);
		destinationAccount.setID(5);

		List<TransactionBase> transactionList = new ArrayList<>();
		Transaction transaction = new Transaction();
		transaction.setAccount(account1);
		transaction.setTransferAccount(transferAccount);
		transaction.setName("Whatever");
		transaction.setAmount(-525);
		transaction.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transactionList.add(transaction);

		// expected
		Transaction expectedTransaction = new Transaction();
		expectedTransaction.setAccount(account1);
		expectedTransaction.setTransferAccount(destinationAccount);
		expectedTransaction.setName("Whatever");
		expectedTransaction.setAmount(-525);
		expectedTransaction.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));

		assertThat(importService.updateTransferAccountsForTransactions(transactionList, transferAccount.getID(), destinationAccount))
				.hasSize(1)
				.contains(expectedTransaction);
	}

	@Test
	public void test_updateAccountsForTemplates()
	{
		Account account1 = new Account("Account_1", AccountType.CUSTOM);
		account1.setID(2);
		Account account2 = new Account("Account_2", AccountType.CUSTOM);
		account2.setID(3);

		Account destinationAccount = new Account("DestinationAccount_1", AccountType.CUSTOM);
		destinationAccount.setID(5);

		Template template1 = new Template();
		template1.setAccount(account1);
		template1.setTemplateName("ShouldGoInAccount_1");
		template1.setAmount(200);
		template1.setName("Test");
		template1.setTags(new ArrayList<>());

		Template template2 = new Template();
		template2.setAccount(account2);
		template2.setTemplateName("ImPartOfAccount_2");
		template2.setAmount(-525);
		template2.setName("Test_2");
		template2.setTags(new ArrayList<>());

		List<TransactionBase> templateList = new ArrayList<>();
		templateList.add(template1);
		templateList.add(template2);

		List<TransactionBase> updatedTransactions = importService.updateAccountsForItems(templateList, account1.getID(), destinationAccount);
		assertThat(updatedTransactions).hasSize(1);
		assertThat(updatedTransactions.get(0).getAccount().getID()).isEqualTo(5);
	}

	@Test
	public void test_updateTagsForItem_ExistingTag()
	{
		Account account1 = new Account("Account_1", AccountType.CUSTOM);
		account1.setID(2);

		Tag existingTag = new Tag("ExistingTag");
		existingTag.setID(2);

		Transaction transaction1 = new Transaction();
		transaction1.setAccount(account1);
		transaction1.setName("ShouldGoInAccount_1");
		transaction1.setAmount(200);
		transaction1.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		List<Tag> tags = new ArrayList<>();
		tags.add(existingTag);
		transaction1.setTags(tags);

		Mockito.when(tagRepository.findByName(existingTag.getName())).thenReturn(existingTag);

		importService.updateTagsForItem(transaction1);
		assertThat(transaction1.getTags()).hasSize(1);
		assertThat(transaction1.getTags().get(0))
				.hasFieldOrPropertyWithValue("ID", 2)
				.hasFieldOrPropertyWithValue("name", existingTag.getName());
	}

	@Test
	public void test_updateTagsForItem_NewTag()
	{
		Account account1 = new Account("Account_1", AccountType.CUSTOM);
		account1.setID(2);

		Tag newTag = new Tag("NewTag");
		newTag.setID(5);

		Transaction transaction1 = new Transaction();
		transaction1.setAccount(account1);
		transaction1.setName("ShouldGoInAccount_1");
		transaction1.setAmount(200);
		transaction1.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		List<Tag> tags = new ArrayList<>();
		tags.add(newTag);
		transaction1.setTags(tags);

		Tag savedTag = new Tag("NewTag");
		savedTag.setID(1);
		Mockito.when(tagRepository.save(Mockito.any(Tag.class))).thenReturn(savedTag);
		Mockito.when(tagRepository.findByName(newTag.getName())).thenReturn(null);

		importService.updateTagsForItem(transaction1);
		assertThat(transaction1.getTags()).hasSize(1);
		assertThat(transaction1.getTags().get(0))
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("name", newTag.getName());
	}

	@Test
	public void test_importFullDatabase()
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

		// transactions
		List<Transaction> transactions = new ArrayList<>();
		Transaction transaction1 = new Transaction();
		transaction1.setAccount(sourceAccount1);
		transaction1.setName("ShouldGoInAccount_1");
		transaction1.setAmount(200);
		transaction1.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction1.setTags(tags);
		transactions.add(transaction1);

		Transaction transaction2 = new Transaction();
		transaction2.setAccount(sourceAccount2);
		transaction2.setName("ImPartOfAccount_2");
		transaction2.setAmount(-525);
		transaction2.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction2.setTags(new ArrayList<>());
		transactions.add(transaction2);

		// templates
		Template template1 = new Template();
		template1.setTemplateName("MyTemplate");
		template1.setAmount(1500);
		template1.setAccount(sourceAccount1);
		template1.setName("Transaction from Template");
		List<Tag> tags2 = new ArrayList<>();
		tags2.add(tag1);
		template1.setTags(tags2);

		Template template2 = new Template();
		template2.setTemplateName("MyTemplate2");
		template2.setTags(new ArrayList<>());

		List<Template> templates = new ArrayList<>();
		templates.add(template1);
		templates.add(template2);

		// charts
		Chart chart = new Chart();
		chart.setID(9);
		chart.setName("The best chart");
		chart.setType(ChartType.CUSTOM);
		chart.setVersion(7);
		chart.setScript("/* This list will be dynamically filled with all the transactions between\r\n* the start and and date you select on the \"Show Chart\" page\r\n* and filtered according to your specified filter.\r\n* An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:\r\n* https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts\r\n*/\r\nvar transactionData \u003d [];\r\n\r\n// Prepare your chart settings here (mandatory)\r\nvar plotlyData \u003d [{\r\n    x: [],\r\n    y: [],\r\n    type: \u0027bar\u0027\r\n}];\r\n\r\n// Add your Plotly layout settings here (optional)\r\nvar plotlyLayout \u003d {};\r\n\r\n// Add your Plotly configuration settings here (optional)\r\nvar plotlyConfig \u003d {\r\n    showSendToCloud: false,\r\n    displaylogo: false,\r\n    showLink: false,\r\n    responsive: true\r\n};\r\n\r\n// Don\u0027t touch this line\r\nPlotly.newPlot(\"containerID\", plotlyData, plotlyLayout, plotlyConfig);\r\n");

		// database
		//TODO
		Database database = new Database(new ArrayList<>(), accounts, transactions, templates, List.of(chart), List.of());

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
		Transaction expectedTransaction1 = new Transaction();
		expectedTransaction1.setAccount(destAccount1);
		expectedTransaction1.setName("ShouldGoInAccount_1");
		expectedTransaction1.setAmount(200);
		expectedTransaction1.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		expectedTransaction1.setTags(tags);

		Transaction expectedTransaction2 = new Transaction();
		expectedTransaction2.setAccount(destAccount2);
		expectedTransaction2.setName("ImPartOfAccount_2");
		expectedTransaction2.setAmount(-525);
		expectedTransaction2.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		expectedTransaction2.setTags(new ArrayList<>());

		Template expectedTemplate1 = new Template();
		expectedTemplate1.setTemplateName("MyTemplate");
		expectedTemplate1.setAmount(1500);
		expectedTemplate1.setAccount(destAccount1);
		expectedTemplate1.setName("Transaction from Template");
		List<Tag> expectedTemplateTags = new ArrayList<>();
		expectedTemplateTags.add(tag1);
		expectedTemplate1.setTags(expectedTemplateTags);

		Template expectedTemplate2 = new Template();
		expectedTemplate2.setTemplateName("MyTemplate2");
		expectedTemplate2.setTags(new ArrayList<>());

		// act
		Mockito.when(tagRepository.save(Mockito.any(Tag.class))).thenReturn(tag1);

		Mockito.when(chartService.getHighestUsedID()).thenReturn(8);
		final ChartRepository chartRepositoryMock = Mockito.mock(ChartRepository.class);
		Mockito.when(chartService.getRepository()).thenReturn(chartRepositoryMock);

		importService.importDatabase(database, accountMatchList);
		Database databaseResult = importService.getDatabase();

		// assert
		assertThat(databaseResult.getTransactions())
				.hasSize(2)
				.contains(expectedTransaction1, expectedTransaction2);
		assertThat(databaseResult.getTemplates())
				.hasSize(2)
				.contains(expectedTemplate1, expectedTemplate2);
		assertThat(databaseResult.getCharts())
				.hasSize(1)
				.contains(chart);
	}

	@Test
	public void test_chartId()
	{
		Chart chart = new Chart();
		chart.setID(9);
		chart.setName("The best chart");
		chart.setType(ChartType.CUSTOM);
		chart.setVersion(7);
		chart.setScript("/* This list will be dynamically filled with all the transactions between\r\n* the start and and date you select on the \"Show Chart\" page\r\n* and filtered according to your specified filter.\r\n* An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:\r\n* https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts\r\n*/\r\nvar transactionData \u003d [];\r\n\r\n// Prepare your chart settings here (mandatory)\r\nvar plotlyData \u003d [{\r\n    x: [],\r\n    y: [],\r\n    type: \u0027bar\u0027\r\n}];\r\n\r\n// Add your Plotly layout settings here (optional)\r\nvar plotlyLayout \u003d {};\r\n\r\n// Add your Plotly configuration settings here (optional)\r\nvar plotlyConfig \u003d {\r\n    showSendToCloud: false,\r\n    displaylogo: false,\r\n    showLink: false,\r\n    responsive: true\r\n};\r\n\r\n// Don\u0027t touch this line\r\nPlotly.newPlot(\"containerID\", plotlyData, plotlyLayout, plotlyConfig);\r\n");

		// database
		Database database = new Database(List.of(), List.of(), List.of(), List.of(), List.of(chart), List.of());

		// act
		int highestUsedID = 22;
		Mockito.when(chartService.getHighestUsedID()).thenReturn(highestUsedID);
		final ChartRepository chartRepositoryMock = Mockito.mock(ChartRepository.class);
		Mockito.when(chartService.getRepository()).thenReturn(chartRepositoryMock);

		importService.importDatabase(database, new AccountMatchList(List.of()));
		Database databaseResult = importService.getDatabase();

		// assert
		assertThat(databaseResult.getCharts().get(0))
				.hasFieldOrPropertyWithValue("ID", highestUsedID + 1);
	}

	@Test
	public void test_updateImagesForAccounts()
	{
		Image image1 = new Image(new Byte[0], "png");
		image1.setID(3);

		Image image2 = new Image(new Byte[0], "jpg");
		image2.setID(4);

		Account account1 = new Account("Account_1", AccountType.CUSTOM, image1);
		Account account2 = new Account("Account_2", AccountType.CUSTOM, image2);

		final List<Account> accountList = List.of(account1, account2);

		List<Account> updatedAccounts = importService.updateImagesForAccounts(accountList, 3, 5);
		assertThat(updatedAccounts).hasSize(1);
		final Image icon = updatedAccounts.get(0).getIcon();
		assertThat(icon.getBase64EncodedImage()).isEqualTo("data:image/png;base64,");
		assertThat(icon.getID()).isEqualTo(5);
	}

	@Test
	public void test_importImages_notExisting()
	{
		Image image = new Image(new Byte[0], "png");
		image.setID(3);

		Image newImage = new Image(new Byte[0], "png");
		newImage.setID(5);

		final ImageRepository imageRepositoryMock = Mockito.mock(ImageRepository.class);
		Mockito.when(imageService.getRepository()).thenReturn(imageRepositoryMock);
		Mockito.when(imageRepositoryMock.findById(Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(imageRepositoryMock.save(Mockito.any())).thenReturn(newImage);

		Database database = new Database(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(image));
		importService.importDatabase(database, new AccountMatchList(List.of()));

		Mockito.verify(imageRepositoryMock, Mockito.atLeast(1)).save(image);
	}

	@Test
	public void test_importImages_alreadyExisting()
	{
		Image image = new Image(new Byte[0], "png");
		image.setID(3);

		final ImageRepository imageRepositoryMock = Mockito.mock(ImageRepository.class);
		Mockito.when(imageService.getRepository()).thenReturn(imageRepositoryMock);
		Mockito.when(imageRepositoryMock.findById(Mockito.any())).thenReturn(Optional.of(image));

		Database database = new Database(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(image));
		importService.importDatabase(database, new AccountMatchList(List.of()));

		Mockito.verify(imageRepositoryMock, Mockito.never()).save(image);
	}
}