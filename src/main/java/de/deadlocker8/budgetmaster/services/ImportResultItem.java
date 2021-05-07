package de.deadlocker8.budgetmaster.services;

import java.util.Objects;

public class ImportResultItem
{
	private final EntityType entityType;
	private final int numberOfImportedItems;
	private final int numberOfAvailableItems;

	public ImportResultItem(EntityType entityType, int numberOfImportedItems, int numberOfAvailableItems)
	{
		this.entityType = entityType;
		this.numberOfImportedItems = numberOfImportedItems;
		this.numberOfAvailableItems = numberOfAvailableItems;
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

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ImportResultItem that = (ImportResultItem) o;
		return numberOfImportedItems == that.numberOfImportedItems && numberOfAvailableItems == that.numberOfAvailableItems && entityType == that.entityType;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(entityType, numberOfImportedItems, numberOfAvailableItems);
	}

	@Override
	public String toString()
	{
		return "ImportResultItem{" +
				"entityType=" + entityType +
				", numberOfImportedItems=" + numberOfImportedItems +
				", numberOfAvailableItems=" + numberOfAvailableItems +
				'}';
	}
}
