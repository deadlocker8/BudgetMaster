package de.deadlocker8.budgetmaster.transactions.csvimport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
public class CsvImportSettingsService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CsvImportSettingsService.class);

	private final CsvImportSettingsRepository csvImportSettingsRepository;

	@Autowired
	public CsvImportSettingsService(CsvImportSettingsRepository csvImportSettingsRepository)
	{
		this.csvImportSettingsRepository = csvImportSettingsRepository;
	}

	@PostConstruct
	public void postInit()
	{
		this.createDefaultSettingsIfNotExists();
	}

	public void createDefaultSettingsIfNotExists()
	{
		if(csvImportSettingsRepository.findById(1).isEmpty())
		{
			csvImportSettingsRepository.save(CsvImportSettings.getDefault());
			LOGGER.debug("Created default settings");
		}
	}

	public CsvImportSettings getCsvImportSettings()
	{
		return csvImportSettingsRepository.findById(1).orElseThrow();
	}

	@Transactional
	public void updateSettings(CsvImport csvImport)
	{
		final CsvImportSettings settings = getCsvImportSettings();
		if(hasContent(csvImport.separator()))
		{
			settings.setSeparator(csvImport.separator());
		}

		if(hasContent(csvImport.encoding()))
		{
			settings.setEncoding(csvImport.encoding());
		}

		settings.setNumberOfLinesToSkip(csvImport.numberOfLinesToSkip());
	}

	@Transactional
	public void updateSettings(CsvColumnSettings columnSettings)
	{
		final CsvImportSettings settings = getCsvImportSettings();

		settings.setColumnDate(columnSettings.columnDate());

		if(hasContent(columnSettings.datePattern()))
		{
			settings.setDatePattern("dd.MM.yyyy");
		}

		settings.setColumnName(columnSettings.columnName());
		settings.setColumnAmount(columnSettings.columnAmount());

		if(hasContent(columnSettings.decimalSeparator()))
		{
			settings.setDecimalSeparator(columnSettings.decimalSeparator());

		}
		if(hasContent(columnSettings.groupingSeparator()))
		{
			settings.setGroupingSeparator(columnSettings.groupingSeparator());
		}

		settings.setColumnDescription(columnSettings.columnDescription());
	}

	private boolean hasContent(String value)
	{
		if(value == null)
		{
			return false;
		}

		if(value.isEmpty())
		{
			return false;
		}

		return !value.isBlank();
	}
}