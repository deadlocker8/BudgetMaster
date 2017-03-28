package de.deadlocker8.budgetmaster.logic;

public class Settings
{
	private String url;
	private String secret;
	private String currency;
	private boolean restActivated;
	
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

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public boolean isRestActivated()
	{
		return restActivated;
	}

	public void setRestActivated(boolean restActivated)
	{
		this.restActivated = restActivated;
	}

	@Override
	public String toString()
	{
		return "Settings [url=" + url + ", secret=" + secret + ", currency=" + currency + ", restActivated=" + restActivated + "]";
	}
}