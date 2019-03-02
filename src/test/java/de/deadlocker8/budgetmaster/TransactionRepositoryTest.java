package de.deadlocker8.budgetmaster;

import de.deadlocker8.budgetmaster.entities.account.Account;
import de.deadlocker8.budgetmaster.entities.account.AccountType;
import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.category.CategoryType;
import de.deadlocker8.budgetmaster.entities.tag.Tag;
import de.deadlocker8.budgetmaster.entities.transaction.Transaction;
import de.deadlocker8.budgetmaster.entities.transaction.TransactionSpecifications;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.repositories.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class TransactionRepositoryTest
{
	@Autowired
	private TransactionRepository transactionRepository;
	private Transaction transaction1;
	private Transaction transaction2;
	private Transaction repeatingTransaction;

	@Autowired
	private CategoryRepository categoryRepository;
	private Category categoryUnused;
	private Category category1;
	private Category category2;

	@Autowired
	private AccountRepository accountRepository;
	private Account account;

	@Autowired
	private TagRepository tagRepository;
	private Tag tag1;

	@Autowired
	private RepeatingOptionRepository repeatingOptionRepository;
	private RepeatingOption repeatingOption;

	private DateTime startDate = new DateTime(2018, 1, 1, 12, 0, 0, 0);


	@Before
	public void init()
	{
		account = accountRepository.save(new Account("TestAccount", AccountType.CUSTOM));

		categoryUnused = categoryRepository.save(new Category("CategoryUnused", "#00ff00", CategoryType.CUSTOM));
		category1 = categoryRepository.save(new Category("Category1", "#ff0000", CategoryType.CUSTOM));
		category2 = categoryRepository.save(new Category("Category2", "#ff0000", CategoryType.CUSTOM));

		tag1 = tagRepository.save(new Tag("MyAwesomeTag"));

		transaction1 = new Transaction();
		transaction1.setName("Test");
		transaction1.setAmount(200);
		transaction1.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction1.setCategory(category1);
		transaction1.setAccount(account);
		transaction1 = transactionRepository.save(transaction1);

		transaction2 = new Transaction();
		transaction2.setName("Test_2");
		transaction2.setAmount(-525);
		transaction2.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction2.setCategory(category2);
		transaction2.setAccount(account);
		transaction2 = transactionRepository.save(transaction2);


		DateTime repeatingTransactionDate = DateTime.parse("2018-03-13", DateTimeFormat.forPattern("yyyy-MM-dd"));
		repeatingOption = new RepeatingOption();
		repeatingOption.setModifier(new RepeatingModifierDays(10));
		repeatingOption.setStartDate(repeatingTransactionDate);
		repeatingOption.setEndOption(new RepeatingEndAfterXTimes(2));
		repeatingOption = repeatingOptionRepository.save(repeatingOption);

		repeatingTransaction = new Transaction();
		repeatingTransaction.setAmount(-12300);
		repeatingTransaction.setDate(repeatingTransactionDate);
		repeatingTransaction.setCategory(category1);
		repeatingTransaction.setName("Repeating");
		repeatingTransaction.setDescription("");
		repeatingTransaction.setAccount(account);
		repeatingTransaction.setRepeatingOption(repeatingOption);
		ArrayList<Tag> tags = new ArrayList<>();
		tags.add(tag1);
		repeatingTransaction.setTags(tags);
		repeatingTransaction = transactionRepository.save(repeatingTransaction);
	}

	@Test
	public void getIncomesAndExpenditures()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertTrue(results.contains(transaction1));
		assertTrue(results.contains(transaction2));
		assertTrue(results.contains(repeatingTransaction));
	}

	@Test
	public void getIncomes()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, false, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertTrue(results.contains(transaction1));
		assertFalse(results.contains(transaction2));
		assertFalse(results.contains(repeatingTransaction));
	}

	@Test
	public void getExpenditures()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, false, true, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertFalse(results.contains(transaction1));
		assertTrue(results.contains(transaction2));
		assertTrue(results.contains(repeatingTransaction));
	}

	@Test
	public void incomesAndExpendituresFalse()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, false, false, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertTrue(results.contains(transaction1));
		assertTrue(results.contains(transaction2));
		assertTrue(results.contains(repeatingTransaction));
	}

	@Test
	public void getRepeating()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, true, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertFalse(results.contains(transaction1));
		assertFalse(results.contains(transaction2));
		assertTrue(results.contains(repeatingTransaction));
	}

	@Test
	public void noRepeating()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, false, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertTrue(results.contains(transaction1));
		assertTrue(results.contains(transaction2));
		assertFalse(results.contains(repeatingTransaction));
	}

	@Test
	public void noMatchingCategory()
	{
		List<Integer> categoryIDs = new ArrayList<>();
		categoryIDs.add(categoryUnused.getID());
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, null, categoryIDs, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertFalse(results.contains(transaction1));
		assertFalse(results.contains(transaction2));
		assertFalse(results.contains(repeatingTransaction));
	}

	@Test
	public void getByCategory()
	{
		List<Integer> categoryIDs = new ArrayList<>();
		categoryIDs.add(category1.getID());
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, null, categoryIDs, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertTrue(results.contains(transaction1));
		assertFalse(results.contains(transaction2));
		assertTrue(results.contains(repeatingTransaction));
	}

	@Test
	public void getByFullName()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, null, null, null, "Repeating");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertFalse(results.contains(transaction1));
		assertFalse(results.contains(transaction2));
		assertTrue(results.contains(repeatingTransaction));
	}

	@Test
	public void getByPartialName()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, null, null, null, "tin");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertFalse(results.contains(transaction1));
		assertFalse(results.contains(transaction2));
		assertTrue(results.contains(repeatingTransaction));
	}

	@Test
	public void getRepeatingExpenditureByCategoryAndTagsAndName()
	{
		List<Integer> categoryIDs = new ArrayList<>();
		categoryIDs.add(category1.getID());

		List<Integer> tagIDs = new ArrayList<>();
		tagIDs.add(tag1.getID());

		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, false, true, true, categoryIDs, tagIDs, "Repeating");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertFalse(results.contains(transaction1));
		assertFalse(results.contains(transaction2));
		assertTrue(results.contains(repeatingTransaction));
	}
}