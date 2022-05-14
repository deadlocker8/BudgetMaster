package de.deadlocker8.budgetmaster.databasemigrator.destination;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface DestinationIntegerRepository<T, ID> extends DestinationRepository<T, ID>
{
	List<T> findAllByOrderByIDDesc();
}
