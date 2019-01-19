package de.deadlocker8.budgetmaster.repositories.report;

import de.deadlocker8.budgetmaster.entities.report.ReportSettings;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportSettingsRepository extends JpaRepository<ReportSettings, Integer>
{
}