package de.deadlocker8.budgetmaster.transactions.keywords;

import de.deadlocker8.budgetmaster.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface TransactionNameKeywordRepository extends JpaRepository<TransactionNameKeyword, Integer>, JpaSpecificationExecutor<Transaction>
{
}