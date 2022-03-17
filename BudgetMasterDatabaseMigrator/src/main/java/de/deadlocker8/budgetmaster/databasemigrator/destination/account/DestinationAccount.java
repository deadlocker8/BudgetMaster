package de.deadlocker8.budgetmaster.databasemigrator.destination.account;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "account")
public class DestinationAccount
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;

	@Column(unique = true)
	private String name;

	@Column(name = "is_selected")
	private boolean isSelected;

	@Column(name = "is_default")
	private boolean isDefault;

	@Column(name = "account_state")
	private Integer accountState;

	@Column(name = "icon_reference_id")
	private Integer iconReferenceID;

	private Integer type;

	public DestinationAccount()
	{
		// empty
	}

	public DestinationAccount(Integer ID, String name, boolean isSelected, boolean isDefault, Integer accountState, Integer iconReferenceID, Integer type)
	{
		this.ID = ID;
		this.name = name;
		this.isSelected = isSelected;
		this.isDefault = isDefault;
		this.accountState = accountState;
		this.iconReferenceID = iconReferenceID;
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

	public boolean getSelected()
	{
		return isSelected;
	}

	public void setSelected(boolean selected)
	{
		isSelected = selected;
	}

	public boolean getDefault()
	{
		return isDefault;
	}

	public void setDefault(boolean aDefault)
	{
		isDefault = aDefault;
	}

	public Integer getAccountState()
	{
		return accountState;
	}

	public void setAccountState(Integer accountState)
	{
		this.accountState = accountState;
	}

	public Integer getIconReferenceID()
	{
		return iconReferenceID;
	}

	public void setIconReferenceID(Integer iconReferenceID)
	{
		this.iconReferenceID = iconReferenceID;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		DestinationAccount account = (DestinationAccount) o;
		return isSelected == account.isSelected && isDefault == account.isDefault && Objects.equals(ID, account.ID) && Objects.equals(name, account.name) && Objects.equals(accountState, account.accountState) && Objects.equals(iconReferenceID, account.iconReferenceID) && Objects.equals(type, account.type);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, isSelected, isDefault, accountState, iconReferenceID, type);
	}

	@Override
	public String toString()
	{
		return "DestinationAccount{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", isSelected=" + isSelected +
				", isDefault=" + isDefault +
				", accountState=" + accountState +
				", iconReferenceID=" + iconReferenceID +
				", type=" + type +
				'}';
	}
}
