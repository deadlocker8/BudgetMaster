package de.deadlocker8.budgetmaster.repositories;

import de.deadlocker8.budgetmaster.entities.account.Account;
import de.deadlocker8.budgetmaster.entities.Transaction;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Integer>
{
	List<Transaction> findAllByAccountAndDateBetweenOrderByDateDesc(Account account, DateTime startDate, DateTime endDate);

	List<Transaction> findAllByDateBetweenOrderByDateDesc(DateTime startDate, DateTime endDate);

	List<Transaction> findAllByAccount(Account account);

	@Query(value = "SELECT SUM(t.amount) FROM `transaction` as t WHERE t.account_id = ?1 AND t.date BETWEEN ?2 AND ?3", nativeQuery = true)
	Integer getRest(int accountID, String startDate, String endDate);
}