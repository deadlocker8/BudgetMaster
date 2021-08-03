package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.Tag_;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecifications
{
	private TransactionSpecifications()
	{
	}

	public static Specification<Transaction> withDynamicQuery(final DateTime startDate, final DateTime endDate,
															  Account account,
															  final boolean isIncome, boolean isExpenditure, boolean isTransfer,
															  final Boolean isRepeating,
															  final List<Integer> categoryIDs,
															  final List<Integer> tagIDs,
															  final String name)
	{
		return (transaction, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			Predicate dateConstraint = builder.between(transaction.get(Transaction_.date), startDate, endDate);

			Predicate transferBackReference = null;

			if(isIncome && !isExpenditure)
			{
				predicates.add(builder.gt(transaction.get(Transaction_.amount), 0));
			}

			if(!isIncome && isExpenditure)
			{
				predicates.add(builder.le(transaction.get(Transaction_.amount), 0));
			}

			if(isRepeating != null)
			{
				if(isRepeating)
				{
					predicates.add(builder.isNotNull(transaction.get(Transaction_.repeatingOption)));
				}
				else
				{
					predicates.add(builder.isNull(transaction.get(Transaction_.repeatingOption)));
				}
			}

			if(isTransfer)
			{
				final Predicate transferAccountNotEqualsAccount = builder.notEqual(transaction.get(Transaction_.transferAccount), transaction.get(Transaction_.account));
				transferBackReference = builder.and(transferAccountNotEqualsAccount, builder.equal(transaction.get(Transaction_.transferAccount), account));

				if(!isIncome && !isExpenditure)
				{
					predicates.add(builder.isNotNull(transaction.get(Transaction_.transferAccount)));
				}
			}
			else
			{
				predicates.add(builder.isNull(transaction.get(Transaction_.transferAccount)));
			}

			if(!categoryIDs.isEmpty())
			{
				predicates.add(transaction.get(Transaction_.category).get("ID").in(categoryIDs));
			}

			if(!tagIDs.isEmpty())
			{
				Join<Transaction, Tag> join = transaction.join(Transaction_.tags);
				Predicate tagPredicate = builder.disjunction();
				for(Integer tagID : tagIDs)
				{
					tagPredicate.getExpressions().add(builder.equal(join.get(Tag_.ID), tagID));
				}

				predicates.add(tagPredicate);
			}

			if(name != null && name.length() > 0)
			{
				predicates.add(builder.like(builder.lower(transaction.get(Transaction_.name)), "%" + name.toLowerCase() + "%"));
			}

			query.orderBy(builder.desc(transaction.get(Transaction_.date)));

			Predicate[] predicatesArray = new Predicate[predicates.size()];

			final Predicate predicatesCombined = builder.and(predicates.toArray(predicatesArray));
			Predicate generalPredicates = builder.and(dateConstraint, predicatesCombined);
			if(account != null)
			{
				Predicate accountPredicate = builder.equal(transaction.get(Transaction_.account), account);
				generalPredicates = builder.and(generalPredicates, accountPredicate);
			}

			if(transferBackReference == null)
			{
				return generalPredicates;
			}
			else
			{
				final Predicate transferPredicates = builder.and(dateConstraint, predicatesCombined, transferBackReference);
				return builder.or(generalPredicates, transferPredicates);
			}
		};
	}
}
