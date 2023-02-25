package de.deadlocker8.budgetmaster.templategroup;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.utils.ProvidesID;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class TemplateGroup implements ProvidesID
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer ID;

	@Expose
	private String name;

	@Expose
	private TemplateGroupType type;

	@OneToMany(mappedBy = "templateGroup", fetch = FetchType.LAZY)
	private List<Template> referringTemplates;

	public TemplateGroup()
	{
	}

	public TemplateGroup(Integer ID, String name, TemplateGroupType type)
	{
		this.ID = ID;
		this.name = name;
		this.type = type;
	}

	public TemplateGroup(String name, TemplateGroupType type)
	{
		this.name = name;
		this.type = type;
	}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public TemplateGroupType getType()
	{
		return type;
	}

	public void setType(TemplateGroupType type)
	{
		this.type = type;
	}

	public List<Template> getReferringTemplates()
	{
		return referringTemplates;
	}

	public void setReferringTemplates(List<Template> referringTemplates)
	{
		this.referringTemplates = referringTemplates;
	}

	@Override
	public String toString()
	{
		return "TemplateGroup{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		TemplateGroup that = (TemplateGroup) o;
		return Objects.equals(ID, that.ID) && Objects.equals(name, that.name) && type == that.type;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, type);
	}
}
