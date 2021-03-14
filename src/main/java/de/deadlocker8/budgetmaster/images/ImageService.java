package de.deadlocker8.budgetmaster.images;

import de.deadlocker8.budgetmaster.services.Resetable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

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

	@Transactional
	public void saveImageFile(MultipartFile file) throws IOException
	{
		Byte[] byteObjects = new Byte[file.getBytes().length];

		int i = 0;
		for(byte b : file.getBytes())
		{
			byteObjects[i++] = b;
		}

		final Optional<String> fileExtensionOptional = getFileExtension(file.getOriginalFilename());
		if(fileExtensionOptional.isEmpty())
		{
			throw new IllegalArgumentException("Could not determine file extension from file name: " + file.getOriginalFilename());
		}

		final Image image = new Image(byteObjects, fileExtensionOptional.get());
		imageRepository.save(image);
	}

	private Optional<String> getFileExtension(String filename) {
		return Optional.ofNullable(filename)
				.filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
}
