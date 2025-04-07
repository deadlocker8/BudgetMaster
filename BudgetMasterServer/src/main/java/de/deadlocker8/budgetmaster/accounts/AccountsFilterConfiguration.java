package de.deadlocker8.budgetmaster.accounts;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AccountsFilterConfiguration
{
	private boolean includeFullAccess;
	private boolean includeReadOnly;
	private boolean includeHidden;
	private boolean includeWithEndDate;
	private boolean includeWithoutEndDate;
	private String name;
	private String description;

	public static final AccountsFilterConfiguration DEFAULT = new AccountsFilterConfiguration(true, true, false, true, true, "", "");

	public List<AccountState> getIncludedStates()
	{
		final List<AccountState> includedStates = new ArrayList<>();
		if(includeFullAccess)
		{
			includedStates.add(AccountState.FULL_ACCESS);
		}

		if(includeReadOnly)
		{
			includedStates.add(AccountState.READ_ONLY);
		}

		if(includeHidden)
		{
			includedStates.add(AccountState.HIDDEN);
		}

		return includedStates;
	}

	public boolean hasName()
	{
		return name != null && !name.isEmpty();
	}

	public boolean hasDescription()
	{
		return description != null && !description.isEmpty();
	}
}