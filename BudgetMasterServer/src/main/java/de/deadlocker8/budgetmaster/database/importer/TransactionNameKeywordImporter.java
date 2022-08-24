package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeyword;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeywordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class TransactionNameKeywordImporter extends ItemImporter<TransactionNameKeyword>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionNameKeywordImporter.class);

	public TransactionNameKeywordImporter(TransactionNameKeywordRepository keywordRepository)
	{
		super(keywordRepository, EntityType.TRANSACTION_NAME_KEYWORD);
	}

	@Override
	protected int importSingleItem(TransactionNameKeyword keyword) throws ImportException
	{
		if(!(repository instanceof TransactionNameKeywordRepository repository))
		{
			throw new IllegalArgumentException("Invalid repository type");
		}

		final TransactionNameKeyword existingKeyword = repository.findByValue(keyword.getValue());
		if(existingKeyword != null)
		{
			LOGGER.debug(MessageFormat.format("Skipping {0} (a transaction name keyword with this value already exists)", keyword.getValue()));
			return existingKeyword.getID();
		}

		final TransactionNameKeyword keywordToCreate = new TransactionNameKeyword();
		keywordToCreate.setValue(keyword.getValue());

		final TransactionNameKeyword savedKeyword = repository.save(keywordToCreate);

		return savedKeyword.getID();
	}

	@Override
	protected String getNameForItem(TransactionNameKeyword item)
	{
		return String.valueOf(item.getValue());
	}
}
