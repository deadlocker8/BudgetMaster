package de.deadlocker8.budgetmaster.accounts;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.images.ImageService;
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
	private final ImageService imageService;

	@Autowired
	public AccountController(AccountService accountService, ImageService imageService)
	{
		this.accountService = accountService;
		this.imageService = imageService;
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

	@GetMapping
	public String accounts(Model model)
	{
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		return "accounts/accounts";
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteAccount(Model model, @PathVariable("ID") Integer ID)
	{
		model.addAttribute("accounts", accountService.getAllAccountsAsc());
		model.addAttribute("currentAccount", accountService.getRepository().getOne(ID));
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
		return "accounts/accounts";
	}

	@GetMapping("/newAccount")
	public String newAccount(Model model)
	{
		Account emptyAccount = new Account();
		model.addAttribute("account", emptyAccount);
		model.addAttribute("availableImages", imageService.getRepository().findAll());
		model.addAttribute("availableAccountStates", AccountState.values());
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
		model.addAttribute("availableImages", imageService.getRepository().findAll());
		model.addAttribute("availableAccountStates", AccountState.values());
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
		boolean isNameAlreadyUsed = accountService.getRepository().findByName(account.getName()) != null;
		if(isNewAccount && isNameAlreadyUsed)
		{
			bindingResult.addError(new FieldError("NewAccount", "name", "", false, new String[]{"warning.duplicate.account.name"}, null, null));
		}

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("account", account);
			model.addAttribute("availableImages", imageService.getRepository().findAll());
			model.addAttribute("availableAccountStates", AccountState.values());
			return "accounts/newAccount";
		}

		if(isNewAccount)
		{
			account.setType(AccountType.CUSTOM);
			accountService.getRepository().save(account);
		}
		else
		{
			updateExistingAccount(account);
		}

		if(request.getSession().getAttribute("database") != null)
		{
			return "redirect:/settings/database/import/step2";
		}

		return "redirect:/accounts";
	}

	private void updateExistingAccount(Account newAccount)
	{
		Optional<Account> existingAccountOptional = accountService.getRepository().findById(newAccount.getID());
		if(existingAccountOptional.isPresent())
		{
			Account existingAccount = existingAccountOptional.get();
			existingAccount.setName(newAccount.getName());
			existingAccount.setIcon(newAccount.getIcon());
			existingAccount.setType(AccountType.CUSTOM);
			existingAccount.setAccountState(newAccount.getAccountState());
			accountService.getRepository().save(existingAccount);
		}
	}
}