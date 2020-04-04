package de.deadlocker8.budgetmaster.unit.database;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.Database;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.services.ImportService;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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
		template2.setTemplateName("MyTemplate");
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

		List<TransactionBase> updatedTransactions = importService.updateAccountsForTransactions(transactionList, account1.getID(), destinationAccount);
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

		// transactions
		List<Transaction> transactions = new ArrayList<>();
		Transaction transaction1 = new Transaction();
		transaction1.setAccount(sourceAccount1);
		transaction1.setName("ShouldGoInAccount_1");
		transaction1.setAmount(200);
		transaction1.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction1.setTags(new ArrayList<>());
		transactions.add(transaction1);

		Transaction transaction2 = new Transaction();
		transaction2.setAccount(sourceAccount2);
		transaction2.setName("ImPartOfAccount_2");
		transaction2.setAmount(-525);
		transaction2.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction2.setTags(new ArrayList<>());
		transactions.add(transaction2);

		List<Template> templates = new ArrayList<>();
		Template template = new Template();
		template.setTemplateName("MyTemplate");
		template.setAmount(1500);
		template.setName("Transaction from Template");
		templates.add(template);
		template.setTags(new ArrayList<>());

		// database
		Database database = new Database(new ArrayList<>(), accounts, transactions, templates);

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
		expectedTransaction1.setTags(new ArrayList<>());

		Transaction expectedTransaction2 = new Transaction();
		expectedTransaction2.setAccount(destAccount2);
		expectedTransaction2.setName("ImPartOfAccount_2");
		expectedTransaction2.setAmount(-525);
		expectedTransaction2.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		expectedTransaction2.setTags(new ArrayList<>());

		// act
		importService.importDatabase(database, accountMatchList);
		Database databaseResult = importService.getDatabase();

		// assert
		assertThat(databaseResult.getTransactions())
				.hasSize(2)
				.contains(expectedTransaction1, expectedTransaction2);
		assertThat(databaseResult.getTemplates())
				.hasSize(1)
				.contains(template);
	}
}