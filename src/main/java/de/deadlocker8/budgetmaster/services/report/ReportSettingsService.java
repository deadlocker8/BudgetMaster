package de.deadlocker8.budgetmaster.services.report;

import de.deadlocker8.budgetmaster.entities.report.ReportColumn;
import de.deadlocker8.budgetmaster.entities.report.ReportSettings;
import de.deadlocker8.budgetmaster.repositories.report.ReportColumnRepository;
import de.deadlocker8.budgetmaster.repositories.report.ReportSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportSettingsService
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private ReportSettingsRepository reportSettingsRepository;
	private ReportColumnService reportColumnService;

	@Autowired
	public ReportSettingsService(ReportSettingsRepository reportSettingsRepository, ReportColumnService reportColumnService)
	{
		this.reportSettingsRepository = reportSettingsRepository;
		this.reportColumnService = reportColumnService;
		createDefaultReportSettingsIfNotExists();
	}

	public ReportSettingsRepository getRepository()
	{
		return reportSettingsRepository;
	}

	private void createDefaultReportSettingsIfNotExists()
	{
		if(reportSettingsRepository.findOne(0) == null)
		{
			ReportSettings reportSettings = ReportSettings.getDefault();
			reportSettings.setID(0);
			for(ReportColumn reportColumn : reportColumnService.getRepository().findAllByOrderByPositionAsc())
			{
				reportSettings.getColumns().add(reportColumn);
			}

			reportSettingsRepository.save(reportSettings);
			LOGGER.debug("Created default report settings");
		}
	}

	public ReportSettings getReportSettings()
	{
		return reportSettingsRepository.findOne(0);
	}
}