package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.account.Account;
import de.deadlocker8.budgetmaster.entities.account.AccountType;
import de.deadlocker8.budgetmaster.repositories.AccountRepository;
import de.deadlocker8.budgetmaster.services.AccountService;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import de.deadlocker8.budgetmaster.validators.AccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
public class AccountController extends BaseController
{
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "/account/{ID}/select")
	public String selectAccount(HttpServletRequest request, @PathVariable("ID") Integer ID)
	{
		accountService.selectAccount(ID);

		String referer = request.getHeader("Referer");
		if(referer.contains("database/import"))
		{
			return "redirect:/settings";
		}
		return "redirect:" + referer;
	}

	@RequestMapping("/accounts")
	public String accounts(HttpServletRequest request, Model model)
	{
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		return "accounts/accounts";
	}

	@RequestMapping("/accounts/{ID}/requestDelete")
	public String requestDeleteAccount(Model model, @PathVariable("ID") Integer ID)
	{
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("currentAccount", accountRepository.getOne(ID));
		return "accounts/accounts";
	}

	@RequestMapping("/accounts/{ID}/delete")
	public String deleteAccountAndReferringTransactions(Model model, @PathVariable("ID") Integer ID)
	{
		if(accountRepository.findAllByType(AccountType.CUSTOM).size() > 1)
		{
			accountService.deleteAccount(ID);
			return "redirect:/accounts";
		}

		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("currentAccount", accountRepository.getOne(ID));
		model.addAttribute("accountNotDeletable", true);
		return "accounts/accounts";
	}

	@RequestMapping("/accounts/newAccount")
	public String newAccount(Model model)
	{
		Account emptyAccount = new Account();
		model.addAttribute("account", emptyAccount);
		return "accounts/newAccount";
	}

	@RequestMapping("/accounts/{ID}/edit")
	public String editAccount(Model model, @PathVariable("ID") Integer ID)
	{
		Account account = accountRepository.findOne(ID);
		if(account == null)
		{
			throw new ResourceNotFoundException();
		}

		model.addAttribute("account", account);
		return "accounts/newAccount";
	}

	@RequestMapping(value = "/accounts/newAccount", method = RequestMethod.POST)
	public String post(HttpServletRequest request, Model model,
					   @ModelAttribute("NewAccount") Account account,
					   BindingResult bindingResult)
	{
		AccountValidator accountValidator = new AccountValidator();
		accountValidator.validate(account, bindingResult);

		if(accountRepository.findByName(account.getName()) != null)
		{
			bindingResult.addError(new FieldError("NewAccount", "name", "", false, new String[]{"warning.duplicate.account.name"}, null, null));
		}

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("account", account);
			return "accounts/newAccount";
		}
		else
		{
			account.setType(AccountType.CUSTOM);
			if(account.getID() == null)
			{
				// new account
				accountRepository.save(account);
			}
			else
			{
				// edit existing account
				Account existingAccount = accountRepository.findOne(account.getID());
				existingAccount.setName(account.getName());
				accountRepository.save(existingAccount);
			}
		}

		if(request.getSession().getAttribute("database") != null)
		{
			return "redirect:/settings/database/accountMatcher";
		}

		return "redirect:/accounts";
	}
}