package de.deadlocker8.budgetmaster.databasemigrator.destination;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface DestinationRepository<T> extends JpaRepository<T, Integer>
{
	List<T> findAllByOrderByIDDesc();
}
