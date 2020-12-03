package de.deadlocker8.budgetmaster.tags;

import de.deadlocker8.budgetmaster.services.Resetable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService implements Resetable
{
	private TagRepository tagRepository;

	@Autowired
	public TagService(TagRepository tagRepository)
	{
		this.tagRepository = tagRepository;
	}

	public TagRepository getRepository()
	{
		return tagRepository;
	}

	@Override
	public void deleteAll()
	{
		tagRepository.deleteAll();
	}

	@Override
	public void createDefaults()
	{
	}
}
