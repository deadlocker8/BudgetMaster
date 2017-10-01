package de.deadlocker8.budgetmaster.logic.updater;

public class VersionInformation
{
	private int versionCode;
	private String versionName;
	private String date;
	
	public VersionInformation(int versionCode, String versionName, String date)
	{		
		this.versionCode = versionCode;
		this.versionName = versionName;
		this.date = date;
	}
	
	public VersionInformation()
	{
		this.versionCode = -1;
	}

	public int getVersionCode()
	{
		return versionCode;
	}

	public void setVersionCode(int versionCode)
	{
		this.versionCode = versionCode;
	}

	public String getVersionName()
	{
		return versionName;
	}

	public void setVersionName(String versionName)
	{
		this.versionName = versionName;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}
	
	public boolean isComplete()
	{
		return versionCode != -1 && versionName != null && date != null;
	}

	@Override
	public String toString()
	{
		return "VersionInformation [versionCode=" + versionCode + ", versionName=" + versionName + ", date=" + date + "]";
	}
}