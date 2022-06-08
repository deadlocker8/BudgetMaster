package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class TemplateImporter extends ItemImporter<Template>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateImporter.class);

	private final TagImporter tagImporter;
	private final TemplateGroup defaultTemplateGroup;
	private final boolean alwaysAssignDefaultTemplateGroup;

	public TemplateImporter(TemplateRepository templateRepository, TagImporter tagImporter, TemplateGroup defaultTemplateGroup, boolean alwaysAssignDefaultTemplateGroup)
	{
		super(templateRepository, EntityType.TEMPLATE);
		this.tagImporter = tagImporter;
		this.defaultTemplateGroup = defaultTemplateGroup;
		this.alwaysAssignDefaultTemplateGroup = alwaysAssignDefaultTemplateGroup;
	}

	@Override
	protected int importSingleItem(Template template) throws ImportException
	{
		if(!(repository instanceof TemplateRepository repository))
		{
			throw new IllegalArgumentException("Invalid repository type");
		}

		LOGGER.debug(MessageFormat.format("Importing template with templateName: {0})", template.getTemplateName()));

		tagImporter.importItems(template.getTags());
		template.setID(null);

		if(alwaysAssignDefaultTemplateGroup || template.getTemplateGroup() == null)
		{
			template.setTemplateGroup(defaultTemplateGroup);
		}

		final Template newTemplate = repository.save(template);
		return newTemplate.getID();
	}

	@Override
	protected String getNameForItem(Template item)
	{
		return item.getTemplateName();
	}
}
