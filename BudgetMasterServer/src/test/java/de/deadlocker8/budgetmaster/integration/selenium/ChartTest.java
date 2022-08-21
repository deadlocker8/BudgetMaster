package de.deadlocker8.budgetmaster.integration.selenium;

import de.deadlocker8.budgetmaster.authentication.UserService;
import de.deadlocker8.budgetmaster.charts.ChartDisplayType;
import de.deadlocker8.budgetmaster.charts.ChartGroupType;
import de.deadlocker8.budgetmaster.integration.helpers.IntegrationTestHelper;
import de.deadlocker8.budgetmaster.integration.helpers.SeleniumTestBase;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChartTest extends SeleniumTestBase
{
	private final String SELECTOR_ACTIVE_DISPLAY_TYPE = ".button-display-type.active";
	private final String SELECTOR_ACTIVE_GROUP_TYPE = ".button-group-type.active";
	private final String SELECTOR_VISIBLE_CHART_PREVIEWS = ".chart-preview-column:not(.hidden)";
	private final String SELECTOR_ACTIVE_CHART_PREVIEWS = ".chart-preview.active";

	private static IntegrationTestHelper helper;

	@Override
	protected void importDatabaseOnce()
	{
		helper = new IntegrationTestHelper(driver, port);
		helper.start();
		helper.login(UserService.DEFAULT_PASSWORD);
		helper.hideBackupReminder();
		helper.hideWhatsNewDialog();

		String path = getClass().getClassLoader().getResource("SearchDatabase.json").getFile().replace("/", File.separator);
		helper.uploadDatabase(path);
	}

	@Test
	void test_defaultSelection()
	{
		driver.get(helper.getUrl() + "/charts");

		// check display type
		assertThat(getSelectedType(SELECTOR_ACTIVE_DISPLAY_TYPE)).isEqualTo(ChartDisplayType.BAR.name());
		assertThat(driver.findElement(By.id("buttonCustomCharts")).isDisplayed()).isFalse();

		// check group type
		assertThat(driver.findElement(By.id("chart-group-type-buttons")).isDisplayed()).isTrue();
		assertThat(getSelectedType(SELECTOR_ACTIVE_GROUP_TYPE)).isEqualTo(ChartGroupType.MONTH.name());

		// check displayed chart previews
		final List<WebElement> displayedChartPreviews = driver.findElements(By.cssSelector(SELECTOR_VISIBLE_CHART_PREVIEWS));
		assertThat(displayedChartPreviews)
				.hasSize(3);
		assertThat(displayedChartPreviews.get(0).findElement(By.cssSelector(".card-action span")).getText())
				.isEqualTo("Incomes/Expenditures");

		// filter
		assertThat(driver.findElement(By.id("filterActiveBadge")).isDisplayed()).isFalse();

		// button
		assertThat(driver.findElement(By.name("buttonSave")).isEnabled()).isFalse();
	}

	@Test
	void test_selectDisplayType()
	{
		driver.get(helper.getUrl() + "/charts");

		final String buttonSelector = ".button-display-type[data-value='" + ChartDisplayType.LINE.name() + "']";
		driver.findElement(By.cssSelector(buttonSelector)).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(buttonSelector), "class", "active"));

		// check display type
		assertThat(getSelectedType(SELECTOR_ACTIVE_DISPLAY_TYPE)).isEqualTo(ChartDisplayType.LINE.name());
		assertThat(driver.findElement(By.id("buttonCustomCharts")).isDisplayed()).isFalse();

		// check group type
		assertThat(driver.findElement(By.id("chart-group-type-buttons")).isDisplayed()).isTrue();
		assertThat(getSelectedType(SELECTOR_ACTIVE_GROUP_TYPE)).isEqualTo(ChartGroupType.MONTH.name());

		// check displayed chart previews
		final List<WebElement> displayedChartPreviews = driver.findElements(By.cssSelector(SELECTOR_VISIBLE_CHART_PREVIEWS));
		assertThat(displayedChartPreviews)
				.hasSize(1);
		assertThat(displayedChartPreviews.get(0).findElement(By.cssSelector(".card-action span")).getText())
				.isEqualTo("Incomes/Expenditures");

		// filter
		assertThat(driver.findElement(By.id("filterActiveBadge")).isDisplayed()).isFalse();

		// button
		assertThat(driver.findElement(By.name("buttonSave")).isEnabled()).isFalse();
	}

	@Test
	void test_hideGroupTypeIfOnlyOneDistinct()
	{
		driver.get(helper.getUrl() + "/charts");

		final String buttonSelector = ".button-display-type[data-value='" + ChartDisplayType.PIE.name() + "']";
		driver.findElement(By.cssSelector(buttonSelector)).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(buttonSelector), "class", "active"));

		// check display type
		assertThat(getSelectedType(SELECTOR_ACTIVE_DISPLAY_TYPE)).isEqualTo(ChartDisplayType.PIE.name());

		// check group type
		assertThat(driver.findElement(By.id("chart-group-type-buttons")).isDisplayed()).isFalse();
	}

	@Test
	void test_displayGroupTypeAfterHiding()
	{
		driver.get(helper.getUrl() + "/charts");

		String buttonSelector = ".button-display-type[data-value='" + ChartDisplayType.PIE.name() + "']";
		driver.findElement(By.cssSelector(buttonSelector)).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(buttonSelector), "class", "active"));

		// check display type
		assertThat(getSelectedType(SELECTOR_ACTIVE_DISPLAY_TYPE)).isEqualTo(ChartDisplayType.PIE.name());

		// check group type
		assertThat(driver.findElement(By.id("chart-group-type-buttons")).isDisplayed()).isFalse();

		buttonSelector = ".button-display-type[data-value='" + ChartDisplayType.BAR.name() + "']";
		driver.findElement(By.cssSelector(buttonSelector)).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(buttonSelector), "class", "active"));

		// check display type
		assertThat(getSelectedType(SELECTOR_ACTIVE_DISPLAY_TYPE)).isEqualTo(ChartDisplayType.BAR.name());

		// check group type
		assertThat(driver.findElement(By.id("chart-group-type-buttons")).isDisplayed()).isTrue();
	}

	@Test
	void test_selectGroupType()
	{
		driver.get(helper.getUrl() + "/charts");

		final String buttonSelector = ".button-group-type[data-value='" + ChartGroupType.YEAR.name() + "']";
		driver.findElement(By.cssSelector(buttonSelector)).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(buttonSelector), "class", "active"));

		// check group type
		assertThat(driver.findElement(By.id("chart-group-type-buttons")).isDisplayed()).isTrue();
		assertThat(getSelectedType(SELECTOR_ACTIVE_GROUP_TYPE)).isEqualTo(ChartGroupType.YEAR.name());

		// check displayed chart previews
		final List<WebElement> displayedChartPreviews = driver.findElements(By.cssSelector(SELECTOR_VISIBLE_CHART_PREVIEWS));
		assertThat(displayedChartPreviews)
				.hasSize(3);
		assertThat(displayedChartPreviews.get(0).findElement(By.cssSelector(".card-action span")).getText())
				.isEqualTo("Average monthly incomes/expenditures");

		// filter
		assertThat(driver.findElement(By.id("filterActiveBadge")).isDisplayed()).isFalse();

		// button
		assertThat(driver.findElement(By.name("buttonSave")).isEnabled()).isFalse();
	}

	@Test
	void test_selectChartEnabledButton()
	{
		driver.get(helper.getUrl() + "/charts");

		final String chartPreviewSelector = ".chart-preview-column[data-id='6']";
		driver.findElement(By.cssSelector(chartPreviewSelector)).click();

		final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(chartPreviewSelector + " .chart-preview"), "class", "active"));

		// check displayed chart previews
		final List<WebElement> activeChartPreviews = driver.findElements(By.cssSelector(SELECTOR_ACTIVE_CHART_PREVIEWS));
		assertThat(activeChartPreviews)
				.hasSize(1);
		assertThat(activeChartPreviews.get(0).findElement(By.cssSelector(".card-action span")).getText())
				.isEqualTo("Incomes/Expenditures per category");

		// button
		assertThat(driver.findElement(By.name("buttonSave")).isEnabled()).isTrue();
	}

	@Test
	void test_selectDisplayTypeAfterSelectingChartDisablesButton()
	{
		driver.get(helper.getUrl() + "/charts");

		final String chartPreviewSelector = ".chart-preview-column[data-id='6']";
		driver.findElement(By.cssSelector(chartPreviewSelector)).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(chartPreviewSelector + " .chart-preview"), "class", "active"));

		final String buttonSelector = ".button-display-type[data-value='" + ChartDisplayType.LINE.name() + "']";
		driver.findElement(By.cssSelector(buttonSelector)).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(buttonSelector), "class", "active"));

		// check displayed chart previews
		final List<WebElement> activeChartPreviews = driver.findElements(By.cssSelector(SELECTOR_ACTIVE_CHART_PREVIEWS));
		assertThat(activeChartPreviews).isEmpty();

		// button
		assertThat(driver.findElement(By.name("buttonSave")).isEnabled()).isFalse();
	}

	@Test
	void test_showFilterBadge()
	{
		driver.get(helper.getUrl() + "/charts");

		assertThat(driver.findElement(By.id("filterActiveBadge")).isDisplayed()).isFalse();

		driver.findElement(By.id("chart-filter-container")).click();
		driver.findElement(By.id("section-type")).click();
		final WebElement checkBox = driver.findElement(By.cssSelector("#section-type .text-default"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkBox);
		checkBox.click();

		final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("filter-button-reset")));

		assertThat(driver.findElement(By.id("filterActiveBadge")).isDisplayed()).isTrue();
	}

	@Test
	void test_hideFilterBadgeOnReset()
	{
		driver.get(helper.getUrl() + "/charts");

		driver.findElement(By.id("chart-filter-container")).click();
		driver.findElement(By.id("section-type")).click();
		final WebElement checkBox = driver.findElement(By.cssSelector("#section-type .text-default"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkBox);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#section-type .text-default")));
		checkBox.click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("filter-button-reset")));

		driver.findElement(By.className("filter-button-reset")).click();
		assertThat(driver.findElement(By.id("filterActiveBadge")).isDisplayed()).isFalse();
	}

	@Test
	void test_showFilterBadgeOnShowChartSettings()
	{
		driver.get(helper.getUrl() + "/charts");

		final String chartPreviewSelector = ".chart-preview-column[data-id='6']";
		driver.findElement(By.cssSelector(chartPreviewSelector)).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(chartPreviewSelector + " .chart-preview"), "class", "active"));

		driver.findElement(By.id("chart-filter-container")).click();
		driver.findElement(By.id("section-type")).click();
		final WebElement checkBox = driver.findElement(By.cssSelector("#section-type .text-default"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkBox);
		checkBox.click();

		driver.findElement(By.name("buttonSave")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("chart-canvas")));

		driver.findElement(By.id("buttonShowChartSettings")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(chartPreviewSelector + " .chart-preview"), "class", "active"));

		assertThat(driver.findElement(By.id("filterActiveBadge")).isDisplayed()).isTrue();
	}

	@Test
	void test_showManageButtonForCustomCharts()
	{
		driver.get(helper.getUrl() + "/charts");

		final String buttonSelector = ".button-display-type[data-value='" + ChartDisplayType.CUSTOM.name() + "']";
		driver.findElement(By.cssSelector(buttonSelector)).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(buttonSelector), "class", "active"));

		// check display type
		assertThat(getSelectedType(SELECTOR_ACTIVE_DISPLAY_TYPE)).isEqualTo(ChartDisplayType.CUSTOM.name());

		assertThat(driver.findElement(By.id("buttonCustomCharts")).isDisplayed()).isTrue();
	}

	@Test
	void test_showChart()
	{
		driver.get(helper.getUrl() + "/charts");

		final String chartPreviewSelector = ".chart-preview-column[data-id='6']";
		driver.findElement(By.cssSelector(chartPreviewSelector)).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(chartPreviewSelector + " .chart-preview"), "class", "active"));

		driver.findElement(By.name("buttonSave")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("chart-canvas")));

		assertThat(driver.findElements(By.cssSelector(".chart-canvas .plot-container"))).hasSize(1);
	}

	@Test
	void test_enabledButtonAfterShowChart()
	{
		driver.get(helper.getUrl() + "/charts");

		final String chartPreviewSelector = ".chart-preview-column[data-id='6']";
		driver.findElement(By.cssSelector(chartPreviewSelector)).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(chartPreviewSelector + " .chart-preview"), "class", "active"));

		driver.findElement(By.name("buttonSave")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("chart-canvas")));

		driver.findElement(By.id("buttonShowChartSettings")).click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeContains(By.cssSelector(chartPreviewSelector + " .chart-preview"), "class", "active"));

		assertThat(driver.findElement(By.name("buttonSave")).isEnabled()).isTrue();
	}

	private String getSelectedType(String selector)
	{
		final List<WebElement> activeTypeButtons = driver.findElements(By.cssSelector(selector));
		assertThat(activeTypeButtons)
				.hasSize(1);

		return activeTypeButtons.get(0).getAttribute("data-value");
	}
}