package de.deadlocker8.budgetmaster.databasemigrator.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class GenericWriter<T> implements ItemWriter<T>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericWriter.class);

	final JpaRepository<T, Integer> repository;

	public GenericWriter(JpaRepository<T, Integer> repository)
	{
		this.repository = repository;
	}

	@Override
	public void write(List<? extends T> list)
	{
		for(T data : list)
		{
			LOGGER.debug("GenericWriter: Writing: {}", data);
			repository.save(data);
		}
	}
}
