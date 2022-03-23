package de.deadlocker8.budgetmaster.databasemigrator.destination.report;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.*;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;

	private boolean activated;

	private String key;

	private Integer position;

	@Column(name = "referring_settings_id")
	private Integer referringSettingsID = 0;
}
