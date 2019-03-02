package de.deadlocker8.budgetmaster.settings;

import de.deadlocker8.budgetmaster.settings.Settings;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SettingsRepository extends JpaRepository<Settings, Integer>
{
}