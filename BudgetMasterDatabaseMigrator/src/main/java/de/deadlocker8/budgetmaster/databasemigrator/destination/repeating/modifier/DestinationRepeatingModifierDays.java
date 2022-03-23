package de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.modifier;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = TableNames.REPEATING_MODIFIER_DAYS)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationRepeatingModifierDays
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;
}
