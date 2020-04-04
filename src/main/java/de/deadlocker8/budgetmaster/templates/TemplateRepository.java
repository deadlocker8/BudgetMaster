package de.deadlocker8.budgetmaster.templates;

import de.deadlocker8.budgetmaster.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface TemplateRepository extends JpaRepository<Template, Integer>, JpaSpecificationExecutor<Template>
{
	List<Template> findAllByAccount(Account account);

	List<Template> findAllByOrderByTemplateNameAsc();
}