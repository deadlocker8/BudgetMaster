package de.deadlocker8.budgetmaster.unit.database.importer;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.importer.CategoryImporter;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconRepository;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryImporterTest extends ImporterTestBase
{
	@Override
	List<String> getTableNamesToResetSequence()
	{
		return List.of("category");
	}

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private IconRepository iconRepository;

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
		final Category categoryExisting = new Category("Category1", "#ff0000", CategoryType.CUSTOM);
		categoryExisting.setID(3);

		final Category categoryToImport = new Category("Category1", "#ff0000", CategoryType.CUSTOM);
		categoryToImport.setID(15);

		categoryRepository.save(new Category("existing category 1", "#ffffff", CategoryType.CUSTOM));
		categoryRepository.save(new Category("existing category 2", "#ffffff", CategoryType.CUSTOM));
		categoryRepository.save(categoryExisting);

		final CategoryImporter importer = new CategoryImporter(categoryRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(categoryToImport));

		final ImportResultItem expected = new ImportResultItem(EntityType.CATEGORY, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(categoryToImport).hasFieldOrPropertyWithValue("ID", 3);
	}

	@Test
	void test_importCategories_existingCategory_takeExistingIcon()
	{
		final Icon icon = new Icon("fas fa-icons");
		icon.setID(1);
		iconRepository.save(icon);

		final Category categoryExisting = new Category("Category1", "#ff0000", CategoryType.CUSTOM);
		categoryExisting.setIconReference(icon);
		categoryExisting.setID(3);

		final Category categoryToImport = new Category("Category1", "#ff0000", CategoryType.CUSTOM);
		categoryToImport.setID(15);

		categoryRepository.save(categoryExisting);

		final CategoryImporter importer = new CategoryImporter(categoryRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(categoryToImport));

		final ImportResultItem expected = new ImportResultItem(EntityType.CATEGORY, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(categoryToImport)
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("iconReference", icon);

	}
}