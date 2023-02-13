package de.deadlocker8.budgetmaster.transactions.csvimport;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountParser
{
	private static final Pattern PATTERN_AMOUNT = Pattern.compile("^\\s*([-+]?)\\s*(\\d+(.*\\d+)?)");

	private AmountParser()
	{
	}

	public static Optional<Integer> parse(String amountString, char decimalSeparator, char groupingSeparator)
	{
		if(amountString == null)
		{
			return Optional.empty();
		}

		final Matcher matcher = PATTERN_AMOUNT.matcher(amountString);
		boolean matchFound = matcher.find();
		if(matchFound)
		{
			String sign = matcher.group(1);
			if(sign.equals("+"))
			{
				sign = "";
			}

			final String amount = matcher.group(2);

			final DecimalFormat decimalFormat = new DecimalFormat();
			final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setDecimalSeparator(decimalSeparator);
			symbols.setGroupingSeparator(groupingSeparator);
			decimalFormat.setNegativePrefix("-");
			decimalFormat.setDecimalFormatSymbols(symbols);

			final String parseableString = MessageFormat.format("{0}{1}", sign, amount);
			try
			{
				final double parseDouble = decimalFormat.parse(parseableString).doubleValue();
				return Optional.of((int) (parseDouble * 100));
			}
			catch(ParseException e)
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
