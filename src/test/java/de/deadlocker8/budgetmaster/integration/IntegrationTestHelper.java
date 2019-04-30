package de.deadlocker8.budgetmaster.integration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class IntegrationTestHelper
{
	public static String getTextNode(WebElement e)
	{
		String text = e.getText().trim();
		List<WebElement> children = e.findElements(By.xpath("./*"));
		for(WebElement child : children)
		{
			text = text.replaceFirst(child.getText(), "").trim();
		}
		return text;
	}
}
