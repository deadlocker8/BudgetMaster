package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.csvimport.*;
import de.thecodelabs.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TransactionImportService
{
	private final HelpersService helpersService;
	private final SettingsService settingsService;
	private final CategoryService categoryService;

	@Autowired
	public TransactionImportService(HelpersService helpersService, SettingsService settingsService, CategoryService categoryService)
	{
		this.helpersService = helpersService;
		this.settingsService = settingsService;
		this.categoryService = categoryService;
	}

	public CsvTransaction createCsvTransactionFromCsvRow(CsvRow csvRow, CsvColumnSettings csvColumnSettings, Integer index) throws CsvTransactionParseException
	{
		final String date = csvRow.getColumns().get(csvColumnSettings.columnDate() - 1);
		final Optional<LocalDate> parsedDateOptional = DateParser.parse(date, csvColumnSettings.datePattern(), settingsService.getSettings().getLanguage().getLocale());
		if(parsedDateOptional.isEmpty())
		{
			throw new CsvTransactionParseException(Localization.getString("transactions.import.error.parse.date", index + 1, date, csvColumnSettings.datePattern()));
		}

		final String name = csvRow.getColumns().get(csvColumnSettings.columnName() - 1);
		final String description = csvRow.getColumns().get(csvColumnSettings.columnDescription() - 1);

		final String amount = csvRow.getColumns().get(csvColumnSettings.columnAmount() - 1);
		final Optional<Integer> parsedAmountOptional = AmountParser.parse(amount, csvColumnSettings.decimalSeparator().charAt(0), csvColumnSettings.groupingSeparator().charAt(0));
		if(parsedAmountOptional.isEmpty())
		{
			throw new CsvTransactionParseException(Localization.getString("transactions.import.error.parse.amount", index + 1));
		}

		final Category categoryNone = categoryService.findByType(CategoryType.NONE);
		return new CsvTransaction(parsedDateOptional.get(), name, parsedAmountOptional.get(), description, CsvTransactionStatus.PENDING, categoryNone);
	}

	public Transaction createTransactionFromCsvTransaction(CsvTransaction csvTransaction)
	{
		final Transaction newTransaction = new Transaction();
		newTransaction.setDate(csvTransaction.getDate());
		newTransaction.setName(csvTransaction.getName());
		newTransaction.setDescription(csvTransaction.getDescription());
		newTransaction.setAmount(csvTransaction.getAmount());
		newTransaction.setIsExpenditure(csvTransaction.getAmount() <= 0);
		newTransaction.setAccount(helpersService.getCurrentAccountOrDefault());
		newTransaction.setCategory(csvTransaction.getCategory());

		return newTransaction;
	}

	public void updateCsvTransaction(CsvTransaction existingCsvTransaction, CsvTransaction csvTransactionWithNewAttributes)
	{
		existingCsvTransaction.setName(csvTransactionWithNewAttributes.getName());
		existingCsvTransaction.setDescription(csvTransactionWithNewAttributes.getDescription());
		existingCsvTransaction.setCategory(csvTransactionWithNewAttributes.getCategory());
	}
}
