package de.deadlocker8.budgetmaster.unit.repeating;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.RepeatingOptionRepository;
import de.deadlocker8.budgetmaster.repeating.RepeatingTransactionUpdater;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndDate;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndNever;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierMonths;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierYears;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@LocalizedTest
class RepeatingTransactionUpdaterTest
{
	@Mock
	private TransactionService transactionService;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private RepeatingOptionRepository repeatingOptionRepository;

	private Transaction TRANSACTION_1;
	private Transaction TRANSACTION_2;
	private Transaction TRANSACTION_3;
	private Transaction TRANSACTION_4;

	private RepeatingTransactionUpdater repeatingTransactionUpdater;

	@BeforeEach
	void beforeEach()
	{
		final RepeatingOption REPEATING_OPTION_END_NEVER = new RepeatingOption(LocalDate.of(2022, 12, 1),
				new RepeatingModifierDays(3),
				new RepeatingEndNever());

		final RepeatingOption REPEATING_OPTION_END_DATE = new RepeatingOption(LocalDate.of(2022, 10, 24),
				new RepeatingModifierMonths(1),
				new RepeatingEndDate(LocalDate.of(2023, 2, 15)));

		final RepeatingOption REPEATING_OPTION_END_AFTER_X_TIMES = new RepeatingOption(LocalDate.of(2018, 4, 30),
				new RepeatingModifierYears(1),
				new RepeatingEndAfterXTimes(2));

		final Account account = new Account("Account", "", AccountType.CUSTOM, null);

		TRANSACTION_1 = new Transaction();
		TRANSACTION_1.setName("abc");
		TRANSACTION_1.setAmount(700);
		TRANSACTION_1.setAccount(account);
		TRANSACTION_1.setIsExpenditure(true);
		TRANSACTION_1.setRepeatingOption(REPEATING_OPTION_END_NEVER);
		Mockito.when(transactionRepository.findAllByRepeatingOption(REPEATING_OPTION_END_NEVER)).thenReturn(List.of(TRANSACTION_1));

		TRANSACTION_2 = new Transaction();
		TRANSACTION_2.setName("Lorem");
		TRANSACTION_2.setAmount(200);
		TRANSACTION_2.setAccount(account);
		TRANSACTION_2.setIsExpenditure(true);
		TRANSACTION_2.setRepeatingOption(REPEATING_OPTION_END_DATE);

		TRANSACTION_3 = new Transaction();
		TRANSACTION_3.setName("Ipsum");
		TRANSACTION_3.setAmount(75);
		TRANSACTION_3.setAccount(account);
		TRANSACTION_3.setIsExpenditure(true);
		TRANSACTION_3.setRepeatingOption(REPEATING_OPTION_END_AFTER_X_TIMES);
		Mockito.when(transactionRepository.findAllByRepeatingOption(REPEATING_OPTION_END_DATE)).thenReturn(List.of(TRANSACTION_2, TRANSACTION_3));

		TRANSACTION_4 = new Transaction();
		TRANSACTION_4.setName("dolor");
		TRANSACTION_4.setAmount(50);
		TRANSACTION_4.setAccount(account);
		TRANSACTION_4.setIsExpenditure(true);
		TRANSACTION_4.setRepeatingOption(REPEATING_OPTION_END_AFTER_X_TIMES);
		Mockito.when(transactionRepository.findAllByRepeatingOption(REPEATING_OPTION_END_AFTER_X_TIMES)).thenReturn(List.of(TRANSACTION_4));

		Mockito.when(transactionService.getRepository()).thenReturn(transactionRepository);
		Mockito.when(repeatingOptionRepository.findAllByOrderByStartDateAsc()).thenReturn(List.of(REPEATING_OPTION_END_NEVER, REPEATING_OPTION_END_DATE, REPEATING_OPTION_END_AFTER_X_TIMES));
		repeatingTransactionUpdater = new RepeatingTransactionUpdater(transactionService, repeatingOptionRepository);
	}

	@Test
	void test_getActiveRepeatingTransactionsAfter()
	{
		assertThat(repeatingTransactionUpdater.getActiveRepeatingTransactionsAfter(LocalDate.of(2023, 1, 20)))
				.containsExactly(TRANSACTION_1, TRANSACTION_2);
	}

	@Test
	void test_getActiveRepeatingTransactionsAfter_2()
	{
		assertThat(repeatingTransactionUpdater.getActiveRepeatingTransactionsAfter(LocalDate.of(2017, 1, 20)))
				.containsExactly(TRANSACTION_1, TRANSACTION_2, TRANSACTION_4);
	}

	@Test
	void test_getActiveRepeatingTransactionsAfter_3()
	{
		assertThat(repeatingTransactionUpdater.getActiveRepeatingTransactionsAfter(LocalDate.of(2023, 10, 1)))
				.containsExactly(TRANSACTION_1);
	}
}
