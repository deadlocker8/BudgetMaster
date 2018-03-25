package de.deadlocker8.budgetmaster.repositories;

import de.deadlocker8.budgetmaster.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TagRepository extends JpaRepository<Tag, Integer>
{
	Tag findByName(String name);

	List<Tag> findAllByOrderByNameAsc();
}