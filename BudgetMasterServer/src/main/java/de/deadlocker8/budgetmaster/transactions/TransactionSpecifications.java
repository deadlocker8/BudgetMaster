package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.Tag_;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecifications
{
	private TransactionSpecifications()
	{
	}

	public static Specification<Transaction> withDynamicQuery(final LocalDate startDate, final LocalDate endDate,
															  Account account,
															  final boolean isIncome, boolean isExpenditure, boolean isTransfer,
															  final Boolean isRepeating,
															  final List<Integer> categoryIDs,
															  final List<Integer> tagIDs,
															  final String name)
	{
		return (transaction, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			List<Predicate> transferPredicates = new ArrayList<>();

			Predicate dateConstraint = builder.between(transaction.get(Transaction_.date), startDate, endDate);

			Predicate transferBackReference = null;

			// The amount of a transfer is always saved as a negative value.
			// Therefore, the following predicates are ignored for transfers to avoid excluding transfers in destination accounts
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
					final Predicate repeatingOptionIsNotNull = builder.isNotNull(transaction.get(Transaction_.repeatingOption));
					predicates.add(repeatingOptionIsNotNull);
					transferPredicates.add(repeatingOptionIsNotNull);
				}
				else
				{
					final Predicate repeatingOptionIsNull = builder.isNull(transaction.get(Transaction_.repeatingOption));
					predicates.add(repeatingOptionIsNull);
					transferPredicates.add(repeatingOptionIsNull);
				}
			}

			if(isTransfer)
			{
				// transactions in accounts that are destinations of transfers should be included in results
				// transfers in origin accounts are already included by the general predicates

				// allow transactions that have a transfer account set that matches the provided account-variable
				final Predicate transferAccountNotEqualsAccount = builder.notEqual(transaction.get(Transaction_.transferAccount), transaction.get(Transaction_.account));
				transferBackReference = builder.and(transferAccountNotEqualsAccount, builder.equal(transaction.get(Transaction_.transferAccount), account));

				if(!isIncome && !isExpenditure)
				{
					// if only transfers should be included just check if a transfer account is set in normal predicates!
					predicates.add(builder.isNotNull(transaction.get(Transaction_.transferAccount)));
				}
				else if(!isIncome)
				{
					// if incomes should not be included in results all transactions that are in destination accounts
					// of a transfer must be excluded since they are incomes

					// arbitrary predicate that is always false for transactions in destination accounts of transfers
					transferPredicates.add(builder.gt(transaction.get(Transaction_.amount), 0));
				}
			}
			else
			{
				predicates.add(builder.isNull(transaction.get(Transaction_.transferAccount)));
			}

			if(!categoryIDs.isEmpty())
			{
				final Predicate categoryIdIncluded = transaction.get(Transaction_.category).get("ID").in(categoryIDs);
				predicates.add(categoryIdIncluded);
				transferPredicates.add(categoryIdIncluded);
			}

			if(!tagIDs.isEmpty())
			{
				Join<Transaction, Tag> join = transaction.join(Transaction_.tags, JoinType.LEFT);
				final List<Predicate> tagPredicates = new ArrayList<>();

				for(Integer tagID : tagIDs)
				{
					tagPredicates.add(builder.equal(join.get(Tag_.ID), tagID));
				}

				// transactions without any tags should be included in results
				tagPredicates.add(builder.isEmpty(transaction.get(Transaction_.tags)));

				final Predicate[] predicatesArray = new Predicate[tagPredicates.size()];
				final Predicate tagPredicatesCombined = builder.or(tagPredicates.toArray(predicatesArray));

				predicates.add(tagPredicatesCombined);
				transferPredicates.add(tagPredicatesCombined);
			}

			if(name != null && name.length() > 0)
			{
				final Predicate nameLike = builder.like(builder.lower(transaction.get(Transaction_.name)), "%" + name.toLowerCase() + "%");
				predicates.add(nameLike);
				transferPredicates.add(nameLike);
			}

			query.orderBy(builder.desc(transaction.get(Transaction_.date)), builder.desc(transaction.get(Transaction_.ID)));

			final Predicate predicatesCombined = combinePredicates(predicates, builder);
			Predicate generalPredicates = builder.and(dateConstraint, predicatesCombined);
			if(account == null)
			{
				Predicate accountPredicate = transaction.get(Transaction_.account).get("accountState").in(List.of(AccountState.FULL_ACCESS, AccountState.READ_ONLY));
				generalPredicates = builder.and(generalPredicates, accountPredicate);
			}
			else
			{
				Predicate accountPredicate = builder.equal(transaction.get(Transaction_.account), account);
				generalPredicates = builder.and(generalPredicates, accountPredicate);
			}

			if(isTransfer)
			{
				final Predicate transferPredicatesCombined = combinePredicates(transferPredicates, builder);
				final Predicate allTransferPredicates = builder.and(dateConstraint, transferPredicatesCombined, transferBackReference);

				return builder.or(generalPredicates, allTransferPredicates);
			}
			else
			{
				return generalPredicates;
			}
		};
	}

	private static Predicate combinePredicates(List<Predicate> predicates, CriteriaBuilder builder)
	{
		final Predicate[] predicatesArray = new Predicate[predicates.size()];
		return builder.and(predicates.toArray(predicatesArray));
	}
}
