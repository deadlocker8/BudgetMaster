package de.deadlocker8.budgetmaster.logic;

import java.util.ArrayList;

public class Settings
{
	private String url;
	private String secret;
	private String currency;
	private boolean restActivated;
	private ArrayList<String> trustedHosts;
	
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
	
	public ArrayList<String> getTrustedHosts()
	{
		return trustedHosts;
	}

	public void setTrustedHosts(ArrayList<String> trustedHosts)
	{
		this.trustedHosts = trustedHosts;
	}

	@Override
	public String toString()
	{
		return "Settings [url=" + url + ", secret=" + secret + ", currency=" + currency + ", restActivated=" + restActivated + ", trustedHosts=" + trustedHosts + "]";
	}
}