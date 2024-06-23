package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest
{

	@Test
	void test_getRemainingDays_NoEndDate()
	{
		final Account account = new Account("account", "", AccountType.CUSTOM, null);
		assertThat(account.getRemainingDays())
				.isNull();
	}

	@Test
	void test_getRemainingDays_EndDateNotReached()
	{
		final LocalDate today = LocalDate.now();
		final Account account = new Account("account", "", AccountType.CUSTOM, today.plusDays(30));
		assertThat(account.getRemainingDays())
				.isEqualTo(30);
	}

	@Test
	void test_getRemainingDays_EndDateExactlyReached()
	{
		final LocalDate today = LocalDate.now();
		final Account account = new Account("account", "", AccountType.CUSTOM, today);
		assertThat(account.getRemainingDays())
				.isZero();
	}

	@Test
	void test_getRemainingDays_EndDateReached()
	{
		final LocalDate today = LocalDate.now();
		final Account account = new Account("account", "", AccountType.CUSTOM, today.minusDays(5));
		assertThat(account.getRemainingDays())
				.isEqualTo(-5);
	}
}
