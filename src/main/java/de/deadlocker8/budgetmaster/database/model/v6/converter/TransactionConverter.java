package de.deadlocker8.budgetmaster.database.model.v6.converter;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.database.model.Converter;
import de.deadlocker8.budgetmaster.database.model.v4.BackupTag_v4;
import de.deadlocker8.budgetmaster.database.model.v6.BackupTransaction_v6;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class TransactionConverter implements Converter<Transaction, BackupTransaction_v6>
{
	private final List<Category> availableCategories;
	private final List<Account> availableAccounts;

	public TransactionConverter(List<Category> availableCategories, List<Account> availableAccounts)
	{
		this.availableCategories = availableCategories;
		this.availableAccounts = availableAccounts;
	}

	public Transaction convertToInternalForm(BackupTransaction_v6 backupTransaction)
	{
		if(backupTransaction == null)
		{
			return null;
		}

		final Transaction transaction = new Transaction();
		transaction.setAmount(backupTransaction.getAmount());
		transaction.setName(backupTransaction.getName());
		transaction.setCategory(getItemById(availableCategories, backupTransaction.getCategoryID()));
		transaction.setDescription(backupTransaction.getDescription());
		transaction.setIsExpenditure(backupTransaction.getExpenditure());
		transaction.setAccount(getItemById(availableAccounts, backupTransaction.getAccountID()));
		transaction.setTransferAccount(getItemById(availableAccounts, backupTransaction.getTransferAccountID()));

		DateTime date = DateTime.parse(backupTransaction.getDate(), DateTimeFormat.forPattern("yyyy-MM-dd"));
		date = date.withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
		transaction.setDate(date);

		List<Tag> convertedTags = new ArrayList<>();
		TagConverter tagConverter = new TagConverter();
		for(BackupTag_v4 tag : backupTransaction.getTags())
		{
			convertedTags.add(tagConverter.convertToInternalForm(tag));
		}
		transaction.setTags(convertedTags);

		transaction.setRepeatingOption(new RepeatingOptionConverter().convertToInternalForm(backupTransaction.getRepeatingOption()));
		return transaction;
	}

	@Override
	public BackupTransaction_v6 convertToExternalForm(Transaction internalItem)
	{
		if(internalItem == null)
		{
			return null;
		}

		final BackupTransaction_v6 transaction = new BackupTransaction_v6();
		transaction.setAmount(internalItem.getAmount());
		transaction.setName(internalItem.getName());
		transaction.setCategoryID(internalItem.getCategory().getID());
		transaction.setDescription(internalItem.getDescription());
		transaction.setExpenditure(internalItem.getExpenditure());
		transaction.setAccountID(internalItem.getAccount().getID());

		if(internalItem.getTransferAccount() != null)
		{
			transaction.setTransferAccountID(internalItem.getTransferAccount().getID());
		}

		transaction.setDate(internalItem.getDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd")));

		List<BackupTag_v4> convertedTags = new ArrayList<>();
		TagConverter tagConverter = new TagConverter();
		for(Tag tag : internalItem.getTags())
		{
			convertedTags.add(tagConverter.convertToExternalForm(tag));
		}
		transaction.setTags(convertedTags);

		transaction.setRepeatingOption(new RepeatingOptionConverter().convertToExternalForm(internalItem.getRepeatingOption()));
		return transaction;
	}
}
