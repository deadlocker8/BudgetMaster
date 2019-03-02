package de.deadlocker8.budgetmaster.categories;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Integer>
{
	List<Category> findAllByOrderByNameAsc();

	Category findByName(String name);

	Category findByType(CategoryType categoryType);

	Category findByNameAndColorAndType(String name, String color, CategoryType categoryType);
}