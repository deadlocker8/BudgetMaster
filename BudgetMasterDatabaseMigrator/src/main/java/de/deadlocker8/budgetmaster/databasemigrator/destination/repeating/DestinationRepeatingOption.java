package de.deadlocker8.budgetmaster.databasemigrator.destination.repeating;


import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = TableNames.REPEATING_OPTION)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationRepeatingOption implements ProvidesID
{
	@Id
	private Integer ID;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_option_id")
	private Integer endOptionID;

	@Column(name = "modifier_id")
	private Integer modifierID;
}
