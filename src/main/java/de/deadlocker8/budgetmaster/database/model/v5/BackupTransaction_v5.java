package de.deadlocker8.budgetmaster.database.model.v5;

import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.tags.Tag;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Objects;

public class BackupTransaction_v5
{
	private Integer ID;
	private Integer amount;
	private Boolean isExpenditure;
	private DateTime date;
	private BackupAccount_v5 account;
	private BackupCategory_v5 category;
	private String name;
	private String description;
	private List<Tag> tags;
	private RepeatingOption repeatingOption;
	private BackupAccount_v5 transferAccount;

	public BackupTransaction_v5()
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

	public DateTime getDate()
	{
		return date;
	}

	public void setDate(DateTime date)
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

	public List<Tag> getTags()
	{
		return tags;
	}

	public void setTags(List<Tag> tags)
	{
		this.tags = tags;
	}

	public RepeatingOption getRepeatingOption()
	{
		return repeatingOption;
	}

	public void setRepeatingOption(RepeatingOption repeatingOption)
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
		return Objects.equals(ID, that.ID) && Objects.equals(amount, that.amount) && Objects.equals(isExpenditure, that.isExpenditure) && Objects.equals(date, that.date) && Objects.equals(account, that.account) && Objects.equals(category, that.category) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(tags, that.tags) && Objects.equals(repeatingOption, that.repeatingOption) && Objects.equals(transferAccount, that.transferAccount);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, amount, isExpenditure, date, account, category, name, description, tags, repeatingOption, transferAccount);
	}

	@Override
	public String toString()
	{
		return "BackupTransaction_v5{" +
				"ID=" + ID +
				", amount=" + amount +
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
}
