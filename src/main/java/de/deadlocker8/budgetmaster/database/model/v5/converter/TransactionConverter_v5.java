package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.database.model.v5.BackupTransaction_v5;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class TransactionConverter_v5 implements Converter<Transaction, BackupTransaction_v5>
{
	public Transaction convert(BackupTransaction_v5 backupTransaction)
	{
		if(backupTransaction == null)
		{
			return null;
		}

		final Transaction transaction = new Transaction();
		transaction.setAmount(backupTransaction.getAmount());
		transaction.setName(backupTransaction.getName());
		transaction.setCategory(new CategoryConverter_v5().convert(backupTransaction.getCategory()));
		transaction.setDescription(backupTransaction.getDescription());
		transaction.setIsExpenditure(backupTransaction.getExpenditure());
		transaction.setAccount(new AccountConverter_v5().convert(backupTransaction.getAccount()));
		transaction.setTransferAccount(new AccountConverter_v5().convert(backupTransaction.getTransferAccount()));
		transaction.setDate(DateTime.parse(backupTransaction.getDate(), DateTimeFormat.forPattern("yyyy-MM-dd")));
		transaction.setTags(backupTransaction.getTags());
		transaction.setRepeatingOption(new RepeatingOptionConverter_v5().convert(backupTransaction.getRepeatingOption()));
		return transaction;
	}
}
