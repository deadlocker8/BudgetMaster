package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvImportTest extends SeleniumTestBase
{
	private static IntegrationTestHelper helper;

	@Override
	protected void importDatabaseOnce()
	{
		helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		final String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path);
	}

	@BeforeEach
	public void afterEach()
	{
		// cancel any running import process
		try
		{
			final WebElement cancelButton = driver.findElement(By.id("button-cancel-csv-import"));

			cancelButton.click();

			final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("button-cancel-csv-import")));
		}
		catch(NoSuchElementException e)
		{
			// nothing to do
		}
	}

	@Test
	void test_uploadEmptyFile_showWarningNotification()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/empty.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, ";", "UTF-8", "1");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("notification-wrapper")));

		assertThat(driver.findElement(By.cssSelector(".notification span.notification-item")).getText()).isEqualTo("Invalid empty file");
	}

	@Test
	void test_upload_invalidSeparator_showValidationError()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, "abc", "UTF-8", "1");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBe(By.id("inputCsvImport"), ""));

		assertThat(driver.findElement(By.cssSelector("#separator.invalid"))).isNotNull();
	}

	@Test
	void test_upload_emptySeparator_showValidationError()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, "", "UTF-8", "1");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBe(By.id("inputCsvImport"), ""));

		assertThat(driver.findElement(By.cssSelector("#separator.invalid"))).isNotNull();
	}

	@Test
	void test_upload_invalidEncoding_showValidationError()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, ";", "invalid_encoding", "1");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBe(By.id("inputCsvImport"), ""));

		assertThat(driver.findElement(By.cssSelector("#encoding.invalid"))).isNotNull();
	}

	@Test
	void test_upload_emptyEncoding_showValidationError()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, ";", "", "1");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBe(By.id("inputCsvImport"), ""));

		assertThat(driver.findElement(By.cssSelector("#encoding.invalid"))).isNotNull();
	}

	@Test
	void test_upload_invalidNumberOfLinesToSkip_showValidationError()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, ";", "UTF-8", "-5");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBe(By.id("inputCsvImport"), ""));

		assertThat(driver.findElement(By.cssSelector("#numberOfLinesToSkip.invalid"))).isNotNull();
	}

	@Test
	void test_upload_emptyNumberOfLinesToSkip_showValidationError()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, ";", "UTF-8", "");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBe(By.id("inputCsvImport"), ""));

		assertThat(driver.findElement(By.cssSelector("#numberOfLinesToSkip.invalid"))).isNotNull();
	}

	@Test
	void test_upload_emptyColumnSettings_showValidationError()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, ";", "UTF-8", "1");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("button-cancel-csv-import")));

		fillColumnSettings("", "dd.MM.YYYY", "", "", ".", ",", "");

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#columnDate.invalid")));

		assertThat(driver.findElement(By.cssSelector("#columnDate.invalid"))).isNotNull();
		assertThat(driver.findElement(By.cssSelector("#columnName.invalid"))).isNotNull();
		assertThat(driver.findElement(By.cssSelector("#columnAmount.invalid"))).isNotNull();
		assertThat(driver.findElement(By.cssSelector("#columnDescription.invalid"))).isNotNull();
	}

	@Test
	void test_upload_emptyDatePattern_showValidationError()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, ";", "UTF-8", "1");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("button-cancel-csv-import")));

		assertThat(driver.findElement(By.id("csv-file-name")).getText()).isEqualTo("three_entries.csv");

		fillColumnSettings(1, "", 2, 3, ".", ",", 2);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#datePattern.invalid")));

		assertThat(driver.findElement(By.cssSelector("#datePattern.invalid"))).isNotNull();
	}

	@Test
	void test_upload_invalidColumnSettings_showValidationError()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, ";", "UTF-8", "1");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("button-cancel-csv-import")));

		assertThat(driver.findElement(By.id("csv-file-name")).getText()).isEqualTo("three_entries.csv");

		fillColumnSettings(-3, "dd.MM.yyyy", 200, -12, ".", ",", 115);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#columnDate.invalid")));

		assertThat(driver.findElement(By.cssSelector("#columnDate.invalid"))).isNotNull();
		assertThat(driver.findElement(By.cssSelector("#columnName.invalid"))).isNotNull();
		assertThat(driver.findElement(By.cssSelector("#columnAmount.invalid"))).isNotNull();
		assertThat(driver.findElement(By.cssSelector("#columnDescription.invalid"))).isNotNull();
	}

	@Test
	void test_upload_emptyDecimalSeparator_showValidationError()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, ";", "UTF-8", "1");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("button-cancel-csv-import")));

		assertThat(driver.findElement(By.id("csv-file-name")).getText()).isEqualTo("three_entries.csv");

		fillColumnSettings(1, "", 2, 3, "", ",",2);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#decimalSeparator.invalid")));

		assertThat(driver.findElement(By.cssSelector("#decimalSeparator.invalid"))).isNotNull();
	}

	@Test
	void test_upload_emptyGroupingSeparator_showValidationError()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, ";", "UTF-8", "1");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("button-cancel-csv-import")));

		assertThat(driver.findElement(By.id("csv-file-name")).getText()).isEqualTo("three_entries.csv");

		fillColumnSettings(1, "", 2, 3, "", ",",2);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#decimalSeparator.invalid")));

		assertThat(driver.findElement(By.cssSelector("#decimalSeparator.invalid"))).isNotNull();
	}

	@Test
	void test_upload_valid()
	{
		uploadAndSetColumnSettings();

		assertThat(driver.findElements(By.className("transaction-import-row"))).hasSize(3);

		final WebElement row1 = driver.findElements(By.className("transaction-import-row")).get(0);
		assertRow(row1, "blue", "2023-01-08", "No category", "dolor sit amet", "dolor sit amet", "-12.00 €");
		final WebElement row2 = driver.findElements(By.className("transaction-import-row")).get(1);
		assertRow(row2, "blue", "2023-01-05", "No category", "Ipsum", "Ipsum", "-8.36 €");
		final WebElement row3 = driver.findElements(By.className("transaction-import-row")).get(2);
		assertRow(row3, "blue", "2023-01-03", "No category", "Lorem", "Lorem", "50.00 €");
	}

	@Test
	void test_upload_valid_parseErrors()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		// skip zero lines, so first line will lead to parse error because it is the row containing the column names
		uploadCsv(csvPath, ";", "UTF-8", "0");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("button-cancel-csv-import")));

		assertThat(driver.findElement(By.id("csv-file-name")).getText()).isEqualTo("three_entries.csv");

		fillColumnSettings(1, "dd.MM.yyyy", 2, 3, ".", ",",2);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("button-confirm-csv-column-settings")));

		assertThat(driver.findElements(By.className("transaction-import-row"))).hasSize(3);

		assertThat(driver.findElements(By.cssSelector("#parseErrors table tr"))).hasSize(1);
	}

	@Test
	void test_cancel()
	{
		uploadAndSetColumnSettings();

		driver.findElement(By.id("button-cancel-csv-import")).click();
		final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("button-confirm-csv-import")));

		assertThat(driver.findElement(By.id("button-confirm-csv-import")).isDisplayed()).isTrue();
	}

	@Test
	void test_skipRow()
	{
		uploadAndSetColumnSettings();
		final List<WebElement> rows = driver.findElements(By.className("transaction-import-row"));

		rows.get(0).findElement(By.className("button-request-transaction-import-skip")).click();

		final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("transaction-import-row-skipped")));

		assertThat(driver.findElement(By.className("transaction-import-row-skipped")).isDisplayed()).isTrue();
	}

	@Test
	void test_undoSkipRow()
	{
		uploadAndSetColumnSettings();
		List<WebElement> rows = driver.findElements(By.className("transaction-import-row"));

		// skip
		rows.get(0).findElement(By.className("button-request-transaction-import-skip")).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("transaction-import-row-skipped")));

		assertThat(driver.findElement(By.className("transaction-import-row-skipped")).isDisplayed()).isTrue();

		// undo skip
		rows = driver.findElements(By.className("transaction-import-row"));
		rows.get(0).findElement(By.className("button-request-transaction-import-undo-skip")).click();
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("transaction-import-row-skipped")));

		assertThat(driver.findElements(By.className("transaction-import-row-skipped"))).isEmpty();
	}

	@Test
	void test_saveInPlace()
	{
		uploadAndSetColumnSettings();

		final List<WebElement> rows = driver.findElements(By.className("transaction-import-row"));
		final WebElement row = rows.get(0);

		// change data
		final WebElement categorySelect = row.findElement(By.cssSelector(".category-select-wrapper .custom-select"));
		categorySelect.click();
		row.findElements(By.cssSelector(".category-select-wrapper .custom-select-item-name")).stream()
				.filter(webElement -> webElement.getText().equals("sdfdsf"))
				.findFirst().orElseThrow().click();

		final WebElement nameInput = row.findElement(By.name("name"));
		nameInput.clear();
		nameInput.sendKeys("Groceries");

		final WebElement descriptionInput = row.findElement(By.name("description"));
		descriptionInput.clear();
		descriptionInput.sendKeys("Everything in a bottle");

		row.findElement(By.name("action")).click();

		final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".transaction-import-row .banner.background-green")));

		final WebElement rowAfterSave = driver.findElements(By.className("transaction-import-row")).get(0);
		assertRow(rowAfterSave, "green", "2023-01-08", "sdfdsf", "Groceries", "Everything in a bottle", "-12.00 €");
	}

	@Test
	void test_editNewTransaction()
	{
		uploadAndSetColumnSettings();

		// click new transaction button
		final List<WebElement> rows = driver.findElements(By.className("transaction-import-row"));
		final WebElement row = rows.get(0);

		row.findElement(By.className("edit-transaction-button")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		final By locator = By.xpath("//div[contains(@class, 'edit-transaction-button')]//a[contains(text(),'Transaction')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		row.findElement(locator).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// assert transaction page is filled correctly
		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("background-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("dolor sit amet");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("12.00");
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEqualTo("08.01.2023");
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEqualTo("dolor sit amet");
		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("1");

		// save transaction
		driver.findElement(By.id("button-save-transaction")).click();
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final WebElement rowAfterSave = driver.findElements(By.className("transaction-import-row")).get(0);
		assertRow(rowAfterSave, "green", "2023-01-08", "No category", "dolor sit amet", "dolor sit amet", "-12.00 €");
	}

	@Test
	void test_editNewTransfer()
	{
		uploadAndSetColumnSettings();

		// click new transaction button
		final List<WebElement> rows = driver.findElements(By.className("transaction-import-row"));
		final WebElement row = rows.get(0);

		row.findElement(By.className("edit-transaction-button")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		final By locator = By.xpath("//div[contains(@class, 'edit-transaction-button')]//a[contains(text(),'Transfer')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		row.findElement(locator).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transfer"));

		// assert transaction page is filled correctly
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("dolor sit amet");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("12.00");
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEqualTo("08.01.2023");
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEqualTo("dolor sit amet");
		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("1");

		// save transaction
		driver.findElement(By.id("button-save-transaction")).click();
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final WebElement rowAfterSave = driver.findElements(By.className("transaction-import-row")).get(0);
		assertRow(rowAfterSave, "green", "2023-01-08", "No category", "dolor sit amet", "dolor sit amet", "-12.00 €");
	}

	@Test
	void test_editNewFromTemplate()
	{
		uploadAndSetColumnSettings();

		// click new transaction button
		final List<WebElement> rows = driver.findElements(By.className("transaction-import-row"));
		final WebElement row = rows.get(0);

		row.findElement(By.className("edit-transaction-button")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		final By locator = By.xpath("//div[contains(@class, 'edit-transaction-button')]//a[contains(text(),'template')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		row.findElement(locator).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		// choose template
		driver.findElements(By.cssSelector(".template-item .btn-flat no-padding text-default"));
		driver.findElement(By.xpath("//li[contains(@class, 'template-item')]//a[contains(@href, '/templates/2/select')]")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// assert transaction page is filled correctly
		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("background-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("NameFromTemplate");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("12.00");
		assertThat(driver.findElement(By.id("transaction-datepicker")).getAttribute("value")).isEqualTo("08.01.2023");
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEqualTo("DescriptionFromTemplate");
		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("1");

		// save transaction
		driver.findElement(By.id("button-save-transaction")).click();
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final WebElement rowAfterSave = driver.findElements(By.className("transaction-import-row")).get(0);
		assertRow(rowAfterSave, "green", "2023-01-08", "No category", "dolor sit amet", "dolor sit amet", "-12.00 €");
	}

	@Test
	void test_showTransactionNameSuggestions()
	{
		uploadAndSetColumnSettings();

		final List<WebElement> rows = driver.findElements(By.className("transaction-import-row"));
		final WebElement row = rows.get(0);

		// change data
		final WebElement categorySelect = row.findElement(By.cssSelector(".category-select-wrapper .custom-select"));
		categorySelect.click();
		row.findElements(By.cssSelector(".category-select-wrapper .custom-select-item-name")).stream()
				.filter(webElement -> webElement.getText().equals("sdfdsf"))
				.findFirst().orElseThrow().click();

		final WebElement nameInput = row.findElement(By.name("name"));
		nameInput.clear();
		nameInput.click();
		nameInput.sendKeys("e");

		final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".autocomplete-content li")));

		assertThat(driver.findElements(By.cssSelector(".autocomplete-content li"))).hasSize(3);
	}

	private void uploadAndSetColumnSettings()
	{
		driver.get(helper.getUrl() + "/transactionImport");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Import from bank CSV"));

		final String csvPath = new File(getClass().getClassLoader().getResource("csv/three_entries.csv").getFile()).getAbsolutePath();

		uploadCsv(csvPath, ";", "UTF-8", "1");
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("button-cancel-csv-import")));

		assertThat(driver.findElement(By.id("csv-file-name")).getText()).isEqualTo("three_entries.csv");

		final List<WebElement> overviewRows = driver.findElements(By.cssSelector("#transaction-import-overview tr"));
		assertThat(overviewRows).hasSize(4);
		final List<WebElement> columns = overviewRows.get(1).findElements(By.tagName("td"));
		assertThat(columns).hasSize(3);
		assertThat(columns.get(0).getText()).isEqualTo("03.01.2023");
		assertThat(columns.get(1).getText()).isEqualTo("Lorem");
		assertThat(columns.get(2).getText()).isEqualTo("50.00");

		fillColumnSettings(1, "dd.MM.yyyy", 2, 3, ".", ",",2);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("button-confirm-csv-column-settings")));
	}

	private void uploadCsv(String csvPath, String separator, String encoding, String numberOfLinesToSkip)
	{
		driver.findElement(By.id("inputCsvImport")).sendKeys(csvPath);

		final WebElement separatorInput = driver.findElement(By.name("separator"));
		separatorInput.clear();
		separatorInput.sendKeys(separator);

		final WebElement encodingInput = driver.findElement(By.name("encoding"));
		encodingInput.clear();
		encodingInput.sendKeys(encoding);

		final WebElement numberOfLinesToSkipInput = driver.findElement(By.name("numberOfLinesToSkip"));
		numberOfLinesToSkipInput.clear();
		numberOfLinesToSkipInput.sendKeys(numberOfLinesToSkip);

		driver.findElement(By.id("button-confirm-csv-import")).click();
	}

	private void fillColumnSettings(int columnDate, String datePattern, int columnName, int columnAmount, String decimalSeparator, String groupingSeparator, int columnDescription)
	{
		fillColumnSettings(String.valueOf(columnDate), datePattern, String.valueOf(columnName), String.valueOf(columnAmount), decimalSeparator, groupingSeparator, String.valueOf(columnDescription));
	}

	private void fillColumnSettings(String columnDate, String datePattern, String columnName, String columnAmount, String decimalSeparator, String groupingSeparator, String columnDescription)
	{
		final WebElement columnDateInput = driver.findElement(By.name("columnDate"));
		columnDateInput.clear();
		columnDateInput.sendKeys(columnDate);

		final WebElement datePatternInput = driver.findElement(By.name("datePattern"));
		datePatternInput.clear();
		datePatternInput.sendKeys(datePattern);

		final WebElement columnNameInput = driver.findElement(By.name("columnName"));
		columnNameInput.clear();
		columnNameInput.sendKeys(columnName);

		final WebElement columnAmountInput = driver.findElement(By.name("columnAmount"));
		columnAmountInput.clear();
		columnAmountInput.sendKeys(columnAmount);

		final WebElement decimalSeparatorInput = driver.findElement(By.name("decimalSeparator"));
		decimalSeparatorInput.clear();
		decimalSeparatorInput.sendKeys(decimalSeparator);

		final WebElement groupingSeparatorInput = driver.findElement(By.name("groupingSeparator"));
		groupingSeparatorInput.clear();
		groupingSeparatorInput.sendKeys(groupingSeparator);

		final WebElement columnDescriptionInput = driver.findElement(By.name("columnDescription"));
		columnDescriptionInput.clear();
		columnDescriptionInput.sendKeys(columnDescription);

		driver.findElement(By.id("button-confirm-csv-column-settings")).click();
	}

	private static void assertRow(WebElement row, String statusColor, String date, String categoryName, String name, String description, String amount)
	{
		final List<WebElement> columns = row.findElements(By.tagName("td"));

		assertThat(columns.get(0).findElements(By.cssSelector(".banner.background-" + statusColor)))
				.hasSize(1);

		assertThat(columns.get(1).getText())
				.isEqualTo(date);

		final WebElement categoryCircle = columns.get(2).findElement(By.className("category-circle"));
		categoryName = categoryName.substring(0, 1).toUpperCase();
		assertThat(categoryCircle.findElement(By.tagName("span"))).hasFieldOrPropertyWithValue("text", categoryName);

		assertThat(columns.get(3).findElement(By.name("name")).getAttribute("value"))
				.isEqualTo(name);

		assertThat(columns.get(4).findElement(By.name("description")).getAttribute("value"))
				.isEqualTo(description);

		assertThat(columns.get(5).getText())
				.isEqualTo(amount);
	}
}