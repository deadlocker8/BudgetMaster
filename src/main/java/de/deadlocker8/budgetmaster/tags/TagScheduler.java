package de.deadlocker8.budgetmaster.tags;

import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagScheduler
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private TagRepository tagRepository;
	private TransactionRepository transactionRepository;

	@Autowired
	public TagScheduler(TagRepository tagRepository, TransactionRepository transactionRepository)
	{
		this.tagRepository = tagRepository;
		this.transactionRepository = transactionRepository;
	}

	@Scheduled(fixedRate = 15*60*1000)
	public void tagCleaner()
	{
		LOGGER.debug("Cleaning Tags...");
		List<Tag> tags = tagRepository.findAll();
		for(Tag tag : tags)
		{
			if(transactionRepository.findAllByTagsContaining(tag).isEmpty())
			{
				tagRepository.delete(tag);
			}
		}
	}
}
