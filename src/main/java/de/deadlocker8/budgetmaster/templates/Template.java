package de.deadlocker8.budgetmaster.templates;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.Iconizable;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionBase;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Template implements TransactionBase, Iconizable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer ID;

	@Expose
	private String templateName;

	@Expose
	private Integer amount;

	@Expose
	private Boolean isExpenditure;

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

	@OneToOne(cascade = CascadeType.REMOVE)
	@Expose
	private Icon iconReference;

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

	@ManyToOne
	@Expose
	private TemplateGroup templateGroup;


	public Template()
	{
	}

	public Template(Template template)
	{
		this.ID = template.getID();
		this.templateName = template.getTemplateName();
		this.amount = template.getAmount();
		this.isExpenditure = template.isExpenditure();
		this.account = template.getAccount();
		this.category = template.getCategory();
		this.name = template.getName();
		this.description = template.getDescription();
		this.iconReference = template.getIconReference();
		this.tags = new ArrayList<>(template.getTags());
		this.transferAccount = template.getTransferAccount();
		this.templateGroup = template.getTemplateGroup();
	}

	public Template(String templateName, Transaction transaction)
	{
		this.templateName = templateName;
		this.amount = transaction.getAmount();
		this.isExpenditure = transaction.isExpenditure();
		if(this.isExpenditure == null)
		{
			this.isExpenditure = true;
		}

		this.account = transaction.getAccount();
		this.category = transaction.getCategory();
		this.name = transaction.getName();
		this.description = transaction.getDescription();
		this.iconReference = null;
		if(transaction.getTags() == null)
		{
			this.tags = new ArrayList<>();
		}
		else
		{
			this.tags = new ArrayList<>(transaction.getTags());
		}
		this.transferAccount = transaction.getTransferAccount();
		this.templateGroup = null;
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

	public Boolean isExpenditure()
	{
		return isExpenditure;
	}

	public Boolean getExpenditure()
	{
		return isExpenditure;
	}

	public void setIsExpenditure(Boolean expenditure)
	{
		isExpenditure = expenditure;
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

	@Override
	public Icon getIconReference()
	{
		return iconReference;
	}

	@Override
	public void setIconReference(Icon iconReference)
	{
		this.iconReference = iconReference;
	}

	@Override
	public String getFontColor()
	{
		final Icon icon = getIconReference();
		if(icon == null)
		{
			return "#FFFFFF";
		}

		final String fontColor = icon.getFontColor();
		if(fontColor == null)
		{
			return "#FFFFFF";
		}

		return fontColor;
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

	public TemplateGroup getTemplateGroup()
	{
		return templateGroup;
	}

	public void setTemplateGroup(TemplateGroup templateGroup)
	{
		this.templateGroup = templateGroup;
	}

	@Override
	public String toString()
	{
		String value = "Template{" +
				"ID=" + ID +
				", templateName='" + templateName + '\'' +
				", amount=" + amount +
				", isExpenditure=" + isExpenditure +
				", category=" + category +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", iconReference='" + iconReference + '\'' +
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

		value += ", transferAccount=" + templateGroup;

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
				Objects.equals(isExpenditure, template.isExpenditure) &&
				Objects.equals(account, template.account) &&
				Objects.equals(category, template.category) &&
				Objects.equals(name, template.name) &&
				Objects.equals(description, template.description) &&
				Objects.equals(iconReference, template.iconReference) &&
				Objects.equals(tags, template.tags) &&
				Objects.equals(transferAccount, template.transferAccount) &&
				Objects.equals(templateGroup, template.templateGroup);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, templateName, amount, isExpenditure, account, category, name, description, iconReference, tags, transferAccount, templateGroup);
	}
}
