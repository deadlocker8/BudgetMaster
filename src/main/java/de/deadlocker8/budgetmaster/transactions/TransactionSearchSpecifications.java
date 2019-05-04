package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.search.Search;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSearchSpecifications
{
	public static Specification<Transaction> withDynamicQuery(final Search search)
	{
		return (transaction, query, builder) -> {
			final String pattern = "%" + search.getSearchText().toLowerCase() + "%";

			List<Predicate> predicates = new ArrayList<>();

			if(search.isSearchName())
			{
				predicates.add(builder.like(builder.lower(transaction.get(Transaction_.name)), pattern));
			}

			if(search.isSearchDescription())
			{
				predicates.add(builder.like(builder.lower(transaction.get(Transaction_.description)), pattern));
			}

			if(search.isSearchCategory())
			{
				Join<Transaction, Category> categoryJoin = transaction.join(Transaction_.category, JoinType.INNER);
				predicates.add(builder.like(builder.lower(categoryJoin.get("name").as(String.class)), pattern));
			}

//			if(search.isSearchTags())
//			{
//				Join<Transaction, Tag> tagJoin = transaction.join(Transaction_.tags, JoinType.INNER);
//				predicates.add(builder.like(builder.lower(tagJoin.get(Tag_.name)), pattern));
//			}

			query.orderBy(builder.desc(transaction.get(Transaction_.date)));
			Predicate[] predicatesArray = new Predicate[predicates.size()];
			return builder.or(predicates.toArray(predicatesArray));
		};
	}
}
