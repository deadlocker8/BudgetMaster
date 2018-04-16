package de.deadlocker8.budgetmaster.repositories;

import de.deadlocker8.budgetmaster.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AccountRepository extends JpaRepository<Account, Integer>
{
	List<Account> findAllByOrderByNameAsc();

	Account findByName(String name);

	Account findByIsSelected(boolean isSelected);
}