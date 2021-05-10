package de.deadlocker8.budgetmaster.accounts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AccountRepository extends JpaRepository<Account, Integer>
{
	List<Account> findAllByTypeOrderByNameAsc(AccountType accountType);

	Account findByName(String name);

	List<Account> findAllByType(AccountType accountType);

	List<Account> findAllByTypeAndAccountStateOrderByNameAsc(AccountType accountType, AccountState accountState);

	Account findByIsSelected(boolean isSelected);

	Account findByIsDefault(boolean isDefault);
}