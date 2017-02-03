package de.deadlocker8.budgetmaster.logic;

public class Settings
{
	private String url;
	private String secret;
	
	public Settings()
	{
		
	}

	public String getUrl()
	{
		return url;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public String getSecret()
	{
		return secret;
	}

	public void setSecret(String secret)
	{
		this.secret = secret;
	}
}