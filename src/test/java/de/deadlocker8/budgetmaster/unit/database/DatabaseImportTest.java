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
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class DatabaseImportTest
{
	@TestConfiguration
	static class DatabaseImportTestConfiguration
	{
		@Bean
		public CategoryRepository getCategoryRepository()
		{
			return new CategoryRepository()
			{
				@Override
				public List<Category> findAllByOrderByNameAsc()
				{
					return null;
				}

				@Override
				public Category findByName(String name)
				{
					return null;
				}

				@Override
				public Category findByType(CategoryType categoryType)
				{
					return null;
				}

				@Override
				public Category findByNameAndColorAndType(String name, String color, CategoryType categoryType)
				{
					return null;
				}

				@Override
				public List<Category> findAll()
				{
					return null;
				}

				@Override
				public List<Category> findAll(Sort sort)
				{
					return null;
				}

				@Override
				public List<Category> findAllById(Iterable<Integer> iterable)
				{
					return null;
				}

				@Override
				public <S extends Category> List<S> saveAll(Iterable<S> iterable)
				{
					return null;
				}

				@Override
				public Optional<Category> findById(Integer integer)
				{
					return Optional.empty();
				}

				@Override
				public boolean existsById(Integer integer)
				{
					return false;
				}

				@Override
				public void flush()
				{

				}

				@Override
				public <S extends Category> S saveAndFlush(S s)
				{
					return null;
				}

				@Override
				public void deleteInBatch(Iterable<Category> iterable)
				{

				}

				@Override
				public void deleteAllInBatch()
				{

				}

				@Override
				public Category getOne(Integer integer)
				{
					return null;
				}

				@Override
				public <S extends Category> Optional<S> findOne(Example<S> example)
				{
					return Optional.empty();
				}

				@Override
				public <S extends Category> List<S> findAll(Example<S> example)
				{
					return null;
				}

				@Override
				public <S extends Category> List<S> findAll(Example<S> example, Sort sort)
				{
					return null;
				}

				@Override
				public Page<Category> findAll(Pageable pageable)
				{
					return null;
				}

				@Override
				public <S extends Category> S save(S s)
				{
					return null;
				}

				@Override
				public long count()
				{
					return 0;
				}

				@Override
				public void deleteById(Integer integer)
				{

				}

				@Override
				public void delete(Category category)
				{

				}

				@Override
				public void deleteAll(Iterable<? extends Category> iterable)
				{
				}

				@Override
				public void deleteAll()
				{

				}

				@Override
				public <S extends Category> Page<S> findAll(Example<S> example, Pageable pageable)
				{
					return null;
				}

				@Override
				public <S extends Category> long count(Example<S> example)
				{
					return 0;
				}

				@Override
				public <S extends Category> boolean exists(Example<S> example)
				{
					return false;
				}
			};
		}

		@Bean
		public TransactionRepository getTransactionRepository()
		{
			return new TransactionRepository()
			{
				@Override
				public Optional<Transaction> findOne(Specification<Transaction> specification)
				{
					return Optional.empty();
				}

				@Override
				public List<Transaction> findAll(Specification<Transaction> specification)
				{
					return null;
				}

				@Override
				public Long countByCategory(Category category)
				{
					return null;
				}

				@Override
				public Page<Transaction> findAll(Specification<Transaction> specification, Pageable pageable)
				{
					return null;
				}

				@Override
				public List<Transaction> findAll(Specification<Transaction> specification, Sort sort)
				{
					return null;
				}

				@Override
				public long count(Specification<Transaction> specification)
				{
					return 0;
				}

				@Override
				public List<Transaction> findAllByAccountAndDateBetweenOrderByDateDesc(Account account, DateTime startDate, DateTime endDate)
				{
					return null;
				}

				@Override
				public List<Transaction> findAllByDateBetweenOrderByDateDesc(DateTime startDate, DateTime endDate)
				{
					return null;
				}

				@Override
				public List<Transaction> findAllByAccount(Account account)
				{
					return null;
				}

				@Override
				public List<Transaction> findAllByTagsContaining(Tag tag)
				{
					return null;
				}

				@Override
				public Integer getRestForNormalAndRepeating(int accountID, String startDate, String endDate)
				{
					return null;
				}

				@Override
				public Integer getRestForTransferSource(int accountID, String startDate, String endDate)
				{
					return null;
				}

				@Override
				public Integer getRestForTransferDestination(int accountID, String startDate, String endDate)
				{
					return null;
				}

				@Override
				public List<Transaction> findAllByTransferAccount(Account account)
				{
					return null;
				}

				@Override
				public List<Transaction> findAll()
				{
					return null;
				}

				@Override
				public List<Transaction> findAll(Sort sort)
				{
					return null;
				}

				@Override
				public List<Transaction> findAllById(Iterable<Integer> iterable)
				{
					return null;
				}

				@Override
				public <S extends Transaction> List<S> saveAll(Iterable<S> iterable)
				{
					return null;
				}

				@Override
				public Optional<Transaction> findById(Integer integer)
				{
					return Optional.empty();
				}

				@Override
				public boolean existsById(Integer integer)
				{
					return false;
				}

				@Override
				public void flush()
				{

				}

				@Override
				public <S extends Transaction> S saveAndFlush(S s)
				{
					return null;
				}

				@Override
				public void deleteInBatch(Iterable<Transaction> iterable)
				{

				}

				@Override
				public void deleteAllInBatch()
				{

				}

				@Override
				public Transaction getOne(Integer integer)
				{
					return null;
				}

				@Override
				public <S extends Transaction> Optional<S> findOne(Example<S> example)
				{
					return Optional.empty();
				}

				@Override
				public <S extends Transaction> List<S> findAll(Example<S> example)
				{
					return null;
				}

				@Override
				public <S extends Transaction> List<S> findAll(Example<S> example, Sort sort)
				{
					return null;
				}

				@Override
				public Page<Transaction> findAll(Pageable pageable)
				{
					return null;
				}

				@Override
				public <S extends Transaction> S save(S s)
				{
					return null;
				}

				@Override
				public long count()
				{
					return 0;
				}

				@Override
				public void deleteById(Integer integer)
				{

				}

				@Override
				public void delete(Transaction transaction)
				{

				}

				@Override
				public void deleteAll(Iterable<? extends Transaction> iterable)
				{

				}

				@Override
				public void deleteAll()
				{

				}

				@Override
				public <S extends Transaction> Page<S> findAll(Example<S> example, Pageable pageable)
				{
					return null;
				}

				@Override
				public <S extends Transaction> long count(Example<S> example)
				{
					return 0;
				}

				@Override
				public <S extends Transaction> boolean exists(Example<S> example)
				{
					return false;
				}
			};
		}

		@Bean
		public TagRepository getTagRepository()
		{
			return new TagRepository()
			{
				@Override
				public Tag findByName(String name)
				{
					return null;
				}

				@Override
				public List<Tag> findAllByOrderByNameAsc()
				{
					return null;
				}

				@Override
				public List<Tag> findAll()
				{
					return null;
				}

				@Override
				public List<Tag> findAll(Sort sort)
				{
					return null;
				}

				@Override
				public List<Tag> findAllById(Iterable<Integer> iterable)
				{
					return null;
				}

				@Override
				public <S extends Tag> List<S> saveAll(Iterable<S> iterable)
				{
					return null;
				}

				@Override
				public Optional<Tag> findById(Integer integer)
				{
					return Optional.empty();
				}

				@Override
				public boolean existsById(Integer integer)
				{
					return false;
				}

				@Override
				public void flush()
				{

				}

				@Override
				public <S extends Tag> S saveAndFlush(S s)
				{
					return null;
				}

				@Override
				public void deleteInBatch(Iterable<Tag> iterable)
				{

				}

				@Override
				public void deleteAllInBatch()
				{

				}

				@Override
				public Tag getOne(Integer integer)
				{
					return null;
				}

				@Override
				public <S extends Tag> Optional<S> findOne(Example<S> example)
				{
					return Optional.empty();
				}

				@Override
				public <S extends Tag> List<S> findAll(Example<S> example)
				{
					return null;
				}

				@Override
				public <S extends Tag> List<S> findAll(Example<S> example, Sort sort)
				{
					return null;
				}

				@Override
				public Page<Tag> findAll(Pageable pageable)
				{
					return null;
				}

				@Override
				public <S extends Tag> S save(S s)
				{
					return null;
				}

				@Override
				public long count()
				{
					return 0;
				}

				@Override
				public void deleteById(Integer integer)
				{

				}

				@Override
				public void delete(Tag tag)
				{

				}

				@Override
				public void deleteAll(Iterable<? extends Tag> iterable)
				{

				}

				@Override
				public void deleteAll()
				{

				}

				@Override
				public <S extends Tag> Page<S> findAll(Example<S> example, Pageable pageable)
				{
					return null;
				}

				@Override
				public <S extends Tag> long count(Example<S> example)
				{
					return 0;
				}

				@Override
				public <S extends Tag> boolean exists(Example<S> example)
				{
					return false;
				}
			};
		}

		@Bean
		public ImportService getImportService()
		{
			return new ImportService(getCategoryRepository(), getTransactionRepository(), getTagRepository());
		}
	}

	@Autowired
	private ImportService importService;

	@Test
	public void test_updateCategoriesForTransactions()
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
		transactionList.add(transaction1);

		Transaction transaction2 = new Transaction();
		transaction2.setName("Test_2");
		transaction2.setAmount(-525);
		transaction2.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction2.setCategory(category2);
		transactionList.add(transaction2);

		List<Transaction> updatedTransactions = importService.updateCategoriesForTransactions(transactionList, 3, 5);
		assertEquals(1, updatedTransactions.size());
		assertEquals(new Integer(5), updatedTransactions.get(0).getCategory().getID());
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
		assertEquals(1, transactionList.size());
		assertEquals(transaction2, transactionList.get(0));
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

		List<Transaction> transactionList = new ArrayList<>();
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

		List<Transaction> updatedTransactions = importService.updateAccountsForTransactions(transactionList, account1.getID(), destinationAccount);
		assertEquals(1, updatedTransactions.size());
		assertEquals(new Integer(5), updatedTransactions.get(0).getAccount().getID());
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

		List<Transaction> transactionList = new ArrayList<>();
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

		List<Transaction> updatedTransactions = importService.updateTransferAccountsForTransactions(transactionList, transferAccount.getID(), destinationAccount);
		assertEquals(1, updatedTransactions.size());
		assertEquals(expectedTransaction, updatedTransactions.get(0));
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

		// database
		Database database = new Database(new ArrayList<>(), accounts, transactions);

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
		List<Transaction> resultTransactions = databaseResult.getTransactions();
		assertEquals(2, resultTransactions.size());
		assertEquals(expectedTransaction1, resultTransactions.get(0));
		assertEquals(expectedTransaction2, resultTransactions.get(1));
	}
}