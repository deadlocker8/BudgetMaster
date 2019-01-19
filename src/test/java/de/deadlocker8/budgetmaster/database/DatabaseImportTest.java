package de.deadlocker8.budgetmaster.database;

import de.deadlocker8.budgetmaster.entities.*;
import de.deadlocker8.budgetmaster.entities.account.Account;
import de.deadlocker8.budgetmaster.entities.account.AccountType;
import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.category.CategoryType;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import de.deadlocker8.budgetmaster.repositories.TagRepository;
import de.deadlocker8.budgetmaster.repositories.TransactionRepository;
import de.deadlocker8.budgetmaster.services.ImportService;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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
				public List<Category> findAll(Iterable<Integer> iterable)
				{
					return null;
				}

				@Override
				public <S extends Category> List<S> save(Iterable<S> iterable)
				{
					return null;
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
				public Category findOne(Integer integer)
				{
					return null;
				}

				@Override
				public boolean exists(Integer integer)
				{
					return false;
				}

				@Override
				public long count()
				{
					return 0;
				}

				@Override
				public void delete(Integer integer)
				{

				}

				@Override
				public void delete(Category category)
				{

				}

				@Override
				public void delete(Iterable<? extends Category> iterable)
				{

				}

				@Override
				public void deleteAll()
				{

				}

				@Override
				public <S extends Category> S findOne(Example<S> example)
				{
					return null;
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
				public Integer getRest(int accountID, String startDate, String endDate)
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
				public List<Transaction> findAll(Iterable<Integer> iterable)
				{
					return null;
				}

				@Override
				public <S extends Transaction> List<S> save(Iterable<S> iterable)
				{
					return null;
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
				public Transaction findOne(Integer integer)
				{
					return null;
				}

				@Override
				public boolean exists(Integer integer)
				{
					return false;
				}

				@Override
				public long count()
				{
					return 0;
				}

				@Override
				public void delete(Integer integer)
				{

				}

				@Override
				public void delete(Transaction transaction)
				{

				}

				@Override
				public void delete(Iterable<? extends Transaction> iterable)
				{

				}

				@Override
				public void deleteAll()
				{

				}

				@Override
				public <S extends Transaction> S findOne(Example<S> example)
				{
					return null;
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
				public List<Tag> findAll(Iterable<Integer> iterable)
				{
					return null;
				}

				@Override
				public <S extends Tag> List<S> save(Iterable<S> iterable)
				{
					return null;
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
				public Tag findOne(Integer integer)
				{
					return null;
				}

				@Override
				public boolean exists(Integer integer)
				{
					return false;
				}

				@Override
				public long count()
				{
					return 0;
				}

				@Override
				public void delete(Integer integer)
				{

				}

				@Override
				public void delete(Tag tag)
				{

				}

				@Override
				public void delete(Iterable<? extends Tag> iterable)
				{

				}

				@Override
				public void deleteAll()
				{

				}

				@Override
				public <S extends Tag> S findOne(Example<S> example)
				{
					return null;
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
}