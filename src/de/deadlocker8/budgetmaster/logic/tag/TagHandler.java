package de.deadlocker8.budgetmaster.logic.tag;

import java.util.ArrayList;

public class TagHandler
{
	private ArrayList<Tag> tags;

	public TagHandler()
	{
		this.tags = new ArrayList<>();
	}

	public ArrayList<Tag> getTags()
	{
		return tags;
	}

	public void setTags(ArrayList<Tag> tags)
	{
		this.tags = tags;
	}

	@Override
	public String toString()
	{
		return "TagHandler [tags=" + tags + "]";
	}
}