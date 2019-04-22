package de.deadlocker8.budgetmaster.reports.columns;

import de.deadlocker8.budgetmaster.reports.ColumnType;
import de.deadlocker8.budgetmaster.reports.settings.ReportSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportColumnService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private ReportColumnRepository reportColumnRepository;

	@Autowired
	public ReportColumnService(ReportColumnRepository reportColumnRepository)
	{
		this.reportColumnRepository = reportColumnRepository;
	}

	public ReportColumnRepository getRepository()
	{
		return reportColumnRepository;
	}

	public void createDefaultsWithReportSettings(ReportSettings settings)
	{
		if(reportColumnRepository.findAllByOrderByPositionAsc().size() != ColumnType.values().length)
		{
			reportColumnRepository.deleteAllInBatch();

			for(int i = 0; i < ColumnType.values().length; i++)
			{
				ColumnType currentType = ColumnType.values()[i];
				if(reportColumnRepository.findByKey(currentType.getKey()) == null)
				{
					reportColumnRepository.save(new ReportColumn(currentType.getKey(), i));
				}
			}

			for(ReportColumn column : reportColumnRepository.findAll())
			{
				column.setReferringSettings(settings);
				reportColumnRepository.save(column);
			}

			LOGGER.debug("Created default report columns");
		}
	}
}
