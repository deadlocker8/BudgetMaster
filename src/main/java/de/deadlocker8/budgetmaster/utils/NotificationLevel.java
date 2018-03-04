package de.deadlocker8.budgetmaster.utils;

public enum NotificationLevel
{
	INFO("title.info"),
	WARNING("title.warning"),
	ERROR("title.error");

	private String key;

	NotificationLevel(String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}

	@Override
	public String toString()
	{
		return "NotificationLevel{" +
				"key='" + key + '\'' +
				'}';
	}
}
