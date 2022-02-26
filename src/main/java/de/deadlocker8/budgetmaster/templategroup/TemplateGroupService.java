package de.deadlocker8.budgetmaster.templategroup;

import de.deadlocker8.budgetmaster.services.AccessAllEntities;
import de.deadlocker8.budgetmaster.services.AccessEntityByID;
import de.deadlocker8.budgetmaster.services.Resettable;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;
import org.padler.natorder.NaturalOrderComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TemplateGroupService implements Resettable, AccessAllEntities<TemplateGroup>, AccessEntityByID<TemplateGroup>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateGroupService.class);

	private final TemplateGroupRepository templateGroupRepository;

	@Autowired
	public TemplateGroupService(TemplateGroupRepository templateGroupRepository)
	{
		this.templateGroupRepository = templateGroupRepository;

		createDefaults();
	}

	public TemplateGroupRepository getRepository()
	{
		return templateGroupRepository;
	}

	@SuppressWarnings("OptionalIsPresent")
	public boolean isDeletable(Integer ID)
	{
		Optional<TemplateGroup> templateGroupOptional = findById(ID);
		if(templateGroupOptional.isPresent())
		{
			return templateGroupOptional.get().getType() == TemplateGroupType.CUSTOM;
		}

		return false;
	}

	@Override
	public void deleteAll()
	{
		for(TemplateGroup templateGroup : getAllEntitiesAsc())
		{
			deleteTemplateGroup(templateGroup.getID());
		}
	}

	public void deleteTemplateGroup(int ID)
	{
		Optional<TemplateGroup> templateGroupOptional = templateGroupRepository.findById(ID);
		if(templateGroupOptional.isEmpty())
		{
			throw new NoSuchElementException("Can't delete non-existing template group with ID: " + ID);
		}

		TemplateGroup templateGroupToDelete = templateGroupOptional.get();
		List<Template> referringTemplates = templateGroupToDelete.getReferringTemplates();
		if(referringTemplates != null)
		{
			for(Template template : referringTemplates)
			{
				template.setTemplateGroup(getDefaultGroup());
			}
		}

		templateGroupRepository.deleteById(ID);
	}

	@Override
	public void createDefaults()
	{
		if(templateGroupRepository.findAll().isEmpty() || templateGroupRepository.findFirstByType(TemplateGroupType.DEFAULT) == null)
		{
			TemplateGroup defaultGroup = new TemplateGroup();
			defaultGroup.setName(Localization.getString(Strings.TEMPLATE_GROUP_DEFAULT));
			defaultGroup.setType(TemplateGroupType.DEFAULT);

			templateGroupRepository.save(defaultGroup);
			LOGGER.debug("Created default template group");
		}
	}

	@Override
	public List<TemplateGroup> getAllEntitiesAsc()
	{
		final List<TemplateGroup> templateGroups = templateGroupRepository.findAllByType(TemplateGroupType.CUSTOM);
		templateGroups.sort((t1, t2) -> new NaturalOrderComparator().compare(t1.getName(), t2.getName()));
		return templateGroups;
	}

	@Override
	public Optional<TemplateGroup> findById(Integer ID)
	{
		return templateGroupRepository.findById(ID);
	}

	public TemplateGroup getDefaultGroup()
	{
		return templateGroupRepository.findFirstByType(TemplateGroupType.DEFAULT);
	}

	public Map<TemplateGroup, List<Template>> getTemplatesByGroupedByTemplateGroup()
	{
		final List<TemplateGroup> templateGroups = getAllEntitiesAsc();
		final TemplateGroup defaultGroup = getDefaultGroup();

		final Map<TemplateGroup, List<Template>> templatesGrouped = new LinkedHashMap<>();

		for(TemplateGroup templateGroup : templateGroups)
		{
			templatesGrouped.put(templateGroup, getReferringTemplatesSorted(templateGroup));
		}

		templatesGrouped.put(defaultGroup, getReferringTemplatesSorted(defaultGroup));

		return templatesGrouped;
	}

	private List<Template> getReferringTemplatesSorted(TemplateGroup templateGroup)
	{
		final List<Template> referringTemplates = new ArrayList<>(templateGroup.getReferringTemplates());
		referringTemplates.sort((t1, t2) -> new NaturalOrderComparator().compare(t1.getTemplateName(), t2.getTemplateName()));
		return referringTemplates;
	}
}
