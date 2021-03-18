package de.deadlocker8.budgetmaster.images;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.services.Resetable;
import de.thecodelabs.utils.util.Localization;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService implements Resetable
{
	private static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("png", "jpeg", "jpg");

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
	@Transactional
	public void deleteAll()
	{
		final List<Image> images = imageRepository.findAll();
		for(Image image : images)
		{
			deleteImage(image);
		}

		imageRepository.deleteAll();
	}

	@Transactional
	public void deleteImage(Image image)
	{
		final List<Account> referringAccounts = image.getReferringAccounts();
		for(Account account : referringAccounts)
		{
			account.setIcon(null);
		}

		getRepository().delete(image);
	}

	@Override
	public void createDefaults()
	{
	}

	@Transactional
	public void saveImageFile(MultipartFile file) throws IOException, InvalidFileExtensionException
	{
		final String originalFilename = file.getOriginalFilename();
		final Optional<String> fileExtensionOptional = getFileExtension(originalFilename);
		if(fileExtensionOptional.isEmpty())
		{
			throw new IllegalArgumentException("Could not determine file extension from file name: " + originalFilename);
		}

		final String fileExtension = fileExtensionOptional.get();
		if(!ALLOWED_IMAGE_EXTENSIONS.contains(fileExtension))
		{
			throw new InvalidFileExtensionException(Localization.getString("upload.image.error.invalid.extension", fileExtension));
		}

		final Byte[] byteObjects = ArrayUtils.toObject(file.getBytes());
		final Image image = new Image(byteObjects, originalFilename, fileExtension);
		imageRepository.save(image);
	}

	public static Optional<String> getFileExtension(String filename)
	{
		return Optional.ofNullable(filename)
				.filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1).toLowerCase());
	}
}
