package de.deadlocker8.budgetmaster.databasemigrator.destination.chart;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = TableNames.CHART)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationChart
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;

	private String name;

	@Column(columnDefinition = "TEXT")
	private String script;

	private Integer type;
	private int version;
	private Integer displayType;
	private Integer groupType;
	private String previewImageFileName;
}
