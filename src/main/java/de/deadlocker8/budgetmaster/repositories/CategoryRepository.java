package de.deadlocker8.budgetmaster.repositories;

import de.deadlocker8.budgetmaster.entities.Category;
import de.deadlocker8.budgetmaster.entities.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Integer>
{
	Category findByName(String name);

	Category findByType(CategoryType categoryType);
}