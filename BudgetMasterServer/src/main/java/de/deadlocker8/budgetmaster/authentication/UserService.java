package de.deadlocker8.budgetmaster.authentication;

import de.deadlocker8.budgetmaster.ProgramArgs;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	public static final String DEFAULT_PASSWORD = "BudgetMaster";
	private static final String DEFAULT_USERNAME = "Default";

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository, AccountService accountService)
	{
		this.userRepository = userRepository;

		if(ProgramArgs.getArgs().contains("--resetPassword"))
		{
			LOGGER.info("Password reset");
			userRepository.deleteAll();
		}

		if(userRepository.findAll().isEmpty())
		{
			final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			final String encryptedPassword = bCryptPasswordEncoder.encode(DEFAULT_PASSWORD);
			final User user = new User(DEFAULT_USERNAME, encryptedPassword);
			userRepository.save(user);
			LOGGER.info("Created default user");

			accountService.selectAccount(accountService.getRepository().findByIsSelected(true).getID());
		}
	}

	public boolean isPasswordValid(String password)
	{
		final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		final User user = userRepository.findByName(DEFAULT_USERNAME);
		return bCryptPasswordEncoder.matches(password, user.getPassword());
	}
}
