package de.deadlocker8.budgetmaster.tags;

import de.deadlocker8.budgetmaster.services.AccessAllEntities;
import de.deadlocker8.budgetmaster.services.Resettable;
import org.padler.natorder.NaturalOrderComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagService implements Resettable, AccessAllEntities<Tag>
{
	private final TagRepository tagRepository;

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

	@Override
	public List<Tag> getAllEntitiesAsc()
	{
		final List<Tag> tags = tagRepository.findAllByOrderByNameAsc();
		tags.sort((t1, t2) -> new NaturalOrderComparator().compare(t1.getName(), t2.getName()));
		return tags;
	}

	public Map<String, Integer> getUsageCounts()
	{
		HashMap<String, Integer> usageCounts = new HashMap<>();

		final List<Tag> tags = getAllEntitiesAsc();
		for(Tag tag : tags)
		{
			int numberOfTransactions = tag.getReferringTransactions().size();
			usageCounts.put(tag.getName(), numberOfTransactions);
		}

		return usageCounts;
	}
}
