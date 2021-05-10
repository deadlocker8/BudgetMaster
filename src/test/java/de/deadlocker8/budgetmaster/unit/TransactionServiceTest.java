package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@LocalizedTest
public class TransactionServiceTest
{
	private static final Category CATEGORY_REST = new Category("Rest", "#FFFF00", CategoryType.REST);
	private static final Category CATEGORY_CUSTOM = new Category("CustomCategory", "#0F0F0F", CategoryType.CUSTOM);

	private static final Account ACCOUNT = new Account("MyAccount", AccountType.CUSTOM);

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionService transactionService;

	@Test
	public void test_isDeletable_rest()
	{
		Transaction transactionRest = new Transaction();
		transactionRest.setID(1);
		transactionRest.setName("Rest");
		transactionRest.setAmount(700);
		transactionRest.setCategory(CATEGORY_REST);
		transactionRest.setAccount(ACCOUNT);
		transactionRest.setIsExpenditure(false);

		Mockito.when(transactionRepository.findById(1)).thenReturn(Optional.of(transactionRest));

		assertThat(transactionService.isDeletable(1)).isFalse();
	}

	@Test
	public void test_isDeletable_custom()
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
	public void test_handleAmount_null()
	{
		Transaction transaction = new Transaction();
		transaction.setAmount(null);
		transaction.setIsExpenditure(null);

		transactionService.handleAmount(transaction);

		assertThat(transaction).hasFieldOrPropertyWithValue("amount", 0)
				.hasFieldOrPropertyWithValue("isExpenditure", true);
	}

	@Test
	public void test_handleAmount_expenditure()
	{
		Transaction transaction = new Transaction();
		transaction.setAmount(500);
		transaction.setIsExpenditure(true);

		transactionService.handleAmount(transaction);

		assertThat(transaction).hasFieldOrPropertyWithValue("amount", -500)
				.hasFieldOrPropertyWithValue("isExpenditure", true);
	}

	@Test
	public void test_handleAmount_income()
	{
		Transaction transaction = new Transaction();
		transaction.setAmount(-500);
		transaction.setIsExpenditure(false);

		transactionService.handleAmount(transaction);

		assertThat(transaction).hasFieldOrPropertyWithValue("amount", 500)
				.hasFieldOrPropertyWithValue("isExpenditure", false);
	}
}
