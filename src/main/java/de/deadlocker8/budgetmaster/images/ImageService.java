package de.deadlocker8.budgetmaster.images;

import de.deadlocker8.budgetmaster.services.Resetable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService implements Resetable
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

	private final ImageRepository imageRepository;

	@Autowired
	public ImageService(ImageRepository imageRepository)
	{
		this.imageRepository = imageRepository;
	}

	public ImageRepository getRepository()
	{
		return imageRepository;
	}

	@Override
	public void deleteAll()
	{
		// TODO: set placeholder image for all accounts
		imageRepository.deleteAll();
	}

	@Override
	public void createDefaults()
	{
		if(imageRepository.findAll().isEmpty())
		{
			// TODO: insert placeholder image
//			Account placeholder = new Account("Placeholder", AccountType.ALL);
//			accountRepository.save(placeholder);
//			LOGGER.debug("Created placeholder account");
//
//			Account account = accountRepository.save(new Account(Localization.getString(Strings.ACCOUNT_DEFAULT_NAME), AccountType.CUSTOM));
//			selectAccount(account.getID());
//			setAsDefaultAccount(account.getID());
//			LOGGER.debug("Created default account");
		}

	}
}
