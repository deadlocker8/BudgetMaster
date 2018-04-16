package de.deadlocker8.budgetmaster.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Account
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ID;

	@NotNull
	@Size(min = 1)
	private String name;

	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
	private List<Payment> referringPayments;

	public Account(String name)
	{
		this.name = name;
	}

	public Account()
	{
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

	public List<Payment> getReferringPayments()
	{
		return referringPayments;
	}

	public void setReferringPayments(List<Payment> referringPayments)
	{
		this.referringPayments = referringPayments;
	}

	@Override
	public String toString()
	{
		return "Account{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", referringPayments=" + referringPayments +
				'}';
	}
}