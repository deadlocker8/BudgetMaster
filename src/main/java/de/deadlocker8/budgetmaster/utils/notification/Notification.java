package de.deadlocker8.budgetmaster.utils.notification;

public class Notification
{
	private final String message;
	private final String icon;
	private final String backgroundColor;
	private final String textColor;

	public Notification(String message, NotificationType notificationType)
	{
		this.message = message;
		this.icon = notificationType.getIcon();
		this.backgroundColor = notificationType.getBackgroundColor();
		this.textColor = notificationType.getTextColor();
	}

	public Notification(String message, String icon, String backgroundColor, String textColor)
	{
		this.message = message;
		this.icon = icon;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
	}

	public String getMessage()
	{
		return message;
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

	@Override
	public String toString()
	{
		return "Notification{" +
				"message='" + message + '\'' +
				", icon='" + icon + '\'' +
				", backgroundColor='" + backgroundColor + '\'' +
				", textColor='" + textColor + '\'' +
				'}';
	}
}
