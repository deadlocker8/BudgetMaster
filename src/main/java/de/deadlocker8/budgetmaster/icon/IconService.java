package de.deadlocker8.budgetmaster.icon;

import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.services.Resettable;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IconService implements Resettable
{
	private final IconRepository iconRepository;
	private final ImageService imageService;

	@Autowired
	public IconService(IconRepository iconRepository, ImageService imageService)
	{
		this.iconRepository = iconRepository;
		this.imageService = imageService;
	}

	public IconRepository getRepository()
	{
		return iconRepository;
	}

	@Override
	@Transactional
	public void deleteAll()
	{
		final List<Icon> icons = iconRepository.findAll();
		for(Icon icon : icons)
		{
			deleteIcon(icon);
		}

		iconRepository.deleteAll();
	}

	@Transactional
	public void deleteIcon(Icon icon)
	{
		if(icon != null)
		{
			getRepository().delete(icon);
		}
	}

	@Override
	public void createDefaults()
	{
	}

	public Icon createIconReference(Integer iconImageID, String builtinIconIdentifier)
	{
		if(iconImageID != null)
		{
			final Optional<Image> imageByIdOptional = imageService.getRepository().findById(iconImageID);
			if(imageByIdOptional.isEmpty())
			{
				throw new ResourceNotFoundException();
			}

			return new Icon(imageByIdOptional.get());
		}

		if(builtinIconIdentifier != null)
		{
			return new Icon(builtinIconIdentifier);
		}

		return null;
	}
}
