package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.Tag_;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransactionSpecifications
{
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
			predicates.add(builder.and(builder.between(transaction.get(Transaction_.date), startDate, endDate)));

			Predicate transferBackReference = null;

			if(account != null)
			{
				predicates.add(builder.and(builder.equal(transaction.get(Transaction_.account), account)));
			}

			if(isIncome && !isExpenditure)
			{
				predicates.add(builder.and(builder.gt(transaction.get(Transaction_.amount), 0)));
			}

			if(!isIncome && isExpenditure)
			{
				predicates.add(builder.and(builder.le(transaction.get(Transaction_.amount), 0)));
			}

			if(isRepeating != null)
			{
				if(isRepeating)
				{
					predicates.add(builder.and(builder.isNotNull(transaction.get(Transaction_.repeatingOption))));
				}
				else
				{
					predicates.add(builder.and(builder.isNull(transaction.get(Transaction_.repeatingOption))));
				}
			}

			if(isTransfer)
			{
				transferBackReference = builder.equal(transaction.get(Transaction_.transferAccount), account);

				if(!isIncome && !isExpenditure)
				{
					predicates.add(builder.and(builder.isNotNull(transaction.get(Transaction_.transferAccount))));
				}
			}
			else
			{
				predicates.add(builder.and(builder.isNull(transaction.get(Transaction_.transferAccount))));
			}

			if(categoryIDs != null)
			{
				predicates.add(builder.and(transaction.get(Transaction_.category).get("ID").in(categoryIDs)));
			}

			if(tagIDs != null)
			{
				Join<Transaction, Tag> join = transaction.join(Transaction_.tags);
				Predicate actorIdPredicate = builder.disjunction();
				for(Integer tagID : tagIDs)
				{
					actorIdPredicate.getExpressions().add(builder.equal(join.get(Tag_.ID), tagID));
				}

				predicates.add(actorIdPredicate);
			}

			if(name != null && name.length() > 0)
			{
				predicates.add(builder.and(builder.like(builder.lower(transaction.get(Transaction_.name)), "%" + name.toLowerCase() + "%")));
			}

			query.orderBy(builder.desc(transaction.get(Transaction_.date)));

			Predicate[] predicatesArray = new Predicate[predicates.size()];

			if(transferBackReference == null)
			{
				return builder.and(predicates.toArray(predicatesArray));
			}

			return builder.or(builder.and(predicates.toArray(predicatesArray)), transferBackReference);
		};
	}
}
