package de.deadlocker8.budgetmaster.repeating;

import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.repositories.TransactionRepository;
import de.deadlocker8.budgetmaster.repositories.RepeatingOptionRepository;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.services.TransactionService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepeatingTransactionUpdater
{
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private RepeatingOptionRepository repeatingOptionRepository;

	@Autowired
	private HelpersService helpers;

	public void updateRepeatingTransactions(DateTime now)
	{
		List<RepeatingOption> repeatingOptions = repeatingOptionRepository.findAllByOrderByStartDateAsc();
		for(RepeatingOption option : repeatingOptions)
		{
			List<Transaction> transactions = option.getReferringTransactions();
			List<DateTime> correctDates = option.getRepeatingDates(now);
			for(DateTime currentDate : correctDates)
			{
				if(!containsDate(transactions, currentDate))
				{
					Transaction newTransaction = new Transaction(transactions.get(0));
					newTransaction.setID(null);
					newTransaction.setDate(currentDate);
					transactionRepository.save(newTransaction);
				}
			}
		}
	}

	private boolean containsDate(List<Transaction> transactions, DateTime date)
	{
		for(Transaction currentTransaction : transactions)
		{
			if(date.equals(currentTransaction.getDate()))
			{
				return true;
			}
		}

		return false;
	}
}