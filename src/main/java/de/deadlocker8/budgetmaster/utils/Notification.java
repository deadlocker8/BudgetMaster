package de.deadlocker8.budgetmaster.utils;

import de.thecodelabs.utils.util.Localization;

public class Notification
{
	private NotificationLevel level;
	private String messageTitle;
	private String messageHeader;
	private String messageBody;

	public Notification(NotificationLevel level, String messageHeader, String messageBody)
	{
		this.level = level;
		this.messageTitle = Localization.getString(level.getKey());
		this.messageHeader = messageHeader;
		this.messageBody = messageBody;
	}

	public NotificationLevel getLevel()
	{
		return level;
	}

	public String getMessageTitle()
	{
		return messageTitle;
	}

	public String getMessageHeader()
	{
		return messageHeader;
	}

	public String getMessageBody()
	{
		return messageBody;
	}

	@Override
	public String toString()
	{
		return "Notification{" +
				"level=" + level +
				", messageHeader='" + messageHeader + '\'' +
				", messageBody='" + messageBody + '\'' +
				'}';
	}
}