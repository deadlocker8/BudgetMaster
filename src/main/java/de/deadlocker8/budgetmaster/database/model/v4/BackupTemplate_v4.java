package de.deadlocker8.budgetmaster.database.model.v4;

import de.deadlocker8.budgetmaster.database.model.Upgradeable;
import de.deadlocker8.budgetmaster.database.model.v5.BackupAccount_v5;
import de.deadlocker8.budgetmaster.database.model.v5.BackupCategory_v5;
import de.deadlocker8.budgetmaster.database.model.v5.BackupTemplate_v5;
import de.deadlocker8.budgetmaster.tags.Tag;

import java.util.List;
import java.util.Objects;

public class BackupTemplate_v4 implements BackupTransactionBase_v4, Upgradeable<BackupTemplate_v5>
{
	private String templateName;
	private Integer amount;
	private Boolean isExpenditure;
	private BackupAccount_v4 account;
	private BackupCategory_v4 category;
	private String name;
	private String description;
	private List<BackupTag_v4> tags;
	private BackupAccount_v4 transferAccount;

	public BackupTemplate_v4()
	{
		// for GSON
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

	public BackupAccount_v4 getAccount()
	{
		return account;
	}

	public void setAccount(BackupAccount_v4 account)
	{
		this.account = account;
	}

	public BackupCategory_v4 getCategory()
	{
		return category;
	}

	public void setCategory(BackupCategory_v4 category)
	{
		this.category = category;
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

	public List<BackupTag_v4> getTags()
	{
		return tags;
	}

	public void setTags(List<BackupTag_v4> tags)
	{
		this.tags = tags;
	}

	public BackupAccount_v4 getTransferAccount()
	{
		return transferAccount;
	}

	public void setTransferAccount(BackupAccount_v4 transferAccount)
	{
		this.transferAccount = transferAccount;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupTemplate_v4 that = (BackupTemplate_v4) o;
		return Objects.equals(templateName, that.templateName) && Objects.equals(amount, that.amount) && Objects.equals(isExpenditure, that.isExpenditure) && Objects.equals(account, that.account) && Objects.equals(category, that.category) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(tags, that.tags) && Objects.equals(transferAccount, that.transferAccount);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(templateName, amount, isExpenditure, account, category, name, description, tags, transferAccount);
	}

	@Override
	public String toString()
	{
		return "BackupTemplate_v4{templateName='" + templateName + '\'' +
				", amount=" + amount +
				", isExpenditure=" + isExpenditure +
				", account=" + account +
				", category=" + category +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", tags=" + tags +
				", transferAccount=" + transferAccount +
				'}';
	}

	public BackupTemplate_v5 upgrade()
	{
		BackupAccount_v5 upgradedAccount = null;
		if(account != null)
		{
			upgradedAccount = account.upgrade();
		}

		BackupCategory_v5 upgradedCategory = null;
		if(category != null)
		{
			upgradedCategory = category.upgrade();
		}

		BackupAccount_v5 upgradedTransferAccount = null;
		if(transferAccount != null)
		{
			upgradedTransferAccount = transferAccount.upgrade();
		}

		return new BackupTemplate_v5(templateName, amount, isExpenditure, upgradedAccount, upgradedCategory, name, description, null, tags, upgradedTransferAccount);
	}
}
