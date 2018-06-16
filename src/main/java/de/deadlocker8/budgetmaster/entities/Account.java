package de.deadlocker8.budgetmaster.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
public class Account
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
	private Integer ID;

	@NotNull
	@Size(min = 1)
	@Column(unique=true)
	@Expose
	private String name;

	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
	private List<Payment> referringPayments;

	private boolean isSelected;

	public Account(String name)
	{
		this.name = name;
		this.isSelected = false;
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

	public boolean isSelected()
	{
		return isSelected;
	}

	public void setSelected(boolean selected)
	{
		isSelected = selected;
	}

	@Override
	public String toString()
	{
		return "Account{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", referringPayments=" + referringPayments +
				", isSelected=" + isSelected +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Account account = (Account) o;
		return isSelected == account.isSelected &&
				Objects.equals(ID, account.ID) &&
				Objects.equals(name, account.name);
	}

	@Override
	public int hashCode()
	{

		return Objects.hash(ID, name, isSelected);
	}
}