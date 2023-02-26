package de.deadlocker8.budgetmaster.transactions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.filter.FilterConfiguration;
import de.deadlocker8.budgetmaster.repeating.RepeatingOptionRepository;
import de.deadlocker8.budgetmaster.services.Resettable;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.tags.Tag;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.tags.TagService;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
public class TransactionService implements Resettable
{
	private static final Gson GSON = new GsonBuilder()
			.create();

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

	private final TransactionRepository transactionRepository;
	private final RepeatingOptionRepository repeatingOptionRepository;
	private final CategoryService categoryService;
	private final TagService tagService;

	@Autowired
	public TransactionService(TransactionRepository transactionRepository, RepeatingOptionRepository repeatingOptionRepository, CategoryService categoryService, TagService tagService, SettingsService settingsService)
	{
		this.transactionRepository = transactionRepository;
		this.repeatingOptionRepository = repeatingOptionRepository;
		this.categoryService = categoryService;
		this.tagService = tagService;
	}

	public TransactionRepository getRepository()
	{
		return transactionRepository;
	}

	public Transaction getTransactionForBalanceLastMonth(Account account, int month, int year)
	{
		final LocalDate endDateLastMonth = LocalDate.of(year, month, 1).minusMonths(1).with(lastDayOfMonth());

		final Transaction transactionBalanceLastMonth = new Transaction();
		transactionBalanceLastMonth.setCategory(categoryService.findByType(CategoryType.REST));
		transactionBalanceLastMonth.setName(Localization.getString(Strings.TRANSACTION_BALANCE_LAST_MONTH));
		transactionBalanceLastMonth.setDate(LocalDate.of(year, month, 1));
		transactionBalanceLastMonth.setAmount(getRest(account, endDateLastMonth));
		transactionBalanceLastMonth.setTags(new ArrayList<>());

		return transactionBalanceLastMonth;
	}

	public Transaction getTransactionForBalanceCurrentMonth(Account account, int month, int year)
	{
		final LocalDate endDateCurrentMonth = LocalDate.of(year, month, 1).with(lastDayOfMonth());

		final Transaction transactionBalanceCurrentMonth = new Transaction();
		transactionBalanceCurrentMonth.setCategory(categoryService.findByType(CategoryType.REST));
		transactionBalanceCurrentMonth.setName(Localization.getString(Strings.TRANSACTION_BALANCE_CURRENT_MONTH));
		transactionBalanceCurrentMonth.setDate(endDateCurrentMonth);
		transactionBalanceCurrentMonth.setAmount(getRest(account, endDateCurrentMonth));
		transactionBalanceCurrentMonth.setTags(new ArrayList<>());

		return transactionBalanceCurrentMonth;
	}

	public List<Transaction> getTransactionsForMonthAndYear(Account account, int month, int year, FilterConfiguration filterConfiguration)
	{
		final LocalDate startDate = LocalDate.of(year, month, 1);
		final LocalDate endDate = LocalDate.of(year, month, 1).with(lastDayOfMonth());

		return getTransactionsForAccount(account, startDate, endDate, filterConfiguration);
	}

	public List<Transaction> getTransactionsForAccountUntilDate(Account account, LocalDate date, FilterConfiguration filterConfiguration)
	{
		LocalDate startDate = LocalDate.of(1900, 1, 1);
		return getTransactionsForAccount(account, startDate, date, filterConfiguration);
	}

	public List<Transaction> getTransactionsForAccount(Account account, LocalDate startDate, LocalDate endDate, FilterConfiguration filterConfiguration)
	{
		if(filterConfiguration == null)
		{
			filterConfiguration = FilterConfiguration.DEFAULT;
		}

		if(account.getType().equals(AccountType.ALL))
		{
			Specification<Transaction> spec = TransactionSpecifications.withDynamicQuery(startDate, endDate, null, filterConfiguration.isIncludeIncome(), filterConfiguration.isIncludeExpenditure(), false, filterConfiguration.isIncludeRepeatingAndNotRepeating(), filterConfiguration.getIncludedCategoryIDs(), filterConfiguration.getIncludedTagIDs(), filterConfiguration.getName());
			return transactionRepository.findAll(spec);
		}

		Specification<Transaction> spec = TransactionSpecifications.withDynamicQuery(startDate, endDate, account, filterConfiguration.isIncludeIncome(), filterConfiguration.isIncludeExpenditure(), filterConfiguration.isIncludeTransfer(), filterConfiguration.isIncludeRepeatingAndNotRepeating(), filterConfiguration.getIncludedCategoryIDs(), filterConfiguration.getIncludedTagIDs(), filterConfiguration.getName());
		return transactionRepository.findAll(spec);
	}

