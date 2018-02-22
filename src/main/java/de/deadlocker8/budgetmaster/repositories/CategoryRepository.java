package de.deadlocker8.budgetmaster.repositories;

import de.deadlocker8.budgetmaster.entities.Category;
import org.springframework.data.repository.CrudRepository;


public interface CategoryRepository extends CrudRepository<Category, Long>
{

}