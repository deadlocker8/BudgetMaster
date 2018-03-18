package de.deadlocker8.budgetmaster.repositories;

import de.deadlocker8.budgetmaster.entities.Payment;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment, Integer>
{
	List<Payment> findAllByDateBetweenOrderByDateDesc(DateTime startDate, DateTime endDate);
}