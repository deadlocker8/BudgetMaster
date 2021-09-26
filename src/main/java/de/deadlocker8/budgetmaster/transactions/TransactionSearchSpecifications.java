package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.search.Search;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.Tag_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSearchSpecifications
{
	private TransactionSearchSpecifications()
	{
	}

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

			if(search.isSearchTags())
			{
				Join<Transaction, Tag> tagJoin = transaction.join(Transaction_.tags, JoinType.LEFT);
				predicates.add(builder.like(builder.lower(tagJoin.get(Tag_.name).as(String.class)), pattern));
			}

			Predicate[] predicatesArray = new Predicate[predicates.size()];
			Predicate predicatesCombined = builder.or(predicates.toArray(predicatesArray));

			Predicate accountStatePredicate = transaction.get(Transaction_.account).get("accountState").in(getAllowedAccountStates(search));

			query.orderBy(builder.desc(transaction.get(Transaction_.date)));
			query.distinct(true);
			return builder.and(accountStatePredicate, predicatesCombined);
		};
	}

	private static List<AccountState> getAllowedAccountStates(Search search)
	{
		List<AccountState> allowedAccountStates = new ArrayList<>();
		allowedAccountStates.add(AccountState.FULL_ACCESS);
		allowedAccountStates.add(AccountState.READ_ONLY);

		if(search.isIncludeHiddenAccounts())
		{
			allowedAccountStates.add(AccountState.HIDDEN);
		}

		return allowedAccountStates;
	}
}
