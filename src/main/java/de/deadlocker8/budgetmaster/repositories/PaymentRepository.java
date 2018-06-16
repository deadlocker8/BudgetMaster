package de.deadlocker8.budgetmaster.repositories;

import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.entities.Payment;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment, Integer>
{
	List<Payment> findAllByAccountAndDateBetweenOrderByDateDesc(Account account, DateTime startDate, DateTime endDate);

	List<Payment> findAllByAccount(Account account);

	@Query(value = "SELECT SUM(p.amount) FROM Payment as p WHERE p.account_id = ?1 AND p.date BETWEEN ?2 AND ?3", nativeQuery = true)
	Integer getRest(int accountID, String startDate, String endDate);
}