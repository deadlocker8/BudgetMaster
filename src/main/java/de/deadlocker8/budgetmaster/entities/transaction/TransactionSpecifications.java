package de.deadlocker8.budgetmaster.entities.transaction;

import de.deadlocker8.budgetmaster.entities.category.Category;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecifications
{
	public static Specification<Transaction> withDynamicQuery(final boolean isIncome, boolean isExpenditure, final Boolean isRepeating, final List<Category> categories, final Integer amount, final String name)
	{
		return (transaction, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
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

			if(categories != null)
			{
				predicates.add(builder.and(transaction.get(Transaction_.category).in(categories)));
			}

			if(name != null)
			{
				predicates.add(builder.and(builder.like(builder.lower(transaction.get(Transaction_.name)), "%"+name.toLowerCase()+"%")));
			}
			Predicate[] predicatesArray = new Predicate[predicates.size()];
			return builder.and(predicates.toArray(predicatesArray));
		};
	}
}
