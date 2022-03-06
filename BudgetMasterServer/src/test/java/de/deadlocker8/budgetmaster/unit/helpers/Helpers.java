package de.deadlocker8.budgetmaster.unit.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Helpers
{
	public static String runCommand(File directory, String... command)
	{
		final ProcessBuilder processBuilder = new ProcessBuilder()
				.command(command)
				.redirectErrorStream(true)
				.directory(directory);

		try
		{
			final Process process = processBuilder.start();

			//read the output
			final InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
			final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			final StringBuilder stringBuilder = new StringBuilder();
			String output = null;
			while((output = bufferedReader.readLine()) != null)
			{
				stringBuilder.append(output);
			}

			process.waitFor();

			bufferedReader.close();
			process.destroy();

			return stringBuilder.toString();
		}
		catch(IOException | InterruptedException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
