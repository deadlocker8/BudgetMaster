package de.deadlocker8.budgetmaster.integration.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionTestHelper
{
	public static void assertTransactionColumns(List<WebElement> columns, String shortDate, String categoryName, String categoryColor, boolean repeatIconVisible, boolean transferIconIsVisible, String name, String description, String amount)
	{
		// date
		assertThat(columns.get(0)).hasFieldOrPropertyWithValue("text", shortDate);

		// category
		final WebElement categoryCircle = columns.get(1).findElement(By.className("category-circle"));
		assertThat(categoryCircle.getCssValue("background-color")).isEqualTo(categoryColor);
		categoryName = categoryName.substring(0, 1).toUpperCase();
		assertThat(categoryCircle.findElement(By.tagName("span"))).hasFieldOrPropertyWithValue("text", categoryName);

		// icon
		final List<WebElement> icons = columns.get(2).findElements(By.tagName("i"));
		assertThat(icons).hasSize(1);
		assertThat(icons.get(0).isDisplayed()).isEqualTo(repeatIconVisible || transferIconIsVisible);
		if(repeatIconVisible)
		{
			assertThat(icons.get(0)).hasFieldOrPropertyWithValue("text", "repeat");
		}
		else if(transferIconIsVisible)
		{
			assertThat(icons.get(0)).hasFieldOrPropertyWithValue("text", "swap_horiz");
		}

		// name
		assertThat(columns.get(3).findElement(By.className("transaction-text")).getText())
				.isEqualTo(name);

		//description
		assertThat(columns.get(3).findElement(By.className("italic")).getText())
				.isEqualTo(description);

		// amount
		assertThat(columns.get(4).getText()).contains(amount);
	}

	public static void selectOptionFromDropdown(WebDriver driver, By selectLocator, String nameToSelect)
	{
		WebElement select = driver.findElement(selectLocator);
		select.findElement(By.className("select-dropdown")).click();

		WebElement itemToSelect = select.findElement(By.xpath(".//ul/li/span[text()='" + nameToSelect + "']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", itemToSelect);
		itemToSelect.click();
	}

	public static void selectCategoryByName(WebDriver driver, String categoryName)
	{
		final WebElement categorySelect = driver.findElement(By.className("custom-select"));
		categorySelect.click();
		driver.findElements(By.className("custom-select-item-name")).stream()
				.filter(webElement -> webElement.getText().equals(categoryName))
				.findFirst().orElseThrow().click();
	}
}
