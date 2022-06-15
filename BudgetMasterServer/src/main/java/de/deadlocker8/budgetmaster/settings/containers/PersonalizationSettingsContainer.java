package de.deadlocker8.budgetmaster.settings.containers;

import de.deadlocker8.budgetmaster.utils.LanguageType;
import org.springframework.validation.Errors;

public final class PersonalizationSettingsContainer implements SettingsContainer
{
	private final String language;
	private final String currency;
	private Boolean useDarkTheme;
	private Boolean showCategoriesAsCircles;
	private final Integer searchItemsPerPage;

	public PersonalizationSettingsContainer(String language, String currency, Boolean useDarkTheme,
											Boolean showCategoriesAsCircles, Integer searchItemsPerPage)
	{
		this.language = language;
		this.currency = currency;
		this.useDarkTheme = useDarkTheme;
		this.showCategoriesAsCircles = showCategoriesAsCircles;
		this.searchItemsPerPage = searchItemsPerPage;
	}

	public LanguageType getLanguageType()
	{
		return LanguageType.fromName(language);
	}

	public String currency()
	{
		return currency;
	}

	public Boolean useDarkTheme()
	{
		return useDarkTheme;
	}

	public Boolean showCategoriesAsCircles()
	{
		return showCategoriesAsCircles;
	}

	public Integer searchItemsPerPage()
	{
		return searchItemsPerPage;
	}

	@Override
	public void validate(Errors errors)
	{
		// nothing to do
	}

	@Override
	public void fixBooleans()
	{
		if(useDarkTheme == null)
		{
			useDarkTheme = false;
		}

		if(showCategoriesAsCircles == null)
		{
			showCategoriesAsCircles = false;
		}
	}
}
