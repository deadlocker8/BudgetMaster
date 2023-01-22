package de.deadlocker8.budgetmaster.unit.transaction.csvimport;

import de.deadlocker8.budgetmaster.transactions.csvimport.AmountParser;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

class AmountParserTest
{
	@Test
	void test_dot_positive_noCurrency()
	{
		assertThat(AmountParser.parse("12.03"))
				.isPresent()
				.get().isEqualTo(1203);
	}

	@Test
	void test_dot_negative_noCurrency()
	{
		assertThat(AmountParser.parse("-18.41"))
				.isPresent()
				.get().isEqualTo(-1841);
	}

	@Test
	void test_dot_negativeWithSpace_noCurrency()
	{
		assertThat(AmountParser.parse("- 200.30"))
				.isPresent()
				.get().isEqualTo(-20030);
	}

	@Test
	void test_dot_positive_currency()
	{
		assertThat(AmountParser.parse("12.03 €"))
				.isPresent()
				.get().isEqualTo(1203);
	}

	@Test
	void test_dot_negative_currency()
	{
		assertThat(AmountParser.parse("-18.41€"))
				.isPresent()
				.get().isEqualTo(-1841);
	}

	@Test
	void test_dot_positiveWithSign_noCurrency()
	{
		assertThat(AmountParser.parse("+12.03"))
				.isPresent()
				.get().isEqualTo(1203);
	}

	@Test
	void test_dot_positiveWithSignWithSpace_noCurrency()
	{
		assertThat(AmountParser.parse("+ 12.03"))
				.isPresent()
				.get().isEqualTo(1203);
	}

	@Test
	void test_comma_positive_noCurrency()
	{
		assertThat(AmountParser.parse("12,03"))
				.isPresent()
				.get().isEqualTo(1203);
	}

	@Test
	void test_comma_negative_noCurrency()
	{
		assertThat(AmountParser.parse("-18,41"))
				.isPresent()
				.get().isEqualTo(-1841);
	}

	@Test
	void test_comma_negativeWithSpace_noCurrency()
	{
		assertThat(AmountParser.parse("- 200,30"))
				.isPresent()
				.get().isEqualTo(-20030);
	}

	@Test
	void test_comma_positive_currency()
	{
		assertThat(AmountParser.parse("12,03 €"))
				.isPresent()
				.get().isEqualTo(1203);
	}

	@Test
	void test_comma_negative_currency()
	{
		assertThat(AmountParser.parse("-18,41€"))
				.isPresent()
				.get().isEqualTo(-1841);
	}

	@Test
	void test_comma_positiveWithSign_noCurrency()
	{
		assertThat(AmountParser.parse("+12,03"))
				.isPresent()
				.get().isEqualTo(1203);
	}

	@Test
	void test_comma_positiveWithSignWithSpace_noCurrency()
	{
		assertThat(AmountParser.parse("+12,03"))
				.isPresent()
				.get().isEqualTo(1203);
	}

	@Test
	void test_invalid_null()
	{
		assertThat(AmountParser.parse(null))
				.isEmpty();
	}

	@Test
	void test_invalid_empty()
	{
		assertThat(AmountParser.parse(""))
				.isEmpty();
	}

	@Test
	void test_invalid_empty2()
	{
		assertThat(AmountParser.parse("    "))
				.isEmpty();
	}

	@Test
	void test_invalid()
	{
		assertThat(AmountParser.parse("abc.42€"))
				.isEmpty();
	}

	@Test
	void test_integer_positive_noCurrency()
	{
		assertThat(AmountParser.parse("12"))
				.isPresent()
				.get().isEqualTo(1200);
	}

	@Test
	void test_integer_negative_noCurrency()
	{
		assertThat(AmountParser.parse("-18"))
				.isPresent()
				.get().isEqualTo(-1800);
	}

	@Test
	void test_integer_negativeWithSpace_noCurrency()
	{
		assertThat(AmountParser.parse("- 200"))
				.isPresent()
				.get().isEqualTo(-20000);
	}

	@Test
	void test_integer_positive_currency()
	{
		assertThat(AmountParser.parse("12 €"))
				.isPresent()
				.get().isEqualTo(1200);
	}

	@Test
	void test_integer_negative_currency()
	{
		assertThat(AmountParser.parse("-18€"))
				.isPresent()
				.get().isEqualTo(-1800);
	}

	@Test
	void test_integer_positiveWithSign_noCurrency()
	{
		assertThat(AmountParser.parse("+12"))
				.isPresent()
				.get().isEqualTo(1200);
	}

	@Test
	void test_integer_positiveWithSignWithSpace_noCurrency()
	{
		assertThat(AmountParser.parse("+ 12"))
				.isPresent()
				.get().isEqualTo(1200);
	}
}
