package de.deadlocker8.budgetmaster.repeating;

import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepeatingTransactionUpdater
{
	private final TransactionService transactionService;
	private final RepeatingOptionRepository repeatingOptionRepository;

	@Autowired
	public RepeatingTransactionUpdater(TransactionService transactionService, RepeatingOptionRepository repeatingOptionRepository)
	{
		this.transactionService = transactionService;
		this.repeatingOptionRepository = repeatingOptionRepository;
	}

	public void updateRepeatingTransactions(DateTime now)
	{
		List<RepeatingOption> repeatingOptions = repeatingOptionRepository.findAllByOrderByStartDateAsc();
		for(RepeatingOption option : repeatingOptions)
		{
			List<Transaction> transactions = transactionService.getRepository().findAllByRepeatingOption(option);
			List<DateTime> correctDates = option.getRepeatingDates(now);
			for(DateTime currentDate : correctDates)
			{
				if(!containsDate(transactions, currentDate))
				{
					Transaction newTransaction = new Transaction(transactions.get(0));
					newTransaction.setID(null);
					newTransaction.setDate(currentDate);
					transactionService.getRepository().save(newTransaction);
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