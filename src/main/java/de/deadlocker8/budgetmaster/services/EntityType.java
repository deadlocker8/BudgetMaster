package de.deadlocker8.budgetmaster.services;

public enum EntityType
{
	HOME("home", "background-blue"),
	ACCOUNT("account_balance", "background-red"),
	TRANSACTION("list", "background-blue-baby"),
	TEMPLATE("file_copy", "background-orange-dark"),
	CHART("show_chart", "background-purple"),
	REPORT("description", "background-green"),
	CATEGORY("label", "background-orange"),
	STATISTICS("insert_chart", "background-grey"),
	SETTINGS("settings", "background-red"),
	IMAGE("image", "background-grey"),
	HOTKEYS("keyboard", "background-grey"),
	ABOUT("info", "background-grey");


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
