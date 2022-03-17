package de.deadlocker8.budgetmaster.databasemigrator.destination.chart;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "chart")
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
