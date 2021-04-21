package de.deadlocker8.budgetmaster.services;

public enum EntityType
{
	ACCOUNT("account_balance"),
	TRANSACTION("list"),
	CATEGORY("label"),
	TEMPLATE("file_copy"),
	CHART("show_chart"),
	IMAGE("image");

	private final String icon;

	EntityType(String icon)
	{
		this.icon = icon;
	}

	public String getIcon()
	{
		return icon;
	}

	public String getLocalizationKey()
	{
		return "entity." + this.name().toLowerCase();
	}
}
