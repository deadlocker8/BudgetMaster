package de.deadlocker8.budgetmaster.databasemigrator.destination;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DestinationRepository<T, ID> extends JpaRepository<T, ID>
{
}
