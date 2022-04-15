package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import de.deadlocker8.budgetmaster.utils.ProvidesID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class ItemImporter<T extends ProvidesID>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemImporter.class);

	protected final JpaRepository<T, Integer> repository;
	protected final EntityType entityType;

	protected ItemImporter(JpaRepository<T, Integer> repository, EntityType entityType)
	{
		this.repository = repository;
		this.entityType = entityType;
	}

	public ImportResultItem importItems(List<T> items)
	{
		LOGGER.debug(MessageFormat.format("Importing {0} {1}...", items.size(), entityType.getAllItemsName()));
		final List<String> collectedErrorMessages = new ArrayList<>();
		int numberOfImportedItems = 0;

		for(int i = 0; i < items.size(); i++)
		{
			T item = items.get(i);
			LOGGER.debug(MessageFormat.format("Importing {0} {1}/{2} (ID: {3})", entityType.getSingleItemName(), i + 1, items.size(), item.getID()));

			try
			{
				int newID = importSingleItem(item);

				item.setID(newID);
				numberOfImportedItems++;
			}
			catch(Exception e)
			{
				final String errorMessage = MessageFormat.format("Error while importing {0} \"{1}\"", entityType.getSingleItemName(), getNameForItem(item));
				LOGGER.error(errorMessage, e);
				collectedErrorMessages.add(formatErrorMessage(errorMessage, e));
			}
		}

		LOGGER.debug(MessageFormat.format("Importing {0} DONE ({1}/{2})", entityType.getAllItemsName(), numberOfImportedItems, items.size()));
		return new ImportResultItem(entityType, numberOfImportedItems, items.size(), collectedErrorMessages);
	}

	protected abstract int importSingleItem(T item);

	protected abstract String getNameForItem(T item);

	protected String formatErrorMessage(String errorMessage, Exception e)
	{
		return MessageFormat.format("{0}: {1} ({2})", errorMessage, e.getClass().getName(), e.getMessage());
	}
}
