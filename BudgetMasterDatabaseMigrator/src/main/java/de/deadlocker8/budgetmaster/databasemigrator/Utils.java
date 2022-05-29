package de.deadlocker8.budgetmaster.databasemigrator;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.StepExecution;

public class Utils
{
	private Utils()
	{
		// empty
	}

	public static int getCommitCount(StepExecution stepExecution)
	{
		final int commitCount = stepExecution.getCommitCount();
		if(commitCount > 0 || stepExecution.getStatus().equals(BatchStatus.COMPLETED))
		{
			// subtract one because the commit count includes the final commit transaction
			return commitCount - 1;
		}
		else
		{
			return commitCount;
		}
	}
}
