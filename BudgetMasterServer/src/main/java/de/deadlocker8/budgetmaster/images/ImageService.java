package de.deadlocker8.budgetmaster.images;

import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.services.Resettable;
import de.thecodelabs.utils.util.Localization;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService implements Resettable
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);


	private final ImageRepository imageRepository;
	private final IconService iconService;

	@Autowired
	public ImageService(ImageRepository imageRepository, IconService iconService)
	{
		this.imageRepository = imageRepository;
		this.iconService = iconService;
	}

	public ImageRepository getRepository()
	{
		return imageRepository;
	}

	@Override
	@Transactional
	public void deleteAll()
	{
		LOGGER.info("Resetting images...");
		final List<Image> images = imageRepository.findAll();
		for(Image image : images)
		{
			deleteImage(image);
		}

		imageRepository.deleteAll();
		LOGGER.info("All images reset.");
	}

	@Transactional
	public void deleteImage(Image image)
	{
		final List<Icon> referringIcons = image.getReferringIcons();
		for(Icon icon : referringIcons)
		{
			iconService.deleteIcon(icon);
		}

		getRepository().delete(image);
	}

	@Override
	public void createDefaults()
	{
		// no defaults needed
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
		final Optional<ImageFileExtension> imageFileExtensionOptional = ImageFileExtension.getByExtension(fileExtension);
		if(imageFileExtensionOptional.isEmpty())
		{
			throw new InvalidFileExtensionException(Localization.getString("upload.image.error.invalid.extension", fileExtension));
		}

		final Byte[] byteObjects = ArrayUtils.toObject(file.getBytes());
		final Image image = new Image(byteObjects, originalFilename, imageFileExtensionOptional.get());
		imageRepository.save(image);
	}

	public static Optional<String> getFileExtension(String filename)
	{
		return Optional.ofNullable(filename)
				.filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1).toLowerCase());
	}
}
