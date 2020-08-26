package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest
{
	private static final Category CATEGORY_NONE = new Category("No Category", "#FFFFFF", CategoryType.NONE);
	private static final Category CATEGORY_REST = new Category("Rest", "#FFFF00", CategoryType.REST);

	@MockBean(CategoryRepository.class)
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryService categoryService;

	@Test
	public void test_getAllCategories()
	{
		List<Category> categories = new ArrayList<>();
		categories.add(CATEGORY_NONE);
		categories.add(CATEGORY_REST);

		Category category_BB = new Category("BB", "#ff0000", CategoryType.CUSTOM);
		categories.add(category_BB);

		Category category_AA = new Category("AA", "#ff0000", CategoryType.CUSTOM);
		categories.add(category_AA);

		Category category_0 = new Category("0", "#ff0000", CategoryType.CUSTOM);
		categories.add(category_0);

		Category category_aa = new Category("aa", "#ff0000", CategoryType.CUSTOM);
		categories.add(category_aa);

		Mockito.when(categoryRepository.findByType(CategoryType.NONE)).thenReturn(CATEGORY_NONE);
		Mockito.when(categoryRepository.findByType(CategoryType.REST)).thenReturn(CATEGORY_REST);
		Mockito.when(categoryRepository.findAllByOrderByNameAsc()).thenReturn(categories);

		assertThat(categoryService.getAllCategories()).hasSize(6)
				.containsExactly(category_0, category_AA, category_aa, category_BB, CATEGORY_NONE, CATEGORY_REST);
	}

	@Test
	public void test_createDefaults()
	{
		categoryService.createDefaults();

		// createDefaults() may also be called in constructor so 2 calls are possible
		Mockito.verify(categoryRepository, Mockito.atLeast(1)).save(CATEGORY_NONE);
		Mockito.verify(categoryRepository, Mockito.atLeast(1)).save(CATEGORY_REST);
	}

	@Test
	public void test_isDeletable_default()
	{
		Mockito.when(categoryRepository.findById(1)).thenReturn(Optional.of(CATEGORY_NONE));

		assertThat(categoryService.isDeletable(1)).isFalse();
	}

	@Test
	public void test_isDeletable_custom()
	{
		Category customCategory = new Category("aa", "#ff0000", CategoryType.CUSTOM);

		Mockito.when(categoryRepository.findById(1)).thenReturn(Optional.of(customCategory));

		assertThat(categoryService.isDeletable(1)).isTrue();
	}
}
