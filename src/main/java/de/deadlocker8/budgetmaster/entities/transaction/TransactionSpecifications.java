package de.deadlocker8.budgetmaster.entities.transaction;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecifications
{
	public static Specification<Transaction> withDynamicQuery(final Integer amount, final String name)
	{
		return (transaction, query, builder) -> {
			if(amount == null && name == null)
			{
				throw new IllegalStateException("At least one parameter should be provided to construct complex query");
			}
			List<Predicate> predicates = new ArrayList<>();
			if(amount != null)
			{
				predicates.add(builder.and(builder.equal(transaction.get(Transaction_.amount), amount)));
			}
			if(name != null)
			{
				predicates.add(builder.and(builder.equal(transaction.get(Transaction_.name), name)));
			}
			Predicate[] predicatesArray = new Predicate[predicates.size()];
			return builder.and(predicates.toArray(predicatesArray));
		};
	}
}
