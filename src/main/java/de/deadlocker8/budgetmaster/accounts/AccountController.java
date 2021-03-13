package de.deadlocker8.budgetmaster.accounts;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.settings.SettingsService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Controller
@RequestMapping(Mappings.ACCOUNTS)
public class AccountController extends BaseController
{
	private final AccountService accountService;
	private final SettingsService settingsService;

	@Autowired
	public AccountController(AccountService accountService, SettingsService settingsService)
	{
		this.accountService = accountService;
		this.settingsService = settingsService;
	}

	@GetMapping(value = "/{ID}/select")
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

	@GetMapping(value = "/{ID}/setAsDefault")
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

	@GetMapping(value = "/{ID}/toggleReadOnly")
	public String toggleReadOnly(HttpServletRequest request, @PathVariable("ID") Integer ID)
	{
		final Optional<Account> accountOptional = accountService.getRepository().findById(ID);
		if(accountOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		final Account account = accountOptional.get();
		if(!account.isDefault())
		{
			account.setReadOnly(!account.isReadOnly());
			accountService.getRepository().save(account);
		}

		String referer = request.getHeader("Referer");
		if(referer.contains("database/import"))
		{
			return "redirect:/settings";
		}
		return "redirect:" + referer;
	}

	@GetMapping
	public String accounts(Model model)
	{
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("settings", settingsService.getSettings());
		return "accounts/accounts";
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteAccount(Model model, @PathVariable("ID") Integer ID)
	{
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("currentAccount", accountService.getRepository().getOne(ID));
		model.addAttribute("settings", settingsService.getSettings());
		return "accounts/accounts";
	}

	@GetMapping("/{ID}/delete")
	public String deleteAccountAndReferringTransactions(WebRequest request, Model model, @PathVariable("ID") Integer ID)
	{
		// at least one account is required (to delete a sole account another one has to be created first)
		final Account accountToDelete = accountService.getRepository().getOne(ID);
		if(accountService.getRepository().findAllByType(AccountType.CUSTOM).size() > 1)
		{
			accountService.deleteAccount(ID);

			WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.account.delete.success", accountToDelete.getName()), NotificationType.SUCCESS));

			return "redirect:/accounts";
		}

		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("currentAccount", accountToDelete);
		model.addAttribute("accountNotDeletable", true);
		model.addAttribute("settings", settingsService.getSettings());
		return "accounts/accounts";
	}

	@GetMapping("/newAccount")
	public String newAccount(Model model)
	{
		Account emptyAccount = new Account();
		model.addAttribute("account", emptyAccount);
		model.addAttribute("settings", settingsService.getSettings());
		return "accounts/newAccount";
	}

	@GetMapping("/{ID}/edit")
	public String editAccount(Model model, @PathVariable("ID") Integer ID)
	{
		Optional<Account> accountOptional = accountService.getRepository().findById(ID);
		if(accountOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		model.addAttribute("account", accountOptional.get());
		model.addAttribute("settings", settingsService.getSettings());
		return "accounts/newAccount";
	}

	@PostMapping(value = "/newAccount")
	public String post(HttpServletRequest request, Model model,
					   @ModelAttribute("NewAccount") Account account,
					   BindingResult bindingResult)
	{
		AccountValidator accountValidator = new AccountValidator();
		accountValidator.validate(account, bindingResult);

		boolean isNewAccount = account.getID() == null;

		if(isNewAccount && accountService.getRepository().findByName(account.getName()) != null)
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
				accountService.getRepository().save(account);
			}
			else
			{
				// edit existing account
				Optional<Account> existingAccountOptional = accountService.getRepository().findById(account.getID());
				if(existingAccountOptional.isPresent())
				{
					Account existingAccount = existingAccountOptional.get();
					existingAccount.setName(account.getName());
					existingAccount.setIconPath(account.getIconPath());
					accountService.getRepository().save(existingAccount);
				}
			}
		}

		if(request.getSession().getAttribute("database") != null)
		{
			return "redirect:/settings/database/accountMatcher";
		}

		return "redirect:/accounts";
	}
}