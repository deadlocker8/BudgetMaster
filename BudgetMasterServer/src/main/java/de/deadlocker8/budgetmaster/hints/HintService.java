package de.deadlocker8.budgetmaster.hints;

import de.deadlocker8.budgetmaster.services.Resettable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HintService implements Resettable
{
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
		final List<String> hintKeys = List.of("hint.first.use.teaser",
				"hint.report.columns",
				"hint.template.arrow.keys",
				"hint.transaction.save",
				"hint.globalDatePicker.hotkeys",
				"hint.icon.upload.image.size",
				"hint.template.sort.groups",
				"hint.globalAccountSelect.hotkeys");

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