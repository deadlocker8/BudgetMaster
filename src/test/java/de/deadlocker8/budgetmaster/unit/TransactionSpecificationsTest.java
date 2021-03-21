package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.RepeatingOptionRepository;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionSpecifications;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class TransactionSpecificationsTest
{
	@Autowired
	private TransactionRepository transactionRepository;
	private Transaction transaction1;
	private Transaction transaction2;
	private Transaction repeatingTransaction;
	private Transaction transferTransaction;
	private Transaction transferTransactionWrongAccount;
	private Transaction transactionInHiddenAccount;

	@Autowired
	private CategoryRepository categoryRepository;
	private Category categoryUnused;
	private Category category1;
	private Category category2;

	@Autowired
	private AccountRepository accountRepository;
	private Account account;
	private Account account2;
	private Account accountHidden;

	@Autowired
	private TagRepository tagRepository;
	private Tag tag1;
	private Tag tag2;
	private Tag tagUnused;

	@Autowired
	private RepeatingOptionRepository repeatingOptionRepository;
	private RepeatingOption repeatingOption;

	private DateTime startDate = new DateTime(2018, 1, 1, 12, 0, 0, 0);


	@Before
	public void init()
	{
		account = accountRepository.save(new Account("TestAccount", AccountType.CUSTOM));
		account2 = accountRepository.save(new Account("TestAccount2", AccountType.CUSTOM));
		accountHidden = accountRepository.save(new Account("Hidden Account", AccountType.CUSTOM));
		accountHidden.setAccountState(AccountState.HIDDEN);

		categoryUnused = categoryRepository.save(new Category("CategoryUnused", "#00ff00", CategoryType.CUSTOM));
		category1 = categoryRepository.save(new Category("Category1", "#ff0000", CategoryType.CUSTOM));
		category2 = categoryRepository.save(new Category("Category2", "#ff0000", CategoryType.CUSTOM));

		tag1 = tagRepository.save(new Tag("MyAwesomeTag"));
		tag2 = tagRepository.save(new Tag("TagMaster_2"));
		tagUnused = tagRepository.save(new Tag("Unused"));

		transaction1 = new Transaction();
		transaction1.setName("Test");
		transaction1.setAmount(200);
		transaction1.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transaction1.setCategory(category1);
		transaction1.setAccount(account);
		ArrayList<Tag> tags = new ArrayList<>();
		tags.add(tag1);
		transaction1.setTags(tags);
		transaction1 = transactionRepository.save(transaction1);

		transaction2 = new Transaction();
		transaction2.setName("Test_2");
		transaction2.setAmount(-525);
		transaction2.setDate(new DateTime(2018, 12, 3, 12, 0, 0, 0));
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
		ArrayList<Tag> tags2 = new ArrayList<>();
		tags2.add(tag2);
		repeatingTransaction.setTags(tags2);
		repeatingTransaction = transactionRepository.save(repeatingTransaction);

		transferTransaction = new Transaction();
		transferTransaction.setName("TransferTransaction");
		transferTransaction.setAmount(-500);
		transferTransaction.setDate(new DateTime(2018, 10, 3, 12, 0, 0, 0));
		transferTransaction.setCategory(category2);
		transferTransaction.setAccount(account);
		transferTransaction.setTransferAccount(account2);
		transferTransaction = transactionRepository.save(transferTransaction);

		transferTransactionWrongAccount = new Transaction();
		transferTransactionWrongAccount.setName("Lunch");
		transferTransactionWrongAccount.setAmount(-1100);
		transferTransactionWrongAccount.setDate(new DateTime(2018, 9, 18, 12, 0, 0, 0));
		transferTransactionWrongAccount.setCategory(category2);
		transferTransactionWrongAccount.setAccount(account2);
		transferTransactionWrongAccount.setTransferAccount(account2);
		transferTransactionWrongAccount = transactionRepository.save(transferTransactionWrongAccount);

		transactionInHiddenAccount = new Transaction();
		transactionInHiddenAccount.setName("Transaction in Hidden Account");
		transactionInHiddenAccount.setAmount(-1100);
		transactionInHiddenAccount.setDate(new DateTime(2018, 9, 18, 12, 0, 0, 0));
		transactionInHiddenAccount.setAccount(accountHidden);
		transactionInHiddenAccount = transactionRepository.save(transactionInHiddenAccount);
	}

	@Test
	public void getIncomesAndExpendituresAndTransfers()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, true, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(4)
				.contains(transaction1)
				.contains(transaction2)
				.contains(repeatingTransaction)
				.contains(transferTransaction);
	}

	@Test
	public void getIncomesAndExpenditures()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, false, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(3)
				.contains(transaction1)
				.contains(transaction2)
				.contains(repeatingTransaction);
	}

	@Test
	public void getIncomes()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, false, false, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(transaction1);
	}

	@Test
	public void getExpenditures()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, false, true, false, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(2)
				.contains(transaction2)
				.contains(repeatingTransaction);
	}

	@Test
	public void getTransfers()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, false, false, true, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(transferTransaction);
	}

	@Test
	public void incomesAndExpendituresFalse()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, false, false, false, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(3)
				.contains(transaction1)
				.contains(transaction2)
				.contains(repeatingTransaction);
	}

	@Test
	public void getTransferBackReferences_NoReferences()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, false, true, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(transaction1);
	}

	@Test
	public void getTransferBackReferences()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account2, false, false, true, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(2)
				.contains(transferTransaction)
				.contains(transferTransactionWrongAccount);
	}

	@Test
	public void getTransferBackReferences_WithStartDate()
	{
		DateTime startDate2019 = new DateTime(2019, 1, 1, 12, 0, 0, 0);
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate2019, DateTime.now(), account2, false, false, true, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).isEmpty();
	}

	@Test
	public void getRepeating()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, false, true, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(repeatingTransaction);
	}

	@Test
	public void noRepeating()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, true, false, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(3)
				.contains(transaction1)
				.contains(transaction2)
				.contains(transferTransaction);
	}

	@Test
	public void noMatchingCategory()
	{
		List<Integer> categoryIDs = new ArrayList<>();
		categoryIDs.add(categoryUnused.getID());
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, true, null, categoryIDs, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).isEmpty();
	}

	@Test
	public void getByCategory()
	{
		List<Integer> categoryIDs = new ArrayList<>();
		categoryIDs.add(category1.getID());
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, true, null, categoryIDs, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(2)
				.contains(transaction1)
				.contains(repeatingTransaction);
	}

	@Test
	public void getByFullName()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, true, null, null, null, "Repeating");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(repeatingTransaction);
	}

	@Test
	public void getByPartialName()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, true, null, null, null, "tin");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(repeatingTransaction);
	}

	@Test
	public void getByPartialName_ExcludeTransfersWithWrongAccount()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account2, true, true, true, null, null, null, "tion");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
			.contains(transferTransaction);
	}

	@Test
	public void getByTags()
	{
		List<Integer> tagIDs = new ArrayList<>();
		tagIDs.add(tag1.getID());

		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, true, null, null, tagIDs, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(transaction1);
	}

	@Test
	public void getByMultipleTags()
	{
		List<Integer> tagIDs = new ArrayList<>();
		tagIDs.add(tag1.getID());
		tagIDs.add(tag2.getID());

		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, true,null, null, tagIDs, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(2)
				.contains(transaction1)
				.contains(repeatingTransaction);
	}


	@Test
	public void getByUnusedTags()
	{
		List<Integer> tagIDs = new ArrayList<>();
		tagIDs.add(tagUnused.getID());

		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, true, true, true, null, null, tagIDs, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).isEmpty();
	}

	@Test
	public void getRepeatingExpenditureByCategoryAndTagsAndName()
	{
		List<Integer> categoryIDs = new ArrayList<>();
		categoryIDs.add(category1.getID());

		List<Integer> tagIDs = new ArrayList<>();
		tagIDs.add(tag1.getID());
		tagIDs.add(tag2.getID());

		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, DateTime.now(), account, false, true, true, true, categoryIDs, tagIDs, "Repeating");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(repeatingTransaction);
	}

	@Test
	public void getFromAllAccountsExceptTransfersWithSpecificEndDateOrWithHiddenAccount()
	{
		DateTime endDate = new DateTime(2018, 11, 30, 12, 0, 0, 0);
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, endDate, null, true, true, false, null, null, null, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(2)
				.contains(transaction1)
				.contains(repeatingTransaction);
	}
}