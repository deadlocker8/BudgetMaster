package de.deadlocker8.budgetmaster.database.importer;

import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;

public class TagImporter extends ItemImporter<Tag>
{
	public TagImporter(TagRepository tagRepository)
	{
		super(tagRepository, EntityType.TAGS);
	}

	@Override
	protected int importSingleItem(Tag tag) throws ImportException
	{
		if(!(repository instanceof TagRepository repository))
		{
			throw new IllegalArgumentException("Invalid repository type");
		}

		final Tag existingTag = repository.findByName(tag.getName());
		if(existingTag == null)
		{
			final Tag newTag = repository.save(new Tag(tag.getName()));
			return newTag.getID();
		}

		return existingTag.getID();
	}

	@Override
	protected String getNameForItem(Tag item)
	{
		return String.valueOf(item.getName());
	}
}
