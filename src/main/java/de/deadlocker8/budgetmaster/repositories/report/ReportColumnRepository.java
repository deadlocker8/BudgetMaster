package de.deadlocker8.budgetmaster.repositories.report;

import de.deadlocker8.budgetmaster.entities.report.ReportColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReportColumnRepository extends JpaRepository<ReportColumn, Integer>
{
	List<ReportColumn> findAllByOrderByPositionAsc();

	ReportColumn findByKey(String key);

	ReportColumn findByPosition(int position);
}