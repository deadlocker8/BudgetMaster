package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;

public class TemplateGroupImporter extends ItemImporter<TemplateGroup>
{
	public TemplateGroupImporter(TemplateGroupRepository templateGroupRepository)
	{
		super(templateGroupRepository, EntityType.TEMPLATE_GROUP);
	}

	@Override
	protected int importSingleItem(TemplateGroup templateGroup) throws ImportException
	{
		if(!(repository instanceof TemplateGroupRepository repository))
		{
			throw new IllegalArgumentException("Invalid repository type");
		}

		if(templateGroup.getType().equals(TemplateGroupType.DEFAULT))
		{
			return repository.findFirstByType(TemplateGroupType.DEFAULT).getID();
		}

		TemplateGroup templateGroupToCreate = new TemplateGroup(templateGroup.getName(), templateGroup.getType());
		TemplateGroup savedTemplateGroup = repository.save(templateGroupToCreate);
		return savedTemplateGroup.getID();
	}

	@Override
	protected String getNameForItem(TemplateGroup item)
	{
		return item.getName();
	}
}
