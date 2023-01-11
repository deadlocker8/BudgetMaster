package de.deadlocker8.budgetmaster.transactions.csvimport;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountParser
{
	private static final Pattern PATTERN_AMOUNT = Pattern.compile("^\\s*([-+]?)\\s*(\\d+(,\\d+)?(\\.\\d+)?)");

	private AmountParser()
	{
	}

	public static Optional<Integer> parse(String amountString)
	{
		if(amountString == null)
		{
			return Optional.empty();
		}

		final Matcher matcher = PATTERN_AMOUNT.matcher(amountString);
		boolean matchFound = matcher.find();
		if(matchFound)
		{
			final String sign = matcher.group(1);
			String amount = matcher.group(2);
			amount = amount.replace(',', '.');

			final String parseableString = MessageFormat.format("{0}{1}", sign, amount);
			try
			{
				final double parseDouble = Double.parseDouble(parseableString);
				return Optional.of((int) (parseDouble * 100));
			}
			catch(NumberFormatException e)
			{
				return Optional.empty();
			}
		}
		else
		{
			return Optional.empty();
		}
	}
}
