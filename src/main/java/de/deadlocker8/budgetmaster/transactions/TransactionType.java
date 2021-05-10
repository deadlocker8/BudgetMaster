package de.deadlocker8.budgetmaster.transactions;

import java.util.Optional;

public enum TransactionType
{
	NORMAL(1),
	REPEATING(2),
	TRANSFER(3);

	private final int typeID;

	TransactionType(int typeID)
	{
		this.typeID = typeID;
	}

	public int getTypeID()
	{
		return typeID;
	}

	public static Optional<TransactionType> getByID(int typeID)
	{
		switch(typeID)
		{
			case 1:
				return Optional.of(NORMAL);
			case 2:
				return Optional.of(REPEATING);
			case 3:
				return Optional.of(TRANSFER);
			default:
				return Optional.empty();
		}
	}

	public static TransactionType getFromTransaction(Transaction transaction)
	{
		if(transaction.isTransfer())
		{
			return TRANSFER;
		}
		else if(transaction.isRepeating())
		{
			return REPEATING;
		}
		else
		{
			return NORMAL;
		}
	}

	@Override
	public String toString()
	{
		return "TransactionType{" +
				"typeID=" + typeID +
				'}';
	}
}
