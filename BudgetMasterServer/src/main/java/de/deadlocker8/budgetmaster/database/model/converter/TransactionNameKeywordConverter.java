package de.deadlocker8.budgetmaster.database.model.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v9.BackupTransactionNameKeyword_v9;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeyword;

public class TransactionNameKeywordConverter implements Converter<TransactionNameKeyword, BackupTransactionNameKeyword_v9>
{
	public TransactionNameKeyword convertToInternalForm(BackupTransactionNameKeyword_v9 backupKeyword)
	{
		if(backupKeyword == null)
		{
			return null;
		}

		final TransactionNameKeyword keyword = new TransactionNameKeyword();
		keyword.setValue(backupKeyword.getValue());
		return keyword;
	}

	@Override
	public BackupTransactionNameKeyword_v9 convertToExternalForm(TransactionNameKeyword internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupTransactionNameKeyword_v9 keyword = new BackupTransactionNameKeyword_v9();
		keyword.setValue(internalItem.getValue());
		return keyword;
	}
}
