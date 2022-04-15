package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;

import java.util.Optional;

public class TemplateGroupImporter extends ItemImporter<TemplateGroup>
{
	public TemplateGroupImporter(TemplateGroupRepository templateGroupRepository)
	{
		super(templateGroupRepository, EntityType.TEMPLATE_GROUP);
	}

	@Override
	protected int importSingleItem(TemplateGroup templateGroup)
	{
		if(!(repository instanceof TemplateGroupRepository repository))
		{
			throw new IllegalArgumentException("Invalid repository type");
		}

		final Optional<TemplateGroup> existingTemplateGroupOptional = findExistingTemplateGroup(templateGroup, repository);
		if(existingTemplateGroupOptional.isEmpty())
		{
			// template group does not exist --> create it
			TemplateGroup templateGroupToCreate = new TemplateGroup(templateGroup.getName(), templateGroup.getType());
			TemplateGroup savedTemplateGroup = repository.save(templateGroupToCreate);

			return savedTemplateGroup.getID();
		}
		else
		{
			// template group already exists
			final TemplateGroup existingTemplateGroup = existingTemplateGroupOptional.get();
			return existingTemplateGroup.getID();
		}
	}

	private Optional<TemplateGroup> findExistingTemplateGroup(TemplateGroup templateGroup, TemplateGroupRepository repository)
	{
		if(templateGroup.getType().equals(TemplateGroupType.DEFAULT))
		{
			return Optional.of(repository.findFirstByType(TemplateGroupType.DEFAULT));
		}
		else
		{
			return Optional.ofNullable(repository.findByNameAndType(templateGroup.getName(), templateGroup.getType()));
		}
	}

	@Override
	protected String getNameForItem(TemplateGroup item)
	{
		return item.getName();
	}
}
