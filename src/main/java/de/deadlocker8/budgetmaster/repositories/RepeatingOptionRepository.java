package de.deadlocker8.budgetmaster.repositories;

import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RepeatingOptionRepository extends JpaRepository<RepeatingOption, Integer>
{
	List<RepeatingOption> findAllByOrderByStartDateAsc();

	RepeatingOption findOne(Integer ID);
}