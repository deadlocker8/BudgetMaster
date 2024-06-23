package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.TestConstants;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class TransactionSpecificationsTest
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

	@Autowired
	private TransactionRepository transactionRepository;
	private Transaction transaction1;
	private Transaction transaction2;
	private Transaction transaction3;
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

	private LocalDate startDate = LocalDate.of(2018, 1, 1);

	@BeforeEach
	public void init()
	{
		account = accountRepository.save(new Account("TestAccount", "", AccountType.CUSTOM, null));
		account2 = accountRepository.save(new Account("TestAccount2", "", AccountType.CUSTOM, null));
		accountHidden = accountRepository.save(new Account("Hidden Account", "", AccountType.CUSTOM, null));
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
		transaction1.setDate(LocalDate.of(2018, 10, 3));
		transaction1.setCategory(category1);
		transaction1.setAccount(account);
		ArrayList<Tag> tags = new ArrayList<>();
		tags.add(tag1);
		transaction1.setTags(tags);
		transaction1 = transactionRepository.save(transaction1);

		transaction2 = new Transaction();
		transaction2.setName("Test_2");
		transaction2.setAmount(-525);
		transaction2.setDate(LocalDate.of(2018, 12, 3));
		transaction2.setCategory(category2);
		transaction2.setAccount(account);
		transaction2 = transactionRepository.save(transaction2);

		transaction3 = new Transaction();
		transaction3.setName("Income without tags");
		transaction3.setAmount(100);
		transaction3.setDate(LocalDate.of(2018, 12, 3));
		transaction3.setCategory(category2);
		transaction3.setAccount(account);
		transaction3 = transactionRepository.save(transaction3);

		LocalDate repeatingTransactionDate = LocalDate.of(2018, 3, 13);
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
		transferTransaction.setDate(LocalDate.of(2018, 10, 3));
		transferTransaction.setCategory(category2);
		transferTransaction.setAccount(account);
		transferTransaction.setTransferAccount(account2);
		transferTransaction = transactionRepository.save(transferTransaction);

		transferTransactionWrongAccount = new Transaction();
		transferTransactionWrongAccount.setName("Lunch");
		transferTransactionWrongAccount.setAmount(-1100);
		transferTransactionWrongAccount.setDate(LocalDate.of(2018, 9, 18));
		transferTransactionWrongAccount.setCategory(category2);
		transferTransactionWrongAccount.setAccount(account2);
		transferTransactionWrongAccount.setTransferAccount(account2);
		transferTransactionWrongAccount = transactionRepository.save(transferTransactionWrongAccount);

		transactionInHiddenAccount = new Transaction();
		transactionInHiddenAccount.setName("Transaction in Hidden Account");
		transactionInHiddenAccount.setAmount(-1100);
		transactionInHiddenAccount.setDate(LocalDate.of(2018, 9, 18));
		transactionInHiddenAccount.setAccount(accountHidden);
		transactionInHiddenAccount = transactionRepository.save(transactionInHiddenAccount);
	}

	@Test
	void getIncomesAndExpendituresAndTransfers()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, true, true, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(5)
				.contains(transaction1)
				.contains(transaction2)
				.contains(transaction3)
				.contains(repeatingTransaction)
				.contains(transferTransaction);
	}

	@Test
	void getIncomesAndExpenditures()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, true, false, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(4)
				.contains(transaction1)
				.contains(transaction2)
				.contains(transaction3)
				.contains(repeatingTransaction);
	}

	@Test
	void getIncomes()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, false, false, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(2)
				.contains(transaction1)
				.contains(transaction3);
	}

	@Test
	void getExpenditures()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, false, true, false, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(2)
				.contains(transaction2)
				.contains(repeatingTransaction);
	}

	@Test
	void getTransfers()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, false, false, true, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(transferTransaction);
	}

	@Test
	void incomesAndExpendituresFalse()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, false, false, false, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(4)
				.contains(transaction1)
				.contains(transaction2)
				.contains(transaction3)
				.contains(repeatingTransaction);
	}

	@Test
	void getTransferBackReferences_NoReferences()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, false, true, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(2)
				.contains(transaction1)
				.contains(transaction3);
	}

	@Test
	void getTransferBackReferences()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account2, false, false, true, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(2)
				.contains(transferTransaction)
				.contains(transferTransactionWrongAccount);
	}

	@Test
	void getTransferBackReferences_excludeExpenditures()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account2, true, false, true, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(transferTransaction);
	}

	@Test
	void getTransferBackReferences_excludeIncomes()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account2, false, true, true, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(transferTransactionWrongAccount);
	}

	@Test
	void getTransferBackReferences_WithStartDate()
	{
		LocalDate startDate2019 = LocalDate.of(2019, 1, 1);
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate2019, LocalDate.now(), account2, false, false, true, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).isEmpty();
	}

	@Test
	void getRepeating()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, true, false, true, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(repeatingTransaction);
	}

	@Test
	void noRepeating()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, true, true, false, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(4)
				.contains(transaction1)
				.contains(transaction2)
				.contains(transaction3)
				.contains(transferTransaction);
	}

	@Test
	void noMatchingCategory()
	{
		List<Integer> categoryIDs = new ArrayList<>();
		categoryIDs.add(categoryUnused.getID());
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, true, true, null, categoryIDs, List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).isEmpty();
	}

	@Test
	void getByCategory()
	{
		List<Integer> categoryIDs = new ArrayList<>();
		categoryIDs.add(category1.getID());
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, true, true, null, categoryIDs, List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(2)
				.contains(transaction1)
				.contains(repeatingTransaction);
	}

	@Test
	void getByFullName()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, true, true, null, List.of(), List.of(), "Repeating");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(repeatingTransaction);
	}

	@Test
	void getByPartialName()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, true, true, null, List.of(), List.of(), "tin");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(repeatingTransaction);
	}

	@Test
	void getByPartialName_ExcludeTransfersWithWrongAccount()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account2, true, true, true, null, List.of(), List.of(), "tion");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(transferTransaction);
	}

	@Test
	void getByTags()
	{
		List<Integer> tagIDs = new ArrayList<>();
		tagIDs.add(tag1.getID());

		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, true, true, null, List.of(), tagIDs, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(4)
				.contains(transaction1)
				.contains(transaction2)
				.contains(transaction3)
				.contains(transferTransaction);
	}

	@Test
	void getByMultipleTags()
	{
		List<Integer> tagIDs = new ArrayList<>();
		tagIDs.add(tag1.getID());
		tagIDs.add(tag2.getID());

		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, true, true, null, List.of(), tagIDs, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(5)
				.contains(transaction1)
				.contains(transaction2)
				.contains(transaction3)
				.contains(repeatingTransaction)
				.contains(transferTransaction);
	}


	@Test
	void getByUnusedTags()
	{
		List<Integer> tagIDs = new ArrayList<>();
		tagIDs.add(tagUnused.getID());

		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, true, true, null, List.of(), tagIDs, null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(3)
				.contains(transaction2)
				.contains(transaction3)
				.contains(transferTransaction);
	}

	@Test
	void getRepeatingExpenditureByCategoryAndTagsAndName()
	{
		List<Integer> categoryIDs = new ArrayList<>();
		categoryIDs.add(category1.getID());

		List<Integer> tagIDs = new ArrayList<>();
		tagIDs.add(tag1.getID());
		tagIDs.add(tag2.getID());

		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, false, true, true, true, categoryIDs, tagIDs, "Repeating");

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(1)
				.contains(repeatingTransaction);
	}

	@Test
	void getFromAllAccountsExceptTransfersWithSpecificEndDate()
	{
		LocalDate endDate = LocalDate.of(2018, 11, 30);
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, endDate, null, true, true, false, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(2)
				.contains(transaction1)
				.contains(repeatingTransaction);
	}

	@Test
	void getFromAllAccountsExceptHidden()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), null, true, true, true, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(6)
				.contains(transaction1)
				.contains(transaction2)
				.contains(transaction3)
				.contains(repeatingTransaction)
				.contains(transferTransaction)
				.contains(transferTransactionWrongAccount);
	}

	@Test
	void checkOrder()
	{
		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, LocalDate.now(), account, true, true, true, null, List.of(), List.of(), null);

		List<Transaction> results = transactionRepository.findAll(spec);
		assertThat(results).hasSize(5)
				.containsExactly(transaction3,
						transaction2,
						transferTransaction,
						transaction1,
						repeatingTransaction);
	}
}