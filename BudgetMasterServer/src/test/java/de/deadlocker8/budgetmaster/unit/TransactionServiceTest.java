package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@LocalizedTest
class TransactionServiceTest
{
	private static final Category CATEGORY_REST = new Category("Balance", "#FFFF00", CategoryType.REST);
	private static final Category CATEGORY_CUSTOM = new Category("CustomCategory", "#0F0F0F", CategoryType.CUSTOM);

	private static final Account ACCOUNT = new Account("MyAccount", "", AccountType.CUSTOM, null);

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private SettingsService settingsService;

	@InjectMocks
	private TransactionService transactionService;

	@Test
	void test_isDeletable_rest()
	{
		Transaction transactionRest = new Transaction();
		transactionRest.setID(1);
		transactionRest.setName("Balance");
		transactionRest.setAmount(700);
		transactionRest.setCategory(CATEGORY_REST);
		transactionRest.setAccount(ACCOUNT);
		transactionRest.setIsExpenditure(false);

		Mockito.when(transactionRepository.findById(1)).thenReturn(Optional.of(transactionRest));

		assertThat(transactionService.isDeletable(1)).isFalse();
	}

	@Test
	void test_isDeletable_custom()
	{
		Transaction transaction = new Transaction();
		transaction.setID(1);
		transaction.setName("Fuel");
		transaction.setAmount(-15000);
		transaction.setCategory(CATEGORY_CUSTOM);
		transaction.setAccount(ACCOUNT);
		transaction.setIsExpenditure(true);

		Mockito.when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

		assertThat(transactionService.isDeletable(1)).isTrue();
	}

	@Test
	void test_handleAmount_null()
	{
		Transaction transaction = new Transaction();
		transaction.setAmount(null);
		transaction.setIsExpenditure(null);

		transactionService.handleAmount(transaction);

		assertThat(transaction).hasFieldOrPropertyWithValue("amount", 0)
				.hasFieldOrPropertyWithValue("isExpenditure", true);
	}

	@Test
	void test_handleAmount_expenditure()
	{
		Transaction transaction = new Transaction();
		transaction.setAmount(500);
		transaction.setIsExpenditure(true);

		transactionService.handleAmount(transaction);

		assertThat(transaction).hasFieldOrPropertyWithValue("amount", -500)
				.hasFieldOrPropertyWithValue("isExpenditure", true);
	}

	@Test
	void test_handleAmount_income()
	{
		Transaction transaction = new Transaction();
		transaction.setAmount(-500);
		transaction.setIsExpenditure(false);

		transactionService.handleAmount(transaction);

		assertThat(transaction).hasFieldOrPropertyWithValue("amount", 500)
				.hasFieldOrPropertyWithValue("isExpenditure", false);
	}

	@Test
	void test_getNameSuggestionsJson_sortAlphabetically()
	{
		final Settings settings = Settings.getDefault();

		Mockito.when(settingsService.getSettings()).thenReturn(settings);

		final Transaction transaction1 = new Transaction();
		transaction1.setID(1);
		transaction1.setName("ABC");
		transaction1.setAmount(700);
		transaction1.setCategory(CATEGORY_CUSTOM);
		transaction1.setAccount(ACCOUNT);
		transaction1.setIsExpenditure(false);

		final Transaction transaction2 = new Transaction();
		transaction2.setID(1);
		transaction2.setName("XYZ");
		transaction2.setAmount(700);
		transaction2.setCategory(CATEGORY_CUSTOM);
		transaction2.setAccount(ACCOUNT);
		transaction2.setIsExpenditure(false);

		final Transaction transaction3 = new Transaction();
		transaction3.setID(1);
		transaction3.setName("XYZ");
		transaction3.setAmount(700);
		transaction3.setCategory(CATEGORY_CUSTOM);
		transaction3.setAccount(ACCOUNT);
		transaction3.setIsExpenditure(false);

		Mockito.when(transactionRepository.findAllByOrderByNameAsc()).thenReturn(List.of(transaction1, transaction2, transaction3));

		assertThat(transactionService.getNameSuggestionsJson()).isEqualTo("[\"ABC\",\"XYZ\"]");
	}

	@Test
	void test_getNameSuggestionsJson_sortByFrequencyOfUse()
	{
		final Settings settings = Settings.getDefault();
		settings.setOrderTransactionNameSuggestionsAlphabetically(false);

		Mockito.when(settingsService.getSettings()).thenReturn(settings);

		final Transaction transaction1 = new Transaction();
		transaction1.setID(1);
		transaction1.setName("ABC");
		transaction1.setAmount(700);
		transaction1.setCategory(CATEGORY_CUSTOM);
		transaction1.setAccount(ACCOUNT);
		transaction1.setIsExpenditure(false);

		final Transaction transaction2 = new Transaction();
		transaction2.setID(1);
		transaction2.setName("XYZ");
		transaction2.setAmount(700);
		transaction2.setCategory(CATEGORY_CUSTOM);
		transaction2.setAccount(ACCOUNT);
		transaction2.setIsExpenditure(false);

		final Transaction transaction3 = new Transaction();
		transaction3.setID(1);
		transaction3.setName("XYZ");
		transaction3.setAmount(700);
		transaction3.setCategory(CATEGORY_CUSTOM);
		transaction3.setAccount(ACCOUNT);
		transaction3.setIsExpenditure(false);

		final Transaction transaction4 = new Transaction();
		transaction4.setID(1);
		transaction4.setName("LOREM");
		transaction4.setAmount(700);
		transaction4.setCategory(CATEGORY_CUSTOM);
		transaction4.setAccount(ACCOUNT);
		transaction4.setIsExpenditure(false);

		Mockito.when(transactionRepository.findAllByOrderByNameAsc()).thenReturn(List.of(transaction1, transaction2, transaction3, transaction4));

		assertThat(transactionService.getNameSuggestionsJson()).isEqualTo("[\"XYZ\",\"ABC\",\"LOREM\"]");
	}
}
