package de.deadlocker8.budgetmaster.accounts;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.transactions.Transaction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
public class Account
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer ID;

	@NotNull
	@Size(min = 1)
	@Column(unique = true)
	@Expose
	private String name;

	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
	private List<Transaction> referringTransactions;

	private Boolean isSelected = false;
	private Boolean isDefault = false;
	private Boolean isReadOnly = false;

	@Expose
	private String iconPath;

	@Expose
	private AccountType type;


	public Account(String name, AccountType type)
	{
		this.name = name;
		this.type = type;
		this.isSelected = false;
		this.isDefault = false;
		this.isReadOnly = false;
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

	public List<Transaction> getReferringTransactions()
	{
		return referringTransactions;
	}

	public void setReferringTransactions(List<Transaction> referringTransactions)
	{
		this.referringTransactions = referringTransactions;
	}

	public Boolean isSelected()
	{
		return isSelected;
	}

	public void setSelected(Boolean selected)
	{
		isSelected = selected;
	}

	public Boolean isDefault()
	{
		return isDefault;
	}

	public void setDefault(Boolean aDefault)
	{
		isDefault = aDefault;
	}

	public Boolean isReadOnly()
	{
		return isReadOnly;
	}

	public void setReadOnly(Boolean readOnly)
	{
		isReadOnly = readOnly;
	}

	public AccountType getType()
	{
		return type;
	}

	public void setType(AccountType type)
	{
		this.type = type;
	}

	public String getIconPath()
	{
		return iconPath;
	}

	public void setIconPath(String iconPath)
	{
		this.iconPath = iconPath;
	}

	@Override
	public String toString()
	{
		return "Account{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", referringTransactions=" + referringTransactions +
				", isSelected=" + isSelected +
				", isDefault=" + isDefault +
				", isReadOnly=" + isReadOnly +
				", type=" + type +
				", iconPath=" + iconPath +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Account account = (Account) o;
		return isSelected == account.isSelected &&
				isDefault == account.isDefault &&
				isReadOnly == account.isReadOnly &&
				Objects.equals(ID, account.ID) &&
				Objects.equals(name, account.name) &&
				type == account.type &&
				Objects.equals(iconPath, account.iconPath);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, isSelected, isDefault, isReadOnly, type, iconPath);
	}
}