package de.deadlocker8.budgetmaster.statistics;


public class StatisticItem
{
	private final String icon;
	private final String text;
	private final String backgroundColor;
	private final String textColor;

	public StatisticItem(String icon, String text, String backgroundColor, String textColor)
	{
		this.icon = icon;
		this.text = text;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
	}

	public String getIcon()
	{
		return icon;
	}

	public String getText()
	{
		return text;
	}

	public String getBackgroundColor()
	{
		return backgroundColor;
	}

	public String getTextColor()
	{
		return textColor;
	}

	@Override
	public String toString()
	{
		return "StatisticItem{" +
				"icon='" + icon + '\'' +
				", text='" + text + '\'' +
				", backgroundColor=" + backgroundColor +
				", textColor=" + textColor +
				'}';
	}
}
