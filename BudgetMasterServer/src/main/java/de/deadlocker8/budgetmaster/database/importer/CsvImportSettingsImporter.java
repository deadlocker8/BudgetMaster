package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.transactions.csvimport.CsvImportSettings;
import de.deadlocker8.budgetmaster.transactions.csvimport.CsvImportSettingsService;

public class CsvImportSettingsImporter extends ItemImporter<CsvImportSettings>
{
	private final CsvImportSettingsService csvImportSettingsService;

	public CsvImportSettingsImporter(CsvImportSettingsService csvImportSettingsService)
	{
		super(csvImportSettingsService.getRepository(), EntityType.TRANSACTION_IMPORT);
		this.csvImportSettingsService = csvImportSettingsService;
	}

	@Override
	protected int importSingleItem(CsvImportSettings csvImportSettings) throws ImportException
	{
		csvImportSettingsService.updateSettings(csvImportSettings);
		return 1;
	}

	@Override
	protected String getNameForItem(CsvImportSettings item)
	{
		return String.valueOf(item.toString());
	}
}
