package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.tags.TagService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@LocalizedTest
class TagServiceTest
{
	@Mock
	private TagRepository tagRepository;

	@InjectMocks
	private TagService tagService;

	final Tag tag_1 = new Tag("car stuff");
	final Tag tag_2 = new Tag("fruits");
	final Tag tag_3 = new Tag("10");
	final Tag tag_4 = new Tag("0");
	final Tag tag_5 = new Tag("1");
	final Tag tag_6 = new Tag("Fruits");

	@Test
	void test_getAllTags()
	{
		List<Tag> tags = new ArrayList<>(List.of(tag_1, tag_2, tag_3, tag_4, tag_5, tag_6));

		Mockito.when(tagRepository.findAllByOrderByNameAsc()).thenReturn(tags);

		assertThat(tagService.getAllEntitiesAsc()).hasSize(6)
				.containsExactly(tag_4, tag_5, tag_3, tag_1, tag_2, tag_6);
	}

	@Test
	void test_getUsageCounts()
	{
		tag_1.setReferringTransactions(List.of());
		tag_2.setReferringTransactions(List.of());
		tag_3.setReferringTransactions(List.of());
		tag_4.setReferringTransactions(List.of(new Transaction()));
		tag_5.setReferringTransactions(List.of());
		tag_6.setReferringTransactions(List.of());

		List<Tag> tags = new ArrayList<>(List.of(tag_1, tag_2, tag_3, tag_4, tag_5, tag_6));
		Mockito.when(tagRepository.findAllByOrderByNameAsc()).thenReturn(tags);

		assertThat(tagService.getUsageCounts())
				.hasSize(6)
				.containsExactly(Map.entry("0", 1),
						Map.entry("1", 0),
						Map.entry("10", 0),
						Map.entry("car stuff", 0),
						Map.entry("fruits", 0),
						Map.entry("Fruits", 0));
	}
}
