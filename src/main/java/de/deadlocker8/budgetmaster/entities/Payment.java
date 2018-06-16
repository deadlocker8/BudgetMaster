package de.deadlocker8.budgetmaster.entities;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Payment
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
	private Integer ID;
	@Expose
	private Integer amount;

	@DateTimeFormat(pattern = "dd.MM.yyyy")
	@Expose
	private DateTime date;

	@ManyToOne
	@Expose
	private Account account;

	@ManyToOne
	@Expose
	private Category category;

	@Expose
	private String name;
	@Expose
	private String description;

	@ManyToMany(cascade = CascadeType.ALL)
	@Expose
	private List<Tag> tags;

	@ManyToOne(optional = true, cascade = CascadeType.ALL)
	@Expose
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

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Payment payment = (Payment) o;
		return Objects.equals(ID, payment.ID) &&
				Objects.equals(amount, payment.amount) &&
				Objects.equals(date, payment.date) &&
				Objects.equals(account, payment.account) &&
				Objects.equals(category, payment.category) &&
				Objects.equals(name, payment.name) &&
				Objects.equals(description, payment.description) &&
				Objects.equals(tags, payment.tags) &&
				Objects.equals(repeatingOption, payment.repeatingOption);
	}

	@Override
	public int hashCode()
	{

		return Objects.hash(ID, amount, date, account, category, name, description, tags, repeatingOption);
	}
}