package de.deadlocker8.budgetmaster.database.model.v4;

public interface BackupTransactionBase_v4
{
	Integer getAmount();

	Boolean getExpenditure();

	void setExpenditure(Boolean isExpenditure);
}
