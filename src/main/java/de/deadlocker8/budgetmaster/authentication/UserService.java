package de.deadlocker8.budgetmaster.authentication;

import de.deadlocker8.budgetmaster.ProgramArgs;
import de.deadlocker8.budgetmaster.services.AccountService;
import de.thecodelabs.logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
	@Autowired
	public UserService(UserRepository userRepository, AccountService accountService)
	{
		if(ProgramArgs.getArgs().contains("--resetPassword"))
		{
			Logger.info("Password reset");
			userRepository.deleteAll();
		}

		if(userRepository.findAll().size() == 0)
		{
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String encryptedPassword = bCryptPasswordEncoder.encode("BudgetMaster");
			User user = new User("Default", encryptedPassword);
			userRepository.save(user);
			Logger.info("Created default user");

			accountService.selectAccount(accountService.getRepository().findByIsSelected(true).getID());
		}
	}
}
