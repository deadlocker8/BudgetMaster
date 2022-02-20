package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction>
{
	List<Transaction> findAllByAccountAndDateBetweenOrderByDateDesc(Account account, LocalDate startDate, LocalDate endDate);

	List<Transaction> findAllByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);

	List<Transaction> findAllByAccount(Account account);

	Long countByCategory(Category category);

	List<Transaction> findAllByTagsContaining(Tag tag);

	@Query(value = "SELECT SUM(t.amount) FROM Transaction as t WHERE t.account.ID = ?1 AND t.transferAccount.ID IS NULL AND t.date BETWEEN ?2 AND ?3")
	Integer getRestForNormalAndRepeating(int accountID, LocalDate startDate, LocalDate endDate);

	@Query(value = "SELECT SUM(t.amount) FROM Transaction as t WHERE t.account.ID = ?1 AND t.transferAccount.ID IS NOT NULL AND t.date BETWEEN ?2 AND ?3")
	Integer getRestForTransferSource(int accountID, LocalDate startDate, LocalDate endDate);

	@Query(value = "SELECT SUM(t.amount) FROM Transaction as t WHERE t.transferAccount.ID = ?1 AND t.date BETWEEN ?2 AND ?3")
	Integer getRestForTransferDestination(int accountID, LocalDate startDate, LocalDate endDate);

	List<Transaction> findAllByTransferAccount(Account account);

	List<Transaction> findAllByOrderByDateDesc();

	Transaction findFirstByOrderByDate();

	List<Transaction> findAllByRepeatingOption(RepeatingOption repeatingOption);
}