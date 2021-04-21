package de.deadlocker8.budgetmaster.services;

public enum EntityType
{
	ACCOUNT("account_balance", "background-red"),
	TRANSACTION("list", "background-blue-baby"),
	CATEGORY("label", "background-orange"),
	TEMPLATE("file_copy", "background-orange-dark"),
	CHART("show_chart", "background-purple"),
	IMAGE("image", "background-grey");

	private final String icon;
	private final String color;

	EntityType(String icon, String color)
	{
		this.icon = icon;
		this.color = color;
	}

	public String getIcon()
	{
		return icon;
	}

	public String getColor()
	{
		return color;
	}

	public String getLocalizationKey()
	{
		return "entity." + this.name().toLowerCase();
	}
}
