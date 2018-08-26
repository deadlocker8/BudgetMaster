package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.entities.CategoryType;
import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.repositories.CategoryRepository;
import de.deadlocker8.budgetmaster.repositories.TransactionRepository;
import de.deadlocker8.budgetmaster.repositories.RepeatingOptionRepository;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.Localization;

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

	public List<Transaction> getTransactionsForMonthAndYear(Account account, int month, int year, boolean isRestActivated)
	{
		List<Transaction> transactions;
		if(isRestActivated)
		{
			transactions = getTransactionsForMonthAndYearWithRest(account, month, year);
		}
		else
		{
			transactions = getTransactionsForMonthAndYearWithoutRest(account, month, year);
		}

		return transactions;
	}

	private List<Transaction> getTransactionsForMonthAndYearWithRest(Account account, int month, int year)
	{
		DateTime startDate = DateTime.now().withYear(year).withMonthOfYear(month).minusMonths(1).dayOfMonth().withMaximumValue();
		DateTime endDate = DateTime.now().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue();
		List<Transaction> transactions = transactionRepository.findAllByAccountAndDateBetweenOrderByDateDesc(account, startDate, endDate);

		Transaction transactionRest = new Transaction();
		transactionRest.setCategory(categoryRepository.findByType(CategoryType.REST));
		transactionRest.setName(Localization.getString(Strings.CATEGORY_REST));
		transactionRest.setDate(DateTime.now().withYear(year).withMonthOfYear(month).withDayOfMonth(1));
		transactionRest.setAmount(getRest(account, startDate));
		transactions.add(transactionRest);

		return transactions;
	}

	private List<Transaction> getTransactionsForMonthAndYearWithoutRest(Account account, int month, int year)
	{
		DateTime startDate = DateTime.now().withYear(year).withMonthOfYear(month).minusMonths(1).dayOfMonth().withMaximumValue();
		DateTime endDate = DateTime.now().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue();
		return transactionRepository.findAllByAccountAndDateBetweenOrderByDateDesc(account, startDate, endDate);
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
			Transaction transactionToDelete = transactionRepository.findOne(ID);
			// handle repeating transactions
			if(transactionToDelete.getRepeatingOption() != null)
			{
				repeatingOptionRepository.delete(transactionToDelete.getRepeatingOption().getID());
			}
			else
			{
				transactionRepository.delete(ID);
			}
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
		transactionRepository.deleteAll();
	}

	@Override
	public void createDefaults()
	{
	}
}
