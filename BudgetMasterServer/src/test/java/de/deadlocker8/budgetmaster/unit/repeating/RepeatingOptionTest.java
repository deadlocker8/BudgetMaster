package de.deadlocker8.budgetmaster.unit.repeating;

import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndDate;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndNever;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierMonths;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierYears;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class RepeatingOptionTest
{
	// test repeating every X days

	@Test
	void test_GetRepeatingDates_Every3Days_EndAfter3Times()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 22);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierDays(3),
				new RepeatingEndAfterXTimes(3));

		LocalDate dateFetchLimit = LocalDate.of(2019, 1, 1);

		List<LocalDate> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(LocalDate.of(2018, 4, 25));
		expected.add(LocalDate.of(2018, 4, 28));
		expected.add(LocalDate.of(2018, 5, 1));

		assertThat(repeatingOption.getRepeatingDates(dateFetchLimit))
				.isEqualTo(expected);
	}

	@Test
	void test_GetRepeatingDates_Every3Days_EndAfterDate()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 22);
		LocalDate endDate = LocalDate.of(2018, 4, 28);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierDays(3),
				new RepeatingEndDate(endDate));

		LocalDate dateFetchLimit = LocalDate.of(2019, 1, 1);

		List<LocalDate> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(LocalDate.of(2018, 4, 25));
		expected.add(LocalDate.of(2018, 4, 28));

		assertThat(repeatingOption.getRepeatingDates(dateFetchLimit))
				.isEqualTo(expected);
	}

	@Test
	void test_GetRepeatingDates_Every3Days_EndNever()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 22);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierDays(3),
				new RepeatingEndNever());

		LocalDate dateFetchLimit = LocalDate.of(2018, 5, 2);

		List<LocalDate> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(LocalDate.of(2018, 4, 25));
		expected.add(LocalDate.of(2018, 4, 28));
		expected.add(LocalDate.of(2018, 5, 1));

		assertThat(repeatingOption.getRepeatingDates(dateFetchLimit))
				.isEqualTo(expected);
	}

	// test repeating every X months

	@Test
	void test_GetRepeatingDates_Every2Month_EndAfter5Times()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 30);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierMonths(2),
				new RepeatingEndAfterXTimes(5));

		LocalDate dateFetchLimit = LocalDate.of(2020, 1, 1);

		List<LocalDate> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(LocalDate.of(2018, 6, 30));
		expected.add(LocalDate.of(2018, 8, 30));
		expected.add(LocalDate.of(2018, 10, 30));
		expected.add(LocalDate.of(2018, 12, 30));
		expected.add(LocalDate.of(2019, 2, 28));

		assertThat(repeatingOption.getRepeatingDates(dateFetchLimit))
				.isEqualTo(expected);
	}

	@Test
	void test_GetRepeatingDates_Every2Month_EndAfterDate()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 30);
		LocalDate endDate = LocalDate.of(2018, 9, 28);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierMonths(2),
				new RepeatingEndDate(endDate));

		LocalDate dateFetchLimit = LocalDate.of(2020, 1, 1);

		List<LocalDate> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(LocalDate.of(2018, 6, 30));
		expected.add(LocalDate.of(2018, 8, 30));

		assertThat(repeatingOption.getRepeatingDates(dateFetchLimit))
				.isEqualTo(expected);
	}

	@Test
	void test_GetRepeatingDates_Every2Month_EndNever()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 30);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierMonths(2),
				new RepeatingEndNever());

		LocalDate dateFetchLimit = LocalDate.of(2018, 9, 2);

		List<LocalDate> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(LocalDate.of(2018, 6, 30));
		expected.add(LocalDate.of(2018, 8, 30));

		assertThat(repeatingOption.getRepeatingDates(dateFetchLimit))
				.isEqualTo(expected);
	}

	// test repeating every X years

	@Test
	void test_GetRepeatingDates_EveryYear_EndAfter2Times()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 30);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierYears(1),
				new RepeatingEndAfterXTimes(2));

		LocalDate dateFetchLimit = LocalDate.of(2022, 1, 1);

		List<LocalDate> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(LocalDate.of(2019, 4, 30));
		expected.add(LocalDate.of(2020, 4, 30));

		assertThat(repeatingOption.getRepeatingDates(dateFetchLimit))
				.isEqualTo(expected);
	}

	@Test
	void test_GetRepeatingDates_EveryYear_EndAfterDate()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 30);
		LocalDate endDate = LocalDate.of(2019, 9, 28);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierYears(1),
				new RepeatingEndDate(endDate));

		LocalDate dateFetchLimit = LocalDate.of(2022, 1, 1);

		List<LocalDate> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(LocalDate.of(2019, 4, 30));

		assertThat(repeatingOption.getRepeatingDates(dateFetchLimit))
				.isEqualTo(expected);
	}

	@Test
	void test_GetRepeatingDates_EveryYear_EndNever()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 30);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierYears(1),
				new RepeatingEndNever());

		LocalDate dateFetchLimit = LocalDate.of(2020, 1, 1);

		List<LocalDate> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(LocalDate.of(2019, 4, 30));

		assertThat(repeatingOption.getRepeatingDates(dateFetchLimit))
				.isEqualTo(expected);
	}

	// test hasEndedBefore()

	@Test
	void test_HasEndedBefore_EndNever()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 22);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierDays(3),
				new RepeatingEndNever());

		LocalDate date = LocalDate.of(2018, 5, 2);

		assertThat(repeatingOption.hasEndedBefore(date)).isFalse();
	}

	@Test
	void test_HasEndedBefore_EndDate_NotEnded()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 30);
		LocalDate endDate = LocalDate.of(2019, 9, 28);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierYears(1),
				new RepeatingEndDate(endDate));

		LocalDate date = LocalDate.of(2018, 5, 2);

		assertThat(repeatingOption.hasEndedBefore(date)).isFalse();
	}

	@Test
	void test_HasEndedBefore_EndDate_HasEnded()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 30);
		LocalDate endDate = LocalDate.of(2019, 9, 28);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierYears(1),
				new RepeatingEndDate(endDate));

		LocalDate date = LocalDate.of(2019, 9, 29);

		assertThat(repeatingOption.hasEndedBefore(date)).isTrue();
	}

	@Test
	void test_HasEndedBefore_EndAfterXTimes_NotEnded()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 30);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierYears(1),
				new RepeatingEndAfterXTimes(2));

		LocalDate date = LocalDate.of(2020, 4, 29);

		assertThat(repeatingOption.hasEndedBefore(date)).isFalse();
	}

	@Test
	void test_HasEndedBefore_EndAfterXTimes_HasEnded()
	{
		LocalDate startDate = LocalDate.of(2018, 4, 30);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierYears(1),
				new RepeatingEndAfterXTimes(2));

		LocalDate date = LocalDate.of(2022, 9, 29);

		assertThat(repeatingOption.hasEndedBefore(date)).isTrue();
	}
}