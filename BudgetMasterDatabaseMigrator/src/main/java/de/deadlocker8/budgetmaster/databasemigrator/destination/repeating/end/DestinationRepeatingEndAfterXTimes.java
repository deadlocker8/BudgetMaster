package de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.end;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = TableNames.REPEATING_END_AFTER_X_TIMES)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationRepeatingEndAfterXTimes
{
	@Id

	private Integer ID;

	private Integer times;
}