	private int getRest(Account account, LocalDate endDate)
	{
		LocalDate startDate = LocalDate.of(2000, 1, 1);
		Integer restForNormalAndRepeating = transactionRepository.getRestForNormalAndRepeating(account.getID(), startDate, endDate);
		Integer restForTransferSource = transactionRepository.getRestForTransferSource(account.getID(), startDate, endDate);
		Integer restForTransferDestination = transactionRepository.getRestForTransferDestination(account.getID(), startDate, endDate);

		int rest = 0;

		if(restForNormalAndRepeating != null)
		{
			rest += restForNormalAndRepeating;
		}

		if(restForTransferSource != null)
		{
			rest += restForTransferSource;
		}

		if(restForTransferDestination != null)
		{
			// needs to be inverted
			rest -= restForTransferDestination;
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
		Optional<Transaction> transactionOptional = transactionRepository.findById(ID);
		if(transactionOptional.isEmpty())
		{
			LOGGER.debug(MessageFormat.format("Skipping already deleted transaction with ID: {0}", ID));
			return;
		}
		Transaction transactionToDelete = transactionOptional.get();

		// handle repeating transactions
		if(transactionToDelete.getRepeatingOption() == null)
		{
			transactionRepository.deleteById(ID);
		}
		else
		{
			repeatingOptionRepository.deleteById(transactionToDelete.getRepeatingOption().getID());
		}
	}

	public boolean isDeletable(Integer ID)
	{
		Optional<Transaction> transactionOptional = transactionRepository.findById(ID);
		if(transactionOptional.isPresent())
		{
			final Transaction transaction = transactionOptional.get();
			if(transaction.getCategory() != null)
			{
				if(transaction.getCategory().getType() == CategoryType.REST)
				{
					return false;
				}

				return transaction.getAccount().getAccountState() == AccountState.FULL_ACCESS;
			}
		}
		return false;
	}

	@Override
	public void deleteAll()
	{
		LOGGER.info("Resetting transactions...");
		for(Transaction transaction : transactionRepository.findAll())
		{
			deleteTransactionInRepo(transaction.getID());
		}
		LOGGER.info("All transactions reset.");
	}

	public void deleteTransactionsWithAccount(Account account)
	{
		for(Transaction referringTransaction : account.getReferringTransactions())
		{
			deleteTransactionInRepo(referringTransaction.getID());
		}

		for(Transaction referringTransaction : transactionRepository.findAllByTransferAccount(account))
		{
			deleteTransactionInRepo(referringTransaction.getID());
		}
	}

	@Override
	public void createDefaults()
	{
		// no defaults needed
	}

	public void handleAmount(TransactionBase item)
	{
		if(item.isExpenditure() == null)
		{
			item.setIsExpenditure(true);
		}

		if(item.getAmount() == null)
		{
			item.setAmount(0);
		}

		if(item.isExpenditure())
		{
			item.setAmount(-Math.abs(item.getAmount()));
		}
		else
		{
			item.setAmount(Math.abs(item.getAmount()));
		}
	}

	public void handleTags(TransactionBase item)
	{
		List<Tag> tags = item.getTags();
		if(tags != null)
		{
			item.setTags(new ArrayList<>());
			for(Tag currentTag : tags)
			{
				//noinspection ConstantConditions
				item = addTagForTransactionBase(currentTag.getName(), item);
			}
		}
	}

	private TransactionBase addTagForTransactionBase(String name, TransactionBase item)
	{
		final TagRepository tagRepository = tagService.getRepository();

		if(tagRepository.findByName(name) == null)
		{
			tagRepository.save(new Tag(name));
		}

		List<? extends TransactionBase> referringTransactions;
		if(item instanceof Template)
		{
			referringTransactions = tagRepository.findByName(name).getReferringTemplates();
		}
		else
		{
			referringTransactions = tagRepository.findByName(name).getReferringTransactions();
		}

		if(referringTransactions == null || !referringTransactions.contains(item))
		{
			item.getTags().add(tagRepository.findByName(name));
		}

		return item;
	}

	public void prepareModelNewOrEdit(Model model, boolean isEdit, LocalDate date, boolean changeTypeInProgress, TransactionBase item, List<Account> accounts)
	{
		model.addAttribute(TransactionModelAttributes.IS_EDIT, isEdit);
		model.addAttribute(TransactionModelAttributes.CURRENT_DATE, date);
		model.addAttribute(TransactionModelAttributes.CHANGE_TYPE_IN_PROGRESS, changeTypeInProgress);
		model.addAttribute(TransactionModelAttributes.CATEGORIES, categoryService.getAllEntitiesAsc());
		model.addAttribute(TransactionModelAttributes.ACCOUNTS, accounts);
		model.addAttribute(TransactionModelAttributes.TRANSACTION, item);
		model.addAttribute(TransactionModelAttributes.SUGGESTIONS_JSON, getNameSuggestionsJson());
	}

	public String getNameSuggestionsJson()
	{
		final List<Transaction> allByOrderByDateDesc = getRepository().findAllByOrderByDateDesc();
		final List<String> nameSuggestions = allByOrderByDateDesc.stream()
				.map(Transaction::getName)
				.distinct()
				.toList();
		return  GSON.toJson(nameSuggestions);
	}
}
