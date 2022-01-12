package de.deadlocker8.budgetmaster.utils.eventlistener;

import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

/**
 * Version 27 introduces a new member "isExpenditure" for transactions.
 * This class fill this field for all existing transactions.
 *
 * Run for databases with version 27 (v2.4.4) or older
 */


@Component
@Deprecated(since="v2.9.0", forRemoval = true)
public class IntroduceIsExpenditureMember
{
	private static final Logger LOGGER = LoggerFactory.getLogger(IntroduceIsExpenditureMember.class);

	private static final int ACTIVATION_VERSION_CODE = 27;

	private final TransactionRepository transactionRepository;
	private final SettingsService settingsService;

	@Autowired
	public IntroduceIsExpenditureMember(TransactionRepository transactionRepository, SettingsService settingsService)
	{
		this.transactionRepository = transactionRepository;
		this.settingsService = settingsService;
	}

	@EventListener
	@Transactional
	@Order(2)
	public void onApplicationEvent(ApplicationStartedEvent event)
	{
		if(settingsService.getSettings().getInstalledVersionCode() <= ACTIVATION_VERSION_CODE)
		{
			fixMissingMemberIsExpenditure();
		}
	}

	private void fixMissingMemberIsExpenditure()
	{
		final List<Transaction> transactions = transactionRepository.findAll();
		long fixedTransactionsCount = 0;

		for(Transaction transaction : transactions)
		{
			if(transaction.isExpenditure() != null)
			{
				continue;
			}

			transaction.setIsExpenditure(transaction.getAmount() <= 0);
			fixedTransactionsCount++;
		}

		if(fixedTransactionsCount > 0)
		{
			LOGGER.debug(MessageFormat.format("Fixed {0} transactions (Introduced new member 'isExpenditure')", fixedTransactionsCount));
		}
	}
}
