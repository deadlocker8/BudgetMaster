package de.deadlocker8.budgetmaster.reports.settings;

import de.deadlocker8.budgetmaster.reports.columns.ReportColumn;
import de.deadlocker8.budgetmaster.reports.columns.ReportColumnService;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReportSettingsService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportSettingsService.class);

	private final ReportSettingsRepository reportSettingsRepository;
	private final ReportColumnService reportColumnService;

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
		Optional<ReportSettings> reportSettingsOptional = reportSettingsRepository.findById(0);
		if(!reportSettingsOptional.isPresent())
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

		reportSettingsOptional = reportSettingsRepository.findById(0);
		if(reportSettingsOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		reportColumnService.createDefaultsWithReportSettings(reportSettingsOptional.get());
	}

	public ReportSettings getReportSettings()
	{
		return reportSettingsRepository.findById(0).orElseThrow();
	}
}