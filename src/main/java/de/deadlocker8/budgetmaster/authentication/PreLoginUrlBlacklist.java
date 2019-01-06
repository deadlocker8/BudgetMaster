package de.deadlocker8.budgetmaster.authentication;

import java.util.ArrayList;
import java.util.List;

public class PreLoginUrlBlacklist
{
	private List<String> blacklist;

	public PreLoginUrlBlacklist()
	{
		blacklist = new ArrayList<>();
		blacklist.add("login");
		blacklist.add("performUpdate");
	}

	public boolean isBlacklisted(String preLoginUrl)
	{
		for(String entry : blacklist)
		{
			if(preLoginUrl.contains(entry))
			{
				return true;
			}
		}

		return false;
	}
}
