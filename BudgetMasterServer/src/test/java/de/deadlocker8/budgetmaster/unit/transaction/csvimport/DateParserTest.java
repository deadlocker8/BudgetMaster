package de.deadlocker8.budgetmaster.unit.transaction.csvimport;

import de.deadlocker8.budgetmaster.transactions.csvimport.DateParser;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class DateParserTest
{
	@Test
	void test_nullText()
	{
		assertThat(DateParser.parse(null, "dd.MM", Locale.ENGLISH))
				.isEmpty();
	}

	@Test
	void test_nullPattern()
	{
		assertThat(DateParser.parse("14.01.23", null, Locale.ENGLISH))
				.isEmpty();
	}

	@Test
	void test_textNotMatchingPattern()
	{
		assertThat(DateParser.parse("14.01.23", "dd.MM", Locale.ENGLISH))
				.isEmpty();
	}

	@Test
	void test_matchingPattern()
	{
		assertThat(DateParser.parse("14.01.23", "dd.MM.yy", Locale.ENGLISH))
				.isPresent()
				.get().isEqualTo(LocalDate.of(2023, 1, 14));
	}
}
