package de.deadlocker8.budgetmaster.unit.database.importer;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.database.importer.TagImporter;
import de.deadlocker8.budgetmaster.database.importer.TemplateImporter;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconRepository;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
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
class TemplateImporterTest
{
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private IconRepository iconRepository;

	@Autowired
	private TemplateGroupRepository templateGroupRepository;

	@Test
	void test_importTemplate()
	{
		Category category = new Category("Awesome Category", "#ff0000", CategoryType.CUSTOM);
		category = categoryRepository.save(category);

		Account account = new Account("Awesome Account", AccountType.CUSTOM);
		account = accountRepository.save(account);

		Account transferAccount = new Account("Transfer Account", AccountType.CUSTOM);
		transferAccount = accountRepository.save(transferAccount);

		Icon icon = new Icon("fas fa-icons");
		icon = iconRepository.save(icon);

		final Template template = new Template();
		template.setID(15);
		template.setTemplateName("My awesome template");
		template.setName("My transaction");
		template.setAmount(-100);
		template.setIsExpenditure(true);
		template.setCategory(category);
		template.setAccount(account);
		template.setTags(List.of());
		template.setDescription("Lorem Ipsum");
		template.setIconReference(icon);
		template.setTransferAccount(transferAccount);

		TemplateGroup defaultTemplateGroup = new TemplateGroup("Default group", TemplateGroupType.DEFAULT);
		defaultTemplateGroup = templateGroupRepository.save(defaultTemplateGroup);

		final TagImporter tagImporter = new TagImporter(tagRepository);
		final TemplateImporter importer = new TemplateImporter(templateRepository, tagImporter, defaultTemplateGroup, false);
		final ImportResultItem resultItem = importer.importItems(List.of(template));

		final ImportResultItem expected = new ImportResultItem(EntityType.TEMPLATE, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final List<Template> templates = templateRepository.findAll();
		assertThat(templates).hasSize(1);
		final Template actualTemplate = templates.get(0);
		assertThat(actualTemplate)
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("templateName", "My awesome template")
				.hasFieldOrPropertyWithValue("name", "My transaction")
				.hasFieldOrPropertyWithValue("amount", -100)
				.hasFieldOrPropertyWithValue("isExpenditure", true)
				.hasFieldOrPropertyWithValue("category", category)
				.hasFieldOrPropertyWithValue("account", account)
				.hasFieldOrPropertyWithValue("description", "Lorem Ipsum")
				.hasFieldOrPropertyWithValue("transferAccount", transferAccount)
				.hasFieldOrPropertyWithValue("templateGroup", defaultTemplateGroup)
				.hasFieldOrPropertyWithValue("iconReference", icon);
		assertThat(actualTemplate.getTags()).isEmpty();
	}

	@Test
	void test_importTemplateWithTags()
	{
		Category category = new Category("Awesome Category", "#ff0000", CategoryType.CUSTOM);
		category = categoryRepository.save(category);

		Account account = new Account("Awesome Account", AccountType.CUSTOM);
		account = accountRepository.save(account);

		Icon icon = new Icon("fas fa-icons");
		icon = iconRepository.save(icon);

		final Template template = new Template();
		template.setID(15);
		template.setTemplateName("My awesome template");
		template.setName("My transaction");
		template.setAmount(-100);
		template.setIsExpenditure(true);
		template.setCategory(category);
		template.setAccount(account);
		template.setTags(List.of());
		template.setDescription("Lorem Ipsum");
		template.setIconReference(icon);

		final Tag tag1 = new Tag("0815");
		tag1.setID(1);
		final Tag tag2 = new Tag("Apple Pie");
		tag2.setID(2);
		template.setTags(List.of(tag1, tag2));

		TemplateGroup defaultTemplateGroup = new TemplateGroup("Default group", TemplateGroupType.DEFAULT);
		defaultTemplateGroup = templateGroupRepository.save(defaultTemplateGroup);

		final TagImporter tagImporter = new TagImporter(tagRepository);
		final TemplateImporter importer = new TemplateImporter(templateRepository, tagImporter, defaultTemplateGroup, false);
		final ImportResultItem resultItem = importer.importItems(List.of(template));

		final ImportResultItem expected = new ImportResultItem(EntityType.TEMPLATE, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final List<Template> templates = templateRepository.findAll();
		assertThat(templates).hasSize(1);
		final Template actualTemplate = templates.get(0);
		assertThat(actualTemplate)
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("templateName", "My awesome template")
				.hasFieldOrPropertyWithValue("name", "My transaction")
				.hasFieldOrPropertyWithValue("amount", -100)
				.hasFieldOrPropertyWithValue("isExpenditure", true)
				.hasFieldOrPropertyWithValue("category", category)
				.hasFieldOrPropertyWithValue("account", account)
				.hasFieldOrPropertyWithValue("description", "Lorem Ipsum")
				.hasFieldOrPropertyWithValue("transferAccount", null)
				.hasFieldOrPropertyWithValue("templateGroup", defaultTemplateGroup)
				.hasFieldOrPropertyWithValue("iconReference", icon);

		final Tag expectedTag1 = new Tag("0815");
		expectedTag1.setID(1);
		final Tag expectedTag2 = new Tag("Apple Pie");
		expectedTag2.setID(2);
		assertThat(actualTemplate.getTags())
				.containsExactly(expectedTag1, expectedTag2);
	}

	@Test
	void test_test_importMultipleTemplatesWithSomeSimilarTags()
	{
		Category category = new Category("Awesome Category", "#ff0000", CategoryType.CUSTOM);
		category = categoryRepository.save(category);

		Account account = new Account("Awesome Account", AccountType.CUSTOM);
		account = accountRepository.save(account);

		Icon icon = new Icon("fas fa-icons");
		icon = iconRepository.save(icon);

		final Template template = new Template();
		template.setID(15);
		template.setTemplateName("My awesome template");
		template.setName("My transaction");
		template.setAmount(-100);
		template.setIsExpenditure(true);
		template.setCategory(category);
		template.setAccount(account);
		template.setTags(List.of());
		template.setDescription("Lorem Ipsum");
		template.setIconReference(icon);
		template.setTags(List.of( new Tag("0815"), new Tag("Apple Pie")));

		final Template template2 = new Template();
		template2.setID(16);
		template2.setTemplateName("My awesome template 2");
		template2.setIsExpenditure(true);
		template2.setTags(List.of());
		template2.setDescription("Lorem Ipsum");
		template2.setIconReference(icon);
		template2.setTags(List.of( new Tag("0815")));

		TemplateGroup defaultTemplateGroup = new TemplateGroup("Default group", TemplateGroupType.DEFAULT);
		defaultTemplateGroup = templateGroupRepository.save(defaultTemplateGroup);

		final TagImporter tagImporter = new TagImporter(tagRepository);
		final TemplateImporter importer = new TemplateImporter(templateRepository, tagImporter, defaultTemplateGroup, false);
		final ImportResultItem resultItem = importer.importItems(List.of(template, template2));

		final ImportResultItem expected = new ImportResultItem(EntityType.TEMPLATE, 2, 2, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final List<Template> templates = templateRepository.findAll();
		assertThat(templates).hasSize(2);
		final Template actualTemplate = templates.get(0);
		assertThat(actualTemplate)
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("templateName", "My awesome template");

		final Tag expectedTag1 = new Tag("0815");
		expectedTag1.setID(1);
		final Tag expectedTag2 = new Tag("Apple Pie");
		expectedTag2.setID(2);
		assertThat(actualTemplate.getTags())
				.containsExactly(expectedTag1, expectedTag2);

		final Template actualTemplate2 = templates.get(1);
		assertThat(actualTemplate2)
				.hasFieldOrPropertyWithValue("ID", 2)
				.hasFieldOrPropertyWithValue("templateName", "My awesome template 2");

		assertThat(actualTemplate2.getTags())
				.containsExactly(expectedTag1);


		assertThat(tagRepository.findAll()).hasSize(2);
	}

	@Test
	void test_importTemplateWithTemplateGroup()
	{
		final Template template = new Template();
		template.setID(15);
		template.setTemplateName("My awesome template");
		template.setName("My transaction");
		template.setIsExpenditure(true);
		template.setTags(List.of());

		TemplateGroup templateGroup = new TemplateGroup("My group", TemplateGroupType.CUSTOM);
		templateGroup = templateGroupRepository.save(templateGroup);
		template.setTemplateGroup(templateGroup);

		TemplateGroup defaultTemplateGroup = new TemplateGroup("Default group", TemplateGroupType.DEFAULT);
		defaultTemplateGroup = templateGroupRepository.save(defaultTemplateGroup);

		final TagImporter tagImporter = new TagImporter(tagRepository);
		final TemplateImporter importer = new TemplateImporter(templateRepository, tagImporter, defaultTemplateGroup, false);
		final ImportResultItem resultItem = importer.importItems(List.of(template));

		final ImportResultItem expected = new ImportResultItem(EntityType.TEMPLATE, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final List<Template> templates = templateRepository.findAll();
		assertThat(templates).hasSize(1);
		final Template actualTemplate = templates.get(0);
		assertThat(actualTemplate)
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("templateGroup", templateGroup);
		assertThat(actualTemplate.getTags()).isEmpty();
	}

	@Test
	void test_importTemplateAlwaysAssignDefaultTemplateGroup()
	{
		final Template template = new Template();
		template.setID(15);
		template.setTemplateName("My awesome template");
		template.setName("My transaction");
		template.setIsExpenditure(true);
		template.setTags(List.of());

		TemplateGroup templateGroup = new TemplateGroup("My group", TemplateGroupType.CUSTOM);
		templateGroup = templateGroupRepository.save(templateGroup);
		template.setTemplateGroup(templateGroup);

		TemplateGroup defaultTemplateGroup = new TemplateGroup("Default group", TemplateGroupType.DEFAULT);
		defaultTemplateGroup = templateGroupRepository.save(defaultTemplateGroup);

		final TagImporter tagImporter = new TagImporter(tagRepository);
		final TemplateImporter importer = new TemplateImporter(templateRepository, tagImporter, defaultTemplateGroup, true);
		final ImportResultItem resultItem = importer.importItems(List.of(template));

		final ImportResultItem expected = new ImportResultItem(EntityType.TEMPLATE, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);

		final List<Template> templates = templateRepository.findAll();
		assertThat(templates).hasSize(1);
		final Template actualTemplate = templates.get(0);
		assertThat(actualTemplate)
				.hasFieldOrPropertyWithValue("ID", 1)
				.hasFieldOrPropertyWithValue("templateGroup", defaultTemplateGroup);
		assertThat(actualTemplate.getTags()).isEmpty();
	}
}