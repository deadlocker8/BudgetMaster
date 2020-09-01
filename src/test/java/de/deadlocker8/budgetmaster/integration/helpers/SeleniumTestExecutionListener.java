package de.deadlocker8.budgetmaster.integration.helpers;

import de.thecodelabs.utils.util.Localization;
import de.thecodelabs.utils.util.SystemUtils;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class SeleniumTestExecutionListener extends AbstractTestExecutionListener
{
	@Override
	public int getOrder()
	{
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public void prepareTestInstance(TestContext testContext)
	{
		if(!System.getProperties().containsKey("testProfile"))
		{
			throw new RuntimeException("Test profile not activated. Skipping tests. (Set -DtestProfile=true in your VM arguments)");
		}
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception
	{
		final Path path = SystemUtils.getApplicationSupportDirectoryPath(Localization.getString("folder"), "test", "budgetmaster.mv.db");
		try
		{
			Files.deleteIfExists(path);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
