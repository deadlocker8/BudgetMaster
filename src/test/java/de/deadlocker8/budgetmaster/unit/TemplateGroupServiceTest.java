package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.accounts.*;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupService;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.templates.TemplateService;
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
import static org.assertj.core.api.Assertions.entry;

@ExtendWith(SpringExtension.class)
@LocalizedTest
class TemplateGroupServiceTest
{
	@Mock
	private TemplateGroupRepository templateGroupRepository;

	@InjectMocks
	private TemplateGroupService templateGroupService;

	@Test
	void test_getTemplatesByGroupedByTemplateGroup()
	{
		final TemplateGroup templateGroupDefault = new TemplateGroup("Default", TemplateGroupType.DEFAULT);
		final TemplateGroup templateGroup1 = new TemplateGroup("Template Group 1", TemplateGroupType.CUSTOM);
		final TemplateGroup templateGroup2 = new TemplateGroup("Template Group 2", TemplateGroupType.CUSTOM);

		final Template template1 = new Template();
		template1.setTemplateName("xyz");
		template1.setTemplateGroup(templateGroup1);

		final Template template2 = new Template();
		template2.setTemplateName("0815");
		template2.setTemplateGroup(templateGroupDefault);

		final Template template3 = new Template();
		template3.setTemplateName("bcd");
		template3.setTemplateGroup(templateGroup2);

		final Template template4 = new Template();
		template4.setTemplateName("ab");
		template4.setTemplateGroup(templateGroup1);

		templateGroupDefault.setReferringTemplates(List.of(template2));
		templateGroup1.setReferringTemplates(List.of(template1, template4));
		templateGroup2.setReferringTemplates(List.of(template3));

		final List<TemplateGroup> groups = new ArrayList<>();
		groups.add(templateGroup1);
		groups.add(templateGroup2);

		Mockito.when(templateGroupRepository.findAllByType(TemplateGroupType.CUSTOM)).thenReturn(groups);
		Mockito.when(templateGroupRepository.findFirstByType(TemplateGroupType.DEFAULT)).thenReturn(templateGroupDefault);

		final Map<TemplateGroup, List<Template>> templatesGrouped = templateGroupService.getTemplatesByGroupedByTemplateGroup();
		assertThat(templatesGrouped).hasSize(3)
				.containsExactly(entry(templateGroup1, List.of(template4, template1)),
						entry(templateGroup2, List.of(template3)),
						entry(templateGroupDefault, List.of(template2)));
	}
}