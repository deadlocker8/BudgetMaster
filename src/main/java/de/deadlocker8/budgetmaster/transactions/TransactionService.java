package de.deadlocker8.budgetmaster.transactions;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.services.Resetable;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.repeating.RepeatingOptionRepository;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService implements Resetable
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private TransactionRepository transactionRepository;
	private RepeatingOptionRepository repeatingOptionRepository;
	private CategoryRepository categoryRepository;


	@Autowired
	public TransactionService(TransactionRepository transactionRepository, RepeatingOptionRepository repeatingOptionRepository, CategoryRepository categoryRepository)
	{
		this.transactionRepository = transactionRepository;
		this.repeatingOptionRepository = repeatingOptionRepository;
		this.categoryRepository = categoryRepository;
	}

	public TransactionRepository getRepository()
	{
		return transactionRepository;
	}

	public List<Transaction> getTransactionsForMonthAndYear(Account account, int month, int year, boolean isRestActivated, FilterConfiguration filterConfiguration)
	{
		List<Transaction> transactions;
		if(isRestActivated)
		{
			transactions = getTransactionsForMonthAndYearWithRest(account, month, year, filterConfiguration);
		}
		else
		{
			transactions = getTransactionsForMonthAndYearWithoutRest(account, month, year, filterConfiguration);
		}

		return transactions;
	}

	private List<Transaction> getTransactionsForMonthAndYearWithRest(Account account, int month, int year, FilterConfiguration filterConfiguration)
	{
		DateTime startDate = DateTime.now().withYear(year).withMonthOfYear(month).minusMonths(1).dayOfMonth().withMaximumValue();
		List<Transaction> transactions = getTransactionsForMonthAndYearWithoutRest(account, month, year, filterConfiguration);

		Transaction transactionRest = new Transaction();
		transactionRest.setCategory(categoryRepository.findByType(CategoryType.REST));
		transactionRest.setName(Localization.getString(Strings.CATEGORY_REST));
		transactionRest.setDate(DateTime.now().withYear(year).withMonthOfYear(month).withDayOfMonth(1));
		transactionRest.setAmount(getRest(account, startDate));
		transactionRest.setTags(new ArrayList<>());
		transactions.add(transactionRest);

		return transactions;
	}

	private List<Transaction> getTransactionsForMonthAndYearWithoutRest(Account account, int month, int year, FilterConfiguration filterConfiguration)
	{
		DateTime startDate = DateTime.now().withYear(year).withMonthOfYear(month).minusMonths(1).dayOfMonth().withMaximumValue();
		DateTime endDate = DateTime.now().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue();
		return getTransactionsForAccount(account, startDate, endDate, filterConfiguration);
	}

	public List<Transaction> getTransactionsForAccountUntilDate(Account account, DateTime date, FilterConfiguration filterConfiguration)
	{
		DateTime startDate = DateTime.now().withYear(1900).withMonthOfYear(1).withDayOfMonth(1);
		return getTransactionsForAccount(account, startDate, date, filterConfiguration);
	}

	private List<Transaction> getTransactionsForAccount(Account account, DateTime startDate, DateTime endDate, FilterConfiguration filterConfiguration)
	{
		if(filterConfiguration == null)
		{
			filterConfiguration = FilterConfiguration.DEFAULT;
		}

		if(account.getType().equals(AccountType.ALL))
		{
			Specification spec = TransactionSpecifications.withDynamicQuery(startDate, endDate, null, filterConfiguration.isIncludeIncome(), filterConfiguration.isIncludeExpenditure(), filterConfiguration.isIncludeRepeatingAndNotRepeating(), filterConfiguration.getIncludedCategoryIDs(), filterConfiguration.getIncludedTagIDs(), filterConfiguration.getName());
			return transactionRepository.findAll(spec);
		}

		Specification spec = TransactionSpecifications.withDynamicQuery(startDate, endDate, account, filterConfiguration.isIncludeIncome(), filterConfiguration.isIncludeExpenditure(), filterConfiguration.isIncludeRepeatingAndNotRepeating(), filterConfiguration.getIncludedCategoryIDs(), filterConfiguration.getIncludedTagIDs(), filterConfiguration.getName());
		return transactionRepository.findAll(spec);
	}

	private int getRest(Account account, DateTime endDate)
	{
		DateTime startDate = DateTime.now().withYear(2000).withMonthOfYear(1).withDayOfMonth(1);
		Integer rest = transactionRepository.getRest(account.getID(), ISODateTimeFormat.date().print(startDate), ISODateTimeFormat.date().print(endDate));
		if(rest == null)
		{
			return 0;
		}
		return rest;
	}

	public void deleteTransaction(Integer ID)
	{
		if(isDeletable(ID))
		{
			deleteTransactionInRepo(ID);
		}
	}

	private void deleteTransactionInRepo(Integer ID)
	{
		Transaction transactionToDelete = transactionRepository.findOne(ID);
		// handle repeating transactions
		if(transactionToDelete.getRepeatingOption() == null)
		{
			transactionRepository.delete(ID);
		}
		else
		{
			repeatingOptionRepository.delete(transactionToDelete.getRepeatingOption().getID());
		}
	}

	public boolean isDeletable(Integer ID)
	{
		Transaction transactionToDelete = transactionRepository.getOne(ID);
		return transactionToDelete != null && transactionToDelete.getCategory().getType() != CategoryType.REST;
	}

	@Override
	public void deleteAll()
	{
		for(Transaction transaction : transactionRepository.findAll())
		{
			deleteTransactionInRepo(transaction.getID());
		}
	}

	@Override
	public void createDefaults()
	{
	}
}
