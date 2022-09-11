package de.deadlocker8.budgetmaster.accounts;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.utils.FontAwesomeIcons;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationLinkBuilder;
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
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping(Mappings.ACCOUNTS)
public class AccountController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String ALL_ENTITIES = "accounts";
		public static final String ONE_ENTITY = "account";
		public static final String ENTITY_TO_DELETE = "accountToDelete";
		public static final String CURRENT_ACCOUNT = "currentAccount";
		public static final String ACCOUNT_NOT_DELETABLE = "accountNotDeletable";
		public static final String AVAILABLE_ACCOUNT_STATES = "availableAccountStates";
		public static final String FONTAWESOME_ICONS = "fontawesomeIcons";
		public static final String ERROR = "error";
		public static final String NOTIFICATIONS = "notifications";
	}

	private static class ReturnValues
	{
		public static final String SHOW_ALL = "accounts/accounts";
		public static final String REDIRECT_SHOW_ALL = "redirect:/accounts";
		public static final String NEW_ENTITY = "accounts/newAccount";
		public static final String DELETE_ENTITY = "accounts/deleteAccountModal";
		public static final String SETTINGS = "redirect:/settings";
		public static final String GLOBAL_ACCOUNT_SELECT_MODAL = "globalAccountSelectModal";
	}

	private static final String ACCOUNT_SELECTED_INDICATOR = "accountSelected=1";

	private final AccountService accountService;
	private final IconService iconService;

	@Autowired
	public AccountController(AccountService accountService, IconService iconService)
	{
		this.accountService = accountService;
		this.iconService = iconService;
	}

	@GetMapping(value = "/{ID}/select")
	public String selectAccount(HttpServletRequest request, @PathVariable("ID") Integer ID)
	{
		accountService.selectAccount(ID);

		String referer = request.getHeader("Referer");
		if(referer.contains("database/import"))
		{
			return ReturnValues.SETTINGS;
		}

		if(referer.contains("highlight"))
		{
			final StringBuffer requestURL = request.getRequestURL();
			final String baseUrl = requestURL.substring(0, requestURL.length() - request.getRequestURI().length());
			referer = baseUrl + "/transactions/";
		}

		if(referer.contains(ACCOUNT_SELECTED_INDICATOR))
		{
			return MessageFormat.format("redirect:{0}", referer);
		}
		else if(referer.contains("?"))
		{
			return MessageFormat.format("redirect:{0}&{1}", referer, ACCOUNT_SELECTED_INDICATOR);
		}
		else
		{
			return MessageFormat.format("redirect:{0}?{1}", referer, ACCOUNT_SELECTED_INDICATOR);
		}
	}

	@GetMapping(value = "/{ID}/setAsDefault")
	public String setAsDefault(HttpServletRequest request, @PathVariable("ID") Integer ID)
	{
		accountService.setAsDefaultAccount(ID);

		String referer = request.getHeader("Referer");
		if(referer.contains("database/import"))
		{
			return ReturnValues.SETTINGS;
		}
		return "redirect:" + referer;
	}

	@GetMapping
	public String accounts(Model model)
	{
		model.addAttribute(ModelAttributes.ALL_ENTITIES, accountService.getAllEntitiesAsc());
		return ReturnValues.SHOW_ALL;
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteAccount(Model model, @PathVariable("ID") Integer ID)
	{
		model.addAttribute(ModelAttributes.ALL_ENTITIES, accountService.getAllEntitiesAsc());
		model.addAttribute(ModelAttributes.ENTITY_TO_DELETE, accountService.getRepository().getReferenceById(ID));
		return ReturnValues.DELETE_ENTITY;
	}

	@GetMapping("/{ID}/delete")
	public String deleteAccountAndReferringTransactions(WebRequest request, Model model, @PathVariable("ID") Integer ID)
	{
		// at least one account is required (to delete a sole account another one has to be created first)
		final Account accountToDelete = accountService.getRepository().getReferenceById(ID);
		if(accountService.getRepository().findAllByType(AccountType.CUSTOM).size() > 1)
		{
			accountService.deleteAccount(ID);
			WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.account.delete.success", accountToDelete.getName()), NotificationType.SUCCESS));
			return ReturnValues.REDIRECT_SHOW_ALL;
		}

		model.addAttribute(ModelAttributes.ALL_ENTITIES, accountService.getAllEntitiesAsc());
		model.addAttribute(ModelAttributes.CURRENT_ACCOUNT, accountToDelete);
		model.addAttribute(ModelAttributes.ACCOUNT_NOT_DELETABLE, true);
		return ReturnValues.SHOW_ALL;
	}

	@GetMapping("/newAccount")
	public String newAccount(Model model)
	{
		Account emptyAccount = new Account();
		model.addAttribute(ModelAttributes.ONE_ENTITY, emptyAccount);
		model.addAttribute(ModelAttributes.AVAILABLE_ACCOUNT_STATES, AccountState.values());
		model.addAttribute(ModelAttributes.FONTAWESOME_ICONS, FontAwesomeIcons.ICONS);
		return ReturnValues.NEW_ENTITY;
	}

	@GetMapping("/{ID}/edit")
	public String editAccount(Model model, @PathVariable("ID") Integer ID)
	{
		Optional<Account> accountOptional = accountService.getRepository().findById(ID);
		if(accountOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		model.addAttribute(ModelAttributes.ONE_ENTITY, accountOptional.get());
		model.addAttribute(ModelAttributes.AVAILABLE_ACCOUNT_STATES, AccountState.values());
		model.addAttribute(ModelAttributes.FONTAWESOME_ICONS, FontAwesomeIcons.ICONS);
		return ReturnValues.NEW_ENTITY;
	}

	@PostMapping(value = "/newAccount")
	public String post(HttpServletRequest request, WebRequest webRequest, Model model,
					   @ModelAttribute("NewAccount") Account account,
					   @RequestParam(value = "iconImageID", required = false) Integer iconImageID,
					   @RequestParam(value = "builtinIconIdentifier", required = false) String builtinIconIdentifier,
					   @RequestParam(value = "fontColor", required = false) String fontColor,
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
			final Notification notification = new Notification(warningMessage, NotificationType.WARNING);

			// Adding an error to the bindingResult will result in an FTL template render instead of a redirect.
			// For redirects normally WebRequestUtils.putNotification() is used.
			// Using WebRequestUtils.putNotification() will not work in this case because NotificationAdvice is
			// executed BEFORE this controller method and therefore the notifications list is empty in the
			// resulting page.
			// Quickfix: explicitly set the model attribute "notifications"
			final List<Notification> notifications = WebRequestUtils.getNotifications(webRequest);
			notifications.add(notification);
			model.addAttribute(ModelAttributes.NOTIFICATIONS, notifications);

			bindingResult.addError(new FieldError("NewAccount", "state", account.getAccountState(), false, new String[]{"warning.account.edit.state"}, null, null));
		}

		account.updateIcon(iconService, iconImageID, builtinIconIdentifier, fontColor, accountService);

		if(bindingResult.hasErrors())
		{
			model.addAttribute(ModelAttributes.ERROR, bindingResult);
			model.addAttribute(ModelAttributes.ONE_ENTITY, account);
			model.addAttribute(ModelAttributes.AVAILABLE_ACCOUNT_STATES, AccountState.values());
			model.addAttribute(ModelAttributes.FONTAWESOME_ICONS, FontAwesomeIcons.ICONS);
			return ReturnValues.NEW_ENTITY;
		}

		if(isNewAccount)
		{
			account.setType(AccountType.CUSTOM);
			account = accountService.getRepository().save(account);
		}
		else
		{
			accountService.updateExistingAccount(account);
		}

		final String link = NotificationLinkBuilder.buildEditLink(request, account.getName(), Mappings.ACCOUNTS, account.getID());
		WebRequestUtils.putNotification(webRequest, new Notification(Localization.getString("notification.account.save.success", link), NotificationType.SUCCESS));
		return ReturnValues.REDIRECT_SHOW_ALL;
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

	@GetMapping("/globalAccountSelectModal")
	public String globalAccountSelectModal(Model model)
	{
		model.addAttribute(ModelAttributes.ALL_ENTITIES, accountService.getAllReadableAccounts());

		return ReturnValues.GLOBAL_ACCOUNT_SELECT_MODAL;
	}
}