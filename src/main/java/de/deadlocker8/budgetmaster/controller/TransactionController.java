package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.Settings;
import de.deadlocker8.budgetmaster.entities.Tag;
import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.repeating.RepeatingOption;
import de.deadlocker8.budgetmaster.repeating.RepeatingTransactionUpdater;
import de.deadlocker8.budgetmaster.repeating.endoption.*;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifier;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifierType;
import de.deadlocker8.budgetmaster.repositories.*;
import de.deadlocker8.budgetmaster.services.AccountService;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.services.TransactionService;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import de.deadlocker8.budgetmaster.validators.TransactionValidator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
public class TransactionController extends BaseController
{
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private RepeatingOptionRepository repeatingOptionRepository;

	@Autowired
	private RepeatingTransactionUpdater repeatingTransactionUpdater;

	@Autowired
	private HelpersService helpers;

	@RequestMapping("/transactions")
	public String transactions(Model model, @CookieValue(value = "currentDate", required = false) String cookieDate)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);

		repeatingTransactionUpdater.updateRepeatingTransactions(date.dayOfMonth().withMaximumValue());

		List<Transaction> transactions = transactionService.getTransactionsForMonthAndYear(helpers.getCurrentAccount(), date.getMonthOfYear(), date.getYear(), getSettings().isRestActivated());
		int incomeSum = helpers.getIncomeSumForTransactionList(transactions);
		int paymentSum = helpers.getExpenditureSumForTransactionList(transactions);
		int rest = incomeSum + paymentSum;

		model.addAttribute("transactions", transactions);
		model.addAttribute("incomeSum", incomeSum);
		model.addAttribute("paymentSum", paymentSum);
		model.addAttribute("currentDate", date);
		model.addAttribute("rest", rest);

		return "transactions/transactions";
	}

	@RequestMapping("/transactions/{ID}/requestDelete")
	public String requestDeleteTransaction(Model model, @PathVariable("ID") Integer ID, @CookieValue("currentDate") String cookieDate)
	{
		if(!transactionService.isDeletable(ID))
		{
			return "redirect:/transactions";
		}

		DateTime date = helpers.getDateTimeFromCookie(cookieDate);
		List<Transaction> transactions = transactionService.getTransactionsForMonthAndYear(helpers.getCurrentAccount(), date.getMonthOfYear(), date.getYear(), getSettings().isRestActivated());
		int incomeSum = helpers.getIncomeSumForTransactionList(transactions);
		int paymentSum = helpers.getExpenditureSumForTransactionList(transactions);
		int rest = incomeSum + paymentSum;

		model.addAttribute("transactions", transactions);
		model.addAttribute("incomeSum", incomeSum);
		model.addAttribute("paymentSum", paymentSum);
		model.addAttribute("currentDate", date);
		model.addAttribute("currentTransaction", transactionRepository.getOne(ID));
		model.addAttribute("rest", rest);

		return "transactions/transactions";
	}

	@RequestMapping("/transactions/{ID}/delete")
	public String deleteTransaction(Model model, @PathVariable("ID") Integer ID)
	{
		transactionService.deleteTransaction(ID);
		return "redirect:/transactions";
	}

	@RequestMapping("/transactions/newTransaction")
	public String newTransaction(Model model, @CookieValue("currentDate") String cookieDate)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);
		Transaction emptyTransaction = new Transaction();
		model.addAttribute("currentDate", date);
		model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("transaction", emptyTransaction);
		return "transactions/newTransaction";
	}

	@RequestMapping(value = "/transactions/newTransaction", method = RequestMethod.POST)
	public String post(Model model, @CookieValue("currentDate") String cookieDate,
					   @ModelAttribute("NewTransaction") Transaction transaction, BindingResult bindingResult,
					   @RequestParam(value = "isRepeating", required = false) boolean isRepeating,
					   @RequestParam(value = "isPayment", required = false) boolean isPayment,
					   @RequestParam(value = "enableRepeating", required = false) boolean enableRepeating,
					   @RequestParam(value = "repeatingModifierNumber", required = false) int repeatingModifierNumber,
					   @RequestParam(value = "repeatingModifierType", required = false) String repeatingModifierType,
					   @RequestParam(value = "repeatingEndType", required = false) String repeatingEndType,
					   @RequestParam(value = "repeatingEndValue", required = false) String repeatingEndValue)
	{
		DateTime date = helpers.getDateTimeFromCookie(cookieDate);

		// handle repeating transactions
		if(transaction.getID() != null && isRepeating)
		{
			transactionService.deleteTransaction(transaction.getID());
		}

		TransactionValidator transactionValidator = new TransactionValidator();
		transactionValidator.validate(transaction, bindingResult);

		if(transaction.getAmount() == null)
		{
			transaction.setAmount(0);
		}

		if(isPayment)
		{
			transaction.setAmount(-Math.abs(transaction.getAmount()));
		}
		else
		{
			transaction.setAmount(Math.abs(transaction.getAmount()));
		}

		List<Tag> tags = transaction.getTags();
		if(tags != null)
		{
			transaction.setTags(new ArrayList<>());
			for(Tag currentTag : tags)
			{
				transaction = addTagForTransaction(currentTag.getName(), transaction);
			}
		}

		RepeatingOption repeatingOption = null;
		if(enableRepeating)
		{
			RepeatingModifierType type = RepeatingModifierType.getByLocalization(repeatingModifierType);
			RepeatingModifier repeatingModifier = RepeatingModifier.fromModifierType(type, repeatingModifierNumber);

			RepeatingEnd repeatingEnd = null;
			RepeatingEndType endType = RepeatingEndType.getByLocalization(repeatingEndType);
			switch(endType)
			{
				case NEVER:
					repeatingEnd = new RepeatingEndNever();
					break;
				case AFTER_X_TIMES:
					repeatingEnd = new RepeatingEndAfterXTimes(Integer.parseInt(repeatingEndValue));
					break;
				case DATE:
					DateTime endDate = DateTime.parse(repeatingEndValue, DateTimeFormat.forPattern("dd.MM.yy").withLocale(getSettings().getLanguage().getLocale()));
					repeatingEnd = new RepeatingEndDate(endDate);
					break;
			}

			repeatingOption = new RepeatingOption(transaction.getDate(), repeatingModifier, repeatingEnd);
		}
		transaction.setRepeatingOption(repeatingOption);

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("currentDate", date);
			model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
			model.addAttribute("accounts", accountService.getAllAccountsAsc());
			model.addAttribute("transaction", transaction);
			return "transactions/newTransaction";
		}

		transactionRepository.save(transaction);
		return "redirect:/transactions";
	}

	@RequestMapping("/transactions/{ID}/edit")
	public String editTransaction(Model model, @CookieValue("currentDate") String cookieDate, @PathVariable("ID") Integer ID)
	{
		Transaction transaction = transactionRepository.findOne(ID);
		if(transaction == null)
		{
			throw new ResourceNotFoundException();
		}

		// select first transaction in order to provide correct start date for repeating transactions
		if(transaction.getRepeatingOption() != null)
		{
			transaction = transaction.getRepeatingOption().getReferringTransactions().get(0);
		}

		DateTime date = helpers.getDateTimeFromCookie(cookieDate);
		model.addAttribute("currentDate", date);
		model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("transaction", transaction);
		return "transactions/newTransaction";
	}

	private Settings getSettings()
	{
		return settingsRepository.findOne(0);
	}

	private Transaction addTagForTransaction(String name, Transaction transaction)
	{
		if(tagRepository.findByName(name) == null)
		{
			tagRepository.save(new Tag(name));
		}

		List<Transaction> referringTransactions = tagRepository.findByName(name).getReferringTransactions();
		if(referringTransactions == null || !referringTransactions.contains(transaction))
		{
			transaction.getTags().add(tagRepository.findByName(name));
		}

		return transaction;
	}

	private void removeTagForTransaction(String name, Transaction transaction)
	{
		Tag currentTag = tagRepository.findByName(name);
		currentTag.getReferringTransactions().remove(transaction);
		tagRepository.save(currentTag);
	}
}