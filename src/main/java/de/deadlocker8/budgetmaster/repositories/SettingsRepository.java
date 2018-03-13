package de.deadlocker8.budgetmaster.repositories;

import de.deadlocker8.budgetmaster.entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SettingsRepository extends JpaRepository<Settings, Integer>
{
}