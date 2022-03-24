package de.deadlocker8.budgetmaster.databasemigrator.destination.tag;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = TableNames.TAG)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationTag
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;

	private String name;
}
