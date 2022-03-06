package de.deadlocker8.budgetmaster.reports.columns;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReportColumnRepository extends JpaRepository<ReportColumn, Integer>
{
	List<ReportColumn> findAllByOrderByPositionAsc();

	ReportColumn findByKey(String key);

	ReportColumn findByPosition(int position);
}