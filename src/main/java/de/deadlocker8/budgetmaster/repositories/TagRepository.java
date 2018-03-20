package de.deadlocker8.budgetmaster.repositories;

import de.deadlocker8.budgetmaster.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TagRepository extends JpaRepository<Tag, Integer>
{
	Tag findByName(String name);
}