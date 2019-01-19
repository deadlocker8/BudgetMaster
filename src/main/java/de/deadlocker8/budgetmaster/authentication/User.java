package de.deadlocker8.budgetmaster.authentication;

import de.deadlocker8.budgetmaster.entities.account.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ID;

	@NotNull
	@Size(min = 1)
	@Column(unique = true)
	private String name;

	@NotNull
	@Size(min = 1)
	private String password;

	@OneToOne
	private Account selectedAccount;


	public User(String name, String password)
	{
		this.name = name;
		this.password = password;
	}

	public User()
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

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Account getSelectedAccount()
	{
		return selectedAccount;
	}

	public void setSelectedAccount(Account selectedAccount)
	{
		this.selectedAccount = selectedAccount;
	}

	@Override
	public String toString()
	{
		return "User{" +
				"ID=" + ID +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", selectedAccount=" + selectedAccount +
				'}';
	}
}