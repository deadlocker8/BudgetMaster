package de.deadlocker8.budgetmaster.icon;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageRepository;
import de.deadlocker8.budgetmaster.services.Resettable;
import de.deadlocker8.budgetmaster.templates.Template;
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
	private final ImageRepository imageRepository;

	@Autowired
	public IconService(IconRepository iconRepository, ImageRepository imageRepository)
	{
		this.iconRepository = iconRepository;
		this.imageRepository = imageRepository;
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
		if(icon == null)
		{
			return;
		}

		Account referringAccount = icon.getReferringAccount();
		if(referringAccount != null)
		{
			referringAccount.setIconReference(null);
		}

		Template referringTemplate = icon.getReferringTemplate();
		if(referringTemplate != null)
		{
			referringTemplate.setIconReference(null);
		}

		Category referringCategory = icon.getReferringCategory();
		if(referringCategory != null)
		{
			referringCategory.setIconReference(null);
		}

		getRepository().delete(icon);
	}

	@Override
	public void createDefaults()
	{
		// no defaults needed
	}

	public Icon createIconReference(Integer iconImageID, String builtinIconIdentifier, String fontColor)
	{
		if(iconImageID != null)
		{
			final Optional<Image> imageByIdOptional = imageRepository.findById(iconImageID);
			if(imageByIdOptional.isEmpty())
			{
				throw new ResourceNotFoundException();
			}

			return new Icon(imageByIdOptional.get());
		}

		if(fontColor != null && fontColor.isEmpty())
		{
			fontColor = null;
		}

		if(builtinIconIdentifier != null && !builtinIconIdentifier.isEmpty())
		{
			return new Icon(builtinIconIdentifier, fontColor);
		}

		return new Icon(null, fontColor);
	}
}
