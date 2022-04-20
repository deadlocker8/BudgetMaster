package de.deadlocker8.budgetmaster.databasemigrator.destination.report;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = TableNames.REPORT_COLUMN)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationReportColumn
{
	@Id
	private int ID;

	private boolean activated;

	@Column(name = "localization_key")
	private String localizationKey;

	@Column(name = "column_position")
	private Integer columnPosition;

	@Column(name = "referring_settings_id")
	private Integer referringSettingsID = 1;
}
