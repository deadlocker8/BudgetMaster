package de.deadlocker8.budgetmaster.database.model.v6;

import de.deadlocker8.budgetmaster.database.model.v4.BackupRepeatingOption_v4;
import de.deadlocker8.budgetmaster.database.model.v4.BackupTag_v4;

import java.util.List;
import java.util.Objects;

public class BackupTransaction_v6
{
	private Integer amount;
	private Boolean isExpenditure;
	private String date;
	private Integer accountID;
	private Integer categoryID;
	private String name;
	private String description;
	private List<BackupTag_v4> tags;
	private BackupRepeatingOption_v4 repeatingOption;
	private Integer transferAccountID;

	public BackupTransaction_v6()
	{
		// for GSON
	}

	public BackupTransaction_v6(Integer amount, Boolean isExpenditure, String date, Integer accountID, Integer categoryID, String name, String description, List<BackupTag_v4> tags, BackupRepeatingOption_v4 repeatingOption, Integer transferAccountID)
	{
		this.amount = amount;
		this.isExpenditure = isExpenditure;
		this.date = date;
		this.accountID = accountID;
		this.categoryID = categoryID;
		this.name = name;
		this.description = description;
		this.tags = tags;
		this.repeatingOption = repeatingOption;
		this.transferAccountID = transferAccountID;
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

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
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

	public List<BackupTag_v4> getTags()
	{
		return tags;
	}

	public void setTags(List<BackupTag_v4> tags)
	{
		this.tags = tags;
	}

	public BackupRepeatingOption_v4 getRepeatingOption()
	{
		return repeatingOption;
	}

	public void setRepeatingOption(BackupRepeatingOption_v4 repeatingOption)
	{
		this.repeatingOption = repeatingOption;
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
		BackupTransaction_v6 that = (BackupTransaction_v6) o;
		return Objects.equals(amount, that.amount) && Objects.equals(isExpenditure, that.isExpenditure) && Objects.equals(date, that.date) && Objects.equals(accountID, that.accountID) && Objects.equals(categoryID, that.categoryID) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(tags, that.tags) && Objects.equals(repeatingOption, that.repeatingOption) && Objects.equals(transferAccountID, that.transferAccountID);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(amount, isExpenditure, date, accountID, categoryID, name, description, tags, repeatingOption, transferAccountID);
	}

	@Override
	public String toString()
	{
		return "BackupTransaction_v6{amount=" + amount +
				", isExpenditure=" + isExpenditure +
				", date=" + date +
				", accountID=" + accountID +
				", categoryID=" + categoryID +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", tags=" + tags +
				", repeatingOption=" + repeatingOption +
				", transferAccountID=" + transferAccountID +
				'}';
	}
}
