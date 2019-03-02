package de.deadlocker8.budgetmaster.reports.columns;

import de.deadlocker8.budgetmaster.reports.columns.ReportColumn;
import de.deadlocker8.budgetmaster.reports.settings.ReportSettings;
import de.deadlocker8.budgetmaster.reports.columns.ReportColumnRepository;
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
		if(reportColumnRepository.findAllByOrderByPositionAsc().size() == 0)
		{
			reportColumnRepository.save(new ReportColumn("report.position", 0));
			reportColumnRepository.save(new ReportColumn("report.date", 1));
			reportColumnRepository.save(new ReportColumn("report.repeating", 2));
			reportColumnRepository.save(new ReportColumn("report.name", 3));
			reportColumnRepository.save(new ReportColumn("report.category", 4));
			reportColumnRepository.save(new ReportColumn("report.description", 5));
			reportColumnRepository.save(new ReportColumn("report.tags", 6));
			reportColumnRepository.save(new ReportColumn("report.account", 7));
			reportColumnRepository.save(new ReportColumn("report.rating", 8));
			reportColumnRepository.save(new ReportColumn("report.amount", 9));

			for(ReportColumn column : reportColumnRepository.findAll())
			{
				column.setReferringSettings(settings);
				reportColumnRepository.save(column);
			}

			LOGGER.debug("Created default report columns");
		}
	}
}
