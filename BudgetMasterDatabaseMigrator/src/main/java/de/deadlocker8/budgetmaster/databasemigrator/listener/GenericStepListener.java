package de.deadlocker8.budgetmaster.databasemigrator.listener;

import de.deadlocker8.budgetmaster.databasemigrator.destination.DestinationRepository;
import de.deadlocker8.budgetmaster.databasemigrator.Utils;
import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.text.MessageFormat;
import java.util.List;

public class GenericStepListener<T extends ProvidesID> implements StepExecutionListener
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericStepListener.class);

	private final String tableName;
	private final EntityManager entityManager;
	private final DestinationRepository<T> repository;

	public GenericStepListener(String tableName, EntityManager entityManager, DestinationRepository<T> repository)
	{
		this.tableName = tableName;
		this.entityManager = entityManager;
		this.repository = repository;
	}

	@Override
	@Transactional
	public void beforeStep(StepExecution stepExecution)
	{
		LOGGER.info("\n");
		LOGGER.info(">>> Migrate {}s...", tableName);

		LOGGER.debug("Resetting sequence to 0");
		entityManager.createNativeQuery(MessageFormat.format("ALTER SEQUENCE {0}_id_seq RESTART WITH 1", tableName))
				.executeUpdate();
	}

	@Override
	@Transactional
	public ExitStatus afterStep(StepExecution stepExecution)
	{
		final int count = Utils.getCommitCount(stepExecution);
		LOGGER.info(">>> Successfully migrated {} {}s\n", count, tableName);

		final int highestUsedID = getHighestUsedID();
		final int newSequence = highestUsedID + 1;
		LOGGER.debug("Adjusting sequence to {} ({})", newSequence, highestUsedID);
		entityManager.createNativeQuery(MessageFormat.format("ALTER SEQUENCE {0}_id_seq RESTART WITH {1}}", tableName, newSequence))
				.executeUpdate();

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
