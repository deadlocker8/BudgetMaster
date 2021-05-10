package de.deadlocker8.budgetmaster.services;

public enum EntityType
{
	HOME("home", "background-blue", ImportRequired.NONE),
	ACCOUNT("account_balance", "background-red", ImportRequired.REQUIRED),
	TRANSACTION("list", "background-blue-baby", ImportRequired.REQUIRED),
	TEMPLATE("file_copy", "background-orange-dark", ImportRequired.OPTIONAL),
	CHART("show_chart", "background-purple", ImportRequired.OPTIONAL),
	REPORT("description", "background-green", ImportRequired.NONE),
	CATEGORY("label", "background-orange", ImportRequired.REQUIRED),
	STATISTICS("insert_chart", "background-grey", ImportRequired.NONE),
	SETTINGS("settings", "background-red", ImportRequired.NONE),
	IMAGE("image", "background-grey", ImportRequired.REQUIRED),
	HOTKEYS("keyboard", "background-grey", ImportRequired.NONE),
	ABOUT("info", "background-grey", ImportRequired.NONE);


	public enum ImportRequired
	{
		REQUIRED,
		OPTIONAL,
		NONE;
	}

	private final String icon;
	private final String color;
	private final ImportRequired importRequired;

	EntityType(String icon, String color, ImportRequired importRequired)
	{
		this.icon = icon;
		this.color = color;
		this.importRequired = importRequired;
	}

	public String getIcon()
	{
		return icon;
	}

	public String getColor()
	{
		return color;
	}

	public ImportRequired getImportRequired()
	{
		return importRequired;
	}

	public String getColorAsTextColor()
	{
		return color.replace("background", "text");
	}

	public String getLocalizationKey()
	{
		return "entity." + this.name().toLowerCase();
	}
}
