package de.deadlocker8.budgetmaster.tests.server.settings;

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
			expectedSettings.setDatabaseName("b");
			expectedSettings.setDatabaseUrl("jdbc:mysql://localhost:3306/");
			expectedSettings.setDatabaseUsername("root");
			expectedSettings.setDatabasePassword("");
			expectedSettings.setServerPort(9000);
			expectedSettings.setServerSecret("geheim");
			expectedSettings.setKeystorePath("C:/Programmierung/eclipse/workspace/BudgetMaster/certs/keystore_self_signed.jks");
			expectedSettings.setKeystorePassword("geheim");			
			
			assertEquals(expectedSettings, settings);
		}
		catch(IOException | URISyntaxException e)
		{
			fail(e.getMessage());
		}
	}
}