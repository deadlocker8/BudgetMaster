package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagService;
import de.deadlocker8.budgetmaster.tags.Tag_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSearchSpecifications
{
	public static Specification<Transaction> withDynamicQuery(final String searchText)
	{
		return (transaction, query, builder) -> {
			final String pattern = "%" + searchText.toLowerCase() + "%";

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.like(builder.lower(transaction.get(Transaction_.name)), pattern));
			predicates.add(builder.like(builder.lower(transaction.get(Transaction_.description)), pattern));

			Join<Transaction, Category> categoryJoin = transaction.join(Transaction_.category, JoinType.INNER);
			predicates.add(builder.like(builder.lower(categoryJoin.get("name").as(String.class)), pattern));

//			Join<Transaction, Tag> tagJoin = transaction.join(Transaction_.tags, JoinType.INNER);
//			predicates.add(builder.like(builder.lower(tagJoin.get(Tag_.name)), pattern));

			query.orderBy(builder.desc(transaction.get(Transaction_.date)));
			Predicate[] predicatesArray = new Predicate[predicates.size()];
			return builder.or(predicates.toArray(predicatesArray));
		};
	}
}
