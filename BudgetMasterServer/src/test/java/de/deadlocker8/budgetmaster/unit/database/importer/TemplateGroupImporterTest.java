package de.deadlocker8.budgetmaster.unit.database.importer;

import de.deadlocker8.budgetmaster.database.importer.TemplateGroupImporter;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateGroupImporterTest extends ImporterTestBase
{
	@Override
	List<String> getTableNamesToResetSequence()
	{
		return List.of("template_group");
	}

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
		Assertions.assertThat(templateGroups)
				.hasSize(1)
				.containsExactly(defaultTemplateGroup);
	}

	@Test
	void test_importTemplateGroup()
	{
		final TemplateGroup defaultTemplateGroup = new TemplateGroup(15, "My group", TemplateGroupType.CUSTOM);

		final TemplateGroupImporter importer = new TemplateGroupImporter(templateGroupRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(defaultTemplateGroup));

		final ImportResultItem expected = new ImportResultItem(EntityType.TEMPLATE_GROUP, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final TemplateGroup expectedTemplateGroup = new TemplateGroup(1, "My group", TemplateGroupType.CUSTOM);

		final List<TemplateGroup> templateGroups = templateGroupRepository.findAll();
		Assertions.assertThat(templateGroups)
				.hasSize(1)
				.containsExactly(expectedTemplateGroup);
	}
}