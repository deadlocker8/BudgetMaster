package de.deadlocker8.budgetmaster.transactions.csvimport;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CsvImportSettingsRepository extends JpaRepository<CsvImportSettings, Integer>
{
}