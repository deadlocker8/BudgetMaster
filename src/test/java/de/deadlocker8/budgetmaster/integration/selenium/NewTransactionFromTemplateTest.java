package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import de.thecodelabs.utils.util.Localization;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NewTransactionFromTemplateTest extends SeleniumTestBase
{
	private IntegrationTestHelper helper;

	@BeforeAll
	public void beforeAll()
	{
		helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		final Account account1 = new Account("DefaultAccount0815", AccountType.CUSTOM);
		final Account account2 = new Account("Account2", AccountType.CUSTOM);

		helper.uploadDatabase(path, Arrays.asList("DefaultAccount0815", "sfsdf"), List.of(account1, account2));
	}

	@BeforeEach
	public void beforeEach()
	{
		driver.get(helper.getUrl() + "/transactions");
		driver.findElement(By.id("button-new-transaction")).click();
	}

	@Test
	void test_newTransactionFromTemplate_FullTemplate()
	{
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		final By locator = By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'From template')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		driver.findElement(locator).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		driver.findElements(By.cssSelector(".template-item .btn-flat no-padding text-default"));
		driver.findElement(By.xpath("//li[contains(@class, 'template-item')]//a[contains(@href, '/templates/2/select')]")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// assert
		assertThat(driver.findElement(By.className("buttonExpenditure")).getAttribute("class")).contains("background-red");
		assertThat(driver.findElement(By.id("transaction-name")).getAttribute("value")).isEqualTo("NameFromTemplate");
		assertThat(driver.findElement(By.id("transaction-amount")).getAttribute("value")).isEqualTo("15.00");
		assertThat(driver.findElement(By.id("transaction-description")).getAttribute("value")).isEqualTo("DescriptionFromTemplate");
		assertThat(driver.findElement(By.cssSelector(".category-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("1");

		final List<WebElement> chips = driver.findElements(By.cssSelector("#transaction-chips .chip"));
		assertThat(chips).hasSize(1);
		assertThat(chips.get(0)).hasFieldOrPropertyWithValue("text", "TagFromTemplate\nclose");

		assertThat(driver.findElement(By.cssSelector(".account-select-wrapper .custom-select-selected-item .category-circle")).getAttribute("data-value")).isEqualTo("3");
	}

	@Test
	void test_newTransactionFromTemplate_OnlyIncome()
	{
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		final By locator = By.xpath("//div[contains(@class, 'new-transaction-button')]//a[contains(text(),'From template')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		driver.findElement(locator).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		driver.findElements(By.cssSelector(".template-item .btn-flat no-padding text-default"));
		driver.findElement(By.xpath("//li[contains(@class, 'template-item')]//a[contains(@href, '/templates/1/select')]")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));

		// assert
		assertThat(driver.findElement(By.className("buttonIncome")).getAttribute("class")).contains("background-green");
	}

	@Test
	void test_selectTemplateHotkeys_initialSelect()
	{
		driver.get(helper.getUrl() + "/templates");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		final List<WebElement> templateItemHeaders = driver.findElements(By.cssSelector(".template-item .collapsible-header"));

		// assert
		assertThat(templateItemHeaders).hasSize(2);
		assertThat(templateItemHeaders.get(0).getAttribute("class")).contains("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).doesNotContain("template-selected");
	}

	@Test
	void test_selectTemplateHotkeys_keyDown()
	{
		driver.get(helper.getUrl() + "/templates");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		final List<WebElement> templateItemHeaders = driver.findElements(By.cssSelector(".template-item .collapsible-header"));

		assertThat(templateItemHeaders.get(0).getAttribute("class")).contains("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).doesNotContain("template-selected");

		driver.findElement(By.id("searchTemplate")).sendKeys(Keys.ARROW_DOWN);

		assertThat(templateItemHeaders.get(0).getAttribute("class")).doesNotContain("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).contains("template-selected");
	}

	@Test
	void test_selectTemplateHotkeys_keyDown_goBackToTopFromLastItem()
	{
		driver.get(helper.getUrl() + "/templates");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		final List<WebElement> templateItemHeaders = driver.findElements(By.cssSelector(".template-item .collapsible-header"));

		assertThat(templateItemHeaders.get(0).getAttribute("class")).contains("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).doesNotContain("template-selected");

		driver.findElement(By.id("searchTemplate")).sendKeys(Keys.ARROW_DOWN);
		driver.findElement(By.id("searchTemplate")).sendKeys(Keys.ARROW_DOWN);

		assertThat(templateItemHeaders.get(0).getAttribute("class")).contains("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).doesNotContain("template-selected");
	}

	@Test
	void test_selectTemplateHotkeys_keyUp_goBackToBottomFromFirstItem()
	{
		driver.get(helper.getUrl() + "/templates");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		final List<WebElement> templateItemHeaders = driver.findElements(By.cssSelector(".template-item .collapsible-header"));

		assertThat(templateItemHeaders.get(0).getAttribute("class")).contains("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).doesNotContain("template-selected");

		driver.findElement(By.id("searchTemplate")).sendKeys(Keys.ARROW_UP);

		assertThat(templateItemHeaders.get(0).getAttribute("class")).doesNotContain("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).contains("template-selected");
	}

	@Test
	void test_selectTemplateHotkeys_keyUp()
	{
		driver.get(helper.getUrl() + "/templates");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		final List<WebElement> templateItemHeaders = driver.findElements(By.cssSelector(".template-item .collapsible-header"));

		assertThat(templateItemHeaders.get(0).getAttribute("class")).contains("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).doesNotContain("template-selected");

		driver.findElement(By.id("searchTemplate")).sendKeys(Keys.ARROW_UP);
		driver.findElement(By.id("searchTemplate")).sendKeys(Keys.ARROW_UP);

		assertThat(templateItemHeaders.get(0).getAttribute("class")).contains("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).doesNotContain("template-selected");
	}

	@Test
	void test_selectTemplateHotkeys_confirmSelection()
	{
		driver.get(helper.getUrl() + "/templates");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		final List<WebElement> templateItemHeaders = driver.findElements(By.cssSelector(".template-item .collapsible-header"));

		assertThat(templateItemHeaders.get(0).getAttribute("class")).contains("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).doesNotContain("template-selected");

		driver.findElement(By.id("searchTemplate")).sendKeys(Keys.ENTER);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "New Transaction"));
	}

	@Test
	void test_selectTemplateHotkeys_searchContainsSelection()
	{
		driver.get(helper.getUrl() + "/templates");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		driver.findElement(By.id("searchTemplate")).sendKeys(Keys.ARROW_DOWN);

		List<WebElement> templateItemHeaders = driver.findElements(By.cssSelector(".template-item .collapsible-header"));
		assertThat(templateItemHeaders.get(0).getAttribute("class")).doesNotContain("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).contains("template-selected");

		driver.findElement(By.id("searchTemplate")).sendKeys("emp");

		// assert
		templateItemHeaders = driver.findElements(By.cssSelector(".template-item .collapsible-header"));
		assertThat(templateItemHeaders).hasSize(2);
		assertThat(templateItemHeaders.get(0).getAttribute("class")).doesNotContain("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).contains("template-selected");
	}

	@Test
	void test_selectTemplateHotkeys_searchNotContainsSelection()
	{
		driver.get(helper.getUrl() + "/templates");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		driver.findElement(By.id("searchTemplate")).sendKeys(Keys.ARROW_DOWN);

		List<WebElement> templateItemHeaders = driver.findElements(By.cssSelector(".template-item .collapsible-header"));
		assertThat(templateItemHeaders.get(0).getAttribute("class")).doesNotContain("template-selected");
		assertThat(templateItemHeaders.get(1).getAttribute("class")).contains("template-selected");

		driver.findElement(By.id("searchTemplate")).sendKeys("Income");

		// assert
		templateItemHeaders = driver.findElements(By.cssSelector(".template-item:not(.hidden) .collapsible-header"));
		assertThat(templateItemHeaders).hasSize(1);
		assertThat(templateItemHeaders.get(0).getAttribute("class")).contains("template-selected");
	}

	@Test
	void test_selectTemplateHotkeys_dontBlockEnterInGlobalSearch()
	{
		driver.get(helper.getUrl() + "/templates");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), "Templates"));

		WebElement inputSearch = driver.findElement(By.id("search"));
		inputSearch.sendKeys("e");
		inputSearch.sendKeys(Keys.ENTER);

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		String expected = Localization.getString("menu.search.results", 24);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".headline"), expected));
	}
}