package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v10.BackupCsvImportSettings_v10;
import de.deadlocker8.budgetmaster.transactions.csvimport.CsvImportSettings;

public class CsvImportSettingsConverter implements Converter<CsvImportSettings, BackupCsvImportSettings_v10>
{
	public CsvImportSettings convertToInternalForm(BackupCsvImportSettings_v10 backupCsvImportSettings)
	{
		if(backupCsvImportSettings == null)
		{
			return null;
		}

		final CsvImportSettings settings = new CsvImportSettings();
		settings.setSeparatorChar(backupCsvImportSettings.getSeparator());
		settings.setEncoding(backupCsvImportSettings.getEncoding());
		settings.setNumberOfLinesToSkip(backupCsvImportSettings.getNumberOfLinesToSkip());

		settings.setColumnDate(backupCsvImportSettings.getColumnDate());
		settings.setDatePattern(backupCsvImportSettings.getDatePattern());
		settings.setColumnName(backupCsvImportSettings.getColumnName());
		settings.setColumnAmount(backupCsvImportSettings.getColumnAmount());
		settings.setDecimalSeparator(backupCsvImportSettings.getDecimalSeparator());
		settings.setGroupingSeparator(backupCsvImportSettings.getGroupingSeparator());
		settings.setColumnDescription(backupCsvImportSettings.getColumnDescription());
		return settings;
	}

	@Override
	public BackupCsvImportSettings_v10 convertToExternalForm(CsvImportSettings internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupCsvImportSettings_v10 settings = new BackupCsvImportSettings_v10();
		settings.setSeparator(internalItem.getSeparatorChar());
		settings.setEncoding(internalItem.getEncoding());
		settings.setNumberOfLinesToSkip(internalItem.getNumberOfLinesToSkip());

		settings.setColumnDate(internalItem.getColumnDate());
		settings.setDatePattern(internalItem.getDatePattern());
		settings.setColumnName(internalItem.getColumnName());
		settings.setColumnAmount(internalItem.getColumnAmount());
		settings.setDecimalSeparator(internalItem.getDecimalSeparator());
		settings.setGroupingSeparator(internalItem.getGroupingSeparator());
		settings.setColumnDescription(internalItem.getColumnDescription());
		return settings;
	}
}
