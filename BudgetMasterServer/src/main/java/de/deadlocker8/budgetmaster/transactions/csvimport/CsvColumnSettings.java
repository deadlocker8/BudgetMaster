package de.deadlocker8.budgetmaster.transactions.csvimport;

public record CsvColumnSettings(int columnDate, String datePattern, int columnName, int columnAmount,
								String decimalSeparator, String groupingSeparator, int columnDescription)
{
}
