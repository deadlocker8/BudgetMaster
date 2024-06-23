package de.deadlocker8.budgetmaster.accounts;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.Iconizable;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.utils.ProvidesID;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Entity
public class Account implements ProvidesID, Iconizable
{
	private static final String FONT_COLOR_LIGHT_THEME = "#212121";
	private static final String FONT_COLOR_DARK_THEME = "#FFFFFF";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer ID;

	@NotNull
	@Size(min = 1)
	@Column(unique = true)
	@Expose
	private String name;

	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
	private List<Transaction> referringTransactions;

	private Boolean isSelected = false;
	private Boolean isDefault = false;

	@Expose
	private AccountState accountState;

	@OneToOne(cascade = CascadeType.REMOVE)
	@Expose
	private Icon iconReference;

	@Expose
	private AccountType type;

	@Expose
	private String description;

	@DateTimeFormat(pattern = "dd.MM.yyyy")
	@Expose
	private LocalDate endDate;

	public Account(String name, String description, AccountType type, Icon iconReference, LocalDate endDate)
	{
		this.name = name;
		this.description = description;
		this.type = type;
		this.isSelected = false;
		this.isDefault = false;
		this.accountState = AccountState.FULL_ACCESS;
		this.iconReference = iconReference;
		this.endDate = endDate;
	}

	public Account(String name, String description, AccountType type, LocalDate endDate)
	{
		this(name, description, type, null, endDate);
	}

	public Account()
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<Transaction> getReferringTransactions()
	{
		return referringTransactions;
	}

	public void setReferringTransactions(List<Transaction> referringTransactions)
	{
		this.referringTransactions = referringTransactions;
	}

	public Boolean isSelected()
	{
		return isSelected;
	}

	public void setSelected(Boolean selected)
	{
		isSelected = selected;
	}

	public Boolean isDefault()
	{
		return isDefault;
	}

	public void setDefault(Boolean aDefault)
	{
		isDefault = aDefault;
	}

	public AccountState getAccountState()
	{
		return accountState;
	}

	public void setAccountState(AccountState accountState)
	{
		this.accountState = accountState;
	}

	public AccountType getType()
	{
		return type;
	}

	public void setType(AccountType type)
	{
		this.type = type;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public LocalDate getEndDate()
	{
		return endDate;
	}

	public void setEndDate(LocalDate endDate)
	{
		this.endDate = endDate;
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
	public String getFontColor(boolean isDarkTheme)
	{
		final Icon icon = getIconReference();
		if(icon == null)
		{
			return getDefaultFontColor(isDarkTheme);
		}

		final String fontColor = icon.getFontColor();
		if(fontColor == null)
		{
			return getDefaultFontColor(isDarkTheme);
		}

		return fontColor;
	}

	@Override
	public String getDefaultFontColor(boolean isDarkTheme)
	{
		if(isDarkTheme)
		{
			return FONT_COLOR_DARK_THEME;
		}

		return FONT_COLOR_LIGHT_THEME;
	}

	public Long getRemainingDays()
	{
		if(this.endDate == null)
		{
			return null;
		}

		return LocalDate.now().until(this.endDate, ChronoUnit.DAYS);
	}

	@Override
	public String toString()
	{
		return "Account{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", referringTransactions=" + referringTransactions +
				", isSelected=" + isSelected +
				", isDefault=" + isDefault +
				", accountState=" + accountState +
				", type=" + type +
				", iconReference=" + iconReference +
				", description='" + description + '\'' +
				", endDate=" + endDate +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Account account = (Account) o;
		return Objects.equals(ID, account.ID) &&
				Objects.equals(name, account.name) &&
				Objects.equals(isSelected, account.isSelected) &&
				Objects.equals(isDefault, account.isDefault) &&
				accountState == account.accountState &&
				Objects.equals(iconReference, account.iconReference) &&
				type == account.type &&
				Objects.equals(description, account.description) &&
				Objects.equals(endDate, account.endDate);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, name, description, isSelected, isDefault, accountState, iconReference, type, endDate);
	}
}