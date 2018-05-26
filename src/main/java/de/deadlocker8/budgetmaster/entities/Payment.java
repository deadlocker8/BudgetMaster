package de.deadlocker8.budgetmaster.entities;

import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Payment
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ID;
	private Integer amount;

	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private DateTime date;

	@ManyToOne
	private Account account;

	@ManyToOne
	private Category category;

	private String name;
	private String description;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<Tag> tags;

	@ManyToOne(optional = true, cascade = CascadeType.ALL)
	private RepeatingOption repeatingOption;

	public Payment()
	{
	}

	public Payment(Payment payment)
	{
		this.ID = payment.getID();
		this.amount = payment.getAmount();
		this.date = payment.getDate();
		this.account = payment.getAccount();
		this.category = payment.getCategory();
		this.name = payment.getName();
		this.description = payment.getDescription();
		this.tags = new ArrayList<>(payment.getTags());
		this.repeatingOption = payment.getRepeatingOption();
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

	public DateTime getDate()
	{
		return date;
	}

	public void setDate(DateTime date)
	{
		this.date = date;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
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

	public boolean isRepeating()
	{
		return repeatingOption != null;
	}

	@Override
	public String toString()
	{
		return "Payment{" +
				"ID=" + ID +
				", amount=" + amount +
				", date=" + date +
				", account=Account[ID=" + account.getID() + ", name=" + account.getName() + "]" +
				", category=" + category +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", tags=" + tags +
				", repeatingOption=" + repeatingOption +
				'}';
	}
}