package de.deadlocker8.budgetmaster.databasemigrator.listener;

import de.deadlocker8.budgetmaster.databasemigrator.DatabaseMigratorMain;
import de.deadlocker8.budgetmaster.databasemigrator.DatabaseType;
import de.deadlocker8.budgetmaster.databasemigrator.Utils;
import de.deadlocker8.budgetmaster.databasemigrator.destination.DestinationIntegerRepository;
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

public class GenericStepListener<T extends ProvidesID, ID> implements StepExecutionListener
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericStepListener.class);

	private final String tableName;
	private final DestinationRepository<T, ID> repository;
	private final JdbcTemplate jdbcTemplate;
	private final boolean adjustSequence;

	public GenericStepListener(String tableName, DestinationRepository<T, ID> repository, JdbcTemplate jdbcTemplate, boolean adjustSequence)
	{
		this.tableName = tableName;
		this.repository = repository;
		this.jdbcTemplate = jdbcTemplate;
		this.adjustSequence = adjustSequence;
	}

	@Override
	public void beforeStep(StepExecution stepExecution)
	{
		LOGGER.info("\n");
		LOGGER.info(">>> Migrate {}s...", tableName);

		if(adjustSequence && DatabaseMigratorMain.databaseType.equals(DatabaseType.POSTGRESQL))
		{
			LOGGER.debug("Resetting sequence to 0");
			jdbcTemplate.update(MessageFormat.format("ALTER SEQUENCE {0}_id_seq RESTART WITH 1", tableName));
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution)
	{
		final int count = Utils.getCommitCount(stepExecution);
		LOGGER.info(">>> Successfully migrated {} {}s\n", count, tableName);

		if(adjustSequence && DatabaseMigratorMain.databaseType.equals(DatabaseType.POSTGRESQL))
		{
			final int highestUsedID = getHighestUsedID();
			final int newSequence = highestUsedID + 1;
			LOGGER.debug("Adjusting sequence to {} (highest used id: {})", newSequence, highestUsedID);
			jdbcTemplate.update(MessageFormat.format("ALTER SEQUENCE {0}_id_seq RESTART WITH {1}", tableName, String.valueOf(newSequence)));
		}

		return null;
	}

	public Integer getHighestUsedID()
	{
		if(repository instanceof DestinationIntegerRepository<T,ID> integerRepository)
		{
			final List<T> itemsOrderedByID = integerRepository.findAllByOrderByIDDesc();
			if(itemsOrderedByID.isEmpty())
			{
				return 0;
			}

			return itemsOrderedByID.get(0).getID();
		}

		return null;
	}
}
