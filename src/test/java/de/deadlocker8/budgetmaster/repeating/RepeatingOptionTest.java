package de.deadlocker8.budgetmaster.repeating;

import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndAfterXTimes;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndDate;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEndNever;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierDays;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierMonths;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierYears;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RepeatingOptionTest
{
	// test repeating every X days

	@Test
	public void test_GetRepeatingDates_Every3Days_EndAfter3Times()
	{
		DateTime startDate = new DateTime(2018, 4, 22, 12, 0);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierDays(3),
				new RepeatingEndAfterXTimes(3));

		DateTime dateFetchLimit = new DateTime(2019, 1, 1, 12, 0);

		List<DateTime> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(new DateTime(2018, 4, 25, 12, 0));
		expected.add(new DateTime(2018, 4, 28, 12, 0));
		expected.add(new DateTime(2018, 5, 1, 12, 0));

		assertEquals(expected, repeatingOption.getRepeatingDates(dateFetchLimit));
	}

	@Test
	public void test_GetRepeatingDates_Every3Days_EndAfterDate()
	{
		DateTime startDate = new DateTime(2018, 4, 22, 12, 0);
		DateTime endDate = new DateTime(2018, 4, 28, 12, 0);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierDays(3),
				new RepeatingEndDate(endDate));

		DateTime dateFetchLimit = new DateTime(2019, 1, 1, 12, 0);

		List<DateTime> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(new DateTime(2018, 4, 25, 12, 0));
		expected.add(new DateTime(2018, 4, 28, 12, 0));

		assertEquals(expected, repeatingOption.getRepeatingDates(dateFetchLimit));
	}

	@Test
	public void test_GetRepeatingDates_Every3Days_EndNever()
	{
		DateTime startDate = new DateTime(2018, 4, 22, 12, 0);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierDays(3),
				new RepeatingEndNever());

		DateTime dateFetchLimit = new DateTime(2018, 5, 2, 12, 0);

		List<DateTime> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(new DateTime(2018, 4, 25, 12, 0));
		expected.add(new DateTime(2018, 4, 28, 12, 0));
		expected.add(new DateTime(2018, 5, 1, 12, 0));

		assertEquals(expected, repeatingOption.getRepeatingDates(dateFetchLimit));
	}

	// test repeating every X months

	@Test
	public void test_GetRepeatingDates_Every2Month_EndAfter5Times()
	{
		DateTime startDate = new DateTime(2018, 4, 30, 12, 0);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierMonths(2),
				new RepeatingEndAfterXTimes(5));

		DateTime dateFetchLimit = new DateTime(2020, 1, 1, 12, 0);

		List<DateTime> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(new DateTime(2018, 6, 30, 12, 0));
		expected.add(new DateTime(2018, 8, 30, 12, 0));
		expected.add(new DateTime(2018, 10, 30, 12, 0));
		expected.add(new DateTime(2018, 12, 30, 12, 0));
		expected.add(new DateTime(2019, 2, 28, 12, 0));

		assertEquals(expected, repeatingOption.getRepeatingDates(dateFetchLimit));
	}

	@Test
	public void test_GetRepeatingDates_Every2Month_EndAfterDate()
	{
		DateTime startDate = new DateTime(2018, 4, 30, 12, 0);
		DateTime endDate = new DateTime(2018, 9, 28, 12, 0);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierMonths(2),
				new RepeatingEndDate(endDate));

		DateTime dateFetchLimit = new DateTime(2020, 1, 1, 12, 0);

		List<DateTime> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(new DateTime(2018, 6, 30, 12, 0));
		expected.add(new DateTime(2018, 8, 30, 12, 0));

		assertEquals(expected, repeatingOption.getRepeatingDates(dateFetchLimit));
	}

	@Test
	public void test_GetRepeatingDates_Every2Month_EndNever()
	{
		DateTime startDate = new DateTime(2018, 4, 30, 12, 0);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierMonths(2),
				new RepeatingEndNever());

		DateTime dateFetchLimit = new DateTime(2018, 9, 2, 12, 0);

		List<DateTime> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(new DateTime(2018, 6, 30, 12, 0));
		expected.add(new DateTime(2018, 8, 30, 12, 0));

		assertEquals(expected, repeatingOption.getRepeatingDates(dateFetchLimit));
	}

	// test repeating every X years

	@Test
	public void test_GetRepeatingDates_EveryYear_EndAfter2Times()
	{
		DateTime startDate = new DateTime(2018, 4, 30, 12, 0);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierYears(1),
				new RepeatingEndAfterXTimes(2));

		DateTime dateFetchLimit = new DateTime(2022, 1, 1, 12, 0);

		List<DateTime> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(new DateTime(2019, 4, 30, 12, 0));
		expected.add(new DateTime(2020, 4, 30, 12, 0));

		assertEquals(expected, repeatingOption.getRepeatingDates(dateFetchLimit));
	}

	@Test
	public void test_GetRepeatingDates_EveryYear_EndAfterDate()
	{
		DateTime startDate = new DateTime(2018, 4, 30, 12, 0);
		DateTime endDate = new DateTime(2019, 9, 28, 12, 0);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierYears(1),
				new RepeatingEndDate(endDate));

		DateTime dateFetchLimit = new DateTime(2022, 1, 1, 12, 0);

		List<DateTime> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(new DateTime(2019, 4, 30, 12, 0));

		assertEquals(expected, repeatingOption.getRepeatingDates(dateFetchLimit));
	}

	@Test
	public void test_GetRepeatingDates_EveryYear_EndNever()
	{
		DateTime startDate = new DateTime(2018, 4, 30, 12, 0);
		RepeatingOption repeatingOption = new RepeatingOption(startDate,
				new RepeatingModifierYears(1),
				new RepeatingEndNever());

		DateTime dateFetchLimit = new DateTime(2020, 1, 1, 12, 0);

		List<DateTime> expected = new ArrayList<>();
		expected.add(startDate);
		expected.add(new DateTime(2019, 4, 30, 12, 0));

		assertEquals(expected, repeatingOption.getRepeatingDates(dateFetchLimit));
	}
}