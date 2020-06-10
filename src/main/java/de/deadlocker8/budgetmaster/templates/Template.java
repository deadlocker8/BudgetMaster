package de.deadlocker8.budgetmaster.templates;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionBase;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Template implements TransactionBase
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer ID;

	@Expose
	private String templateName;

	@Expose
	private Integer amount;

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

	@ManyToMany
	@Expose
	@JoinTable(
			name = "template_tags",
			joinColumns = @JoinColumn(
					name = "template_id", referencedColumnName = "ID"),
			inverseJoinColumns = @JoinColumn(
					name = "tags_id", referencedColumnName = "ID"))
	private List<Tag> tags;

	@OneToOne(optional = true)
	@Expose
	private Account transferAccount;

	public Template()
	{
	}

	public Template(Template template)
	{
		this.ID = template.getID();
		this.templateName = template.getTemplateName();
		this.amount = template.getAmount();
		this.account = template.getAccount();
		this.category = template.getCategory();
		this.name = template.getName();
		this.description = template.getDescription();
		this.tags = new ArrayList<>(template.getTags());
		this.transferAccount = template.getTransferAccount();
	}

	public Template(String templateName, Transaction transaction)
	{
		this.templateName = templateName;
		this.amount = transaction.getAmount();
		this.account = transaction.getAccount();
		this.category = transaction.getCategory();
		this.name = transaction.getName();
		this.description = transaction.getDescription();
		if(transaction.getTags() == null)
		{
			this.tags = new ArrayList<>();
		}
		else
		{
			this.tags = new ArrayList<>(transaction.getTags());
		}
		this.transferAccount = transaction.getTransferAccount();
	}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
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

	public Account getTransferAccount()
	{
		return transferAccount;
	}

	public void setTransferAccount(Account transferAccount)
	{
		this.transferAccount = transferAccount;
	}

	public boolean isTransfer()
	{
		return transferAccount != null;
	}

	public boolean isPayment()
	{
		if(this.amount == null)
		{
			return true;
		}

		return this.amount <= 0;
	}

	@Override
	public String toString()
	{
		String value = "Template{" +
				"ID=" + ID +
				", templateName='" + templateName + '\'' +
				", amount=" + amount +
				", category=" + category +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", tags=" + tags;

		if(account == null)
		{
			value += ", account=null";
		}
		else
		{
			value += ", account=Account[ID=" + account.getID() + ", name=" + account.getName() + "]";
		}

		if(transferAccount == null)
		{
			value += ", transferAccount=null";
		}
		else
		{
			value += ", transferAccount=Account[ID=" + transferAccount.getID() + ", name=" + transferAccount.getName() + "]";
		}

		value += '}';
		return value;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Template template = (Template) o;
		return Objects.equals(ID, template.ID) &&
				Objects.equals(templateName, template.templateName) &&
				Objects.equals(amount, template.amount) &&
				Objects.equals(account, template.account) &&
				Objects.equals(category, template.category) &&
				Objects.equals(name, template.name) &&
				Objects.equals(description, template.description) &&
				Objects.equals(tags, template.tags) &&
				Objects.equals(transferAccount, template.transferAccount);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, templateName, amount, account, category, name, description, tags, transferAccount);
	}
}
