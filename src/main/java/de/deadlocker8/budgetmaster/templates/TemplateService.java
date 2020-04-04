package de.deadlocker8.budgetmaster.templates;

import de.deadlocker8.budgetmaster.services.Resetable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateService implements Resetable
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private TemplateRepository templateRepository;

	@Autowired
	public TemplateService(TemplateRepository templateRepository)
	{
		this.templateRepository = templateRepository;
	}

	public TemplateRepository getRepository()
	{
		return templateRepository;
	}

	@Override
	public void deleteAll()
	{
		templateRepository.deleteAll();
	}

	@Override
	public void createDefaults()
	{
	}
}
