package de.deadlocker8.budgetmaster.tags;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.utils.ProvidesID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
public class Tag implements ProvidesID
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer ID;

	@NotNull
	@Size(min=1)
	@Column(unique=true)
	@Expose
	private String name;

	@ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
	private List<Transaction> referringTransactions;

	@ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
	private List<Template> referringTemplates;

	public Tag()
	{
	}

	public Tag(String name)
	{
		this.name = name;
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

	public void setReferringTransactions(List<Transaction> referringTransactions)
	{
		this.referringTransactions = referringTransactions;
	}

	public List<Transaction> getReferringTransactions()
	{
		return referringTransactions;
	}

	public List<Template> getReferringTemplates()
	{
		return referringTemplates;
	}

	@Override
	public String toString()
	{
		return "Tag{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Tag tag = (Tag) o;
		return Objects.equals(ID, tag.ID) &&
				Objects.equals(name, tag.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name);
	}
}