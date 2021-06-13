package de.deadlocker8.budgetmaster.icon;

import de.deadlocker8.budgetmaster.services.Resettable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IconService implements Resettable
{
	private final IconRepository iconRepository;

	@Autowired
	public IconService(IconRepository iconRepository)
	{
		this.iconRepository = iconRepository;
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
		getRepository().delete(icon);
	}

	@Override
	public void createDefaults()
	{
	}
}
