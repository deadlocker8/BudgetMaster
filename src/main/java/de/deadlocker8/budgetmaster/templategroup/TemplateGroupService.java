package de.deadlocker8.budgetmaster.templategroup;

import de.deadlocker8.budgetmaster.services.AccessAllEntities;
import de.deadlocker8.budgetmaster.services.AccessEntityByID;
import de.deadlocker8.budgetmaster.services.Resettable;
import org.padler.natorder.NaturalOrderComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

	@Override
	public void deleteAll()
	{
		templateGroupRepository.deleteAll();
	}

	@Override
	public void createDefaults()
	{
		if(templateGroupRepository.findAll().isEmpty() || templateGroupRepository.findFirstByType(TemplateGroupType.ALL) == null)
		{
			TemplateGroup defaultGroup = new TemplateGroup();
			defaultGroup.setName("Default");
			defaultGroup.setType(TemplateGroupType.ALL);

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

}
