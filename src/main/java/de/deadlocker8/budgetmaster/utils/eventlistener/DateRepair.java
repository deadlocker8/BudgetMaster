package de.deadlocker8.budgetmaster.utils.eventlistener;

import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.RepeatingOptionRepository;
import de.deadlocker8.budgetmaster.repeating.RepeatingTransactionUpdater;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DateRepair
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DateRepair.class);

	private static final int DATE_REPAIR_VERSION_CODE = 23;

	private final TransactionRepository transactionRepository;
	private final RepeatingOptionRepository repeatingOptionRepository;
	private final RepeatingTransactionUpdater repeatingTransactionUpdater;
	private final SettingsService settingsService;
	private final EntityManager entityManager;

	@Autowired
	public DateRepair(TransactionRepository transactionRepository, RepeatingOptionRepository repeatingOptionRepository, RepeatingTransactionUpdater repeatingTransactionUpdater, SettingsService settingsService, EntityManager entityManager)
	{
		this.transactionRepository = transactionRepository;
		this.repeatingOptionRepository = repeatingOptionRepository;
		this.repeatingTransactionUpdater = repeatingTransactionUpdater;
		this.settingsService = settingsService;
		this.entityManager = entityManager;
	}

	@EventListener
	@Transactional
	@Order(1)
	public void onApplicationEvent(ApplicationStartedEvent event)
	{
		checkForPossibleDuplicates();
		if(settingsService.getSettings().getInstalledVersionCode() <= DATE_REPAIR_VERSION_CODE)
		{
			deleteOldRepeatingTransactions();

			repairTransactionDates();
			repairRepeatingOptionsDates();

			LOGGER.debug("Re-created repeating transactions");
			repeatingTransactionUpdater.updateRepeatingTransactions(DateTime.now());
		}
	}

	private void deleteOldRepeatingTransactions()
	{
		int numberOfDeletedItems = 0;

		for(RepeatingOption repeatingOption : repeatingOptionRepository.findAll())
		{
			final List<Transaction> referringTransactions = repeatingOption.getReferringTransactions();
			final List<Transaction> transactionsSorted = referringTransactions.stream()
					.sorted(Comparator.comparing(Transaction::getDate))
					.collect(Collectors.toList());

			final List<Transaction> transactions = transactionsSorted.subList(1, transactionsSorted.size());
			numberOfDeletedItems += transactions.size();

			for(Transaction transaction : transactions)
			{
				final Query nativeQuery = entityManager.createNativeQuery("DELETE FROM `transaction` WHERE id=:ID");
				nativeQuery.setParameter("ID", transaction.getID());
				nativeQuery.executeUpdate();
			}
		}

		LOGGER.debug(MessageFormat.format("Deleted {0} wrong repeating transactions", numberOfDeletedItems));
	}

	private void checkForPossibleDuplicates()
	{
		final List<Transaction> transactions = transactionRepository.findAll();
		final List<Transaction> alreadyScanned = new ArrayList<>();
		final List<List<Transaction>> duplicated = new ArrayList<>();

		for(Transaction transaction : transactions)
		{
			for(Transaction scannedTransaction : alreadyScanned)
			{
				if(isPossibleDuplicate(transaction, scannedTransaction))
				{
					final ArrayList<Transaction> entry = new ArrayList<>();
					entry.add(scannedTransaction);
					entry.add(transaction);
					duplicated.add(entry);
				}
			}

			alreadyScanned.add(transaction);
		}

		if(!duplicated.isEmpty())
		{
			final List<List<Integer>> duplicatedIDs = duplicated.stream()
					.map(entry -> entry.stream().map(Transaction::getID).collect(Collectors.toList()))
					.collect(Collectors.toList());
			LOGGER.warn(MessageFormat.format("Found {0} possible duplicated transactions: {1}", duplicated.size(), duplicatedIDs));
		}
	}

	private boolean isPossibleDuplicate(Transaction transaction1, Transaction transaction2)
	{
		if(transaction1.getRepeatingOption() == null || transaction2.getRepeatingOption() == null)
		{
			return false;
		}

		if(!transaction1.getRepeatingOption().equals(transaction2.getRepeatingOption()))
		{
			return false;
		}

		return areDatesSimilar(transaction1.getDate(), transaction2.getDate());
	}

	private boolean areDatesSimilar(DateTime date1, DateTime date2)
	{
		if(date1.getYear() != date2.getYear())
		{
			return false;
		}

		if(date1.getMonthOfYear() != date2.getMonthOfYear())
		{
			return false;
		}

		return Math.abs(date1.getDayOfMonth() - date2.getDayOfMonth()) <= 1;
	}

	private void repairTransactionDates()
	{
		int numberOfRepairedTransactions = 0;

		final List<Transaction> transactions = transactionRepository.findAll();
		for(Transaction transaction : transactions)
		{
			final DateTime date = transaction.getDate();
			if(dateNeedsToBeRepaired(date))
			{
				final DateTime fixedDate = getRepairedDate(date);
				transaction.setDate(fixedDate);
				numberOfRepairedTransactions++;
			}
		}

		LOGGER.debug(MessageFormat.format("Repaired {0}/{1} transaction dates", numberOfRepairedTransactions, transactions.size()));
	}

	private void repairRepeatingOptionsDates()
	{
		int numberOfRepairedItems = 0;

		final List<RepeatingOption> repeatingOptions = repeatingOptionRepository.findAll();
		for(RepeatingOption repeatingOption : repeatingOptions)
		{
			final DateTime date = repeatingOption.getStartDate();
			if(dateNeedsToBeRepaired(date))
			{
				final DateTime fixedDate = getRepairedDate(date);
				repeatingOption.setStartDate(fixedDate);
				numberOfRepairedItems++;
			}
		}

		LOGGER.debug(MessageFormat.format("Repaired {0}/{1} repeating option dates", numberOfRepairedItems, repeatingOptions.size()));
	}

	private DateTime getRepairedDate(DateTime date)
	{
		return date.plusHours(6)
				.withZone(DateTimeZone.UTC)
				.withHourOfDay(0)
				.withMinuteOfHour(0)
				.withSecondOfMinute(0)
				.withMillisOfSecond(0);
	}

	private boolean dateNeedsToBeRepaired(DateTime date)
	{
		if(date.getHourOfDay() != 0)
		{
			return true;
		}

		if(date.getMinuteOfHour() != 0)
		{
			return true;
		}

		if(date.getSecondOfMinute() != 0)
		{
			return true;
		}

		return date.getMillisOfSecond() != 0;
	}
}
