package de.deadlocker8.budgetmaster.transactions.csvImport;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CsvParser
{
	private CsvParser()
	{
	}

	public static List<CsvRow> parseCsv(String csvString, char separator) throws IOException, CsvValidationException
	{
		final ArrayList<CsvRow> csvRows = new ArrayList<>();

		final CSVParser csvParser = new CSVParserBuilder()
				.withSeparator(separator)
				.build();

		try(CSVReader reader = new CSVReaderBuilder(
				new StringReader(csvString))
				.withCSVParser(csvParser)
				.build())
		{
			String[] columns;
			while((columns = reader.readNext()) != null)
			{
				csvRows.add(new CsvRow(columns));
			}
		}

		return csvRows;
	}
}
