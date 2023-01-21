package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.settings.Settings;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionImportService;
import de.deadlocker8.budgetmaster.transactions.csvimport.*;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import de.deadlocker8.budgetmaster.utils.LanguageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(SpringExtension.class)
@LocalizedTest
class TransactionImportServiceTest
{
	private static final Category CATEGORY_NONE = new Category("No category", "#FFFFFF", CategoryType.NONE);
	private static final Category CATEGORY_CUSTOM = new Category("CustomCategory", "#0F0F0F", CategoryType.CUSTOM);
	private static final Account ACCOUNT = new Account("MyAccount", AccountType.CUSTOM);

	@Mock
	private HelpersService helpersService;

	@Mock
	private SettingsService settingsService;

	@Mock
	private CategoryService categoryService;

	@InjectMocks
	private TransactionImportService transactionImportService;

	@Test
	void test_createTransactionFromCsvTransaction_income()
	{
		final LocalDate date = LocalDate.of(2023, 1, 21);
		final CsvTransaction csvTransaction = new CsvTransaction(date, "Awesome", 750, "Lorem Ipsum", CsvTransactionStatus.PENDING, CATEGORY_NONE);

		final Transaction expectedTransaction = new Transaction();
		expectedTransaction.setID(null);
		expectedTransaction.setName("Awesome");
		expectedTransaction.setAmount(750);
		expectedTransaction.setCategory(CATEGORY_NONE);
		expectedTransaction.setAccount(ACCOUNT);
		expectedTransaction.setIsExpenditure(false);
		expectedTransaction.setDate(date);
		expectedTransaction.setDescription("Lorem Ipsum");

		Mockito.when(helpersService.getCurrentAccountOrDefault()).thenReturn(ACCOUNT);

		assertThat(transactionImportService.createTransactionFromCsvTransaction(csvTransaction))
				.isEqualTo(expectedTransaction);
	}

	@Test
	void test_createTransactionFromCsvTransaction_expenditure()
	{
		final LocalDate date = LocalDate.of(2023, 1, 21);
		final CsvTransaction csvTransaction = new CsvTransaction(date, "Awesome", -1200, "Lorem Ipsum", CsvTransactionStatus.PENDING, CATEGORY_NONE);

		final Transaction expectedTransaction = new Transaction();
		expectedTransaction.setID(null);
		expectedTransaction.setName("Awesome");
		expectedTransaction.setAmount(-1200);
		expectedTransaction.setCategory(CATEGORY_NONE);
		expectedTransaction.setAccount(ACCOUNT);
		expectedTransaction.setIsExpenditure(true);
		expectedTransaction.setDate(date);
		expectedTransaction.setDescription("Lorem Ipsum");

		Mockito.when(helpersService.getCurrentAccountOrDefault()).thenReturn(ACCOUNT);

		assertThat(transactionImportService.createTransactionFromCsvTransaction(csvTransaction))
				.isEqualTo(expectedTransaction);
	}

	@Test
	void test_updateCsvTransaction()
	{
		final LocalDate date = LocalDate.of(2023, 1, 21);
		final CsvTransaction existingCsvTransaction = new CsvTransaction(date, "Awesome", -1200, "Lorem Ipsum", CsvTransactionStatus.IMPORTED, CATEGORY_NONE);
		final CsvTransaction csvTransactionWithNewAttributes = new CsvTransaction(date, "Over 9000", -1200, "dolor sit amet", CsvTransactionStatus.PENDING, CATEGORY_CUSTOM);

		final CsvTransaction expectedCsvTransaction = new CsvTransaction(date, "Over 9000", -1200, "dolor sit amet", CsvTransactionStatus.IMPORTED, CATEGORY_CUSTOM);

		transactionImportService.updateCsvTransaction(existingCsvTransaction, csvTransactionWithNewAttributes);
		assertThat(existingCsvTransaction)
				.isEqualTo(expectedCsvTransaction);
	}

	@Test
	void test_createCsvTransactionFromCsvRow() throws CsvTransactionParseException
	{
		final LocalDate date = LocalDate.of(2023, 1, 21);

		final CsvRow csvRow = new CsvRow("21.01.2023", "Groceries", "-12.00", "dolor sit amet");
		final CsvColumnSettings csvColumnSettings = new CsvColumnSettings(1, "dd.MM.yyyy", 2, 3, 4);

		final CsvTransaction expectedCsvTransaction = new CsvTransaction(date, "Groceries", -1200, "dolor sit amet", CsvTransactionStatus.PENDING, CATEGORY_NONE);

		final Settings settings = new Settings();
		settings.setLanguage(LanguageType.ENGLISH);
		Mockito.when(settingsService.getSettings()).thenReturn(settings);
		Mockito.when(categoryService.findByType(CategoryType.NONE)).thenReturn(CATEGORY_NONE);

		final CsvTransaction csvTransaction = transactionImportService.createCsvTransactionFromCsvRow(csvRow, csvColumnSettings, 0);

		assertThat(csvTransaction)
				.isEqualTo(expectedCsvTransaction);
	}

	@Test
	void test_createCsvTransactionFromCsvRow_dateParseException()
	{
		final LocalDate date = LocalDate.of(2023, 1, 21);

		final CsvRow csvRow = new CsvRow("21.01.2023", "Groceries", "-12.00", "dolor sit amet");
		final CsvColumnSettings csvColumnSettings = new CsvColumnSettings(1, "dd.MM.", 2, 3, 4);

		final CsvTransaction expectedCsvTransaction = new CsvTransaction(date, "Groceries", -1200, "dolor sit amet", CsvTransactionStatus.PENDING, CATEGORY_NONE);

		final Settings settings = new Settings();
		settings.setLanguage(LanguageType.ENGLISH);
		Mockito.when(settingsService.getSettings()).thenReturn(settings);
		Mockito.when(categoryService.findByType(CategoryType.NONE)).thenReturn(CATEGORY_NONE);

		assertThatThrownBy(() -> transactionImportService.createCsvTransactionFromCsvRow(csvRow, csvColumnSettings, 0))
				.isInstanceOf(CsvTransactionParseException.class)
				.hasMessageContaining("Error parsing the date in line 1");
	}

	@Test
	void test_createCsvTransactionFromCsvRow_amountParseException()
	{
		final LocalDate date = LocalDate.of(2023, 1, 21);

		final CsvRow csvRow = new CsvRow("21.01.2023", "Groceries", "non_amount", "dolor sit amet");
		final CsvColumnSettings csvColumnSettings = new CsvColumnSettings(1, "dd.MM.yyyy", 2, 3, 4);

		final CsvTransaction expectedCsvTransaction = new CsvTransaction(date, "Groceries", -1200, "dolor sit amet", CsvTransactionStatus.PENDING, CATEGORY_NONE);

		final Settings settings = new Settings();
		settings.setLanguage(LanguageType.ENGLISH);
		Mockito.when(settingsService.getSettings()).thenReturn(settings);
		Mockito.when(categoryService.findByType(CategoryType.NONE)).thenReturn(CATEGORY_NONE);

		assertThatThrownBy(() -> transactionImportService.createCsvTransactionFromCsvRow(csvRow, csvColumnSettings, 0))
				.isInstanceOf(CsvTransactionParseException.class)
				.hasMessageContaining("Error parsing the amount in line 1");
	}
}
