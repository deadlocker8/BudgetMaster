package de.deadlocker8.budgetmaster.transactions.csvImport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.text.MessageFormat;

public record CsvImport(MultipartFile file, String separator, String encoding, int numberOfLinesToSkip)
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CsvImport.class);

	public boolean isValidSeparator()
	{
		if(separator == null)
		{
			return false;
		}

		return separator.strip().length() == 1;
	}

	public boolean isEncodingSupported()
	{
		try
		{
			Charset.forName(encoding);
			return true;
		}
		catch(IllegalArgumentException e)
		{
			LOGGER.error(MessageFormat.format("Could not create charset from encoding name: {0}", encoding), e);
		}

		return false;
	}

	public String getFileName()
	{
		if(file == null)
		{
			return null;
		}

		final String fileName = file.getOriginalFilename();
		if(fileName == null)
		{
			return file.getName();
		}

		return fileName;
	}
}
