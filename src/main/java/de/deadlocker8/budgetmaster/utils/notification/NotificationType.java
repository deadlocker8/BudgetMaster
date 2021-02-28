package de.deadlocker8.budgetmaster.utils.notification;

public enum NotificationType
{
	INFO("fas fa-info", "background-grey", "text-black"),
	SUCCESS("fas fa-check", "background-green", "text-white"),
	WARNING("fas fa-exclamation-triangle", "background-yellow", "text-black"),
	ERROR("fas fa-exclamation-triangle", "background-red", "text-white");

	private final String icon;
	private final String backgroundColor;
	private final String textColor;

	NotificationType(String icon, String backgroundColor, String textColor)
	{
		this.icon = icon;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
	}

	public String getIcon()
	{
		return icon;
	}

	public String getBackgroundColor()
	{
		return backgroundColor;
	}

	public String getTextColor()
	{
		return textColor;
	}
}
