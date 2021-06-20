package de.deadlocker8.budgetmaster.database.model.v6;

import de.deadlocker8.budgetmaster.database.model.BackupInfo;
import de.deadlocker8.budgetmaster.database.model.Upgradeable;
import de.deadlocker8.budgetmaster.database.model.v4.BackupTag_v4;
import de.deadlocker8.budgetmaster.database.model.v7.BackupIcon_v7;
import de.deadlocker8.budgetmaster.database.model.v7.BackupTemplate_v7;

import java.util.List;
import java.util.Objects;

public class BackupTemplate_v6 implements Upgradeable<BackupTemplate_v7>
{
	private String templateName;
	private Integer amount;
	private Boolean isExpenditure;
	private Integer accountID;
	private Integer categoryID;
	private String name;
	private String description;
	private Integer iconID;
	private List<BackupTag_v4> tags;
	private Integer transferAccountID;

	public BackupTemplate_v6()
	{
		// for GSON
	}

	public BackupTemplate_v6(String templateName, Integer amount, Boolean isExpenditure, Integer accountID, Integer categoryID, String name, String description, Integer iconID, List<BackupTag_v4> tags, Integer transferAccountID)
	{
		this.templateName = templateName;
		this.amount = amount;
		this.isExpenditure = isExpenditure;
		this.accountID = accountID;
		this.categoryID = categoryID;
		this.name = name;
		this.description = description;
		this.iconID = iconID;
		this.tags = tags;
		this.transferAccountID = transferAccountID;
	}

	public String getTemplateName()
	{
		return templateName;
	}

	public void setTemplateName(String templateName)
	{
		this.templateName = templateName;
	}

	public Integer getAmount()
	{
		return amount;
	}

	public void setAmount(Integer amount)
	{
		this.amount = amount;
	}

	public Boolean getExpenditure()
	{
		return isExpenditure;
	}

	public void setExpenditure(Boolean expenditure)
	{
		isExpenditure = expenditure;
	}

	public Integer getAccountID()
	{
		return accountID;
	}

	public void setAccountID(Integer accountID)
	{
		this.accountID = accountID;
	}

	public Integer getCategoryID()
	{
		return categoryID;
	}

	public void setCategoryID(Integer categoryID)
	{
		this.categoryID = categoryID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Integer getIconID()
	{
		return iconID;
	}

	public void setIconID(Integer iconID)
	{
		this.iconID = iconID;
	}

	public List<BackupTag_v4> getTags()
	{
		return tags;
	}

	public void setTags(List<BackupTag_v4> tags)
	{
		this.tags = tags;
	}

	public Integer getTransferAccountID()
	{
		return transferAccountID;
	}

	public void setTransferAccountID(Integer transferAccountID)
	{
		this.transferAccountID = transferAccountID;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupTemplate_v6 that = (BackupTemplate_v6) o;
		return Objects.equals(templateName, that.templateName) && Objects.equals(amount, that.amount) && Objects.equals(isExpenditure, that.isExpenditure) && Objects.equals(accountID, that.accountID) && Objects.equals(categoryID, that.categoryID) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(iconID, that.iconID) && Objects.equals(tags, that.tags) && Objects.equals(transferAccountID, that.transferAccountID);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(templateName, amount, isExpenditure, accountID, categoryID, name, description, iconID, tags, transferAccountID);
	}

	@Override
	public String toString()
	{
		return "BackupTemplate_v6{templateName='" + templateName + '\'' +
				", amount=" + amount +
				", isExpenditure=" + isExpenditure +
				", accountID=" + accountID +
				", categoryID=" + categoryID +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", iconID=" + iconID +
				", tags=" + tags +
				", transferAccountID=" + transferAccountID +
				'}';
	}

	@Override
	public BackupTemplate_v7 upgrade(List<BackupInfo> backupInfoItems)
	{
		Integer iconReferenceID = null;
		if(iconID != null)
		{
			BackupIcon_v7 iconReference = new BackupIcon_v7(backupInfoItems.size(), iconID, null);
			backupInfoItems.add(iconReference);

			iconReferenceID = iconReference.getID();
		}

		return new BackupTemplate_v7(templateName, amount, isExpenditure, accountID, categoryID, name, description, iconReferenceID, tags, transferAccountID);
	}
}
