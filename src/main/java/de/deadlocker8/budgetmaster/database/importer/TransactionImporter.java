package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class TransactionImporter extends ItemImporter<Transaction>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionImporter.class);

	private final TagImporter tagImporter;

	public TransactionImporter(TransactionRepository transactionRepository, TagImporter tagImporter)
	{
		super(transactionRepository, EntityType.TRANSACTION, true);
		this.tagImporter = tagImporter;
	}

	@Override
	protected int importSingleItem(Transaction transaction)
	{
		if(!(repository instanceof TransactionRepository repository))
		{
			throw new IllegalArgumentException("Invalid repository type");
		}

		LOGGER.debug(MessageFormat.format("Importing transaction with name: {0}, date: {1}", transaction.getName(), transaction.getDate()));
		tagImporter.importItems(transaction.getTags());
		transaction.setID(null);
		final Transaction newTransaction = repository.save(transaction);

		return newTransaction.getID();
	}

	@Override
	protected String getNameForItem(Transaction item)
	{
		return item.getName();
	}
}
