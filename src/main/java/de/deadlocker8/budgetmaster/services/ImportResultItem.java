package de.deadlocker8.budgetmaster.services;

import java.util.List;
import java.util.Objects;

public class ImportResultItem
{
	private final EntityType entityType;
	private final int numberOfImportedItems;
	private final int numberOfAvailableItems;
	private final List<String> collectedErrorMessages;


	public ImportResultItem(EntityType entityType, int numberOfImportedItems, int numberOfAvailableItems, List<String> collectedErrorMessages)
	{
		this.entityType = entityType;
		this.numberOfImportedItems = numberOfImportedItems;
		this.numberOfAvailableItems = numberOfAvailableItems;
		this.collectedErrorMessages = collectedErrorMessages;
	}

	public EntityType getEntityType()
	{
		return entityType;
	}

	public int getNumberOfImportedItems()
	{
		return numberOfImportedItems;
	}

	public int getNumberOfAvailableItems()
	{
		return numberOfAvailableItems;
	}

	public List<String> getCollectedErrorMessages()
	{
		return collectedErrorMessages;
	}

	@Override
	public boolean equals(Object o)
	{

		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ImportResultItem that = (ImportResultItem) o;
		return numberOfImportedItems == that.numberOfImportedItems && numberOfAvailableItems == that.numberOfAvailableItems && entityType == that.entityType && Objects.equals(collectedErrorMessages, that.collectedErrorMessages);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(entityType, numberOfImportedItems, numberOfAvailableItems, collectedErrorMessages);
	}

	@Override
	public String toString()
	{
		return "ImportResultItem{" +
				"entityType=" + entityType +
				", numberOfImportedItems=" + numberOfImportedItems +
				", numberOfAvailableItems=" + numberOfAvailableItems +
				", collectedErrorMessages=" + collectedErrorMessages +
				'}';
	}
}
