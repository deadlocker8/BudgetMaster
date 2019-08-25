package de.deadlocker8.budgetmaster.accounts;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
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
	private final AccountRepository accountRepository;
	private final AccountService accountService;
	private final SettingsService settingsService;

	@Autowired
	public AccountController(AccountRepository accountRepository, AccountService accountService, SettingsService settingsService)
	{
		this.accountRepository = accountRepository;
		this.accountService = accountService;
		this.settingsService = settingsService;
	}

	@RequestMapping(value = "/accounts/{ID}/select")

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
	@RequestMapping(value = "/accounts/{ID}/setAsDefault")
	public String setAsDefault(HttpServletRequest request, @PathVariable("ID") Integer ID)
	{
		accountService.setAsDefaultAccount(ID);

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
		model.addAttribute("settings", settingsService.getSettings());
		return "accounts/accounts";
	}

	@RequestMapping("/accounts/{ID}/requestDelete")
	public String requestDeleteAccount(Model model, @PathVariable("ID") Integer ID)
	{
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("currentAccount", accountRepository.getOne(ID));
		model.addAttribute("settings", settingsService.getSettings());
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
		model.addAttribute("settings", settingsService.getSettings());
		return "accounts/accounts";
	}

	@RequestMapping("/accounts/newAccount")
	public String newAccount(Model model)
	{
		Account emptyAccount = new Account();
		model.addAttribute("account", emptyAccount);
		model.addAttribute("settings", settingsService.getSettings());
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
		model.addAttribute("settings", settingsService.getSettings());
		return "accounts/newAccount";
	}

	@PostMapping(value = "/accounts/newAccount")
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
			model.addAttribute("settings", settingsService.getSettings());
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