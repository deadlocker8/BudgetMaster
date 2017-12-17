package de.deadlocker8.budgetmaster.tests.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import de.deadlocker8.budgetmasterserver.logic.Settings;
import de.deadlocker8.budgetmasterserver.logic.Utils;

public class SettingsTest
{		
	@Test
	public void testRead()
	{		
		try
		{
			Settings settings = Utils.loadSettings();
			
			Settings expectedSettings = new Settings();
			expectedSettings.setDatabaseType("mysql");
			expectedSettings.setDatabaseName("budgetmaster");
			expectedSettings.setDatabaseUrl("localhost:3306/");
			expectedSettings.setDatabaseUsername("root");
			expectedSettings.setDatabasePassword("");
			expectedSettings.setServerPort(9000);
			expectedSettings.setServerSecret("geheim");
			expectedSettings.setKeystorePath("default");
			expectedSettings.setKeystorePassword("BudgetMaster");	
			
			assertEquals(expectedSettings, settings);
		}
		catch(IOException | URISyntaxException e)
		{
			fail(e.getMessage());
		}
	}
}