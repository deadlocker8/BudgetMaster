package de.deadlocker8.budgetmaster.charts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChartRepository extends JpaRepository<Chart, Integer>
{
	List<Chart> findAllByOrderByNameAsc();

	List<Chart> findAllByType(ChartType chartType);

	Chart findByName(String name);

	List<Chart> findAllByOrderByIDDesc();
}