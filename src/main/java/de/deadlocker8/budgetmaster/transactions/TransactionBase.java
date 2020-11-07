package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.tags.Tag;

import java.util.List;

public interface TransactionBase
{
	Integer getAmount();

	void setAmount(Integer amount);

	Boolean isExpenditure();

	void setIsExpenditure(Boolean isExpenditure);

	Category getCategory();

	void setCategory(Category category);

	List<Tag> getTags();

	void setTags(List<Tag> tags);

	Account getAccount();

	void setAccount(Account account);

	Account getTransferAccount();

	void setTransferAccount(Account account);
}
