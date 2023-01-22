package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.utils.LocalizedEnum;

public enum EntityType implements LocalizedEnum
{
	HOME("home", "background-blue", ImportRequired.NONE, "categories", "category"),
	ACCOUNT("account_balance", "background-red", ImportRequired.REQUIRED, "accounts", "account"),
	TRANSACTION("list", "background-blue-baby", ImportRequired.REQUIRED, "transactions", "transaction"),
	TEMPLATE("file_copy", "background-orange-dark", ImportRequired.OPTIONAL, "templates", "template"),
	CHART("show_chart", "background-purple", ImportRequired.OPTIONAL, "charts", "chart"),
	REPORT("description", "background-green", ImportRequired.NONE, null, null),
	CATEGORY("label", "background-orange", ImportRequired.REQUIRED, "categories", "category"),
	TAGS("local_offer", "background-grey", ImportRequired.NONE, "tags", "tag"),
	STATISTICS("insert_chart", "background-grey", ImportRequired.NONE, null, null),
	SETTINGS("settings", "background-red", ImportRequired.NONE, null, null),
	IMAGE("image", "background-grey", ImportRequired.REQUIRED, "images", "image"),
	HOTKEYS("keyboard", "background-grey", ImportRequired.NONE, null, null),
	ABOUT("info", "background-grey", ImportRequired.NONE, null, null),
	TEMPLATE_GROUP("folder", "background-orange-dark", ImportRequired.OPTIONAL, "template groups", "template group"),
	ICON("icon", "background-grey", ImportRequired.NONE, "icons", "icon"),
	TRANSACTION_NAME_KEYWORD("transaction_name_keyword", "background-grey", ImportRequired.NONE, "keywords", "keyword"),
	RECURRING_TRANSACTIONS("repeat", "background-orange-dark", ImportRequired.NONE, "recurring", "recurring"),
	TRANSACTION_IMPORT("fas fa-file-csv", "background-orange-dark", ImportRequired.NONE, "transactionImport", "transactionImport");

	public enum ImportRequired
	{
		REQUIRED,
		OPTIONAL,
		NONE;
	}

	private final String icon;
	private final String color;
	private final ImportRequired importRequired;
	private final String allItemsName;
	private final String singleItemName;

	EntityType(String icon, String color, ImportRequired importRequired, String allItemsName, String singleItemName)
	{
		this.icon = icon;
		this.color = color;
		this.importRequired = importRequired;
		this.allItemsName = allItemsName;
		this.singleItemName = singleItemName;
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

	public String getAllItemsName()
	{
		return allItemsName;
	}

	public String getSingleItemName()
	{
		return singleItemName;
	}

	public String getColorAsTextColor()
	{
		return color.replace("background", "text");
	}

	@Override
	public String getLocalizationKey()
	{
		return "entity." + this.name().toLowerCase();
	}
}
