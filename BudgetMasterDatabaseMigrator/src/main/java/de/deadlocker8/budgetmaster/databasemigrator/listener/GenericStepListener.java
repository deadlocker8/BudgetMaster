package de.deadlocker8.budgetmaster.databasemigrator.listener;

import de.deadlocker8.budgetmaster.databasemigrator.Utils;
import de.deadlocker8.budgetmaster.databasemigrator.destination.DestinationRepository;
import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.MessageFormat;
import java.util.List;

public class GenericStepListener<T extends ProvidesID> implements StepExecutionListener
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericStepListener.class);

	private final String tableName;
	private final DestinationRepository<T> repository;
	private final JdbcTemplate jdbcTemplate;

	public GenericStepListener(String tableName, DestinationRepository<T> repository, JdbcTemplate jdbcTemplate)
	{
		this.tableName = tableName;
		this.repository = repository;
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void beforeStep(StepExecution stepExecution)
	{
		LOGGER.info("\n");
		LOGGER.info(">>> Migrate {}s...", tableName);

		LOGGER.debug("Resetting sequence to 0");
		jdbcTemplate.update(MessageFormat.format("ALTER SEQUENCE {0}_id_seq RESTART WITH 1", tableName));
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution)
	{
		final int count = Utils.getCommitCount(stepExecution);
		LOGGER.info(">>> Successfully migrated {} {}s\n", count, tableName);

		final int highestUsedID = getHighestUsedID();
		final int newSequence = highestUsedID + 1;
		LOGGER.debug("Adjusting sequence to {} ({})", newSequence, highestUsedID);
		jdbcTemplate.update(MessageFormat.format("ALTER SEQUENCE {0}_id_seq RESTART WITH {1}", tableName, newSequence));

		return null;
	}

	public int getHighestUsedID()
	{
		final List<T> itemsOrderedByID = repository.findAllByOrderByIDDesc();
		if(itemsOrderedByID.isEmpty())
		{
			return 0;
		}

		return itemsOrderedByID.get(0).getID();
	}
}
