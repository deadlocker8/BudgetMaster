package de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.end;


import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = TableNames.REPEATING_END_DATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationRepeatingEndDate implements ProvidesID
{
	@Id

	private Integer ID;

	@Column(name = "end_date")
	private LocalDate endDate;
}
