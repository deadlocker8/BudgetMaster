package de.deadlocker8.budgetmaster.reports.columns;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReportColumnRepository extends JpaRepository<ReportColumn, Integer>
{
	List<ReportColumn> findAllByOrderByColumnPositionAsc();

	ReportColumn findByLocalizationKey(String key);

	ReportColumn findByColumnPosition(int position);
}