package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.tags.Tag;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@StaticMetamodel(Transaction.class)
public class Transaction_
{
	public static volatile SingularAttribute<Transaction, Integer> ID;
	public static volatile SingularAttribute<Transaction, Integer> amount;
	public static volatile SingularAttribute<Transaction, LocalDate> date;
	public static volatile SingularAttribute<Transaction, Account> account;
	public static volatile SingularAttribute<Transaction, Category> category;
	public static volatile SingularAttribute<Transaction, String> name;
	public static volatile SingularAttribute<Transaction, String> description;
	public static volatile SingularAttribute<Transaction, Boolean> isExpenditure;
	public static volatile ListAttribute<Transaction, Tag> tags;
	public static volatile SingularAttribute<Transaction, RepeatingOption> repeatingOption;
	public static volatile SingularAttribute<Transaction, Account> transferAccount;
}
