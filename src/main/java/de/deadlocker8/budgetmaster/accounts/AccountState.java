package de.deadlocker8.budgetmaster.accounts;


import de.deadlocker8.budgetmaster.utils.LocalizedEnum;

public enum AccountState implements LocalizedEnum
{
	FULL_ACCESS("fas fa-edit", "account.state.full.access"),
	READ_ONLY("fas fa-lock", "account.state.read.only"),
	HIDDEN("far fa-eye-slash", "account.state.hidden");

	private final String icon;
	private final String localizationKey;

	AccountState(String icon, String localizationKey)
	{
		this.icon = icon;
		this.localizationKey = localizationKey;
	}

	public String getIcon()
	{
		return icon;
	}

	@Override
	public String getLocalizationKey()
	{
		return localizationKey;
	}

	@Override
	public String toString()
	{
		return "AccountState{" +
				"icon='" + icon + '\'' +
				", localizationKey='" + localizationKey + '\'' +
				"} " + super.toString();
	}
}
