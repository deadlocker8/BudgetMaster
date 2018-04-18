package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.entities.Account;
import de.deadlocker8.budgetmaster.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class AccountController extends BaseController
{
	@Autowired
	private AccountRepository accountRepository;

	@RequestMapping(value = "/account/{ID}/select")
	public String selectAccount(HttpServletRequest request, @PathVariable("ID") Integer ID)
	{
		List<Account> accounts = accountRepository.findAll();
		for(Account currentAccount : accounts)
		{
			currentAccount.setSelected(false);
			accountRepository.save(currentAccount);
		}

		Account accountToSelect = accountRepository.findOne(ID);
		accountToSelect.setSelected(true);
		accountRepository.save(accountToSelect);

		return "redirect:" + request.getHeader("Referer");
	}
}