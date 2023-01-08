package de.deadlocker8.budgetmaster.transactions.csvImport;

import org.springframework.web.multipart.MultipartFile;

public record CsvImport(MultipartFile file, String separator)
{
	@Override
	public String toString()
	{
		return "CsvImport{" +
				"file=" + file +
				", separator='" + separator + '\'' +
				'}';
	}

	public boolean isValidSeparator()
	{
		if(separator == null)
		{
			return false;
		}

		return separator.strip().length() == 1;
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
