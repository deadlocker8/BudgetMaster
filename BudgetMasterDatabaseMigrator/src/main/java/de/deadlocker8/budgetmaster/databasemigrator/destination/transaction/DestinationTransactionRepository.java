package de.deadlocker8.budgetmaster.databasemigrator.destination.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationTransactionRepository extends JpaRepository<DestinationTransaction, Integer>
{
}
