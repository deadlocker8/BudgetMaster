package de.deadlocker8.budgetmaster.authentication;

import de.deadlocker8.budgetmaster.ProgramArgs;
import de.deadlocker8.budgetmaster.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public UserService(UserRepository userRepository, AccountService accountService)
	{
		if(ProgramArgs.getArgs().contains("--resetPassword"))
		{
			LOGGER.info("Password reset");
			userRepository.deleteAll();
		}

		if(userRepository.findAll().size() == 0)
		{
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String encryptedPassword = bCryptPasswordEncoder.encode("BudgetMaster");
			User user = new User("Default", encryptedPassword);
			userRepository.save(user);
			LOGGER.info("Created default user");

			accountService.selectAccount(accountService.getRepository().findByIsSelected(true).getID());
		}
	}
}
