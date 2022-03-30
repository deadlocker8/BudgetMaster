package de.deadlocker8.budgetmaster.unit.database.importer;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.importer.CategoryImporter;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CategoryImporterTest
{
	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	void test_importCategories()
	{
		final Category category1 = new Category("Category1", "#ff0000", CategoryType.CUSTOM);
		category1.setID(2);

		final Category category2 = new Category("Category2", "#ff0000", CategoryType.CUSTOM);
		category2.setID(3);

		final CategoryImporter importer = new CategoryImporter(categoryRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(category1, category2));

		final ImportResultItem expected = new ImportResultItem(EntityType.CATEGORY, 2, 2, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(category1).hasFieldOrPropertyWithValue("ID", 1);
		assertThat(category2).hasFieldOrPropertyWithValue("ID", 2);
	}

	@Test
	void test_importCategories_skipExisting_defaultNone()
	{
		final Category category = new Category("No category", "#ff0000", CategoryType.NONE);
		category.setID(1);

		categoryRepository.save(category);

		final CategoryImporter importer = new CategoryImporter(categoryRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(category));

		final ImportResultItem expected = new ImportResultItem(EntityType.CATEGORY, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(category).hasFieldOrPropertyWithValue("ID", 1);
	}

	@Test
	void test_importCategories_skipExisting_defaultRest()
	{
		final Category category = new Category("Rest", "#ff0000", CategoryType.REST);
		category.setID(1);

		categoryRepository.save(category);

		final CategoryImporter importer = new CategoryImporter(categoryRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(category));

		final ImportResultItem expected = new ImportResultItem(EntityType.CATEGORY, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(category).hasFieldOrPropertyWithValue("ID", 1);
	}

	@Test
	void test_importCategories_skipExisting_custom()
	{
		final Category category = new Category("Category1", "#ff0000", CategoryType.CUSTOM);
		category.setID(3);

		categoryRepository.save(new Category("existing category 1", "#ffffff", CategoryType.CUSTOM));
		categoryRepository.save(new Category("existing category 2", "#ffffff", CategoryType.CUSTOM));
		categoryRepository.save(category);

		final CategoryImporter importer = new CategoryImporter(categoryRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(category));

		final ImportResultItem expected = new ImportResultItem(EntityType.CATEGORY, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(category).hasFieldOrPropertyWithValue("ID", 3);
	}
}