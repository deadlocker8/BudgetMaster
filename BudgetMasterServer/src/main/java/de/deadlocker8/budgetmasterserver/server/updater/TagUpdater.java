package de.deadlocker8.budgetmasterserver.server.updater;

import java.util.ArrayList;

import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmasterserver.logic.database.taghandler.DatabaseTagHandler;

public class TagUpdater
{
	private DatabaseTagHandler tagHandler;
	
	public TagUpdater(DatabaseTagHandler tagHandler)
	{	
		this.tagHandler = tagHandler;
	}
	
	public void deleteTagsIfNotReferenced() 
	{
		ArrayList<Tag> tags = tagHandler.getAllTags();
		for(Tag currentTag : tags)
		{
			if(!tagHandler.isTagUsedInMatches(currentTag.getID()))
			{		
				tagHandler.deleteTag(currentTag.getID());
			}
		}
	}
}