package de.deadlocker8.budgetmaster.unit.database.importer;

import de.deadlocker8.budgetmaster.database.importer.TemplateGroupImporter;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;
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
class TemplateGroupImporterTest
{
	@Autowired
	private TemplateGroupRepository templateGroupRepository;

	@Test
	void test_importTemplateGroupDefault()
	{
		TemplateGroup defaultTemplateGroup = new TemplateGroup("Default group", TemplateGroupType.DEFAULT);
		defaultTemplateGroup = templateGroupRepository.save(defaultTemplateGroup);

		final TemplateGroupImporter importer = new TemplateGroupImporter(templateGroupRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(defaultTemplateGroup));

		final ImportResultItem expected = new ImportResultItem(EntityType.TEMPLATE_GROUP, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final List<TemplateGroup> templateGroups = templateGroupRepository.findAll();
		assertThat(templateGroups)
				.hasSize(1)
				.containsExactly(defaultTemplateGroup);
	}

	@Test
	void test_importTemplateGroupAlreadyExisting()
	{
		TemplateGroup templateGroup = new TemplateGroup("My group", TemplateGroupType.CUSTOM);
		templateGroup = templateGroupRepository.save(templateGroup);

		final TemplateGroupImporter importer = new TemplateGroupImporter(templateGroupRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(templateGroup));

		final ImportResultItem expected = new ImportResultItem(EntityType.TEMPLATE_GROUP, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final List<TemplateGroup> templateGroups = templateGroupRepository.findAll();
		assertThat(templateGroups)
				.hasSize(1)
				.containsExactly(templateGroup);
	}

	@Test
	void test_importTemplateGroupNotExisting()
	{
		final TemplateGroup defaultTemplateGroup = new TemplateGroup(15, "My group", TemplateGroupType.CUSTOM);

		final TemplateGroupImporter importer = new TemplateGroupImporter(templateGroupRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(defaultTemplateGroup));

		final ImportResultItem expected = new ImportResultItem(EntityType.TEMPLATE_GROUP, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final TemplateGroup expectedTemplateGroup = new TemplateGroup(1, "My group", TemplateGroupType.CUSTOM);

		final List<TemplateGroup> templateGroups = templateGroupRepository.findAll();
		assertThat(templateGroups)
				.hasSize(1)
				.containsExactly(expectedTemplateGroup);
	}
}