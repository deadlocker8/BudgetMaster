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

	@Column(name = "display_type")
	private Integer displayType;

	@Column(name = "group_type")
	private Integer groupType;

	@Column(name = "preview_image_file_name")
	private String previewImageFileName;
}
