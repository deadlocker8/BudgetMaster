package de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.end;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepeatingEndAfterXTimesRepository extends JpaRepository<DestinationRepeatingEndAfterXTimes, Integer>
{
}
