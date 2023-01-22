package de.deadlocker8.budgetmaster.unit.transaction.csvimport;

import com.opencsv.exceptions.CsvValidationException;
import de.deadlocker8.budgetmaster.transactions.csvimport.CsvParser;
import de.deadlocker8.budgetmaster.transactions.csvimport.CsvRow;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class CsvParserTest
{
	private static final String VALID_CSV = "Date;Title;Amount\n" +
			"03.01.2023;Lorem;50.00\n" +
			"05.01.2023;Ipsum;-8.37\n" +
			"08.01.2023;dolor sit amet;-12.00";

	@Test
	void test_parseCsv_emptyFile() throws CsvValidationException, IOException
	{
		assertThat(CsvParser.parseCsv("", ';', 0))
				.isEmpty();
	}

	@Test
	void test_parseCsv_separatorNotPresent() throws CsvValidationException, IOException
	{
		assertThat(CsvParser.parseCsv("abc,17", ';', 0))
				.containsExactly(new CsvRow("abc,17"));
	}

	@Test
	void test_parseCsv() throws CsvValidationException, IOException
	{
		assertThat(CsvParser.parseCsv(VALID_CSV, ';', 0)).
				containsExactly(new CsvRow("Date", "Title", "Amount"),
						new CsvRow("03.01.2023", "Lorem", "50.00"),
						new CsvRow("05.01.2023", "Ipsum", "-8.37"),
						new CsvRow("08.01.2023", "dolor sit amet", "-12.00"));
	}

	@Test
	void test_parseCsv_skipLines() throws CsvValidationException, IOException
	{
		assertThat(CsvParser.parseCsv(VALID_CSV, ';', 2)).
				containsExactly(new CsvRow("05.01.2023", "Ipsum", "-8.37"),
						new CsvRow("08.01.2023", "dolor sit amet", "-12.00"));
	}

	@Test
	void test_parseCsv_skipLines_moreThanExisting() throws CsvValidationException, IOException
	{
		assertThat(CsvParser.parseCsv(VALID_CSV, ';', 999))
				.isEmpty();
	}
}
