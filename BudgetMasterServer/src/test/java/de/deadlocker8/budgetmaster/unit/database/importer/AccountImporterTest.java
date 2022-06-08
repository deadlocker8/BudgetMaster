package de.deadlocker8.budgetmaster.unit.database.importer;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.database.importer.AccountImporter;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

class AccountImporterTest extends ImporterTestBase
{
	@Override
	List<String> getTableNamesToResetSequence()
	{
		return List.of("account");
	}

	@Autowired
	private AccountRepository accountRepository;

	@Test
	void test_importAccounts()
	{
		final Account sourceAccount = new Account("SourceAccount", AccountType.CUSTOM);
		sourceAccount.setID(2);

		final Account sourceAccount2 = new Account("SourceAccount 2", AccountType.CUSTOM);
		sourceAccount2.setID(7);

		final Account destinationAccount = new Account("DestinationAccount", AccountType.CUSTOM);
		destinationAccount.setID(1);

		final Account destinationAccount2 = new Account("DestinationAccount 2", AccountType.CUSTOM);
		destinationAccount2.setID(2);

		final AccountImporter importer = new AccountImporter(accountRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(sourceAccount, sourceAccount2));

		final ImportResultItem expected = new ImportResultItem(EntityType.ACCOUNT, 2, 2, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(sourceAccount).hasFieldOrPropertyWithValue("ID", destinationAccount.getID());
		assertThat(sourceAccount2).hasFieldOrPropertyWithValue("ID", destinationAccount2.getID());
	}

	@Test
	void test_importAccounts_placeholder()
	{
		final Account placeholderAccount = new Account("Placeholder", AccountType.ALL);
		placeholderAccount.setID(12);

		final Account existingPlaceholderAccount = new Account("Placeholder", AccountType.ALL);
		existingPlaceholderAccount.setID(1);
		accountRepository.save(existingPlaceholderAccount);

		final AccountImporter importer = new AccountImporter(accountRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(placeholderAccount));

		final ImportResultItem expected = new ImportResultItem(EntityType.ACCOUNT, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(placeholderAccount).hasFieldOrPropertyWithValue("ID", existingPlaceholderAccount.getID());
	}

	@Test
	void test_importAccounts_nameAlreadyExists()
	{
		final Account account = new Account("ABC", AccountType.CUSTOM);
		account.setID(12);

		final Account existingAccount = new Account("ABC", AccountType.CUSTOM);
		existingAccount.setID(1);
		accountRepository.save(existingAccount);

		final AccountImporter importer = new AccountImporter(accountRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(account));

		assertThat(resultItem).hasFieldOrPropertyWithValue("numberOfImportedItems", 0);
		assertThat(resultItem.getCollectedErrorMessages()).hasSize(1);
	}
}