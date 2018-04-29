package de.deadlocker8.budgetmaster.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public UserService(UserRepository userRepository)
	{
//		if(userRepository.findAll().size() == 0)
//		{
//			userRepository.save(new User("Default", "123"));
//			LOGGER.debug("Created default user");
//		}
	}
}
