package de.deadlocker8.budgetmaster.tags;

import de.deadlocker8.budgetmaster.transactions.Transaction;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Tag.class)
public class Tag_
{
	public static volatile SingularAttribute<Tag, Integer> ID;
	public static volatile SingularAttribute<Tag, Integer> name;
	public static volatile ListAttribute<Tag, Transaction> referringTransactions;
}
