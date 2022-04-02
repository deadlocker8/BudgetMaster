package de.deadlocker8.budgetmaster.databasemigrator.destination.repeating;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = TableNames.REPEATING_OPTION)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationRepeatingOption
{
	@Id
	private Integer ID;

	@Column(name = "start_date")
	private String startDate;

	@Column(name = "end_option_id")
	private Integer endOptionID;

	@Column(name = "modifier_id")
	private Integer modifierID;
}
