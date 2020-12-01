package de.deadlocker8.budgetmaster.unit.helpers;

import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.localization.LocalizationMessageFormatter;
import de.thecodelabs.utils.util.localization.formatter.JavaMessageFormatter;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.Locale;

public class LocalizationHelpers extends AbstractTestExecutionListener
{
	@Override
	public void beforeTestClass(TestContext testContext) throws Exception
	{
		Localization.setDelegate(new Localization.LocalizationDelegate()
		{
			@Override
			public Locale getLocale()
			{
				return Locale.ENGLISH;
			}

			@Override
			public String[] getBaseResources()
			{
				return new String[]{"languages/base", "languages/news"};
			}

			@Override
			public LocalizationMessageFormatter messageFormatter()
			{
				return new JavaMessageFormatter();
			}

			@Override
			public boolean useMultipleResourceBundles()
			{
				return true;
			}
		});
		Localization.load();
	}
}
