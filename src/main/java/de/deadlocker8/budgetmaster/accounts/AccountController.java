package de.deadlocker8.budgetmaster.accounts;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.icon.Iconizable;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.utils.FontAwesomeIcons;
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
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping(Mappings.ACCOUNTS)
public class AccountController extends BaseController
{
	private final AccountService accountService;
	private final ImageService imageService;
	private final IconService iconService;

	@Autowired
	public AccountController(AccountService accountService, ImageService imageService, IconService iconService)
	{
		this.accountService = accountService;
		this.imageService = imageService;
		this.iconService = iconService;
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
		model.addAttribute("accounts", accountService.getAllEntitiesAsc());
		return "accounts/accounts";
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteAccount(Model model, @PathVariable("ID") Integer ID)
	{
		model.addAttribute("accounts", accountService.getAllEntitiesAsc());
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

		model.addAttribute("accounts", accountService.getAllEntitiesAsc());
		model.addAttribute("currentAccount", accountToDelete);
		model.addAttribute("accountNotDeletable", true);
		return "accounts/accounts";
	}

	@GetMapping("/newAccount")
	public String newAccount(Model model)
	{
		Account emptyAccount = new Account();
		model.addAttribute("account", emptyAccount);
		model.addAttribute("availableAccountStates", AccountState.values());
		model.addAttribute("fontawesomeIcons", FontAwesomeIcons.ICONS);
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
		model.addAttribute("availableAccountStates", AccountState.values());
		model.addAttribute("fontawesomeIcons", FontAwesomeIcons.ICONS);
		return "accounts/newAccount";
	}

	@PostMapping(value = "/newAccount")
	public String post(HttpServletRequest request, WebRequest webRequest, Model model,
					   @ModelAttribute("NewAccount") Account account,
					   @RequestParam(value = "iconImageID", required = false) Integer iconImageID,
					   @RequestParam(value = "builtinIconIdentifier", required = false) String builtinIconIdentifier,
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

		boolean isAccountStateAllowed = isAccountStateAllowed(account);
		if(!isNewAccount && !isAccountStateAllowed)
		{
			final String warningMessage = Localization.getString("warning.account.edit.state", Localization.getString(AccountState.FULL_ACCESS.getLocalizationKey()));
			WebRequestUtils.putNotification(webRequest, new Notification(warningMessage, NotificationType.WARNING));
			bindingResult.addError(new FieldError("NewAccount", "state", account.getAccountState(), false, new String[]{"warning.account.edit.state"}, null, null));
		}

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("account", account);
			model.addAttribute("availableImages", imageService.getRepository().findAll());
			model.addAttribute("availableAccountStates", AccountState.values());
			return "accounts/newAccount";
		}

		Iconizable.updateIcon(iconService, iconImageID, builtinIconIdentifier, account);

		if(isNewAccount)
		{
			account.setType(AccountType.CUSTOM);
			accountService.getRepository().save(account);
		}
		else
		{
			accountService.updateExistingAccount(account);
		}

		if(request.getSession().getAttribute("accountMatchList") != null)
		{
			return "redirect:/settings/database/import/step2";
		}

		return "redirect:/accounts";
	}

	private boolean isAccountStateAllowed(Account account)
	{
		List<Account> activatedAccounts = accountService.getRepository().findAllByTypeAndAccountStateOrderByNameAsc(AccountType.CUSTOM, AccountState.FULL_ACCESS);
		if(activatedAccounts.size() > 1)
		{
			return true;
		}

		final Account lastActivatedAccount = activatedAccounts.get(0);
		final boolean currentAccountIsLastActivated = lastActivatedAccount.getID().equals(account.getID());
		if(currentAccountIsLastActivated)
		{
			return account.getAccountState() == AccountState.FULL_ACCESS;
		}
		else
		{
			return true;
		}
	}
}