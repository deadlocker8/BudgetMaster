package de.deadlocker8.budgetmaster.services.report;

import de.deadlocker8.budgetmaster.entities.report.ReportColumn;
import de.deadlocker8.budgetmaster.repositories.report.ReportColumnRepository;
import de.deadlocker8.budgetmaster.services.Resetable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportColumnService implements Resetable
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private ReportColumnRepository reportColumnRepository;

	@Autowired
	public ReportColumnService(ReportColumnRepository reportColumnRepository)
	{
		this.reportColumnRepository = reportColumnRepository;

		createDefaults();
	}

	public ReportColumnRepository getRepository()
	{
		return reportColumnRepository;
	}

	@Override
	public void deleteAll()
	{
	}

	@Override
	public void createDefaults()
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

			LOGGER.debug("Created default report columns");
		}
	}
}
