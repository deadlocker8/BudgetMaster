package de.deadlocker8.budgetmaster;

import de.deadlocker8.budgetmaster.entities.account.Account;
import de.deadlocker8.budgetmaster.entities.account.AccountType;
import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.entities.category.CategoryType;
import de.deadlocker8.budgetmaster.entities.transaction.Transaction;
import de.deadlocker8.budgetmaster.entities.transaction.TransactionSpecifications;
import de.deadlocker8.budgetmaster.repositories.AccountRepository;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import de.deadlocker8.budgetmaster.repositories.TransactionRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class TransactionRepositoryTest
{
	@Autowired
	private TransactionRepository transactionRepository;
	private Transaction transaction1;
	private Transaction transaction2;

	@Autowired
	private CategoryRepository categoryRepository;
	private Category category1;
	private Category category2;

	@Autowired
	private AccountRepository accountRepository;
	private Account account;

	@Before
	public void init()
	{
		account = accountRepository.save(new Account("TestAccount", AccountType.CUSTOM));

		category1 = categoryRepository.save(new Category("Category1", "#ff0000", CategoryType.CUSTOM));
		category2 = categoryRepository.save(new Category("Category2", "#ff0000", CategoryType.CUSTOM));

		transaction1 = new Transaction();
		transaction1.setName("Test");
		transaction1.setAmount(200);
		transaction1.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction1.setCategory(category1);
		transaction1.setAccount(account);
		transactionRepository.save(transaction1);

		transaction2 = new Transaction();
		transaction2.setName("Test_2");
		transaction2.setAmount(-525);
		transaction2.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction2.setCategory(category2);
		transaction2.setAccount(account);
		transactionRepository.save(transaction2);
	}

	@Test
	public void getSpecificTransaction()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(200, "Test");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertTrue(results.contains(transaction1));
		assertFalse(results.contains(transaction2));
	}

	@Test
	public void oneCriteria()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(-525, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		System.out.println(results);
		assertTrue(results.contains(transaction2));
		assertFalse(results.contains(transaction1));
	}

	@Test
	public void noMatch()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(855, "eimer");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertEquals(0, results.size());
	}
}