package de.deadlocker8.budgetmaster.database.model.v7;

import de.deadlocker8.budgetmaster.database.model.v4.BackupTag_v4;

import java.util.List;
import java.util.Objects;

public class BackupTemplate_v7
{
	private String templateName;
	private Integer amount;
	private Boolean isExpenditure;
	private Integer accountID;
	private Integer categoryID;
	private String name;
	private String description;
	private Integer iconReferenceID;
	private List<BackupTag_v4> tags;
	private Integer transferAccountID;

	public BackupTemplate_v7()
	{
		// for GSON
	}

	public BackupTemplate_v7(String templateName, Integer amount, Boolean isExpenditure, Integer accountID, Integer categoryID, String name, String description, Integer iconReferenceID, List<BackupTag_v4> tags, Integer transferAccountID)
	{
		this.templateName = templateName;
		this.amount = amount;
		this.isExpenditure = isExpenditure;
		this.accountID = accountID;
		this.categoryID = categoryID;
		this.name = name;
		this.description = description;
		this.iconReferenceID = iconReferenceID;
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

	public Integer getIconReferenceID()
	{
		return iconReferenceID;
	}

	public void setIconReferenceID(Integer iconReferenceID)
	{
		this.iconReferenceID = iconReferenceID;
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
		BackupTemplate_v7 that = (BackupTemplate_v7) o;
		return Objects.equals(templateName, that.templateName) && Objects.equals(amount, that.amount) && Objects.equals(isExpenditure, that.isExpenditure) && Objects.equals(accountID, that.accountID) && Objects.equals(categoryID, that.categoryID) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(iconReferenceID, that.iconReferenceID) && Objects.equals(tags, that.tags) && Objects.equals(transferAccountID, that.transferAccountID);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(templateName, amount, isExpenditure, accountID, categoryID, name, description, iconReferenceID, tags, transferAccountID);
	}

	@Override
	public String toString()
	{
		return "BackupTemplate_v7{templateName='" + templateName + '\'' +
				", amount=" + amount +
				", isExpenditure=" + isExpenditure +
				", accountID=" + accountID +
				", categoryID=" + categoryID +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", iconReferenceID=" + iconReferenceID +
				", tags=" + tags +
				", transferAccountID=" + transferAccountID +
				'}';
	}
}
