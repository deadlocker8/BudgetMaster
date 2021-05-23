package de.deadlocker8.budgetmaster.database.model.v5.converter;

import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v4.BackupTag_v4;
import de.deadlocker8.budgetmaster.database.model.v5.BackupTransaction_v5;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class TransactionConverter_v5 implements Converter<Transaction, BackupTransaction_v5>
{
	public Transaction convertToInternalForm(BackupTransaction_v5 backupTransaction)
	{
		if(backupTransaction == null)
		{
			return null;
		}

		final Transaction transaction = new Transaction();
		transaction.setAmount(backupTransaction.getAmount());
		transaction.setName(backupTransaction.getName());
		transaction.setCategory(new CategoryConverter_v5().convertToInternalForm(backupTransaction.getCategory()));
		transaction.setDescription(backupTransaction.getDescription());
		transaction.setIsExpenditure(backupTransaction.getExpenditure());
		transaction.setAccount(new AccountConverter_v5().convertToInternalForm(backupTransaction.getAccount()));
		transaction.setTransferAccount(new AccountConverter_v5().convertToInternalForm(backupTransaction.getTransferAccount()));

		DateTime date = DateTime.parse(backupTransaction.getDate(), DateTimeFormat.forPattern("yyyy-MM-dd"));
		date = date.withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
		transaction.setDate(date);

		List<Tag> convertedTags = new ArrayList<>();
		TagConverter_v5 tagConverter = new TagConverter_v5();
		for(BackupTag_v4 tag : backupTransaction.getTags())
		{
			convertedTags.add(tagConverter.convertToInternalForm(tag));
		}
		transaction.setTags(convertedTags);

		transaction.setRepeatingOption(new RepeatingOptionConverter_v5().convertToInternalForm(backupTransaction.getRepeatingOption()));
		return transaction;
	}

	@Override
	public BackupTransaction_v5 convertToExternalForm(Transaction internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupTransaction_v5 transaction = new BackupTransaction_v5();
		transaction.setAmount(internalItem.getAmount());
		transaction.setName(internalItem.getName());
		transaction.setCategory(new CategoryConverter_v5().convertToExternalForm(internalItem.getCategory()));
		transaction.setDescription(internalItem.getDescription());
		transaction.setExpenditure(internalItem.getExpenditure());
		transaction.setAccount(new AccountConverter_v5().convertToExternalForm(internalItem.getAccount()));
		transaction.setTransferAccount(new AccountConverter_v5().convertToExternalForm(internalItem.getTransferAccount()));
		transaction.setDate(internalItem.getDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd")));

		List<BackupTag_v4> convertedTags = new ArrayList<>();
		TagConverter_v5 tagConverter = new TagConverter_v5();
		for(Tag tag : internalItem.getTags())
		{
			convertedTags.add(tagConverter.convertToExternalForm(tag));
		}
		transaction.setTags(convertedTags);
		
		transaction.setRepeatingOption(new RepeatingOptionConverter_v5().convertToExternalForm(internalItem.getRepeatingOption()));
		return transaction;
	}
}
