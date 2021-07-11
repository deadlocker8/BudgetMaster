package de.deadlocker8.budgetmaster.hints;

import de.deadlocker8.budgetmaster.services.Resettable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HintService implements Resettable
{
	private static final Logger LOGGER = LoggerFactory.getLogger(HintService.class);

	private final HintRepository hintRepository;

	@Autowired
	public HintService(HintRepository hintRepository)
	{
		this.hintRepository = hintRepository;

		createDefaults();
	}

	public Hint findByLocalizationKey(String localizationKey)
	{
		return hintRepository.findByLocalizationKey(localizationKey);
	}

	@Transactional
	public void dismiss(Integer ID)
	{
		final Hint hint = hintRepository.findById(ID).orElseThrow();
		hint.setDismissed(true);
	}

	@Transactional
	public void resetAll()
	{
		for(Hint hint : hintRepository.findAll())
		{
			hint.setDismissed(false);
		}
	}

	@Override
	public void deleteAll()
	{
		// deletion of hints is never needed
	}

	@Override
	public void createDefaults()
	{
		final List<String> hintKeys = List.of("hint.first.use.teaser", "hint.report.columns");

		for(String localizationKey : hintKeys)
		{
			final Hint existingHint = hintRepository.findByLocalizationKey(localizationKey);
			if(existingHint == null)
			{
				hintRepository.save(new Hint(localizationKey, false));
			}
		}
	}
}