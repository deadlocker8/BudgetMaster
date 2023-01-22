package de.deadlocker8.budgetmaster.transactions.csvimport;

public enum CsvTransactionStatus
{
	PENDING("transactions.import.status.pending"),
	IMPORTED("transactions.import.status.imported"),
	SKIPPED("transactions.import.status.skipped");

	private final String localizationKey;

	CsvTransactionStatus(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}
}
