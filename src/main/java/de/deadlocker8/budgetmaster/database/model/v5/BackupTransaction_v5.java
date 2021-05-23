package de.deadlocker8.budgetmaster.database.model.v5;

import de.deadlocker8.budgetmaster.database.model.Upgradeable;
import de.deadlocker8.budgetmaster.database.model.v4.BackupRepeatingOption_v4;
import de.deadlocker8.budgetmaster.database.model.v4.BackupTag_v4;
import de.deadlocker8.budgetmaster.database.model.v6.BackupTransaction_v6;

import java.util.List;
import java.util.Objects;

public class BackupTransaction_v5 implements Upgradeable<BackupTransaction_v6>
{
	private Integer amount;
	private Boolean isExpenditure;
	private String date;
	private BackupAccount_v5 account;
	private BackupCategory_v5 category;
	private String name;
	private String description;
	private List<BackupTag_v4> tags;
	private BackupRepeatingOption_v4 repeatingOption;
	private BackupAccount_v5 transferAccount;

	public BackupTransaction_v5()
	{
		// for GSON
	}

	public BackupTransaction_v5(Integer amount, Boolean isExpenditure, String date, BackupAccount_v5 account, BackupCategory_v5 category, String name, String description, List<BackupTag_v4> tags, BackupRepeatingOption_v4 repeatingOption, BackupAccount_v5 transferAccount)
	{
		this.amount = amount;
		this.isExpenditure = isExpenditure;
		this.date = date;
		this.account = account;
		this.category = category;
		this.name = name;
		this.description = description;
		this.tags = tags;
		this.repeatingOption = repeatingOption;
		this.transferAccount = transferAccount;
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

	public BackupAccount_v5 getAccount()
	{
		return account;
	}

	public void setAccount(BackupAccount_v5 account)
	{
		this.account = account;
	}

	public BackupCategory_v5 getCategory()
	{
		return category;
	}

	public void setCategory(BackupCategory_v5 category)
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

	public BackupRepeatingOption_v4 getRepeatingOption()
	{
		return repeatingOption;
	}

	public void setRepeatingOption(BackupRepeatingOption_v4 repeatingOption)
	{
		this.repeatingOption = repeatingOption;
	}

	public BackupAccount_v5 getTransferAccount()
	{
		return transferAccount;
	}

	public void setTransferAccount(BackupAccount_v5 transferAccount)
	{
		this.transferAccount = transferAccount;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupTransaction_v5 that = (BackupTransaction_v5) o;
		return Objects.equals(amount, that.amount) && Objects.equals(isExpenditure, that.isExpenditure) && Objects.equals(date, that.date) && Objects.equals(account, that.account) && Objects.equals(category, that.category) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(tags, that.tags) && Objects.equals(repeatingOption, that.repeatingOption) && Objects.equals(transferAccount, that.transferAccount);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(amount, isExpenditure, date, account, category, name, description, tags, repeatingOption, transferAccount);
	}

	@Override
	public String toString()
	{
		return "BackupTransaction_v5{amount=" + amount +
				", isExpenditure=" + isExpenditure +
				", date=" + date +
				", account=" + account +
				", category=" + category +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", tags=" + tags +
				", repeatingOption=" + repeatingOption +
				", transferAccount=" + transferAccount +
				'}';
	}

	@Override
	public BackupTransaction_v6 upgrade()
	{
		Integer transferAccountID = null;
		if(transferAccount != null)
		{
			transferAccountID = transferAccount.getID();
		}

		return new BackupTransaction_v6(amount, isExpenditure, date, account.getID(), category.getID(), name, description, tags, repeatingOption, transferAccountID);
	}
}
