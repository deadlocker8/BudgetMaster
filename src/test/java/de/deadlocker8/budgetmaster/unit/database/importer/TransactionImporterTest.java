package de.deadlocker8.budgetmaster.unit.database.importer;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.importer.IconImporter;
import de.deadlocker8.budgetmaster.database.importer.TagImporter;
import de.deadlocker8.budgetmaster.database.importer.TransactionImporter;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconRepository;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageFileExtension;
import de.deadlocker8.budgetmaster.images.ImageRepository;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TransactionImporterTest
{
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TagRepository tagRepository;

	@Test
	void test_importNormalTransaction()
	{
		Category category = new Category("Awesome Category", "#ff0000", CategoryType.CUSTOM);
		category = categoryRepository.save(category);

		Account account = new Account("Awesome Account", AccountType.CUSTOM);
		account = accountRepository.save(account);

		final Transaction transaction = new Transaction();
		transaction.setID(15);
		transaction.setName("My transaction");
		transaction.setAmount(-100);
		transaction.setIsExpenditure(true);
		transaction.setCategory(category);
		transaction.setAccount(account);
		transaction.setTags(List.of());
		transaction.setDate(LocalDate.of(2022, 3, 30));
		transaction.setDescription("Lorem Ipsum");

		final TagImporter tagImporter = new TagImporter(tagRepository);
		final TransactionImporter importer = new TransactionImporter(transactionRepository, tagImporter);
		final ImportResultItem resultItem = importer.importItems(List.of(transaction));

		final ImportResultItem expected = new ImportResultItem(EntityType.TRANSACTION, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final List<Transaction> transactions = transactionRepository.findAll();
		assertThat(transactions).hasSize(1);
		final Transaction actualTransaction = transactions.get(0);
		assertThat(actualTransaction)
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("name", "My transaction")
				.hasFieldOrPropertyWithValue("amount", -100)
				.hasFieldOrPropertyWithValue("isExpenditure", true)
				.hasFieldOrPropertyWithValue("category", category)
				.hasFieldOrPropertyWithValue("account", account)
				.hasFieldOrPropertyWithValue("date", LocalDate.of(2022, 3, 30))
				.hasFieldOrPropertyWithValue("description","Lorem Ipsum")
				.hasFieldOrPropertyWithValue("repeatingOption", null)
				.hasFieldOrPropertyWithValue("transferAccount", null);
		assertThat(actualTransaction.getTags()).isEmpty();
	}

	@Test
	void test_importTransferTransaction()
	{
		Category category = new Category("Awesome Category", "#ff0000", CategoryType.CUSTOM);
		category = categoryRepository.save(category);

		Account account = new Account("Awesome Account", AccountType.CUSTOM);
		account = accountRepository.save(account);

		Account transferAccount = new Account("Transfer Account", AccountType.CUSTOM);
		transferAccount = accountRepository.save(transferAccount);

		final Transaction transaction = new Transaction();
		transaction.setID(15);
		transaction.setName("My transaction");
		transaction.setAmount(-100);
		transaction.setIsExpenditure(true);
		transaction.setCategory(category);
		transaction.setAccount(account);
		transaction.setTransferAccount(transferAccount);
		transaction.setTags(List.of());
		transaction.setDate(LocalDate.of(2022, 3, 30));
		transaction.setDescription("Lorem Ipsum");

		final TagImporter tagImporter = new TagImporter(tagRepository);
		final TransactionImporter importer = new TransactionImporter(transactionRepository, tagImporter);
		final ImportResultItem resultItem = importer.importItems(List.of(transaction));

		final ImportResultItem expected = new ImportResultItem(EntityType.TRANSACTION, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final List<Transaction> transactions = transactionRepository.findAll();
		assertThat(transactions).hasSize(1);
		final Transaction actualTransaction = transactions.get(0);
		assertThat(actualTransaction)
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("name", "My transaction")
				.hasFieldOrPropertyWithValue("amount", -100)
				.hasFieldOrPropertyWithValue("isExpenditure", true)
				.hasFieldOrPropertyWithValue("category", category)
				.hasFieldOrPropertyWithValue("account", account)
				.hasFieldOrPropertyWithValue("date", LocalDate.of(2022, 3, 30))
				.hasFieldOrPropertyWithValue("description","Lorem Ipsum")
				.hasFieldOrPropertyWithValue("repeatingOption", null)
				.hasFieldOrPropertyWithValue("transferAccount", transferAccount);
		assertThat(actualTransaction.getTags()).isEmpty();
	}

	@Test
	void test_importRepeatingTransaction()
	{
		Category category = new Category("Awesome Category", "#ff0000", CategoryType.CUSTOM);
		category = categoryRepository.save(category);

		Account account = new Account("Awesome Account", AccountType.CUSTOM);
		account = accountRepository.save(account);

		final Transaction transaction = new Transaction();
		transaction.setID(15);
		transaction.setName("My transaction");
		transaction.setAmount(-100);
		transaction.setIsExpenditure(true);
		transaction.setCategory(category);
		transaction.setAccount(account);
		transaction.setTags(List.of());
		final LocalDate date = LocalDate.of(2022, 3, 30);
		transaction.setDate(date);
		transaction.setDescription("Lorem Ipsum");

		final RepeatingOption repeatingOption = new RepeatingOption(date, new RepeatingModifierDays(2), new RepeatingEndAfterXTimes(3));
		transaction.setRepeatingOption(repeatingOption);

		final TagImporter tagImporter = new TagImporter(tagRepository);
		final TransactionImporter importer = new TransactionImporter(transactionRepository, tagImporter);
		final ImportResultItem resultItem = importer.importItems(List.of(transaction));

		final ImportResultItem expected = new ImportResultItem(EntityType.TRANSACTION, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final List<Transaction> transactions = transactionRepository.findAll();
		assertThat(transactions).hasSize(1);
		final Transaction actualTransaction = transactions.get(0);
		assertThat(actualTransaction)
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("name", "My transaction")
				.hasFieldOrPropertyWithValue("amount", -100)
				.hasFieldOrPropertyWithValue("isExpenditure", true)
				.hasFieldOrPropertyWithValue("category", category)
				.hasFieldOrPropertyWithValue("account", account)
				.hasFieldOrPropertyWithValue("date", date)
				.hasFieldOrPropertyWithValue("description","Lorem Ipsum")
				.hasFieldOrPropertyWithValue("repeatingOption", repeatingOption)
				.hasFieldOrPropertyWithValue("transferAccount", null);
		assertThat(actualTransaction.getTags()).isEmpty();
	}

	@Test
	void test_importTransactionWithTags()
	{
		Category category = new Category("Awesome Category", "#ff0000", CategoryType.CUSTOM);
		category = categoryRepository.save(category);

		Account account = new Account("Awesome Account", AccountType.CUSTOM);
		account = accountRepository.save(account);

		final Transaction transaction = new Transaction();
		transaction.setID(15);
		transaction.setName("My transaction");
		transaction.setAmount(-100);
		transaction.setIsExpenditure(true);
		transaction.setCategory(category);
		transaction.setAccount(account);
		transaction.setDate(LocalDate.of(2022, 3, 30));
		transaction.setDescription("Lorem Ipsum");

		final Tag tag1 = new Tag("0815");
		tag1.setID(1);
		final Tag tag2 = new Tag("Apple Pie");
		tag2.setID(2);

		transaction.setTags(List.of(tag1, tag2));

		final TagImporter tagImporter = new TagImporter(tagRepository);
		final TransactionImporter importer = new TransactionImporter(transactionRepository, tagImporter);
		final ImportResultItem resultItem = importer.importItems(List.of(transaction));

		final ImportResultItem expected = new ImportResultItem(EntityType.TRANSACTION, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final List<Transaction> transactions = transactionRepository.findAll();
		assertThat(transactions).hasSize(1);
		final Transaction actualTransaction = transactions.get(0);
		assertThat(actualTransaction)
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("name", "My transaction")
				.hasFieldOrPropertyWithValue("amount", -100)
				.hasFieldOrPropertyWithValue("isExpenditure", true)
				.hasFieldOrPropertyWithValue("category", category)
				.hasFieldOrPropertyWithValue("account", account)
				.hasFieldOrPropertyWithValue("date", LocalDate.of(2022, 3, 30))
				.hasFieldOrPropertyWithValue("description","Lorem Ipsum")
				.hasFieldOrPropertyWithValue("repeatingOption", null)
				.hasFieldOrPropertyWithValue("transferAccount", null);

		final Tag expectedTag1 = new Tag("0815");
		expectedTag1.setID(1);
		final Tag expectedTag2 = new Tag("Apple Pie");
		expectedTag2.setID(2);
		assertThat(actualTransaction.getTags())
				.containsExactly(expectedTag1, expectedTag2);
	}

	@Test
	void test_importMultipleTransactionWithSomeSimilarTags()
	{
		Category category = new Category("Awesome Category", "#ff0000", CategoryType.CUSTOM);
		category = categoryRepository.save(category);

		Account account = new Account("Awesome Account", AccountType.CUSTOM);
		account = accountRepository.save(account);

		final Tag tag1 = new Tag("0815");
		tag1.setID(1);
		final Tag tag2 = new Tag("Apple Pie");
		tag2.setID(2);

		final Transaction transaction = new Transaction();
		transaction.setID(15);
		transaction.setName("My transaction");
		transaction.setAmount(-100);
		transaction.setIsExpenditure(true);
		transaction.setCategory(category);
		transaction.setAccount(account);
		transaction.setDate(LocalDate.of(2022, 3, 30));
		transaction.setTags(List.of(tag1, tag2));

		final Transaction transaction2 = new Transaction();
		transaction2.setID(16);
		transaction2.setName("My transaction 2");
		transaction2.setAmount(-250);
		transaction2.setIsExpenditure(true);
		transaction2.setCategory(category);
		transaction2.setAccount(account);
		transaction2.setDate(LocalDate.of(2022, 3, 30));
		transaction2.setTags(List.of(tag1));

		final TagImporter tagImporter = new TagImporter(tagRepository);
		final TransactionImporter importer = new TransactionImporter(transactionRepository, tagImporter);
		final ImportResultItem resultItem = importer.importItems(List.of(transaction, transaction2));

		final ImportResultItem expected = new ImportResultItem(EntityType.TRANSACTION, 2, 2, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final List<Transaction> transactions = transactionRepository.findAll();
		assertThat(transactions).hasSize(2);
		final Transaction actualTransaction = transactions.get(0);
		assertThat(actualTransaction)
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("name", "My transaction");

		final Tag expectedTag1 = new Tag("0815");
		expectedTag1.setID(1);
		final Tag expectedTag2 = new Tag("Apple Pie");
		expectedTag2.setID(2);
		assertThat(actualTransaction.getTags())
				.containsExactly(expectedTag1, expectedTag2);

		final Transaction actualTransaction2 = transactions.get(1);
		assertThat(actualTransaction2)
				.hasFieldOrPropertyWithValue("ID", 2)
				.hasFieldOrPropertyWithValue("name", "My transaction 2");

		assertThat(actualTransaction2.getTags())
				.containsExactly(expectedTag1);

		assertThat(tagRepository.findAll()).hasSize(2);
	}
}