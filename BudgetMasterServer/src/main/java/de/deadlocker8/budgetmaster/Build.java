package de.deadlocker8.budgetmaster;


import de.thecodelabs.storage.settings.Storage;
import de.thecodelabs.storage.settings.StorageTypes;
import de.thecodelabs.storage.settings.annotation.Key;

public class Build
{
	@Key("app.name")
	private String appName;

	@Key("app.version.name")
	private String versionName;

	@Key("app.version.code")
	private String versionCode;

	@Key("app.version.date")
	private String versionDate;

	@Key("app.author")
	private String author;

	private static Build instance;

	public static Build getInstance()
	{
		if(instance == null)
		{
			instance = Storage.load(Build.class.getClassLoader().getResourceAsStream("application.properties"), StorageTypes.PROPERTIES, Build.class);
		}
		return instance;
	}

	public Build(String appName, String versionName, String versionCode, String versionDate, String author)
	{
		this.appName = appName;
		this.versionName = versionName;
		this.versionCode = versionCode;
		this.versionDate = versionDate;
		this.author = author;
	}

	public Build()
	{
	}

	public String getAppName()
	{
		return appName;
	}

	public String getVersionName()
	{
		return versionName;
	}

	public String getVersionCode()
	{
		return versionCode;
	}

	public String getVersionDate()
	{
		return versionDate;
	}

	public String getAuthor()
	{
		return author;
	}

	@Override
	public String toString()
	{
		return "Build{" +
				"appName='" + appName + '\'' +
				", versionName='" + versionName + '\'' +
				", versionCode='" + versionCode + '\'' +
				", versionDate='" + versionDate + '\'' +
				", author='" + author + '\'' +
				'}';
	}
}
